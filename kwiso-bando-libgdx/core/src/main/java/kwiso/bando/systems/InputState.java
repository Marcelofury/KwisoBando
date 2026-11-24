package kwiso.bando.systems;

import com.badlogic.gdx.math.Vector2;

public final class InputState {
    public boolean up;
    public boolean down;
    public boolean left;
    public boolean right;
    public boolean sprint;
    public boolean sneak;
    public boolean action;
    public boolean throwPebble;
    public boolean whistle;

    public Vector2 direction() {
        float x = (right ? 1f : 0f) - (left ? 1f : 0f);
        float y = (up ? 1f : 0f) - (down ? 1f : 0f);
        return new Vector2(x, y);
    }

    public void clear() {
        up = false;
        down = false;
        left = false;
        right = false;
        sprint = false;
        sneak = false;
        action = false;
        throwPebble = false;
        whistle = false;
    }
}
