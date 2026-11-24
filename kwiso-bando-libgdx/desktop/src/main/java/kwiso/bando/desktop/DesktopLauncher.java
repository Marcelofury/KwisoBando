package kwiso.bando.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

import kwiso.bando.KwisoBandoGame;

public final class DesktopLauncher {
    private DesktopLauncher() {
    }

    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("Kwiso Bando: The Base Race");
        config.setWindowedMode(1280, 720);
        config.setResizable(false);
        config.setForegroundFPS(60);
        config.useVsync(true);
        new Lwjgl3Application(new KwisoBandoGame(), config);
    }
}
