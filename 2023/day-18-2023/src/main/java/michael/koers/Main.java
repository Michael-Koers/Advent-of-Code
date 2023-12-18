package michael.koers;

import util.Direction;
import util.FileInput;
import util.Point;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

public class Main {
    public static void main(String[] args) throws URISyntaxException, IOException {

        List<String> read = FileInput.read(FileInput.INPUT, Main.class);

        List<Point> trenches = parseInput(read);

        Field field = getDimensions(trenches);
        solvePart1(trenches, field);
    }

    private static Field getDimensions(List<Point> trenches) {
        return new Field(trenches.stream().mapToInt(Point::y).min().getAsInt(),
                trenches.stream().mapToInt(Point::x).min().getAsInt(),
                trenches.stream().mapToInt(Point::y).max().getAsInt(),
                trenches.stream().mapToInt(Point::x).max().getAsInt());
    }

    private static void solvePart1(List<Point> trenches, Field field) {

        int start_y = trenches.stream().mapToInt(Point::y).min().getAsInt() + 1;
        int start_x = trenches.stream().filter(p -> p.y() == start_y).mapToInt(Point::x).min().getAsInt() + 1;

        Point start = new Point(start_x, start_y);

        System.out.println("Starting position: " + start);

        Set<Point> inside = floodFill(trenches,new HashSet<>(), start);
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
        for (int y = field.start_y(); y <= field.end_y(); y++) {
            for (int x = field.start_x(); x <= field.end_x(); x++) {
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

    private static List<Point> parseInput(List<String> read) {

        Point current = new Point(0, 0);
        List<Point> points = new ArrayList<>();
        points.add(current);

        for (String s : read) {

            // todo: something with color
            String[] parts = s.split(" ");

            String direction = parts[0];
            int meters = Integer.parseInt(parts[1]);
//            int color = parts[2];

            for (int i = 0; i < meters; i++) {
                switch (direction) {
                    case "R" -> current = current.moveDirection(Direction.RIGHT);
                    case "D" -> current = current.moveDirection(Direction.DOWN);
                    case "L" -> current = current.moveDirection(Direction.LEFT);
                    case "U" -> current = current.moveDirection(Direction.UP);
                    default -> throw new NoSuchElementException("Direction " + direction + " unknown");
                }
                points.add(current);
            }
        }
        return points;
    }
}

record Field(int start_y, int start_x, int end_y, int end_x) {
};