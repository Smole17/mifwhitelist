package ru.smole.mifwhitelist;

import net.fabricmc.api.DedicatedServerModInitializer;
import ru.smole.mifwhitelist.config.MIFWhitelistConfig;

public class MIFWhitelist implements DedicatedServerModInitializer {

    public final static MIFWhitelistConfig CONFIG = MIFWhitelistConfig.createAndLoad();

    @Override
    public void onInitializeServer() {}
}
