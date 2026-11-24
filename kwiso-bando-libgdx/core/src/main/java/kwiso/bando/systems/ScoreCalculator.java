package kwiso.bando.systems;

import kwiso.bando.config.GameConfig;
import kwiso.bando.config.RoundResult;

public final class ScoreCalculator {
    private ScoreCalculator() {
    }

    public static int score(RoundResult result) {
        int base = result.kind == RoundResult.Kind.WIN ? 800 : 150;
        int timeBonus = Math.max(0,
            (int) Math.round((GameConfig.SEARCH_DURATION - result.elapsedSeconds) * 12f));
        int stealthBonus = result.kind == RoundResult.Kind.WIN && !result.spotted ? 350 : 0;
        return base + timeBonus + stealthBonus;
    }

    public static String rank(int score) {
        if (score >= 1500) {
            return "LEGEND";
        }
        if (score >= 1100) {
            return "ACE";
        }
        if (score >= 750) {
            return "SCOUT";
        }
        if (score >= 400) {
            return "ROOKIE";
        }
        return "TRAINEE";
    }
}
