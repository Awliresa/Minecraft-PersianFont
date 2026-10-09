package persianfont.mixin;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GlyphSource;
import net.minecraft.client.gui.font.glyphs.BakedGlyph;
import net.minecraft.network.chat.FontDescription;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import persianfont.PersianBidi;
import persianfont.PersianFontConfig;
import persianfont.PersianReshaper;
import persianfont.gui.ScaledBakedGlyph;

@Mixin(Font.class)
public abstract class FontMixin {

    @Shadow
    private GlyphSource getGlyphSource(FontDescription fontDescription) {
        throw new AssertionError();
    }

    @Inject(method = "getGlyph", at = @At("HEAD"), cancellable = true)
    private void onGetGlyph(int codePoint, Style style, CallbackInfoReturnable<BakedGlyph> cir) {
        if (PersianReshaper.isArabicOrPersian((char) codePoint)) {
            // If the style explicitly specifies a custom font (e.g. preview button in dashboard), do NOT override it!
            if (style != null) {
                FontDescription fontDesc = style.getFont();
                if (fontDesc instanceof FontDescription.Resource res) {
                    Identifier id = res.id();
                    if (!id.equals(FontDescription.DEFAULT.id()) && !id.getPath().equals("default") && !id.getPath().equals("active")) {
                        return; // Let Minecraft use the font specified in style!
                    }
                }
            }

            Identifier activeFont = PersianFontConfig.getInstance().getActiveFontIdentifier();
            if (activeFont != null && !activeFont.getPath().equals("default")) {
                GlyphSource source = this.getGlyphSource(new FontDescription.Resource(activeFont));
                if (source != null) {
                    BakedGlyph g = source.getGlyph(codePoint);
                    if (g != null) {
                        float scale = PersianFontConfig.getInstance().fontScale;
                        if (scale != 1.0f) {
                            cir.setReturnValue(new ScaledBakedGlyph(g, scale));
                        } else {
                            cir.setReturnValue(g);
                        }
                    }
                }
            }
        }
    }

    @Inject(method = "lambda$new$0", at = @At("HEAD"), cancellable = true)
    private void onGetWidth(int codePoint, Style style, CallbackInfoReturnable<Float> cir) {
        if (PersianReshaper.isArabicOrPersian((char) codePoint)) {
            if (style != null) {
                FontDescription fontDesc = style.getFont();
                if (fontDesc instanceof FontDescription.Resource res) {
                    Identifier id = res.id();
                    if (!id.equals(FontDescription.DEFAULT.id()) && !id.getPath().equals("default") && !id.getPath().equals("active")) {
                        return;
                    }
                }
            }

            Identifier activeFont = PersianFontConfig.getInstance().getActiveFontIdentifier();
            if (activeFont != null && !activeFont.getPath().equals("default")) {
                GlyphSource source = this.getGlyphSource(new FontDescription.Resource(activeFont));
                if (source != null) {
                    BakedGlyph g = source.getGlyph(codePoint);
                    if (g != null) {
                        boolean bold = style.isBold() || PersianFontConfig.getInstance().forceBold;
                        float advance = g.info().getAdvance(bold);
                        float scale = PersianFontConfig.getInstance().fontScale;
                        cir.setReturnValue(advance * scale);
                    }
                }
            }
        }
    }

    @Inject(method = "bidirectionalShaping", at = @At("HEAD"), cancellable = true)
    private void onBidirectionalShaping(String text, CallbackInfoReturnable<String> cir) {
        if (text != null && PersianBidi.containsArabicOrPersian(text)) {
            cir.setReturnValue(PersianBidi.reorderString(text));
        }
    }

    @ModifyVariable(method = "prepareText(Ljava/lang/String;FFIZI)Lnet/minecraft/client/gui/Font$PreparedText;", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private String onPrepareString(String text) {
        if (text != null && PersianBidi.containsArabicOrPersian(text)) {
            Font self = (Font) (Object) this;
            if (!self.isBidirectional()) {
                return self.bidirectionalShaping(text);
            }
        }
        return text;
    }

    @ModifyVariable(method = "width(Ljava/lang/String;)I", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private String onStringWidth(String text) {
        if (text != null && PersianBidi.containsArabicOrPersian(text)) {
            Font self = (Font) (Object) this;
            return self.bidirectionalShaping(text);
        }
        return text;
    }
}