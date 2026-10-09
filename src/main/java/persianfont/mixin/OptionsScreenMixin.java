package persianfont.mixin;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.OptionsScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import persianfont.PersianFontClient;
import persianfont.gui.PersianFontScreen;

@Mixin(OptionsScreen.class)
public class OptionsScreenMixin extends Screen {

    protected OptionsScreenMixin(Component title) {
        super(title);
    }

    @Inject(method = "init", at = @At("TAIL"))
    private void onInit(CallbackInfo ci) {
        // Place button at top right of the options screen
        int btnWidth = 140;
        int btnHeight = 20;
        int x = this.width - btnWidth - 8;
        int y = 6;

        this.addRenderableWidget(Button.builder(Component.literal(PersianFontClient.MOD_NAME), (btn) -> {
            if (this.minecraft != null) {
                this.minecraft.gui.setScreen(new PersianFontScreen(this));
            }
        }).bounds(x, y, btnWidth, btnHeight).build());
    }
}