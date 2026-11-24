package kwiso.bando.config;

import com.badlogic.gdx.graphics.Color;

public final class Palette {
    public static final Color BACKGROUND = hex("#1E242B");
    public static final Color DEEP = hex("#18100B");
    public static final Color TIMBER = hex("#2B1D14");
    public static final Color TIMBER_LIGHT = hex("#3B2A1E");
    public static final Color EARTH = hex("#8C3B1E");
    public static final Color EARTH_DARK = hex("#5A210F");
    public static final Color PATH = hex("#B46A3A");
    public static final Color PRIMARY = hex("#D95D39");
    public static final Color SECONDARY = hex("#2E7D32");
    public static final Color OCHRE = hex("#F4A261");
    public static final Color PARCHMENT = hex("#F7F3E9");
    public static final Color MUTED = hex("#A78B83");
    public static final Color ALERT = hex("#E5484D");
    public static final Color RUNNER = hex("#38BDF8");
    public static final Color VISION = new Color(1f, 0.83f, 0.45f, 0.22f);

    private Palette() {
    }

    private static Color hex(String value) {
        int rgb = Integer.parseInt(value.substring(1), 16);
        return new Color(
            ((rgb >> 16) & 0xFF) / 255f,
            ((rgb >> 8) & 0xFF) / 255f,
            (rgb & 0xFF) / 255f,
            1f
        );
    }
}
