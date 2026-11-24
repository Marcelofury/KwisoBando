package kwiso.bando.systems;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.badlogic.gdx.math.Vector2;

import kwiso.bando.entities.Obstacle;

class CollisionSystemTest {
    @Test
    void blocksDirectMovementAndSlidesAlongWall() {
        List<Obstacle> obstacles = List.of(new Obstacle(100f, 0f, 20f, 100f, "wall"));
        Vector2 start = new Vector2(80f, 50f);

        Vector2 blocked = CollisionSystem.slide(start, new Vector2(40f, 0f), 5f, obstacles);
        assertEquals(80f, blocked.x, 0.01f);

        Vector2 slid = CollisionSystem.slide(start, new Vector2(40f, 30f), 5f, obstacles);
        assertEquals(80f, slid.x, 0.01f);
        assertEquals(80f, slid.y, 0.01f);
    }
}
