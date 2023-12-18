package michael.koers;

import util.Direction;
import util.FileInput;
import util.Point;
import util.Stopwatch;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

public class Main {
    public static void main(String[] args) throws URISyntaxException, IOException {

        List<String> read = FileInput.read(FileInput.INPUT, Main.class);

        List<Point> trenches;

        trenches = parseInput_1(read);
        Field field = getDimensions(trenches);
        solvePart1(trenches, field);


        Stopwatch stopwatch = new Stopwatch();
        Field_2 field = parseInput_2(read);
        solvePart2(field);
        stopwatch.print();

    }

    private static void solvePart2(Field_2 field2) {
        List<Point> trenches = field2.corners().reversed();
        int n = trenches.size();
        long area = 0L;
        for (int i = 0; i < n - 1; i++) {
            area += trenches.get(i).x() * trenches.get(i + 1).y() - trenches.get(i + 1).x() * trenches.get(i).y();
        }
        long result = Math.abs(area + trenches.get(n - 1).x() * trenches.get(0).y() - trenches.get(0).x() * trenches.get(n - 1).y()) / 2L;
        result += (field2.trenches() / 2) + 1;
        System.out.printf("Solved part 2, total surface area: %s%n", result);
    }

    private static Field_2 parseInput_2(List<String> read) {

        List<Point> extremes = new LinkedList<>();
        Point current = new Point(0L, 0L);
        long trenches = 0L;

        for (String s : read) {
            String instruction = s.split(" ")[2].replace("(", "").replace(")", "");
            String direction = instruction.substring(instruction.length() - 1);
            String hexString = instruction.substring(1, instruction.length() - 1);
            Long hex = Long.parseLong(hexString, 16);
            trenches += hex;
            switch (direction) {
                case "0" -> {
                    current = current.moveDistance(new Point(hex, 0));
                } // Right
                case "1" -> {
                    current = current.moveDistance(new Point(0, hex));
                } // Down
                case "2" -> {
                    current = current.moveDistance(new Point(-hex, 0));
                } // Left
                case "3" -> {
                    current = current.moveDistance(new Point(0, -hex));
                } // Up
            }

            extremes.add(current);
        }
        Field tmp = getDimensions(extremes);
        return new Field_2(tmp.start_y(), tmp.start_x(), tmp.end_y(), tmp.end_x(), extremes, trenches);
    }

    private static Field getDimensions(List<Point> trenches) {
        return new Field(trenches.stream().mapToLong(Point::y).min().getAsLong(),
                trenches.stream().mapToLong(Point::x).min().getAsLong(),
                trenches.stream().mapToLong(Point::y).max().getAsLong(),
                trenches.stream().mapToLong(Point::x).max().getAsLong());
    }

    private static void solvePart1(List<Point> trenches, Field field) {

        long start_y = trenches.stream().mapToLong(Point::y).min().getAsLong() + 1;
        long start_x = trenches.stream().filter(p -> p.y() == start_y).mapToLong(Point::x).min().getAsLong() + 1;

        Point start = new Point(start_x, start_y);

        System.out.println("Starting position: " + start);

        Set<Point> inside = floodFill(trenches, new HashSet<>(), start);
        inside.addAll(trenches);

        prettyPrint(trenches, field);
        System.out.printf("Solved part 1, total cubic meters: %s%n", inside.size());
    }

    private static Set<Point> floodFill(List<Point> trenches, Set<Point> inside, Point next) {

        if (trenches.contains(next)) {
            return inside;
        } else if (inside.contains(next)) {
            return inside;
        }

        inside.add(next);

        // Go left, right, up and down
        inside.addAll(floodFill(trenches, inside, next.moveDirection(Direction.UP)));
        inside.addAll(floodFill(trenches, inside, next.moveDirection(Direction.RIGHT)));
        inside.addAll(floodFill(trenches, inside, next.moveDirection(Direction.LEFT)));
        inside.addAll(floodFill(trenches, inside, next.moveDirection(Direction.DOWN)));

        return inside;
    }

    private static void prettyPrint(Collection<Point> trenches, Field field) {
        for (long y = field.start_y(); y <= field.end_y(); y++) {
            for (long x = field.start_x(); x <= field.end_x(); x++) {
                if (trenches.contains(new Point(x, y))) {
                    System.out.print("#");
                } else {
                    System.out.print(".");
                }
            }
            System.out.println();
        }
        System.out.println();

    }

    private static List<Point> parseInput_1(List<String> read) {

        Point current = new Point(0, 0);
        List<Point> corners = new ArrayList<>();
        corners.add(current);

        for (String s : read) {

            String[] parts = s.split(" ");

            String direction = parts[0];
            int distance = Integer.parseInt(parts[1]);

                switch (direction) {
                    case "R" -> current = current.moveDistance(new Point());
                    case "D" -> current = current.moveDirection(Direction.DOWN);
                    case "L" -> current = current.moveDirection(Direction.LEFT);
                    case "U" -> current = current.moveDirection(Direction.UP);
                    default -> throw new NoSuchElementException("Direction " + direction + " unknown");
                }
                corners.add(current);
            }
        return corners;
    }
}

record Field(long start_y, long start_x, long end_y, long end_x) {
};

record Field_2(long start_y, long start_x, long end_y, long end_x, List<Point> corners, long trenches) {
};