package kwiso.bando.entities;

import java.util.List;

import com.badlogic.gdx.math.Vector2;

import kwiso.bando.config.GameConfig;
import kwiso.bando.systems.CollisionSystem;
import kwiso.bando.systems.InputState;

public class HiderPlayer {
    private final Vector2 position = new Vector2();
    private float stamina = GameConfig.STAMINA_MAX;
    private float facing;
    private float noiseRadius;
    private boolean sprinting;
    private boolean sneaking;

    public void reset(float x, float y) {
        position.set(x, y);
        stamina = GameConfig.STAMINA_MAX;
        facing = 0f;
        noiseRadius = 0f;
        sprinting = false;
        sneaking = false;
    }

    public void update(float delta, InputState input, List<Obstacle> obstacles) {
        Vector2 direction = input.direction();
        boolean moving = direction.len2() > 0f;

        sneaking = input.sneak && !input.sprint;
        sprinting = input.sprint && !sneaking && moving && stamina > 0f;

        if (sprinting) {
            stamina = Math.max(0f, stamina - GameConfig.STAMINA_DRAIN * delta);
        } else {
            stamina = Math.min(GameConfig.STAMINA_MAX, stamina + GameConfig.STAMINA_REGEN * delta);
        }

        float multiplier = sprinting ? GameConfig.SPRINT_MULTIPLIER
            : (sneaking ? GameConfig.SNEAK_MULTIPLIER : 1f);
        Vector2 step = direction.nor().scl(GameConfig.BASE_SPEED * multiplier * delta);
        position.set(CollisionSystem.slide(position, step, GameConfig.PLAYER_RADIUS, obstacles));

        if (moving) {
            facing = direction.angleDeg();
        }

        noiseRadius = sneaking ? 0f : (sprinting ? 150f : 55f);
    }

    public Vector2 getPosition() {
        return position;
    }

    public float getStamina() {
        return stamina;
    }

    public float getFacing() {
        return facing;
    }

    public float getNoiseRadius() {
        return noiseRadius;
    }

    public boolean isSprinting() {
        return sprinting;
    }

    public boolean isSneaking() {
        return sneaking;
    }
}
