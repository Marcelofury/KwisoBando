package kwiso.bando.entities;

import com.badlogic.gdx.math.Rectangle;

public class Obstacle {
    public final Rectangle bounds;
    public final String kind;

    public Obstacle(float x, float y, float width, float height, String kind) {
        this.bounds = new Rectangle(x, y, width, height);
        this.kind = kind;
    }
}
