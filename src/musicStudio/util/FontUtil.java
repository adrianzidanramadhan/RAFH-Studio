package musicStudio.util;

import java.awt.Font;

public final class FontUtil {

    private FontUtil() {
    }

    public static Font title() {
        return new Font("SansSerif", Font.BOLD, 28);
    }

    public static Font heading() {
        return new Font("SansSerif", Font.BOLD, 22);
    }

    public static Font subHeading() {
        return new Font("SansSerif", Font.BOLD, 18);
    }

    public static Font normal() {
        return new Font("SansSerif", Font.PLAIN, 14);
    }

    public static Font small() {
        return new Font("SansSerif", Font.PLAIN, 12);
    }

    public static Font button() {
        return new Font("SansSerif", Font.BOLD, 14);
    }

}