package day16;

import config.Year2024;
import util.Direction;
import util.Point;

import java.io.IOException;
import java.util.*;

public class Day16 extends Year2024 {

    public static void main(String[] args) throws IOException {

        var d = new Day16();

        var lines = d.readTestInput();

        d.stopwatch.start();
        d.solvePart1(lines);
        d.stopwatch.print();
    }

    @Override
    public void solvePart1(final List<String> lines) {

        List<Point> walls = getWalls(lines);
        Point start = getStart(lines);
        Point end = getEnd(lines);

        Map<Point, Integer> pathScores = dijkstra(start, walls);

        System.out.println(pathScores.get(end));
    }

    Map<Point, Integer> dijkstra(Point start, List<Point> walls) {
        Map<Point, Integer> distances = new HashMap<>();
        distances.put(start, 0);

        Queue<Path> queue = new ArrayDeque<>();
        queue.add(new Path(start, Direction.RIGHT));

        while (!queue.isEmpty()) {

            var current = queue.remove();
            var distance = distances.get(current.position());

            for (Direction direction : List.of(current.direction(), current.direction().turnLeft(), current.direction().turnRight())) {

                var next = current.position().moveDirection(direction);

                // Don't walk into walls
                if (walls.contains(next)) continue;

                int cost = distance + (current.direction().equals(direction) ? 1 : 1001);

                if (cost >= distances.getOrDefault(next, Integer.MAX_VALUE)) continue;

                distances.put(next, cost);
                queue.add(new Path(next, direction));

            }

        }

        return distances;
    }

    private Point getEnd(final List<String> lines) {
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
        return end;
    }

    private Point getStart(final List<String> lines) {
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
        return start;
    }

    private List<Point> getWalls(final List<String> lines) {
        List<Point> walls = new ArrayList<>();
        for (int y = 0; y < lines.size(); y++) {
            var line = lines.get(y).split("");

            for (int x = 0; x < line.length; x++) {

                if (line[x].equals("#")) {
                    walls.add(new Point(x, y));
                }

            }
        }
        return walls;
    }

    @Override
    public void solvePart2(final List<String> lines) {

    }
}

record Path(Point position, Direction direction) {
}