package persianfont.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FontDescription;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import org.lwjgl.glfw.GLFW;
import persianfont.PersianFontClient;
import persianfont.PersianFontConfig;
import persianfont.mixin.MinecraftAccessor;

public class PersianFontScreen extends Screen {
    private final Screen parent;
    private boolean listeningForKey = false;

    public PersianFontScreen(Screen parent) {
        super(Component.literal(PersianFontClient.MOD_NAME));
        this.parent = parent;
    }

    private static String getKeyName(int key) {
        if (key >= GLFW.GLFW_KEY_A && key <= GLFW.GLFW_KEY_Z) {
            return String.valueOf((char) ('A' + (key - GLFW.GLFW_KEY_A)));
        }
        if (key >= GLFW.GLFW_KEY_0 && key <= GLFW.GLFW_KEY_9) {
            return String.valueOf((char) ('0' + (key - GLFW.GLFW_KEY_0)));
        }
        if (key == GLFW.GLFW_KEY_GRAVE_ACCENT) return "~";
        if (key == GLFW.GLFW_KEY_TAB) return "TAB";
        if (key == GLFW.GLFW_KEY_F1) return "F1";
        if (key == GLFW.GLFW_KEY_F2) return "F2";
        if (key == GLFW.GLFW_KEY_F3) return "F3";
        if (key == GLFW.GLFW_KEY_F4) return "F4";
        if (key == GLFW.GLFW_KEY_F5) return "F5";
        if (key == GLFW.GLFW_KEY_F6) return "F6";
        if (key == GLFW.GLFW_KEY_F7) return "F7";
        if (key == GLFW.GLFW_KEY_F8) return "F8";
        if (key == GLFW.GLFW_KEY_F9) return "F9";
        if (key == GLFW.GLFW_KEY_F10) return "F10";
        if (key == GLFW.GLFW_KEY_F11) return "F11";
        if (key == GLFW.GLFW_KEY_F12) return "F12";
        String glfwName = GLFW.glfwGetKeyName(key, 0);
        if (glfwName != null && !glfwName.isEmpty()) return glfwName.toUpperCase();
        return "Key #" + key;
    }

    @Override
    protected void init() {
        super.init();
        PersianFontConfig config = PersianFontConfig.getInstance();

        int leftX = this.width / 2 - 175;
        int rightX = this.width / 2 + 5;
        int startY = 36;
        int btnWidth = 170;
        int btnHeight = 21;
        int spacing = 25;

        PersianFontClient.FontEntry[] fonts = PersianFontClient.AVAILABLE_FONTS;

        // Render font choice buttons - Each button displays in ITS OWN respective font!
        for (int i = 0; i < fonts.length; i++) {
            PersianFontClient.FontEntry fontEntry = fonts[i];
            int col = i % 2;
            int row = i / 2;

            int x = (col == 0) ? leftX : rightX;
            int y = startY + (row * spacing);

            // Center the 7th font button
            if (i == 6) {
                x = this.width / 2 - btnWidth / 2;
            }

            boolean isActive = config.activeFont.equalsIgnoreCase(fontEntry.id());

            String labelText = (isActive ? "(فعال) " : "") + fontEntry.displayName();
            
            // Set font specifically for this button's title
            Style btnStyle = Style.EMPTY.withFont(new FontDescription.Resource(fontEntry.identifier()));
            if (isActive) {
                btnStyle = btnStyle.withColor(0x38BDF8).withBold(true);
            }
            MutableComponent labelComp = Component.literal(labelText).withStyle(btnStyle);

            this.addRenderableWidget(Button.builder(labelComp, (btn) -> {
                config.setActiveFont(fontEntry.id());
                Minecraft mc = Minecraft.getInstance();
                if (mc instanceof MinecraftAccessor accessor) {
                    accessor.invokeUpdateFontOptions();
                }
                this.rebuildWidgets();
            }).bounds(x, y, btnWidth, btnHeight).build());
        }

        int optionsY = startY + (4 * spacing) + 2;

        // Row 1: Bold & Italic toggles
        String boldLabel = "ضخیم (Bold): " + (config.forceBold ? "روشن" : "خاموش");
        this.addRenderableWidget(Button.builder(Component.literal(boldLabel), (btn) -> {
            config.forceBold = !config.forceBold;
            config.save();
            this.rebuildWidgets();
        }).bounds(leftX, optionsY, btnWidth, btnHeight).build());

        String italicLabel = "مورب (Italic): " + (config.forceItalic ? "روشن" : "خاموش");
        this.addRenderableWidget(Button.builder(Component.literal(italicLabel), (btn) -> {
            config.forceItalic = !config.forceItalic;
            config.save();
            this.rebuildWidgets();
        }).bounds(rightX, optionsY, btnWidth, btnHeight).build());

        // Row 2: Font Size & Fix English words toggles
        int optionsY2 = optionsY + spacing;
        String sizeLabel = "اندازه قلم: " + config.getFontScaleLabel();
        this.addRenderableWidget(Button.builder(Component.literal(sizeLabel), (btn) -> {
            config.cycleFontScale();
            this.rebuildWidgets();
        }).bounds(leftX, optionsY2, btnWidth, btnHeight).build());

        String bidiLabel = "اصلاح انگلیسی: " + (config.fixEnglishInPersian ? "روشن" : "خاموش");
        this.addRenderableWidget(Button.builder(Component.literal(bidiLabel), (btn) -> {
            config.fixEnglishInPersian = !config.fixEnglishInPersian;
            config.save();
            this.rebuildWidgets();
        }).bounds(rightX, optionsY2, btnWidth, btnHeight).build());

        // Row 3: Hotkey configuration (centered)
        int optionsY3 = optionsY2 + spacing;
        String keyLabel = listeningForKey ? "کلید: (فشار دهید...)" : "کلید میانبر: (" + getKeyName(config.toggleKey) + ")";
        this.addRenderableWidget(Button.builder(Component.literal(keyLabel), (btn) -> {
            this.listeningForKey = !this.listeningForKey;
            this.rebuildWidgets();
        }).bounds(this.width / 2 - btnWidth / 2, optionsY3, btnWidth, btnHeight).build());

        // Done button
        this.addRenderableWidget(Button.builder(Component.literal("بازگشت و ذخیره"), (btn) -> {
            this.onClose();
        }).bounds(this.width / 2 - 80, this.height - 24, 160, 20).build());
    }

    @Override
    public boolean keyPressed(KeyEvent event) {
        if (listeningForKey) {
            if (event.key() != GLFW.GLFW_KEY_ESCAPE) {
                PersianFontConfig.getInstance().toggleKey = event.key();
                PersianFontConfig.getInstance().save();
            }
            listeningForKey = false;
            this.rebuildWidgets();
            return true;
        }
        return super.keyPressed(event);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor extractor, int mouseX, int mouseY, float partialTick) {
        this.extractMenuBackground(extractor);

        // Header Title
        extractor.centeredText(this.font, Component.literal(PersianFontClient.MOD_NAME), this.width / 2, 8, 0x38BDF8);

        // Subtitle
        String subtitleText = "تنظیمات فونت فارسی، اندازه، سبک و کلید میانبر";
        extractor.centeredText(this.font, Component.literal(subtitleText), this.width / 2, 22, 0x94A3B8);

        super.extractRenderState(extractor, mouseX, mouseY, partialTick);
    }

    @Override
    public void onClose() {
        if (this.minecraft != null) {
            this.minecraft.gui.setScreen(this.parent);
        }
    }
}