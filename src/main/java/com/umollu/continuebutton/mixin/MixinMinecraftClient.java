package com.umollu.continuebutton.mixin;

import com.umollu.continuebutton.ContinueButtonMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.client.world.ClientWorld;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public abstract class MixinMinecraftClient {
    @Shadow public @Nullable abstract ServerInfo getCurrentServerEntry();

    @Inject(at = @At("TAIL"), method = "joinWorld(Lnet/minecraft/client/world/ClientWorld;)V")
    public void joinWorld(ClientWorld world, CallbackInfo info) {
        if(this.getCurrentServerEntry() != null) {
            ContinueButtonMod.lastLocal = false;
            ContinueButtonMod.serverName = this.getCurrentServerEntry().name;
            ContinueButtonMod.serverAddress = this.getCurrentServerEntry().address;
        } else {
            ContinueButtonMod.lastLocal = true;
        }
        ContinueButtonMod.saveConfig();
    }
}