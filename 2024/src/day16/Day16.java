package day16;

import config.Year2024;
import util.Direction;
import util.Point;

import java.io.IOException;
import java.util.*;

public class Day16 extends Year2024 {

    int bestScore = 0;

    // Caching! /s
    List<Point> walls;
    Point start;
    Point end;

    public static void main(String[] args) throws IOException {

        var d = new Day16();

        var lines = d.readInput();

        d.stopwatch.start();
        d.solvePart1(lines);
        d.solvePart2(lines);
        d.stopwatch.print();
    }

    @Override
    public void solvePart1(final List<String> lines) {

        List<Point> walls = getWalls(lines);
        Point start = getStart(lines);
        Point end = getEnd(lines);

        Map<Path, Integer> pathScores = dijkstra(start, walls);

        int lowestScore = pathScores
                .entrySet()
                .stream()
                .filter((k) -> k.getKey().position().equals(end))
                .map(Map.Entry::getValue)
                .findFirst().get();
        System.out.println("Part 1: " + lowestScore);
        bestScore = lowestScore;
    }

    Map<Path, Integer> dijkstra(Point start, List<Point> walls) {
        var startPath = new Path(start, Direction.RIGHT);

        Map<Path, Integer> distances = new HashMap<>();
        distances.put(startPath, 0);

        Queue<Path> queue = new ArrayDeque<>();
        queue.add(startPath);

        while (!queue.isEmpty()) {

            var current = queue.remove();
            var distance = distances.get(current);

            for (Direction direction : List.of(current.direction(), current.direction().turnLeft(), current.direction().turnRight())) {

                var next = current.position().moveDirection(direction);
                var nextPath = new Path(next, direction);

                // Don't walk into walls
                if (walls.contains(next)) {
                    continue;
                }

                int cost = distance + (current.direction().equals(direction) ? 1 : 1001);

                if (cost > distances.getOrDefault(nextPath, Integer.MAX_VALUE)) {
                    continue;
                }

                distances.put(nextPath, cost);
                queue.add(nextPath);

            }
        }
        return distances;
    }

    private Point getEnd(final List<String> lines) {
        // Caching!
        if (this.end != null) return this.end;

        Point end = null;

        outer:
        for (int y = 0; y < lines.size(); y++) {
            var line = lines.get(y).split("");

            for (int x = 0; x < line.length; x++) {

                if (line[x].equals("E")) {
                    end = new Point(x, y);
                    break outer;
                }

            }
        }
        assert end != null : "End not found!";
        this.end = end;
        return end;
    }

    private Point getStart(final List<String> lines) {
        // Caching!
        if (this.start != null) return this.start;

        Point start = null;

        outer:
        for (int y = 0; y < lines.size(); y++) {
            var line = lines.get(y).split("");

            for (int x = 0; x < line.length; x++) {

                if (line[x].equals("S")) {
                    start = new Point(x, y);
                    break outer;
                }

            }
        }
        assert start != null : "Start not found!";
        this.start = start;
        return start;
    }

    private List<Point> getWalls(final List<String> lines) {
        // Caching!
        if (walls != null) return walls;
        List<Point> walls = new ArrayList<>();
        for (int y = 0; y < lines.size(); y++) {
            var line = lines.get(y).split("");

            for (int x = 0; x < line.length; x++) {

                if (line[x].equals("#")) {
                    walls.add(new Point(x, y));
                }

            }
        }
        this.walls = walls;
        return walls;
    }

    @Override
    public void solvePart2(final List<String> lines) {

        int steps = bestScore % 1000;
        int turns = bestScore / 1000;

        var start = getStart(lines);
        var end = getEnd(lines);
        var walls = getWalls(lines);

        int tiles = reverseDijkstra(start, end, walls, steps, turns, bestScore);

        System.out.println("Part 2: " + tiles);
    }

    private int reverseDijkstra(Point start, Point end, List<Point> walls, int maxSteps, int maxTurns, int bestScore) {

        var startPath = new Path(start, Direction.RIGHT);

        Map<Path, Integer> distances = new HashMap<>();
        distances.put(startPath, 0);

        Queue<PathHistory> paths = new ArrayDeque<>();
        paths.add(new PathHistory(startPath, 0, 0, List.of(start)));

        Set<Point> tilesToEnd = new HashSet<>();
        tilesToEnd.add(end);

        while (!paths.isEmpty()) {

            var current = paths.poll();
            var distance = distances.get(current.path());

            for (Direction direction : List.of(current.path().direction(), current.path().direction().turnLeft(), current.path().direction().turnRight())) {

                var nextPosition = current.path().position().moveDirection(direction);
                var nextPath = new Path(nextPosition, direction);

                if (nextPosition.equals(end)) {
                    tilesToEnd.addAll(current.history());
                    continue;
                }
                if (walls.contains(nextPosition)) continue;

                int extraCost;
                int extraTurns = 0;
                if (current.path().direction().equals(direction)) {
                    extraCost = 1;
                } else {
                    extraCost = 1001;
                    extraTurns = 1;
                }

                int cost = distance + extraCost;

                // Cost is getting too high
                if (cost > distances.getOrDefault(nextPath, Integer.MAX_VALUE)) continue;
                if (cost > bestScore) continue;

                var nextSteps = new ArrayList<>(current.history());
                nextSteps.add(nextPosition);
                var nextHistory = new PathHistory(nextPath, current.stepsTaken() + 1, current.turnsTaken() + extraTurns, nextSteps);

                // Too many steps / turns
                if (nextHistory.stepsTaken() > maxSteps) continue;
                if (nextHistory.turnsTaken() > maxTurns) continue;

                distances.put(nextPath, cost);
                paths.add(nextHistory);

            }

        }

        return tilesToEnd.size();
    }
}

record Path(Point position, Direction direction) {
}

record PathHistory(Path path, int stepsTaken, int turnsTaken, List<Point> history) {
}
