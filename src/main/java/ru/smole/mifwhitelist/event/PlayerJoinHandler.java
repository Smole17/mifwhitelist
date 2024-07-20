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
                        () -> handler.disconnect(message(MIFWhitelist.CONFIG.noAccessGroupDisconnectMessage())),
                        () -> handler.disconnect(message(MIFWhitelist.CONFIG.noBypassSlotsGroupDisconnectMessage())),
                        () -> handler.disconnect(message(MIFWhitelist.CONFIG.noBypassMaintenanceGroupDisconnectMessage()))
        ));
    }

    private Text message(String[] message) {
        return Arrays.stream(message)
                .map(TextParserUtils::formatText)
                .reduce(Text.empty(), (mutableText, text1) -> mutableText.copy().append("\n").append(text1));
    }

    private void checkGroups(Collection<Group> groups, MinecraftServer server, Runnable onAccessFailure, Runnable onBypassSlotsFailure, Runnable onBypassMaintenanceFailure) {
        val hasAccessGroup = groups.stream().anyMatch(group -> group.getName().equals(MIFWhitelist.CONFIG.accessGroup()));

        if (MIFWhitelist.CONFIG.maintenanceMode()) {
            val hasBypassMaintenanceGroup = groups.stream().anyMatch(group -> group.getName().equals(MIFWhitelist.CONFIG.bypassMaintenanceGroup()));

            if (hasBypassMaintenanceGroup) return;

            onBypassMaintenanceFailure.run();
            return;
        }

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
