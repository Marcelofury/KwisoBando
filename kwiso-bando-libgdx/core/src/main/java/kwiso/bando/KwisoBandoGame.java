package kwiso.bando;

import com.badlogic.gdx.Game;

import kwiso.bando.screens.MenuScreen;

public class KwisoBandoGame extends Game {
    @Override
    public void create() {
        setScreen(new MenuScreen(this));
    }
}
