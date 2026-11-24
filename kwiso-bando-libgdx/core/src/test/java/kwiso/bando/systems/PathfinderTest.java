package kwiso.bando.systems;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import kwiso.bando.entities.Obstacle;

class PathfinderTest {
    @Test
    void routesAroundWallInsteadOfThroughIt() {
        Rectangle walkable = new Rectangle(0f, 0f, 200f, 100f);
        Rectangle wall = new Rectangle(90f, 20f, 20f, 60f);
        Pathfinder pathfinder = new Pathfinder(
            walkable, 10f, List.of(new Obstacle(90f, 20f, 20f, 60f, "wall")), 5f);

        List<Vector2> path = pathfinder.findPath(new Vector2(20f, 50f), new Vector2(180f, 50f));
        assertFalse(path.isEmpty());
        assertTrue(path.get(path.size() - 1).dst2(new Vector2(180f, 50f)) < 1f);

        Rectangle inflated = new Rectangle(85f, 15f, 30f, 70f);
        for (Vector2 point : path) {
            assertFalse(inflated.contains(point.x, point.y));
        }
    }
}
