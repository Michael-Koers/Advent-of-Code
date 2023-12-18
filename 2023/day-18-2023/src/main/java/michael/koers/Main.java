package michael.koers;

import util.Direction;
import util.FileInput;
import util.Point;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class Main {
    public static void main(String[] args) throws URISyntaxException, IOException {

        List<String> read = FileInput.read(FileInput.INPUT_TEST, Main.class);

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

        int n = trenches.size();
        double a = 0.0;
        for (int i = 0; i < n - 1; i++) {
            a += trenches.get(i).x() * trenches.get(i + 1).y() - trenches.get(i + 1).x() * trenches.get(i).y();
        }
        a  = Math.abs(a + trenches.get(n - 1).x() * trenches.get(0).y() - trenches.get(0).x() * trenches.get(n - 1).y()) / 2.0;

        prettyPrint(trenches, field);
        System.out.printf("Solved part 1, total cubic meters: %s%n", a);
    }

    private static void prettyPrint(List<Point> trenches, Field field) {
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

        int edge = 0;
        for (String s : read) {

            // todo: something with color
            String[] parts = s.split(" ");

            String direction = parts[0];
            int meters = Integer.parseInt(parts[1]);
//            int color = parts[2];
            edge += meters - 2;
            for (int i = 0; i < meters; i++) {
                switch (direction) {
                    case "R" -> current = current.moveDirection(Direction.RIGHT);
                    case "D" -> current = current.moveDirection(Direction.DOWN);
                    case "L" -> current = current.moveDirection(Direction.LEFT);
                    case "U" -> current = current.moveDirection(Direction.UP);
                    default -> throw new NoSuchElementException("Direction " + direction + " unknown");
                }
            }
            points.add(current);
        }
        System.out.println(edge);
        return points;
    }
}

record Field(int start_y, int start_x, int end_y, int end_x) {
};