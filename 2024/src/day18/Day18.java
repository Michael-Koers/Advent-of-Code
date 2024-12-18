package day18;

import config.Year2024;
import util.Direction;
import util.Point;

import java.io.IOException;
import java.util.*;

public class Day18 extends Year2024 {

    // Real input
    int bytes = 1024;
    int gridSize = 70;

    // Test input
//    int gridSize = 6;
//    int bytes = 12;

    public static void main(String[] args) throws IOException {

        var d = new Day18();

        var lines = d.readInput();

        d.solve(lines);
    }

    @Override
    public void solvePart1(List<String> lines) {

        var walls = parseWalls(lines);

        int shortestPath = dijkstra(walls.stream().limit(bytes).toList());

        System.out.println("Part 1: " + shortestPath);
    }

    @Override
    public void solvePart2(List<String> lines) {
        var walls = parseWalls(lines);

        int score = 0;
        int i = 0;

        // This could be made much faster by remembering the path, and only re-doing dijkstra if a new walls
        // appears on its path. If a wall appears on a position that's not in our path, no need to re-do dijkstra.
        while (score >= 0) {
            i += 1;
            score = dijkstra(walls.stream().limit(bytes + i).toList());
        }

        System.out.println("Part 2: " + walls.get(bytes + i - 1));

    }

    private int dijkstra(List<Point> walls) {
        var start = new Point(0, 0);

        Queue<Point> paths = new ArrayDeque<>();
        paths.add(start);

        Map<Point, Integer> distances = new HashMap<>();
        distances.put(start, 0);

        var end = new Point(gridSize, gridSize);

        int score = -1;

        outer:
        while (!paths.isEmpty()) {

            var current = paths.poll();
            var distance = distances.get(current) + 1;

            for (Direction direction : List.of(Direction.LEFT, Direction.UP, Direction.RIGHT, Direction.DOWN)) {

                var next = current.moveDirection(direction);

                if (next.equals(end)) {
                    distances.put(next, distance);
                    score = distance;
                    break outer;
                }

                // Out of bounds check
                if (next.x() < 0 || next.y() < 0 || next.x() > gridSize || next.y() > gridSize) continue;

                // Don't walk into walls
                if (walls.contains(next)) continue;

                // If this distance is longer than previously known distance, skip
                if (distance >= distances.getOrDefault(next, Integer.MAX_VALUE)) continue;

                distances.put(next, distance);
                paths.add(next);
            }
        }
        return score;
    }

    List<Point> parseWalls(List<String> lines) {
        List<Point> walls = new ArrayList<>();
        for (String line : lines) {
            long x = Long.parseLong(line.split(",")[0]);
            long y = Long.parseLong(line.split(",")[1]);

            walls.add(new Point(x, y));
        }
        return walls;
    }
}
