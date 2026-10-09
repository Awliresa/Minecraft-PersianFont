package persianfont.mixin;

import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.input.KeyEvent;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import persianfont.PersianFontConfig;
import persianfont.gui.PersianFontScreen;

@Mixin(KeyboardHandler.class)
public class KeyboardHandlerMixin {

    @Inject(method = "keyPress", at = @At("HEAD"))
    private void onKeyPress(long window, int action, KeyEvent event, CallbackInfo ci) {
        // GLFW_PRESS is 1
        int targetKey = PersianFontConfig.getInstance().toggleKey;
        if (action == 1 && targetKey != 0 && event.key() == targetKey) {
            Minecraft mc = Minecraft.getInstance();
            if (mc.gui.screen() == null) {
                mc.gui.setScreen(new PersianFontScreen(null));
            }
        }
    }
}