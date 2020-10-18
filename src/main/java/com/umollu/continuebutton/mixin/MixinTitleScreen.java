package com.umollu.continuebutton.mixin;

import net.minecraft.SharedConstants;
import net.minecraft.client.gui.screen.*;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.gui.screen.world.EditWorldScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.world.level.storage.LevelStorage;
import net.minecraft.world.level.storage.LevelStorageException;
import net.minecraft.world.level.storage.LevelSummary;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Mixin(TitleScreen.class)
public class MixinTitleScreen extends Screen {

    private  LevelSummary level = null;

    protected MixinTitleScreen(Text title) {
        super(title);
    }

    @Inject(at = @At("RETURN"), method = "initWidgetsNormal(II)V")
    public void drawMenuButton(int y, int spacingY, CallbackInfo info) {
        this.addButton(new ButtonWidget(this.width / 2 - 100, y, 98, 20, new TranslatableText("continuebutton.continueButtonTitle"), button -> {

            LevelStorage levelStorage = this.client.getLevelStorage();
            List<LevelSummary> levels = null;
            try {
                levels = levelStorage.getLevelList();
            } catch (LevelStorageException e) {
                e.printStackTrace();
            }
            if (levels.isEmpty()) {
                this.client.openScreen(CreateWorldScreen.method_31130((Screen)null));
            } else {
                Collections.sort(levels);
                level =  levels.get(0);

                if (!level.isLocked()) {
                    if (level.isOutdatedLevel()) {
                        Text text = new TranslatableText("selectWorld.backupQuestion");
                        Text text2 = new TranslatableText("selectWorld.backupWarning", new Object[]{level.getVersion(), SharedConstants.getGameVersion().getName()});
                        this.client.openScreen(new BackupPromptScreen(this, (bl, bl2) -> {
                            if (bl) {
                                String string = level.getName();

                                try {
                                    LevelStorage.Session session = this.client.getLevelStorage().createSession(string);
                                    Throwable var5 = null;

                                    try {
                                        EditWorldScreen.backupLevel(session);
                                    } catch (Throwable var15) {
                                        var5 = var15;
                                        throw var15;
                                    } finally {
                                        if (session != null) {
                                            if (var5 != null) {
                                                try {
                                                    session.close();
                                                } catch (Throwable var14) {
                                                    var5.addSuppressed(var14);
                                                }
                                            } else {
                                                session.close();
                                            }
                                        }

                                    }
                                } catch (IOException var17) {
                                    SystemToast.addWorldAccessFailureToast(this.client, string);
                                    //WorldListWidget.LOGGER.error("Failed to backup level {}", string, var17);
                                }
                            }

                            start();
                        }, text, text2, false));
                    } else if (level.isFutureLevel()) {
                        this.client.openScreen(new ConfirmScreen((bl) -> {
                            if (bl) {
                                try {
                                    start();
                                } catch (Exception var3) {
                                    //WorldListWidget.LOGGER.error("Failure to open 'future world'", var3);
                                    this.client.openScreen(new NoticeScreen(() -> {
                                        this.client.openScreen(this);
                                    }, new TranslatableText("selectWorld.futureworld.error.title"), new TranslatableText("selectWorld.futureworld.error.text")));
                                }
                            } else {
                                this.client.openScreen(this);
                            }

                        }, new TranslatableText("selectWorld.versionQuestion"), new TranslatableText("selectWorld.versionWarning", new Object[]{this.level.getVersion(), new TranslatableText("selectWorld.versionJoinButton"), ScreenTexts.CANCEL})));
                    } else {
                        start();
                    }

                }
            }
        }));
    }

    private void start() {
        this.client.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
        if (this.client.getLevelStorage().levelExists(this.level.getName())) {
            this.method_29990();
            this.client.startIntegratedServer(this.level.getName());
        }


    }

    private void method_29990() {
        this.client.method_29970(new SaveLevelScreen(new TranslatableText("selectWorld.data_read")));
    }
}