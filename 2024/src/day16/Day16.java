package day16;

import config.Year2024;
import util.Direction;
import util.Point;

import java.io.IOException;
import java.util.*;

public class Day16 extends Year2024 {

    Map<Path, Set<Point>> shortestPaths = new HashMap<>();

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

        Map<Path, Integer> pathScores = dijkstra(start, walls);

        var bestPath = pathScores.entrySet().stream().filter(p -> p.getKey().position().equals(end)).min(Comparator.comparingInt(Map.Entry::getValue));
        bestPath.ifPresent(System.out::println);
        bestPath.ifPresent(bp -> System.out.println(shortestPaths.get(bp.getKey()).size()+1));
    }

    Map<Path, Integer> dijkstra(Point start, List<Point> walls) {
        var startPath = new Path(start, Direction.RIGHT);
        Map<Path, Integer> distances = new HashMap<>();
        distances.put(startPath, 0);

        Queue<Path> queue = new ArrayDeque<>();
        queue.add(startPath);

        shortestPaths.put(startPath, new HashSet<>());

        while (!queue.isEmpty()) {

            var current = queue.remove();
            var distance = distances.get(current);

            for (Direction direction : List.of(current.direction(), current.direction().turnLeft(), current.direction().turnRight())) {

                var next = current.position().moveDirection(direction);
                var nextPath = new Path(next, direction);

                // Don't walk into walls
                if (walls.contains(next)) {continue;}

                int cost = distance + (current.direction().equals(direction) ? 1 : 1001);

                if (cost > distances.getOrDefault(nextPath, Integer.MAX_VALUE)) {continue;}

                distances.put(nextPath, cost);
                queue.add(nextPath);

                // If we found a shorter path, forget the previous quickest path
                if(cost < distances.getOrDefault(nextPath, Integer.MAX_VALUE)){
                    shortestPaths.remove(nextPath);
                }

                var newPath = new HashSet<>(shortestPaths.get(current));

                newPath.addAll(shortestPaths.getOrDefault(nextPath, new HashSet<>()));
                newPath.add(current.position());
                shortestPaths.put(nextPath, newPath);
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
