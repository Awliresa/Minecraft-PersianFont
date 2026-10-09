package persianfont.mixin;

import com.google.common.collect.Lists;
import com.ibm.icu.lang.UCharacter;
import com.ibm.icu.text.Bidi;
import com.ibm.icu.text.BidiRun;
import net.minecraft.client.resources.language.FormattedBidiReorder;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.SubStringSource;
import net.minecraft.util.FormattedCharSequence;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import persianfont.PersianBidi;
import persianfont.PersianFontConfig;
import persianfont.PersianReshaper;

import java.util.List;

@Mixin(FormattedBidiReorder.class)
public class FormattedBidiReorderMixin {

    @Inject(method = "shape", at = @At("HEAD"), cancellable = true)
    private static void onShape(String text, CallbackInfoReturnable<String> cir) {
        if (text != null && PersianBidi.containsArabicOrPersian(text)) {
            cir.setReturnValue(PersianReshaper.reshape(text));
        }
    }

    @Inject(method = "reorder", at = @At("HEAD"), cancellable = true)
    private static void onReorder(FormattedText formattedText, boolean defaultRightToLeft, CallbackInfoReturnable<FormattedCharSequence> cir) {
        if (PersianFontConfig.getInstance().fixEnglishInPersian) {
            SubStringSource source = SubStringSource.create(formattedText, UCharacter::getMirror, PersianReshaper::reshape);
            String plainText = source.getPlainText();
            if (plainText != null && PersianBidi.containsArabicOrPersian(plainText)) {
                int prefixEnd = PersianBidi.findPrefixEnd(plainText);
                List<FormattedCharSequence> list = Lists.newArrayList();

                if (prefixEnd > 0) {
                    list.addAll(source.substring(0, prefixEnd, false));
                }

                String msg = plainText.substring(prefixEnd);
                Bidi bidi = new Bidi(msg, 1); // Forced RTL
                bidi.setReorderingMode(Bidi.REORDER_DEFAULT);
                int runs = bidi.countRuns();
                for (int i = 0; i < runs; i++) {
                    BidiRun run = bidi.getVisualRun(i);
                    int start = prefixEnd + run.getStart();
                    int len = run.getLength();
                    boolean isOdd = run.isOddRun();
                    list.addAll(source.substring(start, len, isOdd));
                }

                cir.setReturnValue(FormattedCharSequence.composite(list));
            }
        }
    }
}