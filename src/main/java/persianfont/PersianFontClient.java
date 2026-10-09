package persianfont;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.resources.Identifier;

public class PersianFontClient implements ClientModInitializer {
    public static final String MOD_ID = "persianfont";
    public static final String MOD_NAME = "PersianFont | فونت فارسی";

    public static final Identifier ACTIVE_FONT_ID = Identifier.fromNamespaceAndPath(MOD_ID, "active");

    public static final FontEntry[] AVAILABLE_FONTS = new FontEntry[] {
        new FontEntry(
            "persianfont:sorena",
            "فونت فارسی سورنا",
            "پیکسلی و هندسی (طراحی محمد درویشی)",
            "سلام ماینکرفت! تست جمله فارسی و English 123"
        ),
        new FontEntry(
            "persianfont:unixel",
            "فونت فارسی یونیکسل",
            "پیکسلی الهام‌گرفته از یونی‌فونت و خط نسخ",
            "سلام ماینکرفت! تست جمله فارسی و English 123"
        ),
        new FontEntry(
            "persianfont:rooyin",
            "فونت فارسی رویین",
            "پیکسلی هماهنگ با سبک رترو ماینکرفت",
            "سلام ماینکرفت! تست جمله فارسی و English 123"
        ),
        new FontEntry(
            "persianfont:vazirmatn",
            "فونت فارسی وزیرمتن",
            "مدرن، خوانا و بسیار روان (صابر راستی‌کردار)",
            "سلام ماینکرفت! تست جمله فارسی و English 123"
        ),
        new FontEntry(
            "persianfont:sahel",
            "فونت فارسی ساحل",
            "هندسی، منظم و مدرن",
            "سلام ماینکرفت! تست جمله فارسی و English 123"
        ),
        new FontEntry(
            "persianfont:shabnam",
            "فونت فارسی شبنم",
            "حروف گرد، شاداب و چشم‌نواز",
            "سلام ماینکرفت! تست جمله فارسی و English 123"
        ),
        new FontEntry(
            "minecraft:default",
            "فونت پیش‌فرض بازی",
            "وانیلا ماینکرفت بدون قلم سفارشی",
            "Minecraft Default Vanilla Font"
        )
    };

    public static record FontEntry(String id, String displayName, String description, String sampleText) {
        public Identifier identifier() {
            return Identifier.parse(id);
        }
    }

    @Override
    public void onInitializeClient() {
        PersianFontConfig.getInstance();
        System.out.println("[" + MOD_NAME + "] Initialized successfully with " + AVAILABLE_FONTS.length + " Persian fonts!");
    }
}