package day06;

import config.Year2024;
import util.Direction;
import util.Point;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day06 extends Year2024 {

    public static void main(String[] args) throws IOException {

        var d = new Day06();

        d.stopwatch.start();
        d.solvePart1(d.readTestInput());
        d.solvePart2(d.readInput());
        d.stopwatch.prettyPrint();
    }

    @Override
    public void solvePart1(List<String> lines) {
        Set<Point> visited = walkMaze(lines);
        System.out.printf("Total spaces visited: %s%n", visited.size());
    }


    @Override
    public void solvePart2(List<String> lines) {
        Set<Point> visited = walkMaze(lines);

        int loops = 0;

        Point guard = findGuardPosition(lines);

        for (Point p : visited) {
            String tmp = lines.get((int) p.y());

            // Look at what we need to do to change 1 character in a string! Horrible!
            var sb = new StringBuilder(tmp).replace((int) p.x(), (int) p.x() + 1, "#").toString();
            lines.set((int) p.y(), sb);

            if (isLoopable(lines, guard)) {
                loops++;
            }

            // Put original line/string back
            lines.set((int) p.y(), tmp);
        }

        System.out.printf("Total possible loops: %s%n", loops);


    }

    private boolean isLoopable(List<String> lines, Point guard) {
        Direction direction = Direction.UP;

        Set<Movement> visited = new HashSet<>();
        visited.add(new Movement(guard, direction));

        while (true) {

            var nextPosition = guard.moveDirection(direction);
            if (visited.contains(new Movement(nextPosition, direction))) {
                return true;
            }

            if (reachedBorders(nextPosition, lines)) {
                break;
            }

            if (isTileOccupied(nextPosition, lines)) {
                visited.add(new Movement(nextPosition, direction));
                direction = direction.turnRight();
                continue;
            }
            guard = nextPosition;
        }
        return false;
    }

    private Set<Point> walkMaze(List<String> lines) {
        Set<Point> visited = new HashSet<>();

        Point guard = findGuardPosition(lines);
        Direction direction = Direction.UP;

        visited.add(guard);

        while (true) {
            var nextPosition = guard.moveDirection(direction);

            if (reachedBorders(nextPosition, lines)) {
                break;
            }

            if (isTileOccupied(nextPosition, lines)) {
                direction = direction.turnRight();
                continue;
            }

            visited.add(nextPosition);
            guard = nextPosition;
        }
        return visited;
    }

    private boolean reachedBorders(Point point, List<String> lines) {
        return point.y() < 0 || point.x() < 0 ||
                point.y() > lines.size() - 1 ||
                point.x() > lines.getFirst().length() - 1;
    }

    private boolean isTileOccupied(Point guard, List<String> lines) {
        return lines.get((int) guard.y()).charAt((int) guard.x()) == '#';
    }

    private Point findGuardPosition(List<String> lines) {
        for (int y = 0; y < lines.size(); y++) {
            var x = lines.get(y).indexOf("^");
            if (x == -1) continue;
            return new Point(x, y);
        }
        throw new IllegalArgumentException("No Guard found!");
    }


}

record Movement(Point point, Direction direction) {
}

