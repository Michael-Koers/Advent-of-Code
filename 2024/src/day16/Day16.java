package day16;

import config.Year2024;
import util.Direction;
import util.Point;

import java.io.IOException;
import java.util.*;

public class Day16 extends Year2024 {


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

        Map<Path, Node> pathScores = dijkstra(start, walls);

        var lowestScore = pathScores
                .entrySet()
                .stream()
                .filter((k) -> k.getKey().position().equals(end))
                .map(Map.Entry::getValue)
                .sorted(Comparator.comparingInt(Node::score))
                .findFirst().get();

        System.out.println("Part 1: " + lowestScore.score());

        // Part 2 didn't work
        System.out.println("Part 2: " + lowestScore.path().size());
    }


    Map<Path, Node> dijkstra(Point start, List<Point> walls) {
        var startPath = new Path(start, Direction.RIGHT);

        Map<Path, Node> distances = new HashMap<>();
        distances.put(startPath, new Node(0, Set.of(startPath)));

        Queue<Path> queue = new ArrayDeque<>();
        queue.add(startPath);

        while (!queue.isEmpty()) {

            var current = queue.remove();

            for (Direction direction : List.of(current.direction().turnLeft(), current.direction(), current.direction().turnRight())) {

                var nextPosition = current.position().moveDirection(direction);
                var nextPath = new Path(nextPosition, direction);

                if (walls.contains(nextPosition)) {
                    continue;
                }

                var node = distances.get(current);
                int cost = current.direction().equals(direction) ? 1 : 1001;

                if (node.score() + cost > distances.getOrDefault(nextPath, new Node(Integer.MAX_VALUE, Set.of())).score()) {
                    continue;
                }

                // If paths are equal, merge histories, otherwise overwrite
                if (node.score() + cost == distances.getOrDefault(nextPath, new Node(Integer.MAX_VALUE, Set.of())).score()) {
                    distances.computeIfPresent(nextPath, (k, nextNode) -> nextNode.addScore(cost).addPath(node.path()).addPath(nextPath));
                } else {
                    queue.add(nextPath);
                    distances.put(nextPath, node.addScore(cost).addPath(nextPath));
                }


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

record Node(int score, Set<Path> path) {

    Node addScore(int score) {
        return new Node(this.score + score, this.path);
    }

    Node addPath(Path point) {
        var tmp = new HashSet<>(this.path);
        tmp.add(point);
        return new Node(this.score, tmp);
    }

    Node addPath(Set<Path> points) {
        var tmp = new HashSet<>(this.path);
        tmp.addAll(points);
        return new Node(this.score, tmp);
    }

}
