package kwiso.bando.systems;

import com.badlogic.gdx.math.Vector2;

public class NoiseEvent {
    private final Vector2 position;
    private final float duration;
    private float life;

    public NoiseEvent(Vector2 position, float duration) {
        this.position = position.cpy();
        this.duration = duration;
        this.life = duration;
    }

    public void update(float delta) {
        life -= delta;
    }

    public boolean isExpired() {
        return life <= 0f;
    }

    public Vector2 getPosition() {
        return position;
    }

    public float getProgress() {
        return 1f - life / duration;
    }
}
