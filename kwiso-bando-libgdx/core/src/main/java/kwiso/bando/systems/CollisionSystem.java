package kwiso.bando.systems;

import java.util.List;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import kwiso.bando.config.GameConfig;
import kwiso.bando.entities.Obstacle;

public final class CollisionSystem {
    private CollisionSystem() {
    }

    public static Vector2 slide(Vector2 position, Vector2 delta, float radius, List<Obstacle> obstacles) {
        Vector2 next = position.cpy();
        Rectangle probe = new Rectangle(next.x - radius, next.y - radius, radius * 2f, radius * 2f);

        probe.x += delta.x;
        if (!collides(probe, obstacles)) {
            next.x += delta.x;
        }

        probe.set(next.x - radius, next.y - radius, radius * 2f, radius * 2f);
        probe.y += delta.y;
        if (!collides(probe, obstacles)) {
            next.y += delta.y;
        }

        float min = radius;
        float maxX = GameConfig.VIEW_WIDTH - radius;
        float maxY = GameConfig.VIEW_HEIGHT - radius;
        next.x = MathUtils.clamp(next.x, min, maxX);
        next.y = MathUtils.clamp(next.y, min, maxY);
        return next;
    }

    private static boolean collides(Rectangle candidate, List<Obstacle> obstacles) {
        for (Obstacle obstacle : obstacles) {
            if (candidate.overlaps(obstacle.bounds)) {
                return true;
            }
        }
        return false;
    }
}
