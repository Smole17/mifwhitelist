package ru.smole.mifwhitelist.config;

import io.wispforest.owo.config.annotation.Config;
import io.wispforest.owo.config.annotation.RestartRequired;

@Config(name = "mifwhitelist", wrapperName = "MIFWhitelistConfig")
public class MIFWhitelistConfigModel {

    @RestartRequired
    public boolean maintenanceMode;
    @RestartRequired
    public int bypassSlots = 20;
    @RestartRequired
    public String accessGroup = "access";
    @RestartRequired
    public String bypassSlotsGroup = "bypass_slots";
    @RestartRequired
    public String bypassMaintenanceGroup = "bypass_maintenance";

    @RestartRequired
    public String[] noAccessGroupDisconnectMessage = new String[]{
            "<red>You don't have a access pass to the server!",
            "<red>Please buy the pass to join."
    };

    @RestartRequired
    public String[] noBypassSlotsGroupDisconnectMessage = new String[]{
            "<red>You don't have a bypass slots pass to the server!",
            "<red>Please buy the pass to join on full server."
    };

    @RestartRequired
    public String[] noBypassMaintenanceGroupDisconnectMessage = new String[]{
            "<red>The server is under maintenance.",
            "<red>Please wait."
    };
}
