package ru.smole.mifwhitelist.event;

import eu.pb4.placeholders.api.TextParserUtils;
import lombok.experimental.UtilityClass;
import lombok.val;
import net.luckperms.api.model.group.Group;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.text.Text;
import ru.smole.mifwhitelist.MIFWhitelist;
import ru.smole.mifwhitelist.util.LuckPermsUtil;

import java.util.Arrays;
import java.util.Collection;

@UtilityClass
public class PlayerJoinHandler {

    public void onJoin(ServerPlayNetworkHandler handler, MinecraftServer server) {
        LuckPermsUtil.getUser(
                handler.player.getUuid(),
                user -> checkGroups(user.getInheritedGroups(user.getQueryOptions()), server,
                        () -> handler.disconnect(noAccessGroupDisconnectMessage()),
                        () -> handler.disconnect(noBypassSlotsGroupDisconnectMessage()))
        );
    }

    private Text noAccessGroupDisconnectMessage() {
        return Arrays.stream(MIFWhitelist.CONFIG.noAccessGroupDisconnectMessage())
                .map(TextParserUtils::formatText)
                .reduce(Text.empty(), (mutableText, text1) -> mutableText.copy().append("\n").append(text1));
    }

    private Text noBypassSlotsGroupDisconnectMessage() {
        return Arrays.stream(MIFWhitelist.CONFIG.noBypassSlotsGroupDisconnectMessage())
                .map(TextParserUtils::formatText)
                .reduce(Text.empty(), (mutableText, text1) -> mutableText.copy().append("\n").append(text1));
    }

    private void checkGroups(Collection<Group> groups, MinecraftServer server, Runnable onAccessFailure, Runnable onBypassSlotsFailure) {
        val hasAccessGroup = groups.stream().anyMatch(group -> group.getName().equals(MIFWhitelist.CONFIG.accessGroup()));

        if (!hasAccessGroup) {
            onAccessFailure.run();
            return;
        }

        checkBypassSlotsGroup(server, groups, onBypassSlotsFailure);
    }

    private void checkBypassSlotsGroup(MinecraftServer server, Collection<Group> groups, Runnable onFailure) {
        val players = server.getServerMetadata().getPlayers();

        if (players == null || players.getPlayerLimit() - players.getOnlinePlayerCount() > MIFWhitelist.CONFIG.bypassSlots())
            return;

        val hasBypassSlotsGroup = groups.stream().anyMatch(group -> group.getName().equals(MIFWhitelist.CONFIG.bypassSlotsGroup()));

        if (!hasBypassSlotsGroup) onFailure.run();
    }
}
