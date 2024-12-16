package day16;

import config.Year2024;
import util.Direction;
import util.Point;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class Day16 extends Year2024 {
    private Object i2;

    public static void main(String[] args) throws IOException {

        var d = new Day16();

        var lines = d.readInput();

        d.stopwatch.start();
        d.solvePart1(lines);
        d.stopwatch.print();
    }

    @Override
    public void solvePart1(final List<String> lines) {

        List<Point> walls = getWalls(lines);
        Path start = getPath(lines);
        Point end = getEnd(lines);

        PriorityQueue<Path> paths = new PriorityQueue<>(Comparator.comparing(Path::pathScore).reversed());
//        Queue<Path> paths = new ArrayDeque<>();
        paths.add(start);

        long bestScore = 2 * (walls.stream().mapToLong(Point::x).max().getAsLong() + walls.stream().mapToLong(Point::x).max().getAsLong()) * 1000;

        while (!paths.isEmpty()) {

            var path = paths.remove();

            // This path is worse than the current best path, so don't continue it
            if (path.score() > bestScore) {
                continue;
            }

            // Try all directions
            for (final Direction direction : List.of(path.direction().turnLeft(), path.direction(), path.direction().turnRight())) {

                var nextPath = path.moveDirection(direction);

                // Don't go backwards
                if (path.steps().contains(nextPath.current())) {
                    continue;
                }

                // Don't walk into walls
                if (walls.contains(nextPath.current())) {
                    continue;
                }

                // reached end
                if (nextPath.current().equals(end) && nextPath.score() < bestScore) {
                    System.out.println("Reached end, best score: " + nextPath.score() + ", paths left: " + paths.size());
                    bestScore = nextPath.score();
                    continue;
                }

                // Only add path if it was actually faster than a known path to the same location
                if (paths.stream().noneMatch(p -> p.current() == nextPath.current() && p.score() <= nextPath.score())) {

                    // Remove all paths that took longer to get to the same location
                    paths.removeIf(p -> p.current() == nextPath.current() && p.score() >= nextPath.score());

                    paths.add(nextPath);

                }
            }
        }

        System.out.println(bestScore);
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
                    System.out.print(".");
                    ;
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
        return new Path(List.of(), start, Direction.RIGHT, 0, 0);
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

record Path(List<Point> steps, Point current, Direction direction, int score, int pathScore) {

    Path moveDirection(Direction newDirection) {
        int penalty = (this.direction.equals(newDirection)) ? 1 : 1001;
        var newSteps = new ArrayList<>(steps);
        newSteps.add(current);

        var newPathScore = pathScore + ((newDirection.equals(Direction.UP) || newDirection.equals(Direction.RIGHT)) ? 1 : 0);
        return new Path(newSteps, current.moveDirection(newDirection), newDirection, score + penalty, newPathScore);
    }

}


