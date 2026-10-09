package persianfont;

import java.util.HashMap;
import java.util.Map;

public class PersianReshaper {
    public static class Glyph {
        public final char isolated;
        public final char finalForm;
        public final char initial;
        public final char medial;
        public final boolean canConnectNext;

        public Glyph(char iso, char fin, char ini, char med, boolean conn) {
            this.isolated = iso;
            this.finalForm = fin;
            this.initial = ini;
            this.medial = med;
            this.canConnectNext = conn;
        }
    }

    private static final Map<Character, Glyph> GLYPHS = new HashMap<>();

    static {
        // Standard Arabic & Persian characters mapping to Unicode Presentation Forms
        put('\u0622', '\uFE81', '\uFE82', '\uFE81', '\uFE82', false); // آ
        put('\u0627', '\uFE8D', '\uFE8E', '\uFE8D', '\uFE8E', false); // ا
        put('\u0628', '\uFE8F', '\uFE90', '\uFE91', '\uFE92', true);  // ب
        put('\u067E', '\uFB56', '\uFB57', '\uFB58', '\uFB59', true);  // پ (Persian Peh)
        put('\u062A', '\uFE95', '\uFE96', '\uFE97', '\uFE98', true);  // ت
        put('\u062B', '\uFE99', '\uFE9A', '\uFE9B', '\uFE9C', true);  // ث
        put('\u062C', '\uFE9D', '\uFE9E', '\uFE9F', '\uFEA0', true);  // ج
        put('\u0686', '\uFB7A', '\uFB7B', '\uFB7C', '\uFB7D', true);  // چ (Persian Tcheh)
        put('\u062D', '\uFEA1', '\uFEA2', '\uFEA3', '\uFEA4', true);  // ح
        put('\u062E', '\uFEA5', '\uFEA6', '\uFEA7', '\uFEA8', true);  // خ
        put('\u062F', '\uFEA9', '\uFEAA', '\uFEA9', '\uFEAA', false); // د
        put('\u0630', '\uFEAB', '\uFEAC', '\uFEAB', '\uFEAC', false); // ذ
        put('\u0631', '\uFEAD', '\uFEAE', '\uFEAD', '\uFEAE', false); // ر
        put('\u0632', '\uFEAF', '\uFEB0', '\uFEAF', '\uFEB0', false); // ز
        put('\u0698', '\uFB8A', '\uFB8B', '\uFB8A', '\uFB8B', false); // ژ (Persian Jeh)
        put('\u0633', '\uFEB1', '\uFEB2', '\uFEB3', '\uFEB4', true);  // س
        put('\u0634', '\uFEB5', '\uFEB6', '\uFEB7', '\uFEB8', true);  // ش
        put('\u0635', '\uFEB9', '\uFEBA', '\uFEBB', '\uFEBC', true);  // ص
        put('\u0636', '\uFEBD', '\uFEBE', '\uFEBF', '\uFEC0', true);  // ض
        put('\u0637', '\uFEC1', '\uFEC2', '\uFEC3', '\uFEC4', true);  // ط
        put('\u0638', '\uFEC5', '\uFEC6', '\uFEC7', '\uFEC8', true);  // ظ
        put('\u0639', '\uFEC9', '\uFECA', '\uFECB', '\uFECC', true);  // ع
        put('\u063A', '\uFECD', '\uFECE', '\uFECF', '\uFED0', true);  // غ
        put('\u0641', '\uFED1', '\uFED2', '\uFED3', '\uFED4', true);  // ف
        put('\u0642', '\uFED5', '\uFED6', '\uFED7', '\uFED8', true);  // ق
        put('\u06A9', '\uFB8E', '\uFB8F', '\uFB90', '\uFB91', true);  // ک (Persian Keheh)
        put('\u0643', '\uFED9', '\uFEDA', '\uFEDB', '\uFEDC', true);  // ك (Arabic Kaf)
        put('\u06AF', '\uFB92', '\uFB93', '\uFB94', '\uFB95', true);  // گ (Persian Gaf)
        put('\u0644', '\uFEDD', '\uFEDE', '\uFEDF', '\uFEE0', true);  // ل
        put('\u0645', '\uFEE1', '\uFEE2', '\uFEE3', '\uFEE4', true);  // م
        put('\u0646', '\uFEE5', '\uFEE6', '\uFEE7', '\uFEE8', true);  // ن
        put('\u0648', '\uFEED', '\uFEEE', '\uFEED', '\uFEEE', false); // و
        put('\u0647', '\uFEE9', '\uFEEA', '\uFEEB', '\uFEEC', true);  // ه
        put('\u06CC', '\uFBFC', '\uFBFD', '\uFBFE', '\uFBFF', true);  // ی (Persian Yeh)
        put('\u064A', '\uFEF1', '\uFEF2', '\uFEF3', '\uFEF4', true);  // ي (Arabic Yeh)
        put('\u0649', '\uFEEF', '\uFEF0', '\uFEEF', '\uFEF0', false); // ى (Alef Maksura)
        put('\u0626', '\uFE89', '\uFE8A', '\uFE8B', '\uFE8C', true);  // ئ
        put('\u0624', '\uFE85', '\uFE86', '\uFE85', '\uFE86', false); // ؤ
        put('\u0629', '\uFE93', '\uFE94', '\uFE93', '\uFE94', false); // ة
        put('\u0623', '\uFE83', '\uFE84', '\uFE83', '\uFE84', false); // أ
        put('\u0625', '\uFE87', '\uFE88', '\uFE87', '\uFE88', false); // إ
        put('\u0621', '\uFE80', '\uFE80', '\uFE80', '\uFE80', false); // ء (Hamza isolated)
    }

    private static void put(char orig, char iso, char fin, char ini, char med, boolean conn) {
        GLYPHS.put(orig, new Glyph(iso, fin, ini, med, conn));
    }

    public static boolean isArabicOrPersian(char c) {
        return (c >= 0x0600 && c <= 0x06FF) || (c >= 0xFB50 && c <= 0xFDFF) || (c >= 0xFE70 && c <= 0xFEFF);
    }

    private static boolean isDiacritic(char c) {
        return c >= 0x064B && c <= 0x065F; // Harakat (Fatha, Damma, Kasra, Sukun, Tashdid, etc.)
    }

    public static String reshape(String text) {
        if (text == null || text.isEmpty()) return text;
        StringBuilder sb = new StringBuilder(text.length());
        int len = text.length();

        for (int i = 0; i < len; i++) {
            char c = text.charAt(i);

            // Handle Minecraft formatting codes (§) without breaking styling
            if (c == '\u00A7' && i + 1 < len) {
                sb.append(c).append(text.charAt(i + 1));
                i++;
                continue;
            }

            Glyph g = GLYPHS.get(c);
            if (g == null) {
                sb.append(c);
                continue;
            }

            // Look ahead for Lam-Alef ligature (skip formatting codes if any)
            if (c == '\u0644') {
                int nextIdx = i + 1;
                while (nextIdx + 1 < len && text.charAt(nextIdx) == '\u00A7') {
                    nextIdx += 2;
                }
                while (nextIdx < len && isDiacritic(text.charAt(nextIdx))) {
                    nextIdx++;
                }
                if (nextIdx < len) {
                    char nextC = text.charAt(nextIdx);
                    char la = 0;
                    if (nextC == '\u0622') la = '\uFEF5'; // لآ
                    else if (nextC == '\u0627') la = '\uFEF7'; // لا
                    else if (nextC == '\u0623') la = '\uFEF3'; // لأ
                    else if (nextC == '\u0625') la = '\uFEF9'; // لإ

                    if (la != 0) {
                        boolean prevConnects = false;
                        int prevIdx = i - 1;
                        while (prevIdx > 0 && text.charAt(prevIdx - 1) == '\u00A7') prevIdx -= 2;
                        if (prevIdx >= 0) {
                            Glyph prevG = GLYPHS.get(text.charAt(prevIdx));
                            if (prevG != null && prevG.canConnectNext) prevConnects = true;
                        }

                        sb.append(prevConnects ? (char)(la + 1) : la);
                        i = nextIdx;
                        continue;
                    }
                }
            }

            // Check previous connecting character (skipping formatting codes backwards)
            boolean prevConnects = false;
            int prevIdx = i - 1;
            while (prevIdx > 0 && text.charAt(prevIdx - 1) == '\u00A7') prevIdx -= 2;
            while (prevIdx >= 0 && isDiacritic(text.charAt(prevIdx))) prevIdx--;
            if (prevIdx >= 0) {
                Glyph prevG = GLYPHS.get(text.charAt(prevIdx));
                if (prevG != null && prevG.canConnectNext) {
                    prevConnects = true;
                }
            }

            // Check next connecting character (skipping formatting codes forwards)
            boolean nextConnects = false;
            int nextIdx = i + 1;
            while (nextIdx + 1 < len && text.charAt(nextIdx) == '\u00A7') nextIdx += 2;
            while (nextIdx < len && isDiacritic(text.charAt(nextIdx))) nextIdx++;
            if (nextIdx < len) {
                Glyph nextG = GLYPHS.get(text.charAt(nextIdx));
                if (nextG != null) {
                    nextConnects = true;
                }
            }

            // Shape according to context
            if (prevConnects && nextConnects && g.canConnectNext) {
                sb.append(g.medial);
            } else if (prevConnects) {
                sb.append(g.finalForm);
            } else if (nextConnects && g.canConnectNext) {
                sb.append(g.initial);
            } else {
                sb.append(g.isolated);
            }
        }
        return sb.toString();
    }
}