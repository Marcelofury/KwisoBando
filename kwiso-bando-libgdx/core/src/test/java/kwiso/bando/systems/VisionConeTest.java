package kwiso.bando.systems;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.badlogic.gdx.math.Vector2;

import kwiso.bando.entities.Obstacle;

class VisionConeTest {
    @Test
    void clearPathHasLineOfSight() {
        assertTrue(VisionCone.hasLineOfSight(
            new Vector2(0f, 0f), new Vector2(100f, 0f), new ArrayList<Obstacle>()));
    }

    @Test
    void wallBlocksLineOfSightAndShortensRay() {
        List<Obstacle> obstacles = List.of(new Obstacle(50f, -10f, 10f, 20f, "wall"));
        assertFalse(VisionCone.hasLineOfSight(
            new Vector2(0f, 0f), new Vector2(100f, 0f), obstacles));
        assertEquals(50f, VisionCone.rayLength(new Vector2(0f, 0f), 0f, 100f, obstacles), 0.01f);
    }
}
