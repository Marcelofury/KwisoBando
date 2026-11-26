package kwiso.bando.config;

public final class GameConfig {
    public static final int VIEW_WIDTH = 1280;
    public static final int VIEW_HEIGHT = 720;

    public static final float HIDE_DURATION = 15f;
    public static final float HIDE_CALLOUT_GAP = 0.75f;
    public static final float SEARCH_DURATION = 60f;

    public static final float BASE_SPEED = 210f;
    public static final float SPRINT_MULTIPLIER = 1.6f;
    public static final float SNEAK_MULTIPLIER = 0.6f;
    public static final float SEEKER_PATROL_SPEED = 130f;
    public static final float SEEKER_RACE_MULTIPLIER = 0.95f;
    public static final float DASH_MULTIPLIER = 2.2f;
    public static final float DASH_COST = 20f;
    public static final float DASH_DURATION = 0.22f;
    public static final float DASH_COOLDOWN = 1.2f;
    public static final float SPRINT_NOISE = 150f;
    public static final float WALK_NOISE = 55f;
    public static final float DASH_NOISE = 190f;
    public static final float INVESTIGATE_DURATION = 5f;

    public static final float STAMINA_MAX = 100f;
    public static final float STAMINA_DRAIN = 26f;
    public static final float STAMINA_REGEN = 16f;

    public static final float DETECTION_TIME = 0.5f;
    public static final float VISION_RANGE = 380f;
    public static final float VISION_HALF_ANGLE_DEG = 45f;
    public static final int VISION_RAYS = 48;

    public static final float PLAYER_RADIUS = 14f;
    public static final float SEEKER_RADIUS = 16f;
    public static final float BASE_RADIUS = 62f;

    private GameConfig() {
    }
}
