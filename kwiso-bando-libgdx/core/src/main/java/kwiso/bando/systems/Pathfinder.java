package kwiso.bando.systems;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import kwiso.bando.entities.Obstacle;

public final class Pathfinder {
    private static final float SQRT_2 = 1.41421356f;
    private static final int[] COLUMN_STEPS = {-1, 0, 1, -1, 1, -1, 0, 1};
    private static final int[] ROW_STEPS = {-1, -1, -1, 0, 0, 1, 1, 1};

    private final Rectangle walkable;
    private final float cellSize;
    private final int columns;
    private final int rows;
    private final boolean[] blocked;
    private final List<Obstacle> obstacles;

    public Pathfinder(Rectangle walkable, float cellSize,
                      List<Obstacle> obstacles, float agentRadius) {
        this.walkable = walkable;
        this.cellSize = cellSize;
        this.obstacles = obstacles;
        this.columns = (int) Math.ceil(walkable.width / cellSize);
        this.rows = (int) Math.ceil(walkable.height / cellSize);
        this.blocked = new boolean[columns * rows];
        buildBlockedCells(agentRadius);
    }

    private void buildBlockedCells(float agentRadius) {
        List<Rectangle> inflated = new ArrayList<Rectangle>();
        for (Obstacle obstacle : obstacles) {
            inflated.add(new Rectangle(
                obstacle.bounds.x - agentRadius,
                obstacle.bounds.y - agentRadius,
                obstacle.bounds.width + agentRadius * 2f,
                obstacle.bounds.height + agentRadius * 2f));
        }
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                Rectangle cell = cellBounds(col, row);
                for (Rectangle area : inflated) {
                    if (cell.overlaps(area)) {
                        blocked[row * columns + col] = true;
                        break;
                    }
                }
            }
        }
    }

    public List<Vector2> findPath(Vector2 start, Vector2 goal) {
        int startIndex = nearestFreeCell(start);
        int goalIndex = nearestFreeCell(goal);
        List<Vector2> path = new ArrayList<Vector2>();
        if (startIndex < 0 || goalIndex < 0) {
            return path;
        }
        if (startIndex == goalIndex) {
            path.add(goal.cpy());
            return path;
        }

        int size = blocked.length;
        float[] costs = new float[size];
        int[] previous = new int[size];
        boolean[] closed = new boolean[size];
        Arrays.fill(costs, Float.MAX_VALUE);
        Arrays.fill(previous, -1);

        PriorityQueue<Node> open = new PriorityQueue<Node>((a, b) -> Float.compare(a.priority, b.priority));
        costs[startIndex] = 0f;
        open.add(new Node(startIndex, heuristic(startIndex, goalIndex)));

        while (!open.isEmpty()) {
            Node current = open.poll();
            if (closed[current.index]) {
                continue;
            }
            if (current.index == goalIndex) {
                return reconstruct(previous, current.index, goal, start);
            }
            closed[current.index] = true;

            int col = current.index % columns;
            int row = current.index / columns;
            for (int i = 0; i < COLUMN_STEPS.length; i++) {
                int nextCol = col + COLUMN_STEPS[i];
                int nextRow = row + ROW_STEPS[i];
                if (nextCol < 0 || nextCol >= columns || nextRow < 0 || nextRow >= rows) {
                    continue;
                }
                int nextIndex = nextRow * columns + nextCol;
                if (blocked[nextIndex] || closed[nextIndex]) {
                    continue;
                }
                boolean diagonal = COLUMN_STEPS[i] != 0 && ROW_STEPS[i] != 0;
                if (diagonal && (blocked[row * columns + nextCol]
                    || blocked[nextRow * columns + col])) {
                    continue;
                }
                float stepCost = diagonal ? SQRT_2 : 1f;
                float newCost = costs[current.index] + stepCost;
                if (newCost < costs[nextIndex]) {
                    costs[nextIndex] = newCost;
                    previous[nextIndex] = current.index;
                    open.add(new Node(nextIndex, newCost + heuristic(nextIndex, goalIndex)));
                }
            }
        }
        return path;
    }

    private List<Vector2> reconstruct(int[] previous, int goalIndex,
                                      Vector2 goal, Vector2 start) {
        List<Vector2> path = new ArrayList<Vector2>();
        for (int index = goalIndex; index != -1; index = previous[index]) {
            path.add(cellCenter(index));
        }
        Collections.reverse(path);
        if (path.isEmpty()) {
            path.add(goal.cpy());
            return path;
        }
        if (path.get(path.size() - 1).dst2(goal) > 1f) {
            path.add(goal.cpy());
        }
        if (path.size() > 1 && path.get(0).dst2(start) < cellSize * cellSize) {
            path.remove(0);
        }
        return path;
    }

    private int nearestFreeCell(Vector2 point) {
        int col = (int) Math.floor((point.x - walkable.x) / cellSize);
        int row = (int) Math.floor((point.y - walkable.y) / cellSize);
        col = clamp(col, 0, columns - 1);
        row = clamp(row, 0, rows - 1);
        int index = row * columns + col;
        if (!blocked[index]) {
            return index;
        }

        int best = -1;
        float bestDistance = Float.MAX_VALUE;
        Vector2 center = new Vector2();
        for (int candidate = 0; candidate < blocked.length; candidate++) {
            if (blocked[candidate]) {
                continue;
            }
            center.set(cellCenter(candidate));
            float distance = center.dst2(point);
            if (distance < bestDistance) {
                bestDistance = distance;
                best = candidate;
            }
        }
        return bestDistance <= cellSize * cellSize * 36f ? best : -1;
    }

    private float heuristic(int index, int goalIndex) {
        int col = index % columns;
        int row = index / columns;
        int goalCol = goalIndex % columns;
        int goalRow = goalIndex / columns;
        int dx = Math.abs(col - goalCol);
        int dy = Math.abs(row - goalRow);
        return (dx + dy) + (SQRT_2 - 2f) * Math.min(dx, dy);
    }

    private Rectangle cellBounds(int col, int row) {
        return new Rectangle(
            walkable.x + col * cellSize,
            walkable.y + row * cellSize,
            cellSize,
            cellSize);
    }

    private Vector2 cellCenter(int index) {
        int col = index % columns;
        int row = index / columns;
        return new Vector2(
            walkable.x + (col + 0.5f) * cellSize,
            walkable.y + (row + 0.5f) * cellSize);
    }

    private static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }

    private static final class Node {
        private final int index;
        private final float priority;

        private Node(int index, float priority) {
            this.index = index;
            this.priority = priority;
        }
    }
}
