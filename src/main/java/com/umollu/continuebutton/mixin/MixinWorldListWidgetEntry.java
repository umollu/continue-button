package com.umollu.continuebutton.mixin;

import com.umollu.continuebutton.ContinueButtonMod;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.world.WorldListWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(WorldListWidget.Entry.class)
public class MixinWorldListWidgetEntry {
    @Inject(at = @At("RETURN"), method = "start()V")
    private void start(CallbackInfo info) {
        ContinueButtonMod.lastLocal = true;
        ContinueButtonMod.saveConfig();
    }
}