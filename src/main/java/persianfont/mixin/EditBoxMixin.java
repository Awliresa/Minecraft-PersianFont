package persianfont.mixin;

import net.minecraft.client.gui.components.EditBox;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.util.FormattedCharSequence;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import persianfont.PersianBidi;

@Mixin(EditBox.class)
public class EditBoxMixin {

    @Inject(method = "applyFormat", at = @At("HEAD"), cancellable = true)
    private void onApplyFormat(String value, int firstCharIndex, CallbackInfoReturnable<FormattedCharSequence> cir) {
        if (value != null && PersianBidi.containsArabicOrPersian(value)) {
            cir.setReturnValue(Language.getInstance().getVisualOrder(FormattedText.of(value)));
        }
    }
}
