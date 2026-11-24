package kwiso.bando.screens;

import java.util.Locale;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import kwiso.bando.KwisoBandoGame;
import kwiso.bando.audio.AudioManager;
import kwiso.bando.config.GameSettings;
import kwiso.bando.config.Palette;
import kwiso.bando.config.RoundResult;
import kwiso.bando.systems.ScoreCalculator;

public class RoundSummaryScreen extends BaseScreen {
    private final RoundResult result;
    private final Rectangle againButton = new Rectangle(380f, 190f, 240f, 64f);
    private final Rectangle menuButton = new Rectangle(660f, 190f, 240f, 64f);
    private final int score;
    private final String rank;
    private final int bestScore;
    private final boolean newBest;

    public RoundSummaryScreen(KwisoBandoGame game, RoundResult result) {
        super(game);
        this.result = result;
        this.score = ScoreCalculator.score(result);
        this.rank = ScoreCalculator.rank(score);
        this.newBest = score > GameSettings.getBestScore();
        this.bestScore = Math.max(GameSettings.getBestScore(), score);
        GameSettings.setBestScore(bestScore);
    }

    @Override
    public void render(float delta) {
        clearScreen();

        shapes.begin(ShapeRenderer.ShapeType.Filled);
        shapes.setColor(Palette.BACKGROUND);
        shapes.rect(0f, 0f, 1280f, 720f);
        shapes.setColor(Palette.TIMBER);
        shapes.rect(300f, 130f, 680f, 470f);
        shapes.setColor(Palette.TIMBER_LIGHT);
        shapes.rect(308f, 138f, 664f, 454f);
        shapes.setColor(result.kind == RoundResult.Kind.WIN ? Palette.SECONDARY : Palette.PRIMARY);
        shapes.rect(308f, 576f, 664f, 16f);
        drawButton(againButton, Palette.SECONDARY);
        drawButton(menuButton, Palette.TIMBER_LIGHT);
        shapes.end();

        batch.begin();
        font.setColor(result.kind == RoundResult.Kind.WIN ? Palette.SECONDARY : Palette.PRIMARY);
        font.draw(batch, result.kind == RoundResult.Kind.WIN ? "BANDO! YOU REACHED BASE" : "TAG! ROUND LOST", 370f, 532f);
        font.setColor(Palette.PARCHMENT);
        font.draw(batch, result.reason, 370f, 476f);
        font.draw(batch, String.format(Locale.US, "TIME: %.1f s", result.elapsedSeconds), 370f, 420f);
        font.setColor(Palette.OCHRE);
        font.draw(batch, String.format(Locale.US, "SCORE %d   RANK %s", score, rank), 370f, 364f);
        font.setColor(Palette.PARCHMENT);
        font.draw(batch, newBest ? "NEW PERSONAL BEST!"
            : String.format(Locale.US, "BEST %d", bestScore), 370f, 318f);
        font.setColor(Palette.MUTED);
        font.draw(batch, result.spotted ? "The seeker spotted you" : "You stayed unseen",
            370f, 274f);
        font.setColor(Palette.PARCHMENT);
        drawCentered("PLAY AGAIN", againButton);
        drawCentered("MAIN MENU", menuButton);
        font.setColor(Palette.MUTED);
        font.draw(batch, "SPACE play again     ESC main menu", 470f, 102f);
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
        if (keycode == Input.Keys.SPACE || keycode == Input.Keys.ENTER || keycode == Input.Keys.R) {
            AudioManager.get().play(AudioManager.TAP, 0.8f);
            game.setScreen(new PlayScreen(game));
            return true;
        }
        if (keycode == Input.Keys.ESCAPE) {
            AudioManager.get().play(AudioManager.TAP, 0.8f);
            game.setScreen(new MenuScreen(game));
            return true;
        }
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector2 point = screenToWorld(screenX, screenY);
        if (againButton.contains(point)) {
            AudioManager.get().play(AudioManager.TAP, 0.8f);
            game.setScreen(new PlayScreen(game));
        } else if (menuButton.contains(point)) {
            AudioManager.get().play(AudioManager.TAP, 0.8f);
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
