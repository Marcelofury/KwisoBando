package kwiso.bando;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

import kwiso.bando.audio.AudioManager;
import kwiso.bando.config.GameConfig;
import kwiso.bando.config.GameSettings;
import kwiso.bando.screens.MenuScreen;

public class KwisoBandoGame extends Game {
    @Override
    public void create() {
        GameSettings.init();
        AudioManager.get().init();
        if (GameSettings.isFullscreen()) {
            Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
        } else {
            Gdx.graphics.setWindowedMode(GameConfig.VIEW_WIDTH, GameConfig.VIEW_HEIGHT);
        }
        setScreen(new MenuScreen(this));
    }

    @Override
    public void dispose() {
        AudioManager.get().dispose();
        super.dispose();
    }
}
