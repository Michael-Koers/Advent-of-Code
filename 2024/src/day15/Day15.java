package day15;

import config.Year2024;
import util.Direction;
import util.Point;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class Day15 extends Year2024 {

    public static void main(String[] args) throws IOException {

        var d = new Day15();
        var lines = d.readInput();

        d.solve(lines);
    }

    @Override
    public void solvePart1(List<String> lines) {
        Point robot = null;
        Set<Point> walls = new HashSet<>();
        Set<Point> boxes = new HashSet<>();

        var maze = lines.stream().takeWhile(l -> !l.isBlank()).toList();
        for (int y = 0; y < maze.size(); y++) {

            var line = lines.get(y).split("");

            if (line.length == 1) break;

            for (int x = 0; x < line.length; x++) {

                if (line[x].equals(".")) continue;

                if (line[x].equals("#")) {
                    walls.add(new Point(x, y));
                } else if (line[x].equals("O")) {
                    boxes.add(new Point(x, y));
                } else {
                    robot = new Point(x, y);
                }
            }
        }

        assert robot != null : "Robot not  found!";

        var instructions = lines.stream().skip(maze.size() + 1)
                .reduce(String::concat).get().split("");

        for (int i = 0; i < instructions.length; i++) {

            String instruction = instructions[i];

            // Ignore new lines
            if (instruction.isBlank()) continue;

            Direction direction = translateInstruction(instruction);

            // Move robot
            if (move(robot, direction, boxes, walls)) {
                robot = robot.moveDirection(direction);
            }

            // Printing of maze for debug purposes
//            System.out.println("Move " + instruction + ":");
//            prettyPrint(robot, walls, boxes);
//            System.out.println();
        }

        long result = boxes.stream().mapToLong(this::boxGps).sum();

        System.out.println("Part 1: " + result);
    }


    private long boxGps(Point point) {
        return 100L * point.y() + point.x();
    }


    private void prettyPrint(Point robot, Set<Point> walls, Set<Point> boxes) {
        long maxY = walls.stream().mapToLong(Point::y).max().getAsLong();
        long maxX = walls.stream().mapToLong(Point::x).max().getAsLong();

        for (int y = 0; y <= maxY; y++) {
            for (int x = 0; x <= maxX; x++) {

                var point = new Point(x, y);

                if (walls.contains(point)) System.out.print("#");
                else if (boxes.contains(point)) System.out.print("O");
                else if (robot.equals(point)) System.out.print("@");
                else System.out.print(".");
            }
            System.out.println();
        }
    }

    private boolean move(Point toMove, Direction direction, Set<Point> boxes, Set<Point> walls) {

        var next = toMove.moveDirection(direction);

        // We hit a wall
        if (walls.contains(next)) return false;

        // Empty space
        if (!boxes.contains(next)) {
            // If we are a box, update us in collection
            if (boxes.contains(toMove)) {
                boxes.remove(toMove);
                boxes.add(next);
            }

            return true;
        }

        // If box in front of us can't move, don't move it
        if (!move(next, direction, boxes, walls)) return false;

        // If we are a box, update us in collection
        if (boxes.contains(toMove)) {
            boxes.remove(toMove);
            boxes.add(next);
        }
        return true;
    }


    private Direction translateInstruction(String instruction) {
        return switch (instruction) {
            case "^" -> Direction.UP;
            case "v" -> Direction.DOWN;
            case "<" -> Direction.LEFT;
            case ">" -> Direction.RIGHT;
            default -> throw new IllegalArgumentException("Direction '" + instruction + "' not supported");
        };
    }

    @Override
    public void solvePart2(List<String> lines) {
        Point robot = null;
        Set<Point> walls = new HashSet<>();
        Set<WideBox> boxes = new HashSet<>();

        var maze = lines.stream().takeWhile(l -> !l.isBlank()).toList();
        for (int y = 0; y < maze.size(); y++) {
            var line = lines.get(y).split("");

            if (line.length == 1) break;

            for (int x = 0; x < line.length; x++) {

                int wideX = x * 2;

                if (line[x].equals(".")) continue;
                else if (line[x].equals("#")) {
                    walls.add(new Point(wideX, y));
                    walls.add(new Point(wideX + 1, y));
                } else if (line[x].equals("O")) {
                    boxes.add(new WideBox(new Point(wideX, y), new Point(wideX + 1, y)));
                } else {
                    robot = new Point(wideX, y);
                }
            }
        }

        assert robot != null : "Robot not found!";

        // Printing of maze for debug purposes
//        System.out.println("Initial state:");
//        prettyPrintWide(robot, walls, boxes);
//        System.out.println();

        var instructions = lines.stream().skip(maze.size() + 1)
                .reduce(String::concat).get().split("");

        for (String instruction : instructions) {

            // Ignore new lines
            if (instruction.isBlank()) continue;

            Direction direction = translateInstruction(instruction);

            // Move robot
            if (canMoveWide(robot.moveDirection(direction), direction, boxes, walls)) {
                moveWide(robot.moveDirection(direction), direction, boxes, walls);
                robot = robot.moveDirection(direction);
            }

            // Printing of maze for debug purposes
//            System.out.println("Move " + instruction + ":");
//            prettyPrintWide(robot, walls, boxes);
//            System.out.println();
        }

        long result = boxes.stream().mapToLong(WideBox::boxGps).sum();

        System.out.println("Part 2: " + result);
    }

    private boolean canMoveWide(Point next, Direction direction, Set<WideBox> boxes, Set<Point> walls) {

        Optional<WideBox> nextBoxOptional = boxes.stream().filter(w -> w.contains(next)).findFirst();

        // We hit a wall
        if (walls.contains(next)) return false;

        // Empty space
        if (nextBoxOptional.isEmpty()) return true;

        var nextBox = nextBoxOptional.get();

        return switch (direction) {
            case LEFT -> canMoveWide(nextBox.left().moveDirection(direction), direction, boxes, walls);
            case RIGHT -> canMoveWide(nextBox.right().moveDirection(direction), direction, boxes, walls);
            case UP, DOWN ->
                    canMoveWide(nextBox.left().moveDirection(direction), direction, boxes, walls) && canMoveWide(nextBox.right().moveDirection(direction), direction, boxes, walls);
            default -> throw new IllegalArgumentException("Direction " + direction + " not covered");
        };
    }

    private void moveWide(Point next, Direction direction, Set<WideBox> boxes, Set<Point> walls) {
        Optional<WideBox> nextBoxOptional = boxes.stream().filter(w -> w.contains(next)).findFirst();

        if (nextBoxOptional.isEmpty()) return;

        var nextBox = nextBoxOptional.get();

        switch (direction) {
            case LEFT -> moveWide(nextBox.left().moveDirection(direction), direction, boxes, walls);
            case RIGHT -> moveWide(nextBox.right().moveDirection(direction), direction, boxes, walls);
            case UP, DOWN -> {
                moveWide(nextBox.left().moveDirection(direction), direction, boxes, walls);
                moveWide(nextBox.right().moveDirection(direction), direction, boxes, walls);
            }
            default -> throw new IllegalArgumentException("Direction " + direction + " not covered");

        }

        boxes.remove(nextBox);
        boxes.add(nextBox.moveDirection(direction));
    }

    private void prettyPrintWide(Point robot, Set<Point> walls, Set<WideBox> boxes) {
        long maxY = walls.stream().mapToLong(Point::y).max().getAsLong();
        long maxX = walls.stream().mapToLong(Point::x).max().getAsLong();

        Set<Point> boxesUnwrapped = boxes.stream().flatMap(b -> b.getPoints().stream()).collect(Collectors.toSet());
        for (int y = 0; y <= maxY; y++) {
            for (int x = 0; x <= maxX; x++) {

                var point = new Point(x, y);

                if (walls.contains(point)) System.out.print("#");
                else if (boxesUnwrapped.contains(point)) System.out.print("O");
                else if (robot.equals(point)) System.out.print("@");
                else System.out.print(".");
            }
            System.out.println();
        }
    }

}

record WideBox(Point left, Point right) {

    long boxGps() {
        return 100L * left.y() + left.x();
    }

    WideBox moveDirection(Direction d) {
        return new WideBox(left.moveDirection(d), right.moveDirection(d));
    }

    boolean contains(Point other) {
        return left.equals(other) || right.equals(other);
    }

    Set<Point> getPoints() {
        return Set.of(left, right);
    }
}

