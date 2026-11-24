package kwiso.bando.systems;

import java.util.List;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import kwiso.bando.entities.Obstacle;

public final class VisionCone {
    private VisionCone() {
    }

    public static boolean hasLineOfSight(Vector2 from, Vector2 to, List<Obstacle> obstacles) {
        for (Obstacle obstacle : obstacles) {
            if (entry(from.x, from.y, to.x, to.y, obstacle.bounds) >= 0f) {
                return false;
            }
        }
        return true;
    }

    public static float rayLength(Vector2 origin, float angleDeg, float maxDistance, List<Obstacle> obstacles) {
        float radians = angleDeg * MathUtils.degreesToRadians;
        float endX = origin.x + MathUtils.cos(radians) * maxDistance;
        float endY = origin.y + MathUtils.sin(radians) * maxDistance;
        float nearest = 1f;
        for (Obstacle obstacle : obstacles) {
            float hit = entry(origin.x, origin.y, endX, endY, obstacle.bounds);
            if (hit >= 0f && hit < nearest) {
                nearest = hit;
            }
        }
        return nearest * maxDistance;
    }

    private static float entry(float x0, float y0, float x1, float y1, Rectangle rect) {
        float dx = x1 - x0;
        float dy = y1 - y0;
        float[] p = {-dx, dx, -dy, dy};
        float[] q = {x0 - rect.x, rect.x + rect.width - x0, y0 - rect.y, rect.y + rect.height - y0};
        float t0 = 0f;
        float t1 = 1f;

        for (int i = 0; i < 4; i++) {
            if (p[i] == 0f) {
                if (q[i] < 0f) {
                    return -1f;
                }
                continue;
            }
            float t = q[i] / p[i];
            if (p[i] < 0f) {
                if (t > t1) {
                    return -1f;
                }
                if (t > t0) {
                    t0 = t;
                }
            } else {
                if (t < t0) {
                    return -1f;
                }
                if (t < t1) {
                    t1 = t;
                }
            }
        }
        return t0 <= t1 ? t0 : -1f;
    }
}
