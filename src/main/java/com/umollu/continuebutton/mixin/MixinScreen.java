package com.umollu.continuebutton.mixin;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Screen.class)
public abstract class MixinScreen {

    @Shadow public int width;

    @Inject(at = @At("HEAD"), method = "addButton", cancellable = true)
    public void addButtonInject(AbstractButtonWidget button, CallbackInfoReturnable callback) {
        Text message = button.getMessage();
        if ((Object)this instanceof TitleScreen) {
            if(message.equals(new TranslatableText("menu.singleplayer"))) {
                button.x = this.width / 2 + 2;
                button.setWidth(98);
            }
        }
    }
}