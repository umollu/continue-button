package com.umollu.continuebutton.mixin;

import com.umollu.continuebutton.ContinueButtonMod;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class MixinClientPlayNetworkHandler {
    @Shadow @Nullable public abstract ServerInfo getServerInfo();

    @Inject(at = @At("TAIL"), method = "onGameJoin(Lnet/minecraft/network/packet/s2c/play/GameJoinS2CPacket;)V")
    public void onGameJoin(GameJoinS2CPacket packet, CallbackInfo info) {
        if(this.getServerInfo() != null) {
            ContinueButtonMod.lastLocal = false;
            ContinueButtonMod.serverName = this.getServerInfo().name;
            ContinueButtonMod.serverAddress = this.getServerInfo().address;
        } else {
            ContinueButtonMod.lastLocal = true;
        }
        ContinueButtonMod.saveConfig();
    }
}