package kwiso.bando.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import kwiso.bando.KwisoBandoGame;
import kwiso.bando.audio.AudioManager;
import kwiso.bando.config.GameSettings;
import kwiso.bando.config.Palette;

import static kwiso.bando.config.GameConfig.VIEW_HEIGHT;
import static kwiso.bando.config.GameConfig.VIEW_WIDTH;

public class MenuScreen extends BaseScreen {
    private final Rectangle playButton = new Rectangle(470f, 330f, 340f, 64f);
    private final Rectangle rulesButton = new Rectangle(470f, 254f, 340f, 64f);
    private final Rectangle settingsButton = new Rectangle(470f, 178f, 340f, 64f);
    private final Rectangle exitButton = new Rectangle(470f, 102f, 340f, 58f);

    public MenuScreen(KwisoBandoGame game) {
        super(game);
    }

    @Override
    public void render(float delta) {
        clearScreen();

        shapes.begin(ShapeRenderer.ShapeType.Filled);
        shapes.setColor(Palette.BACKGROUND);
        shapes.rect(0f, 0f, VIEW_WIDTH, VIEW_HEIGHT);
        shapes.setColor(Palette.EARTH_DARK);
        shapes.rect(0f, 0f, VIEW_WIDTH, 120f);
        shapes.setColor(Palette.TIMBER);
        shapes.rect(360f, 460f, 560f, 150f);
        shapes.setColor(Palette.TIMBER_LIGHT);
        shapes.rect(368f, 468f, 544f, 134f);
        drawButton(playButton, Palette.SECONDARY);
        drawButton(rulesButton, Palette.OCHRE);
        drawButton(settingsButton, Palette.PRIMARY);
        drawButton(exitButton, Palette.TIMBER_LIGHT);
        shapes.end();

        batch.begin();
        font.setColor(Palette.PARCHMENT);
        font.draw(batch, "KWISO BANDO", 488f, 548f);
        font.setColor(Palette.OCHRE);
        font.draw(batch, "THE BASE RACE", 528f, 500f);
        font.setColor(Palette.PARCHMENT);
        drawCentered("PLAY GAME", playButton);
        drawCentered("HOW TO PLAY", rulesButton);
        drawCentered("SETTINGS", settingsButton);
        drawCentered("EXIT", exitButton);
        font.setColor(Palette.MUTED);
        font.draw(batch, "A Ugandan hide-and-seek adaptation  |  Java + libGDX", 330f, 76f);
        font.draw(batch, "BEST " + GameSettings.getBestScore()
            + "   |   ENTER play   H rules   S settings   ESC exit", 372f, 44f);
        batch.end();
    }

    private void drawButton(Rectangle area, Color color) {
        shapes.setColor(Palette.DEEP);
        shapes.rect(area.x, area.y - 6f, area.width, area.height);
        shapes.setColor(color);
        shapes.rect(area.x, area.y, area.width, area.height);
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.ENTER || keycode == Input.Keys.SPACE) {
            AudioManager.get().play(AudioManager.TAP, 0.8f);
            game.setScreen(new PlayScreen(game));
            return true;
        }
        if (keycode == Input.Keys.H) {
            AudioManager.get().play(AudioManager.TAP, 0.8f);
            game.setScreen(new HowToPlayScreen(game));
            return true;
        }
        if (keycode == Input.Keys.S) {
            game.setScreen(new SettingsScreen(game));
            return true;
        }
        if (keycode == Input.Keys.ESCAPE) {
            Gdx.app.exit();
            return true;
        }
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector2 point = screenToWorld(screenX, screenY);
        if (playButton.contains(point)) {
            AudioManager.get().play(AudioManager.TAP, 0.8f);
            game.setScreen(new PlayScreen(game));
        } else if (rulesButton.contains(point)) {
            AudioManager.get().play(AudioManager.TAP, 0.8f);
            game.setScreen(new HowToPlayScreen(game));
        } else if (settingsButton.contains(point)) {
            game.setScreen(new SettingsScreen(game));
        } else if (exitButton.contains(point)) {
            Gdx.app.exit();
        }
        return true;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }
}
