package persianfont;

import com.ibm.icu.text.Bidi;

public class PersianBidi {
    public static boolean containsArabicOrPersian(String text) {
        if (text == null || text.isEmpty()) return false;
        int len = text.length();
        for (int i = 0; i < len; i++) {
            char c = text.charAt(i);
            if (c == '\u00A7') {
                i++; // skip Minecraft color code
                continue;
            }
            if ((c >= 0x0600 && c <= 0x06FF) || (c >= 0xFB50 && c <= 0xFDFF) || (c >= 0xFE70 && c <= 0xFEFF)) {
                return true;
            }
        }
        return false;
    }

    public static int findPrefixEnd(String s) {
        if (s == null) return 0;
        if (s.startsWith("<")) {
            int close = s.indexOf('>');
            if (close != -1) {
                int end = close + 1;
                while (end < s.length() && (s.charAt(end) == ' ' || s.charAt(end) == ':')) {
                    end++;
                }
                return end;
            }
        }
        int colon = s.indexOf(':');
        if (colon != -1 && colon < 24) {
            String sub = s.substring(0, colon);
            if (!sub.contains(" ") && !containsArabicOrPersian(sub)) {
                int end = colon + 1;
                while (end < s.length() && s.charAt(end) == ' ') end++;
                return end;
            }
        }
        return 0;
    }

    public static String reorderString(String text) {
        if (text == null || text.isEmpty()) return text;
        if (!containsArabicOrPersian(text)) return text;

        try {
            int prefixEnd = findPrefixEnd(text);
            String prefix = text.substring(0, prefixEnd);
            String msg = text.substring(prefixEnd);

            String shaped = PersianReshaper.reshape(msg);
            Bidi bidi = new Bidi(shaped, 1); // Forced RTL
            bidi.setReorderingMode(Bidi.REORDER_DEFAULT);
            String reordered = bidi.writeReordered(Bidi.DO_MIRRORING);

            return prefix + reordered;
        } catch (Exception e) {
            return text;
        }
    }
}