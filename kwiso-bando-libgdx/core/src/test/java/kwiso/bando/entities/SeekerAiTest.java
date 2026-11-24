package kwiso.bando.entities;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import com.badlogic.gdx.math.Vector2;

import kwiso.bando.config.RoundPhase;

class SeekerAiTest {
    @Test
    void heardNoiseMovesSeekerTowardSource() {
        SeekerAI seeker = new SeekerAI(new Vector2[]{new Vector2(100f, 0f)});
        seeker.reset(0f, 0f);
        seeker.hearNoise(new Vector2(50f, 0f));
        assertTrue(seeker.isInvestigating());

        HiderPlayer player = new HiderPlayer();
        player.reset(500f, 500f);
        seeker.update(0.1f, RoundPhase.SEARCHING, player,
            new ArrayList<Obstacle>(), new HomeBase(0f, 0f));
        assertTrue(seeker.getPosition().x > 0f);
    }
}
