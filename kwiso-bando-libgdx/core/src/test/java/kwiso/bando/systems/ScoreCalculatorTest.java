package kwiso.bando.systems;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import kwiso.bando.config.RoundResult;

class ScoreCalculatorTest {
    @Test
    void fastUnseenWinScoresHigherThanSlowSpottedWin() {
        int fast = ScoreCalculator.score(new RoundResult(
            RoundResult.Kind.WIN, 10f, "fast", false));
        int slow = ScoreCalculator.score(new RoundResult(
            RoundResult.Kind.WIN, 50f, "slow", true));
        assertTrue(fast > slow);
    }

    @Test
    void rankThresholdsAreStable() {
        assertEquals("LEGEND", ScoreCalculator.rank(1500));
        assertEquals("ACE", ScoreCalculator.rank(1100));
        assertEquals("TRAINEE", ScoreCalculator.rank(100));
    }
}
