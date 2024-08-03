package ru.smole.mifwhitelist.event;

import com.mojang.authlib.GameProfile;
import eu.pb4.placeholders.api.TextParserUtils;
import lombok.experimental.UtilityClass;
import lombok.val;
import net.luckperms.api.model.group.Group;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.Text;
import ru.smole.mifwhitelist.MIFWhitelist;
import ru.smole.mifwhitelist.util.LuckPermsUtil;

import java.util.Arrays;
import java.util.Collection;

@UtilityClass
public class PlayerJoinHandler {
    
    public Text onJoin(GameProfile profile, MinecraftServer server) {
        val user = LuckPermsUtil.getUser(profile.getId());
        
        return checkGroups(user.getInheritedGroups(user.getQueryOptions()), server);
    }
    
    private Text message(String[] message) {
        return Arrays.stream(message)
            .map(TextParserUtils::formatText)
            .reduce(Text.empty(), (mutableText, text1) -> mutableText.copy().append("\n").append(text1));
    }
    
    private Text checkGroups(Collection<Group> groups, MinecraftServer server) {
        val hasAccessGroup = groups.stream().anyMatch(group -> group.getName().equals(MIFWhitelist.CONFIG.accessGroup()));
        
        if (MIFWhitelist.CONFIG.maintenanceMode()) {
            val hasBypassMaintenanceGroup = groups.stream().anyMatch(group -> group.getName().equals(MIFWhitelist.CONFIG.bypassMaintenanceGroup()));
            
            if (hasBypassMaintenanceGroup) return null;
            
            return message(MIFWhitelist.CONFIG.noBypassMaintenanceGroupDisconnectMessage());
        }
        
        if (!hasAccessGroup) return message(MIFWhitelist.CONFIG.noAccessGroupDisconnectMessage());
        
       return checkBypassSlotsGroup(server, groups);
    }
    
    private Text checkBypassSlotsGroup(MinecraftServer server, Collection<Group> groups) {
        val players = server.getServerMetadata().getPlayers();
        
        if (players == null || players.getPlayerLimit() - players.getOnlinePlayerCount() > MIFWhitelist.CONFIG.bypassSlots())
            return null;
        
        val hasBypassSlotsGroup = groups.stream().anyMatch(group -> group.getName().equals(MIFWhitelist.CONFIG.bypassSlotsGroup()));
        
        if (!hasBypassSlotsGroup) return message(MIFWhitelist.CONFIG.noBypassSlotsGroupDisconnectMessage());
        return null;
    }
}
