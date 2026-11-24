package kwiso.bando.screens;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import kwiso.bando.KwisoBandoGame;
import kwiso.bando.audio.AudioManager;
import kwiso.bando.config.GameConfig;
import kwiso.bando.config.Palette;
import kwiso.bando.config.RoundPhase;
import kwiso.bando.config.RoundResult;
import kwiso.bando.entities.HiderPlayer;
import kwiso.bando.entities.HomeBase;
import kwiso.bando.entities.Obstacle;
import kwiso.bando.entities.SeekerAI;
import kwiso.bando.systems.InputState;
import kwiso.bando.systems.NoiseEvent;
import kwiso.bando.systems.VisionCone;
import kwiso.bando.ui.HudRenderer;

public class PlayScreen extends BaseScreen {
    private static final Color NOISE_RING = new Color(1f, 1f, 1f, 0.18f);
    private static final Color BASE_GLOW = new Color(0.18f, 0.55f, 0.2f, 0.35f);
    private static final Color SCRIM = new Color(0f, 0f, 0f, 0.72f);
    private static final Rectangle WALKABLE = new Rectangle(60f, 150f, 1160f, 460f);

    private final List<Obstacle> obstacles = buildMap();
    private final HomeBase homeBase = new HomeBase(130f, 390f);
    private final HiderPlayer player = new HiderPlayer();
    private final SeekerAI seeker = new SeekerAI(new Vector2[]{
        new Vector2(930f, 310f), new Vector2(1120f, 300f),
        new Vector2(1120f, 480f), new Vector2(760f, 480f),
        new Vector2(600f, 300f), new Vector2(700f, 180f),
        new Vector2(930f, 180f)
    }, obstacles, WALKABLE);
    private final InputState input = new InputState();
    private final HudRenderer hud = new HudRenderer();
    private final List<NoiseEvent> noiseEvents = new ArrayList<NoiseEvent>();

    private RoundPhase phase;
    private float timer;
    private float elapsed;
    private float whistleCooldown;
    private float stepTimer;
    private int pebbles;
    private boolean paused;
    private boolean finished;
    private String banner;

    public PlayScreen(KwisoBandoGame game) {
        super(game);
        resetRound();
    }

    private void resetRound() {
        player.reset(390f, 264f);
        seeker.reset(930f, 310f);
        input.clear();
        phase = RoundPhase.HIDING;
        timer = GameConfig.HIDE_DURATION;
        elapsed = 0f;
        whistleCooldown = 0f;
        stepTimer = 0f;
        pebbles = 3;
        noiseEvents.clear();
        paused = false;
        finished = false;
        banner = "HIDE! SCATTER BEHIND COVER";
    }

    private static List<Obstacle> buildMap() {
        List<Obstacle> map = new ArrayList<Obstacle>();
        map.add(new Obstacle(40f, 70f, 440f, 35f, "wall"));
        map.add(new Obstacle(40f, 615f, 440f, 30f, "wall"));
        map.add(new Obstacle(460f, 240f, 34f, 240f, "wall"));
        map.add(new Obstacle(760f, 110f, 460f, 35f, "wall"));
        map.add(new Obstacle(420f, 220f, 100f, 80f, "bush"));
        map.add(new Obstacle(680f, 40f, 100f, 70f, "tree"));
        map.add(new Obstacle(900f, 520f, 150f, 120f, "tree"));
        map.add(new Obstacle(660f, 320f, 36f, 36f, "crate"));
        map.add(new Obstacle(280f, 180f, 30f, 30f, "urn"));
        return map;
    }

    @Override
    public void render(float delta) {
        if (!paused) {
            update(Math.min(delta, 1f / 30f));
        }
        if (finished) {
            return;
        }

        clearScreen();
        drawWorld();
        hud.render(batch, font, shapes, phase, timer, player, seeker,
            pebbles, whistleCooldown);
        drawWorldLabels();
        if (paused) {
            drawPauseOverlay();
        }
    }

    private void update(float delta) {
        player.update(delta, input, obstacles);
        boolean tapped = input.action;
        boolean pebble = input.throwPebble;
        boolean whistle = input.whistle;
        input.action = false;
        input.throwPebble = false;
        input.whistle = false;
        whistleCooldown = Math.max(0f, whistleCooldown - delta);

        for (NoiseEvent event : noiseEvents) {
            event.update(delta);
        }
        noiseEvents.removeIf(NoiseEvent::isExpired);

        Vector2 facingDirection = new Vector2(
            MathUtils.cosDeg(player.getFacing()), MathUtils.sinDeg(player.getFacing()));
        if (pebble && pebbles > 0 && phase != RoundPhase.HIDING) {
            pebbles--;
            Vector2 target = player.getPosition().cpy().add(facingDirection.cpy().scl(240f));
            noiseEvents.add(new NoiseEvent(target, 1.4f));
            AudioManager.get().play(AudioManager.PEBBLE, 0.7f);
            if (phase == RoundPhase.SEARCHING) {
                seeker.hearNoise(target);
            }
        }
        if (whistle && whistleCooldown <= 0f && phase != RoundPhase.HIDING) {
            whistleCooldown = 8f;
            Vector2 target = player.getPosition().cpy().add(facingDirection.cpy().scl(360f));
            noiseEvents.add(new NoiseEvent(target, 2f));
            AudioManager.get().play(AudioManager.WHISTLE, 0.6f);
            if (phase == RoundPhase.SEARCHING) {
                seeker.hearNoise(target);
            }
        }

        if (phase == RoundPhase.SEARCHING && player.getNoiseRadius() > 0f
            && seeker.getPosition().dst(player.getPosition()) <= player.getNoiseRadius()) {
            seeker.hearNoise(player.getPosition());
        }

        if (player.getNoiseRadius() > 0f) {
            stepTimer -= delta;
            if (stepTimer <= 0f) {
                AudioManager.get().play(AudioManager.STEP,
                    player.isSprinting() ? 0.35f : 0.2f);
                stepTimer = player.isSprinting() ? 0.24f : 0.4f;
            }
        } else {
            stepTimer = 0f;
        }

        switch (phase) {
            case HIDING:
                seeker.setFacing(180f);
                timer -= delta;
                if (timer <= 0f) {
                    phase = RoundPhase.SEARCHING;
                    timer = GameConfig.SEARCH_DURATION;
                    banner = "SEEKER IS PATROLLING";
                    AudioManager.get().play(AudioManager.KWISO, 0.9f);
                }
                break;
            case SEARCHING:
                seeker.update(delta, phase, player, obstacles, homeBase);
                elapsed += delta;
                timer -= delta;
                if (tapped && player.dash()) {
                    AudioManager.get().play(AudioManager.DASH, 0.7f);
                }
                if (touchesBase(player.getPosition(), GameConfig.PLAYER_RADIUS)
                    || (tapped && touchesBase(player.getPosition(), GameConfig.PLAYER_RADIUS + 60f))) {
                    finish(RoundResult.Kind.WIN, "Tapped Home Base unseen");
                } else if (seeker.isAlerted()) {
                    phase = RoundPhase.RACE;
                    banner = "RACE TO BASE!";
                    AudioManager.get().play(AudioManager.SPOTTED, 0.9f);
                } else if (timer <= 0f) {
                    finish(RoundResult.Kind.LOSS, "Round timer expired");
                }
                break;
            case RACE:
                seeker.update(delta, phase, player, obstacles, homeBase);
                elapsed += delta;
                timer = Math.max(0f, timer - delta);
                if (tapped && player.dash()) {
                    AudioManager.get().play(AudioManager.DASH, 0.7f);
                }
                if (touchesBase(player.getPosition(), GameConfig.PLAYER_RADIUS)) {
                    finish(RoundResult.Kind.WIN, "Beat the seeker to the base");
                } else if (touchesBase(seeker.getPosition(), GameConfig.SEEKER_RADIUS)) {
                    finish(RoundResult.Kind.LOSS, "Seeker tagged the base first");
                } else if (timer <= 0f) {
                    finish(RoundResult.Kind.LOSS, "Round timer expired");
                }
                break;
            default:
                break;
        }
    }

    private boolean touchesBase(Vector2 position, float radius) {
        return homeBase.touches(position, radius);
    }

    private void finish(RoundResult.Kind kind, String reason) {
        if (finished) {
            return;
        }
        finished = true;
        AudioManager.get().play(kind == RoundResult.Kind.WIN
            ? AudioManager.WIN : AudioManager.LOSE, 0.9f);
        game.setScreen(new RoundSummaryScreen(game,
            new RoundResult(kind, elapsed, reason, seeker.isAlerted())));
        dispose();
    }

    private void drawWorld() {
        shapes.begin(ShapeRenderer.ShapeType.Filled);
        shapes.setColor(Palette.EARTH_DARK);
        shapes.rect(0f, 0f, 1280f, 720f);
        shapes.setColor(Palette.EARTH);
        shapes.rect(60f, 150f, 1160f, 460f);
        shapes.setColor(Palette.PATH);
        shapes.rect(180f, 370f, 980f, 46f);
        shapes.rect(500f, 170f, 44f, 400f);

        if (phase == RoundPhase.SEARCHING || phase == RoundPhase.RACE) {
            drawVisionCone();
        }

        for (Obstacle obstacle : obstacles) {
            shapes.setColor(obstacleColor(obstacle.kind));
            shapes.rect(obstacle.bounds.x, obstacle.bounds.y, obstacle.bounds.width, obstacle.bounds.height);
        }

        shapes.setColor(BASE_GLOW);
        shapes.circle(homeBase.getPosition().x, homeBase.getPosition().y, GameConfig.BASE_RADIUS, 48);
        shapes.setColor(Palette.SECONDARY);
        shapes.circle(homeBase.getPosition().x, homeBase.getPosition().y, 28f, 32);
        shapes.setColor(Palette.PARCHMENT);
        shapes.circle(homeBase.getPosition().x, homeBase.getPosition().y, 6f, 16);

        if (player.getNoiseRadius() > 0f) {
            shapes.setColor(NOISE_RING);
            shapes.circle(player.getPosition().x, player.getPosition().y,
                player.getNoiseRadius(), 48);
        }

        for (NoiseEvent event : noiseEvents) {
            shapes.setColor(NOISE_RING);
            shapes.circle(event.getPosition().x, event.getPosition().y,
                8f + 26f * event.getProgress(), 24);
            shapes.setColor(Palette.OCHRE);
            shapes.circle(event.getPosition().x, event.getPosition().y, 4f, 12);
        }

        shapes.setColor(Palette.DEEP);
        shapes.circle(player.getPosition().x, player.getPosition().y, GameConfig.PLAYER_RADIUS + 3f, 24);
        shapes.setColor(player.isDashing() ? Palette.OCHRE : Palette.RUNNER);
        shapes.circle(player.getPosition().x, player.getPosition().y, GameConfig.PLAYER_RADIUS, 24);

        shapes.setColor(Palette.DEEP);
        shapes.circle(seeker.getPosition().x, seeker.getPosition().y, GameConfig.SEEKER_RADIUS + 3f, 24);
        shapes.setColor(seeker.isAlerted() ? Palette.ALERT : Palette.PRIMARY);
        shapes.circle(seeker.getPosition().x, seeker.getPosition().y, GameConfig.SEEKER_RADIUS, 24);
        shapes.setColor(Palette.OCHRE);
        shapes.circle(seeker.getPosition().x, seeker.getPosition().y, 8f, 16);
        shapes.end();
    }

    private void drawVisionCone() {
        shapes.setColor(Palette.VISION);
        float half = GameConfig.VISION_HALF_ANGLE_DEG;
        float start = seeker.getFacing() - half;
        Vector2 previous = conePoint(start);
        for (int i = 1; i <= GameConfig.VISION_RAYS; i++) {
            float angle = start + (2f * half * i) / GameConfig.VISION_RAYS;
            Vector2 current = conePoint(angle);
            shapes.triangle(seeker.getPosition().x, seeker.getPosition().y,
                previous.x, previous.y, current.x, current.y);
            previous = current;
        }
    }

    private Vector2 conePoint(float angleDeg) {
        float distance = VisionCone.rayLength(seeker.getPosition(), angleDeg,
            GameConfig.VISION_RANGE, obstacles);
        float radians = angleDeg * MathUtils.degreesToRadians;
        return new Vector2(
            seeker.getPosition().x + MathUtils.cos(radians) * distance,
            seeker.getPosition().y + MathUtils.sin(radians) * distance
        );
    }

    private Color obstacleColor(String kind) {
        if ("tree".equals(kind) || "bush".equals(kind)) {
            return Palette.SECONDARY;
        }
        if ("crate".equals(kind)) {
            return Palette.OCHRE;
        }
        if ("urn".equals(kind)) {
            return Palette.PRIMARY;
        }
        return Palette.TIMBER;
    }

    private void drawWorldLabels() {
        batch.begin();
        font.setColor(Palette.PARCHMENT);
        font.draw(batch, banner, 470f, 580f);
        if (phase == RoundPhase.HIDING || phase == RoundPhase.SEARCHING) {
            font.setColor(Palette.OCHRE);
            font.draw(batch, "KWISO!", seeker.getPosition().x - 24f, seeker.getPosition().y + 42f);
            font.setColor(Palette.SECONDARY);
            font.draw(batch, "BANDO!", player.getPosition().x - 24f, player.getPosition().y + 38f);
        }
        if (seeker.getDetectionProgress() > 0f && !seeker.isAlerted()) {
            font.setColor(Palette.ALERT);
            font.draw(batch, "?", seeker.getPosition().x - 6f, seeker.getPosition().y + 30f);
        } else if (seeker.isInvestigating()) {
            font.setColor(Palette.OCHRE);
            font.draw(batch, "!", seeker.getPosition().x - 5f, seeker.getPosition().y + 30f);
        }
        batch.end();
    }

    private void drawPauseOverlay() {
        shapes.begin(ShapeRenderer.ShapeType.Filled);
        shapes.setColor(SCRIM);
        shapes.rect(0f, 0f, 1280f, 720f);
        shapes.setColor(Palette.TIMBER);
        shapes.rect(400f, 240f, 480f, 240f);
        shapes.end();

        batch.begin();
        font.setColor(Palette.OCHRE);
        font.draw(batch, "MATCH PAUSED", 506f, 400f);
        font.setColor(Palette.PARCHMENT);
        font.draw(batch, "ESC resume     R restart", 470f, 330f);
        font.draw(batch, String.format(Locale.US, "Phase %s  |  %s", phaseName(phase), banner), 436f, 286f);
        batch.end();
    }

    private String phaseName(RoundPhase value) {
        switch (value) {
            case HIDING:
                return "HIDE";
            case SEARCHING:
                return "SEARCH";
            case RACE:
                return "RACE";
            default:
                return "OVER";
        }
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.ESCAPE) {
            paused = !paused;
            return true;
        }
        if (paused) {
            if (keycode == Input.Keys.R) {
                resetRound();
            }
            return true;
        }
        switch (keycode) {
            case Input.Keys.W:
            case Input.Keys.UP:
                input.up = true;
                return true;
            case Input.Keys.S:
            case Input.Keys.DOWN:
                input.down = true;
                return true;
            case Input.Keys.A:
            case Input.Keys.LEFT:
                input.left = true;
                return true;
            case Input.Keys.D:
            case Input.Keys.RIGHT:
                input.right = true;
                return true;
            case Input.Keys.SHIFT_LEFT:
            case Input.Keys.SHIFT_RIGHT:
                input.sprint = true;
                return true;
            case Input.Keys.CONTROL_LEFT:
            case Input.Keys.CONTROL_RIGHT:
                input.sneak = true;
                return true;
            case Input.Keys.SPACE:
                input.action = true;
                return true;
            case Input.Keys.Q:
                input.throwPebble = true;
                return true;
            case Input.Keys.E:
                input.whistle = true;
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean keyUp(int keycode) {
        if (paused) {
            return false;
        }
        switch (keycode) {
            case Input.Keys.W:
            case Input.Keys.UP:
                input.up = false;
                return true;
            case Input.Keys.S:
            case Input.Keys.DOWN:
                input.down = false;
                return true;
            case Input.Keys.A:
            case Input.Keys.LEFT:
                input.left = false;
                return true;
            case Input.Keys.D:
            case Input.Keys.RIGHT:
                input.right = false;
                return true;
            case Input.Keys.SHIFT_LEFT:
            case Input.Keys.SHIFT_RIGHT:
                input.sprint = false;
                return true;
            case Input.Keys.CONTROL_LEFT:
            case Input.Keys.CONTROL_RIGHT:
                input.sneak = false;
                return true;
            case Input.Keys.Q:
                input.throwPebble = false;
                return true;
            case Input.Keys.E:
                input.whistle = false;
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (paused) {
            paused = false;
        }
        return true;
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
