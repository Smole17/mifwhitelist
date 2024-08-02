package ru.smole.mifwhitelist.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerLoginNetworkHandler;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.smole.mifwhitelist.event.PlayerJoinHandler;

@Mixin(ServerLoginNetworkHandler.class)
public class ServerLoginNetworkHandlerMixin {
    
    @Shadow private @Nullable GameProfile profile;
    
    @Shadow @Final private MinecraftServer server;
    
    @Shadow @Final public ClientConnection connection;
    
    @Inject(at = @At(shift = At.Shift.BEFORE, value = "INVOKE", target = "Lnet/minecraft/server/PlayerManager;checkCanJoin(Ljava/net/SocketAddress;Lcom/mojang/authlib/GameProfile;)Lnet/minecraft/text/Text;"), method = "acceptPlayer")
    public void onAcceptPlayer(CallbackInfo ci) {
        PlayerJoinHandler.onJoin(connection, profile, server);
    }
}
