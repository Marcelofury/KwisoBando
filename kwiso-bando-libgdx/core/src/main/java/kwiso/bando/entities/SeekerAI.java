package kwiso.bando.entities;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import kwiso.bando.config.GameConfig;
import kwiso.bando.config.RoundPhase;
import kwiso.bando.systems.CollisionSystem;
import kwiso.bando.systems.Pathfinder;
import kwiso.bando.systems.VisionCone;

public class SeekerAI {
    private final Vector2 position = new Vector2();
    private final Vector2[] waypoints;
    private final Vector2 noiseTarget = new Vector2();
    private final Vector2 lastTarget = new Vector2();
    private final Pathfinder pathfinder;
    private List<Vector2> path = new ArrayList<Vector2>();
    private int pathIndex;
    private int waypointIndex;
    private float facing;
    private float detectionTimer;
    private float investigateTimer;
    private float repathTimer;
    private boolean alerted;
    private boolean investigating;

    public SeekerAI(Vector2[] waypoints) {
        this(waypoints, new ArrayList<Obstacle>(),
            new Rectangle(0f, 0f, GameConfig.VIEW_WIDTH, GameConfig.VIEW_HEIGHT));
    }

    public SeekerAI(Vector2[] waypoints, List<Obstacle> obstacles, Rectangle walkable) {
        this.waypoints = waypoints;
        this.pathfinder = new Pathfinder(walkable, 32f, obstacles, GameConfig.SEEKER_RADIUS);
    }

    public void reset(float x, float y) {
        position.set(x, y);
        waypointIndex = 0;
        facing = 180f;
        detectionTimer = 0f;
        investigateTimer = 0f;
        repathTimer = 0f;
        path.clear();
        pathIndex = 0;
        lastTarget.set(0f, 0f);
        alerted = false;
        investigating = false;
    }

    public void setFacing(float degrees) {
        facing = degrees;
    }

    public void hearNoise(Vector2 source) {
        noiseTarget.set(source);
        investigateTimer = GameConfig.INVESTIGATE_DURATION;
        investigating = true;
    }

    public void update(float delta, RoundPhase phase, HiderPlayer player,
                       List<Obstacle> obstacles, HomeBase base) {
        if (phase == RoundPhase.HIDING || phase == RoundPhase.FINISHED) {
            return;
        }

        Vector2 target;
        float speed;
        if (phase == RoundPhase.RACE) {
            target = base.getPosition();
            speed = GameConfig.BASE_SPEED * GameConfig.SPRINT_MULTIPLIER
                * GameConfig.SEEKER_RACE_MULTIPLIER;
        } else {
            speed = GameConfig.SEEKER_PATROL_SPEED;
            if (investigating) {
                investigateTimer -= delta;
                target = noiseTarget;
                speed *= 1.25f;
                if (position.dst2(target) < 20f * 20f || investigateTimer <= 0f) {
                    investigating = false;
                }
            } else {
                target = waypoints[waypointIndex];
                if (position.dst2(target) < 18f * 18f) {
                    waypointIndex = (waypointIndex + 1) % waypoints.length;
                    target = waypoints[waypointIndex];
                }
            }
        }

        repathTimer -= delta;
        boolean targetMoved = lastTarget.dst2(target) > 24f * 24f;
        if (path.isEmpty() || pathIndex >= path.size() || targetMoved || repathTimer <= 0f) {
            path = pathfinder.findPath(position, target);
            pathIndex = 0;
            repathTimer = 0.4f;
            lastTarget.set(target);
        }

        Vector2 steeringTarget = pathIndex < path.size() ? path.get(pathIndex) : target;
        if (pathIndex < path.size() && position.dst2(steeringTarget) < 12f * 12f) {
            pathIndex++;
            steeringTarget = pathIndex < path.size() ? path.get(pathIndex) : target;
        }

        Vector2 direction = steeringTarget.cpy().sub(position);
        if (direction.len2() > 0f) {
            facing = direction.angleDeg();
            Vector2 step = direction.nor().scl(speed * delta);
            position.set(CollisionSystem.slide(position, step, GameConfig.SEEKER_RADIUS, obstacles));
        }

        if (phase == RoundPhase.SEARCHING) {
            if (canSee(player, obstacles)) {
                detectionTimer += delta;
                if (detectionTimer >= GameConfig.DETECTION_TIME) {
                    alerted = true;
                    investigating = false;
                }
            } else {
                detectionTimer = Math.max(0f, detectionTimer - delta * 2f);
            }
        }
    }

    public boolean canSee(HiderPlayer player, List<Obstacle> obstacles) {
        Vector2 toPlayer = player.getPosition().cpy().sub(position);
        float distance = toPlayer.len();
        if (distance > GameConfig.VISION_RANGE) {
            return false;
        }
        float difference = Math.abs(wrapAngle(facing - toPlayer.angleDeg()));
        if (difference > GameConfig.VISION_HALF_ANGLE_DEG) {
            return false;
        }
        return VisionCone.hasLineOfSight(position, player.getPosition(), obstacles);
    }

    public Vector2 getPosition() {
        return position;
    }

    public float getFacing() {
        return facing;
    }

    public float getDetectionProgress() {
        return Math.min(1f, detectionTimer / GameConfig.DETECTION_TIME);
    }

    public boolean isAlerted() {
        return alerted;
    }

    public boolean isInvestigating() {
        return investigating;
    }

    public Vector2 getNoiseTarget() {
        return noiseTarget;
    }

    private static float wrapAngle(float degrees) {
        float wrapped = degrees % 360f;
        if (wrapped > 180f) {
            wrapped -= 360f;
        } else if (wrapped < -180f) {
            wrapped += 360f;
        }
        return wrapped;
    }
}
