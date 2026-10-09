package persianfont;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.resources.Identifier;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class PersianFontConfig {
    private static final File CONFIG_FILE = new File("config/persianfont.json");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static PersianFontConfig INSTANCE;

    public String activeFont = "persianfont:sorena";
    public boolean fixEnglishInPersian = true;
    public boolean chatRightAlign = false;
    public boolean forceBold = false;
    public boolean forceItalic = false;
    public float fontScale = 1.0f;
    public int toggleKey = org.lwjgl.glfw.GLFW.GLFW_KEY_P;

    public void cycleFontScale() {
        if (fontScale <= 0.90f) {
            fontScale = 1.0f;
        } else if (fontScale <= 1.05f) {
            fontScale = 1.15f;
        } else if (fontScale <= 1.20f) {
            fontScale = 1.30f;
        } else {
            fontScale = 0.85f;
        }
        save();
    }

    public String getFontScaleLabel() {
        if (fontScale <= 0.90f) return "کوچک (85%)";
        if (fontScale <= 1.05f) return "معمولی (100%)";
        if (fontScale <= 1.20f) return "بزرگ (115%)";
        return "خیلی بزرگ (130%)";
    }

    public static PersianFontConfig getInstance() {
        if (INSTANCE == null) {
            INSTANCE = load();
        }
        return INSTANCE;
    }

    public Identifier getActiveFontIdentifier() {
        if (activeFont == null || activeFont.isEmpty() || activeFont.equals("minecraft:default")) {
            return Identifier.parse("minecraft:default");
        }
        return Identifier.parse(activeFont);
    }

    public void setActiveFont(String fontId) {
        this.activeFont = fontId;
        save();
    }

    public static PersianFontConfig load() {
        if (CONFIG_FILE.exists()) {
            try (FileReader reader = new FileReader(CONFIG_FILE)) {
                PersianFontConfig cfg = GSON.fromJson(reader, PersianFontConfig.class);
                if (cfg != null) return cfg;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        PersianFontConfig cfg = new PersianFontConfig();
        cfg.save();
        return cfg;
    }

    public void save() {
        try {
            File parent = CONFIG_FILE.getParentFile();
            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }
            try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
                GSON.toJson(this, writer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}