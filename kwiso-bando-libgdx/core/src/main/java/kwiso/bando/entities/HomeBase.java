package kwiso.bando.entities;

import com.badlogic.gdx.math.Vector2;

import kwiso.bando.config.GameConfig;

public class HomeBase {
    private final Vector2 position;

    public HomeBase(float x, float y) {
        this.position = new Vector2(x, y);
    }

    public Vector2 getPosition() {
        return position;
    }

    public float getRadius() {
        return GameConfig.BASE_RADIUS;
    }

    public boolean touches(Vector2 point, float entityRadius) {
        float reach = GameConfig.BASE_RADIUS + entityRadius;
        return position.dst2(point) <= reach * reach;
    }
}
