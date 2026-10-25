package persianfont.gui;

import com.mojang.blaze3d.font.GlyphInfo;
import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import com.mojang.renderpearl.api.textures.GpuTextureView;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.font.TextRenderable;
import net.minecraft.client.gui.font.glyphs.BakedGlyph;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.network.chat.Style;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;

public class ScaledBakedGlyph implements BakedGlyph {
    private final BakedGlyph delegate;
    private final float scale;
    private final GlyphInfo scaledInfo;

    public ScaledBakedGlyph(BakedGlyph delegate, float scale) {
        this.delegate = delegate;
        this.scale = scale;
        GlyphInfo origInfo = delegate.info();
        this.scaledInfo = new GlyphInfo() {
            @Override
            public float getAdvance() {
                return origInfo.getAdvance() * scale;
            }

            @Override
            public float getAdvance(boolean bold) {
                return origInfo.getAdvance(bold) * scale;
            }

            @Override
            public float getBoldOffset() {
                return origInfo.getBoldOffset();
            }

            @Override
            public float getShadowOffset() {
                return origInfo.getShadowOffset();
            }
        };
    }

    @Override
    public GlyphInfo info() {
        return this.scaledInfo;
    }

    @Override
    public TextRenderable.Styled createGlyph(float x, float y, int color, int shadowColor, Style style, float boldOffset, float shadowOffset) {
        TextRenderable.Styled original = this.delegate.createGlyph(x, y, color, shadowColor, style, boldOffset, shadowOffset);
        if (scale == 1.0f || original == null) {
            return original;
        }

        return new TextRenderable.Styled() {
            @Override
            public void render(Matrix4fc matrix, VertexConsumer consumer, int light, boolean outline) {
                Matrix4f scaledMatrix = new Matrix4f(matrix);
                scaledMatrix.translate(x, y, 0.0f)
                            .scale(scale, scale, 1.0f)
                            .translate(-x, -y, 0.0f);
                original.render(scaledMatrix, consumer, light, outline);
            }

            @Override
            public RenderType renderType(Font.DisplayMode mode) {
                return original.renderType(mode);
            }

            @Override
            public GpuTextureView textureView() {
                return original.textureView();
            }

            @Override
            public RenderPipeline guiPipeline() {
                return original.guiPipeline();
            }

            @Override
            public float left() {
                return x + (original.left() - x) * scale;
            }

            @Override
            public float top() {
                return y + (original.top() - y) * scale;
            }

            @Override
            public float right() {
                return x + (original.right() - x) * scale;
            }

            @Override
            public float bottom() {
                return y + (original.bottom() - y) * scale;
            }

            @Override
            public Style style() {
                return original.style();
            }

            @Override
            public float activeLeft() {
                return original.activeLeft();
            }

            @Override
            public float activeTop() {
                return original.activeTop();
            }

            @Override
            public float activeRight() {
                return x + (original.activeRight() - x) * scale;
            }

            @Override
            public float activeBottom() {
                return original.activeBottom();
            }
        };
    }
}
