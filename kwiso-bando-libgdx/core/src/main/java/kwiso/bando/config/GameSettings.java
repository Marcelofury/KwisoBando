package kwiso.bando.config;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public final class GameSettings {
    private static final String NAME = "kwiso-bando-settings";
    private static Preferences preferences;

    private GameSettings() {
    }

    public static void init() {
        preferences = Gdx.app.getPreferences(NAME);
    }

    private static Preferences store() {
        if (preferences == null) {
            preferences = Gdx.app.getPreferences(NAME);
        }
        return preferences;
    }

    public static boolean isSoundEnabled() {
        return store().getBoolean("sound", true);
    }

    public static void setSoundEnabled(boolean value) {
        store().putBoolean("sound", value).flush();
    }

    public static boolean isFullscreen() {
        return store().getBoolean("fullscreen", false);
    }

    public static void setFullscreen(boolean value) {
        store().putBoolean("fullscreen", value).flush();
    }

    public static int getBestScore() {
        return store().getInteger("bestScore", 0);
    }

    public static void setBestScore(int value) {
        store().putInteger("bestScore", value).flush();
    }
}
