package day16;

import config.Year2024;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.SortedSet;
import java.util.TreeSet;
import org.jetbrains.annotations.NotNull;
import util.Direction;
import util.Point;

public class Day16 extends Year2024 {
    public static void main(String[] args) throws IOException {

        var d = new Day16();

        var lines = d.readTestInput();

        d.solvePart1(lines);

    }

    @Override
    public void solvePart1(final List<String> lines) {

        List<Point> walls = getWalls(lines);
        Path start = getPath(lines);
        Point end = getEnd(lines);

        SortedSet<Path> paths = new TreeSet<>();
        paths.add(start);

        int bestScore = Integer.MAX_VALUE;
        Path bestPath = null;

        while (!paths.isEmpty()) {

            var path = paths.first();
            paths.remove(path);

            // This path is worse than the current best path, so don't continue it
            if (path.score() >= bestScore) {return;}

            // Try all directions
            for (final Direction direction : List.of(path.direction(), path.direction().turnLeft(), path.direction().turnRight())) {

                var nextStep = path.moveDirection(direction);

                // Don't go backwards
                if (path.steps().contains(nextStep.current())) {continue;}

                // Don't walk into walls
                if (walls.contains(nextStep.current())) {continue;}

                var newPath = path.moveDirection(direction);

                // reached end
                if (newPath.current().equals(end)) {
                    bestScore = newPath.score();
                    bestPath = newPath;
                } else {
                    paths.add(newPath);
                }
            }
        }

        System.out.println(bestPath.score());

        prettyPrint(bestPath, end, walls);
    }

    private void prettyPrint(final Path bestPath, Point end, List<Point> walls) {

        final long maxY = walls.stream().mapToLong(Point::y).max().getAsLong();
        final long maxX = walls.stream().mapToLong(Point::x).max().getAsLong();

        for (int y = 0; y <= maxY; y++) {
            for (int x = 0; x <= maxX; x++) {
                var point = new Point(x, y);

                if (walls.contains(point)) {
                    System.out.print("#");
                } else if (end.equals(point)) {
                    System.out.print("E");
                } else if (bestPath.steps().contains(point)) {
                    System.out.print("P");
                } else {
                    System.out.print("."); ;
                }
            }
            System.out.println();
        }
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

    private Path getPath(final List<String> lines) {
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
        return new Path(List.of(), start, Direction.RIGHT, 0);
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

record Path(List<Point> steps, Point current, Direction direction, int score) implements Comparable<Path> {
    @Override
    public int compareTo(@NotNull final Path o) {
        return Integer.compare(score(), o.score);
    }

    Path moveDirection(Direction direction) {
        int penalty = (direction() == direction) ? 1 : 1000;
        var newSteps = new ArrayList<>(steps);
        newSteps.add(current);
        return new Path(newSteps, current.moveDirection(direction), direction, score + penalty);
    }
}


