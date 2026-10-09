package persianfont.mixin;

import net.minecraft.client.gui.font.FontManager;
import net.minecraft.client.gui.font.FontSet;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import persianfont.PersianFontClient;
import persianfont.PersianFontConfig;

import java.util.Map;

@Mixin(FontManager.class)
public class FontManagerMixin {

    @Shadow
    @Final
    private Map<Identifier, FontSet> fontSets;

    @Inject(method = "getFontSetRaw", at = @At("HEAD"), cancellable = true)
    private void onGetFontSetRaw(Identifier id, CallbackInfoReturnable<FontSet> cir) {
        if (id != null && PersianFontClient.ACTIVE_FONT_ID.equals(id)) {
            Identifier target = PersianFontConfig.getInstance().getActiveFontIdentifier();
            if (target != null && !target.equals(PersianFontClient.ACTIVE_FONT_ID)) {
                FontSet set = this.fontSets.get(target);
                if (set != null) {
                    cir.setReturnValue(set);
                }
            }
        }
    }
}