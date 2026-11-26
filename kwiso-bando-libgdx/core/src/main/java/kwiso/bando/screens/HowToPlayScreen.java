package kwiso.bando.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import kwiso.bando.KwisoBandoGame;
import kwiso.bando.config.Palette;

public class HowToPlayScreen extends BaseScreen {
    private static final String[] RULES = {
        "1. SCATTER & HIDE",
        "   Seeker counts 15 seconds while you reply Bando!",
        "2. EVADE THE CONE",
        "   Stay out of the amber cone; walls block sight.",
        "   Sprinting/dashing makes noise the seeker investigates.",
        "3. RACE TO BASE",
        "   Spotted? Sprint to the green Home Base first.",
        "4. SCORE & RANK",
        "   Faster unseen wins earn more; personal best is saved.",
        "CONTROLS",
        "   WASD move   SHIFT sprint   CTRL sneak   SPACE dash/tap",
        "   Q pebble decoy   E whistle decoy   ESC pause"
    };

    private final Rectangle startButton = new Rectangle(740f, 92f, 320f, 64f);
    private final Rectangle backButton = new Rectangle(220f, 92f, 320f, 64f);

    public HowToPlayScreen(KwisoBandoGame game) {
        super(game);
        font.getData().setScale(1.7f);
    }

    @Override
    public void render(float delta) {
        clearScreen();

        shapes.begin(ShapeRenderer.ShapeType.Filled);
        shapes.setColor(Palette.BACKGROUND);
        shapes.rect(0f, 0f, 1280f, 720f);
        shapes.setColor(Palette.TIMBER);
        shapes.rect(120f, 150f, 1040f, 500f);
        shapes.setColor(Palette.TIMBER_LIGHT);
        shapes.rect(128f, 158f, 1024f, 484f);
        shapes.setColor(Palette.PRIMARY);
        shapes.rect(128f, 626f, 1024f, 16f);
        shapes.setColor(Palette.OCHRE);
        shapes.rect(128f, 610f, 341f, 16f);
        shapes.setColor(Palette.SECONDARY);
        shapes.rect(469f, 610f, 341f, 16f);
        drawButton(startButton, Palette.SECONDARY);
        drawButton(backButton, Palette.TIMBER_LIGHT);
        shapes.end();

        batch.begin();
        font.setColor(Palette.PARCHMENT);
        font.draw(batch, "HOW TO PLAY - KWISO BANDO", 190f, 598f);
        font.setColor(Palette.PARCHMENT);
        float y = 560f;
        for (String line : RULES) {
            font.draw(batch, line, 190f, y);
            y -= 32f;
        }
        drawCentered("START ROUND", startButton);
        drawCentered("MAIN MENU", backButton);
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
            game.setScreen(new PlayScreen(game));
            return true;
        }
        if (keycode == Input.Keys.ESCAPE || keycode == Input.Keys.BACKSPACE) {
            game.setScreen(new MenuScreen(game));
            return true;
        }
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector2 point = screenToWorld(screenX, screenY);
        if (startButton.contains(point)) {
            game.setScreen(new PlayScreen(game));
        } else if (backButton.contains(point)) {
            game.setScreen(new MenuScreen(game));
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
