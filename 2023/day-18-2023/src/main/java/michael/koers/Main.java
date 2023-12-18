package michael.koers;

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

        Lagoon lagoon = parseInput(read, true);
        solveSurfaceArea(lagoon);

        lagoon = parseInput(read, false);
        solveSurfaceArea(lagoon);
    }

    private static Field getDimensions(List<Point> trenches) {
        return new Field(trenches.stream().mapToLong(Point::y).min().getAsLong(),
                trenches.stream().mapToLong(Point::x).min().getAsLong(),
                trenches.stream().mapToLong(Point::y).max().getAsLong(),
                trenches.stream().mapToLong(Point::x).max().getAsLong());
    }

    private static void solveSurfaceArea(Lagoon field2) {
        List<Point> trenches = field2.corners().reversed();
        int n = trenches.size();
        long area = 0L;
        for (int i = 0; i < n - 1; i++) {
            area += trenches.get(i).x() * trenches.get(i + 1).y() - trenches.get(i + 1).x() * trenches.get(i).y();
        }
        long result = Math.abs(area + trenches.get(n - 1).x() * trenches.get(0).y() - trenches.get(0).x() * trenches.get(n - 1).y()) / 2L;
        result += (field2.trenchCount() / 2) + 1;
        System.out.printf("Solved, total surface area: %s%n", result);
    }

    private static Lagoon parseInput(List<String> read, boolean isPart1) {
        List<Point> corners = new ArrayList<>();
        Point current = new Point(0, 0);
        corners.add(current);

        long trenchCount = 0L;

        for (String s : read) {
            String[] parts = s.split(" ");

            String direction = "";
            long distance;

            // Part 1
            if (isPart1) {
                direction = parts[0];
                distance = Long.parseLong(parts[1]);
            }
            // Part 2
            else {
                String instruction = parts[2].replace("(", "").replace(")", "");
                direction = instruction.substring(instruction.length() - 1);
                distance = Long.parseLong(instruction.substring(1, instruction.length() - 1), 16);
            }

            trenchCount += distance;

            switch (direction) {
                case "R", "0" -> current = current.moveDistance(new Point(distance, 0));
                case "D", "1" -> current = current.moveDistance(new Point(0, distance));
                case "L", "2" -> current = current.moveDistance(new Point(-distance, 0));
                case "U", "3" -> current = current.moveDistance(new Point(0, -distance));
                default -> throw new NoSuchElementException("Direction " + direction + " unknown");
            }
            corners.add(current);
        }
        return new Lagoon(corners, getDimensions(corners), trenchCount);
    }
}

record Field(long start_y, long start_x, long end_y, long end_x) {
};

record Lagoon(List<Point> corners, Field field, long trenchCount) {
};