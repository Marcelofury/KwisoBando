package kwiso.bando.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import kwiso.bando.KwisoBandoGame;
import kwiso.bando.audio.AudioManager;
import kwiso.bando.config.GameConfig;
import kwiso.bando.config.GameSettings;
import kwiso.bando.config.Palette;

public class SettingsScreen extends BaseScreen {
    private final Rectangle soundButton = new Rectangle(430f, 380f, 420f, 62f);
    private final Rectangle displayButton = new Rectangle(430f, 296f, 420f, 62f);
    private final Rectangle backButton = new Rectangle(430f, 186f, 420f, 62f);

    public SettingsScreen(KwisoBandoGame game) {
        super(game);
    }

    @Override
    public void render(float delta) {
        clearScreen();

        shapes.begin(ShapeRenderer.ShapeType.Filled);
        shapes.setColor(Palette.BACKGROUND);
        shapes.rect(0f, 0f, GameConfig.VIEW_WIDTH, GameConfig.VIEW_HEIGHT);
        shapes.setColor(Palette.EARTH_DARK);
        shapes.rect(0f, 0f, GameConfig.VIEW_WIDTH, 120f);
        shapes.setColor(Palette.TIMBER);
        shapes.rect(360f, 470f, 560f, 150f);
        shapes.setColor(Palette.TIMBER_LIGHT);
        shapes.rect(368f, 478f, 544f, 134f);
        drawButton(soundButton, Palette.SECONDARY);
        drawButton(displayButton, Palette.OCHRE);
        drawButton(backButton, Palette.TIMBER_LIGHT);
        shapes.end();

        batch.begin();
        font.setColor(Palette.PARCHMENT);
        font.draw(batch, "SETTINGS", 520f, 558f);
        font.setColor(Palette.OCHRE);
        font.draw(batch, "SOUND & DISPLAY", 502f, 510f);
        font.setColor(Palette.PARCHMENT);
        drawCentered("S  SOUND  " + onOff(GameSettings.isSoundEnabled()), soundButton);
        drawCentered("F  FULLSCREEN  " + onOff(GameSettings.isFullscreen()), displayButton);
        drawCentered("BACK TO MENU  (ESC)", backButton);
        font.setColor(Palette.MUTED);
        font.draw(batch, "Settings save automatically", 480f, 76f);
        font.draw(batch, "S toggle sound   F toggle fullscreen   ESC back", 414f, 44f);
        batch.end();
    }

    private String onOff(boolean value) {
        return value ? "ON" : "OFF";
    }

    private void drawButton(Rectangle area, Color color) {
        shapes.setColor(Palette.DEEP);
        shapes.rect(area.x, area.y - 6f, area.width, area.height);
        shapes.setColor(color);
        shapes.rect(area.x, area.y, area.width, area.height);
    }

    private void toggleSound() {
        GameSettings.setSoundEnabled(!GameSettings.isSoundEnabled());
        AudioManager.get().play(AudioManager.TAP, 0.8f);
    }

    private void toggleFullscreen() {
        boolean enabled = !Gdx.graphics.isFullscreen();
        if (enabled) {
            Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
        } else {
            Gdx.graphics.setWindowedMode(GameConfig.VIEW_WIDTH, GameConfig.VIEW_HEIGHT);
        }
        GameSettings.setFullscreen(enabled);
        AudioManager.get().play(AudioManager.TAP, 0.8f);
    }

    private void back() {
        AudioManager.get().play(AudioManager.TAP, 0.8f);
        game.setScreen(new MenuScreen(game));
        dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.S) {
            toggleSound();
            return true;
        }
        if (keycode == Input.Keys.F) {
            toggleFullscreen();
            return true;
        }
        if (keycode == Input.Keys.ESCAPE || keycode == Input.Keys.ENTER) {
            back();
            return true;
        }
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector2 point = screenToWorld(screenX, screenY);
        if (soundButton.contains(point)) {
            toggleSound();
        } else if (displayButton.contains(point)) {
            toggleFullscreen();
        } else if (backButton.contains(point)) {
            back();
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
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
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
