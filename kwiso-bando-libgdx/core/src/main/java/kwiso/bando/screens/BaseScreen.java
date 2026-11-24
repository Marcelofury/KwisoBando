package kwiso.bando.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;

import kwiso.bando.KwisoBandoGame;

import static kwiso.bando.config.GameConfig.VIEW_HEIGHT;
import static kwiso.bando.config.GameConfig.VIEW_WIDTH;

public abstract class BaseScreen implements Screen, InputProcessor {
    protected final KwisoBandoGame game;
    protected final OrthographicCamera camera;
    protected final FitViewport viewport;
    protected final ShapeRenderer shapes;
    protected final SpriteBatch batch;
    protected final BitmapFont font;

    protected BaseScreen(KwisoBandoGame game) {
        this.game = game;
        this.camera = new OrthographicCamera();
        this.viewport = new FitViewport(VIEW_WIDTH, VIEW_HEIGHT, camera);
        this.shapes = new ShapeRenderer();
        this.batch = new SpriteBatch();
        this.font = new BitmapFont();
        this.font.getData().setScale(2f);
        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    protected void clearScreen() {
        Gdx.gl.glClearColor(0.07f, 0.08f, 0.1f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        shapes.setProjectionMatrix(camera.combined);
        batch.setProjectionMatrix(camera.combined);
    }

    protected Vector2 screenToWorld(int screenX, int screenY) {
        Vector2 point = new Vector2(screenX, Gdx.graphics.getHeight() - screenY);
        viewport.unproject(point);
        return point;
    }

    protected void drawCentered(String text, Rectangle area) {
        GlyphLayout layout = new GlyphLayout(font, text);
        font.draw(batch, text,
            area.x + (area.width - layout.width) / 2f,
            area.y + (area.height + layout.height) / 2f);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void hide() {
        if (Gdx.input.getInputProcessor() == this) {
            Gdx.input.setInputProcessor(null);
        }
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public void dispose() {
        shapes.dispose();
        batch.dispose();
        font.dispose();
    }
}
