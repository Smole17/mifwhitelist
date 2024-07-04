package ru.smole.mifwhitelist;

import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import ru.smole.mifwhitelist.config.MIFWhitelistConfig;
import ru.smole.mifwhitelist.event.PlayerJoinHandler;

public class MIFWhitelist implements DedicatedServerModInitializer {

    public final static MIFWhitelistConfig CONFIG = MIFWhitelistConfig.createAndLoad();

    @Override
    public void onInitializeServer() {
        ServerPlayConnectionEvents.INIT.register(PlayerJoinHandler::onJoin);
    }
}
