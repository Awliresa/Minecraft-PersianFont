package persianfont.mixin;

import net.minecraft.network.chat.Style;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import persianfont.PersianFontConfig;

@Mixin(Style.class)
public class StyleMixin {

    @Inject(method = "isBold", at = @At("HEAD"), cancellable = true)
    private void onIsBold(CallbackInfoReturnable<Boolean> cir) {
        if (PersianFontConfig.getInstance().forceBold) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "isItalic", at = @At("HEAD"), cancellable = true)
    private void onIsItalic(CallbackInfoReturnable<Boolean> cir) {
        if (PersianFontConfig.getInstance().forceItalic) {
            cir.setReturnValue(true);
        }
    }
}
