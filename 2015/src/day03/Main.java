package day03;

import util.Direction;
import util.FileInput;
import util.Point;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws URISyntaxException, IOException {

        String input = FileInput.read(FileInput.INPUT, Main.class).getFirst();

//        solvePart1(input);
        solvePart2(input);
    }

    private static void solvePart2(String input) {

        Point santa = new Point(0, 0);
        Point robo = new Point(0, 0);
        Map<Point, Long> visits = new HashMap<>();

        visits.put(santa, 2L);

        long steps = 0;

        for (String direction : input.split("")) {

            Point current;

            if (steps % 2 == 0) {
                current = santa;
            } else {
                current = robo;
            }

            current = switch (direction) {
                case "^" -> current.moveDirection(Direction.UP);
                case "v" -> current.moveDirection(Direction.DOWN);
                case ">" -> current.moveDirection(Direction.RIGHT);
                case "<" -> current.moveDirection(Direction.LEFT);
                default -> throw new NoSuchMethodError();
            };

            if (visits.containsKey(current)) {
                visits.put(current, visits.get(current) + 1L);
            } else {
                visits.put(current, 1L);
            }

            if (steps % 2 == 0) {
                santa = current;
            } else {
                robo = current;
            }
            steps++;
        }

        long visited = visits.values().size();
        System.out.printf("Solved part 1, house with at least 1 visit: %s%n", visited);

    }

    private static void solvePart1(String input) {

        Point current = new Point(0, 0);
        Map<Point, Long> visits = new HashMap<>();

        visits.put(current, 1L);

        for (String direction : input.split("")) {

            current = switch (direction) {
                case "^" -> current.moveDirection(Direction.UP);
                case "v" -> current.moveDirection(Direction.DOWN);
                case ">" -> current.moveDirection(Direction.RIGHT);
                case "<" -> current.moveDirection(Direction.LEFT);
                default -> throw new NoSuchMethodError();
            };

            if (visits.containsKey(current)) {
                visits.put(current, visits.get(current) + 1L);
            } else {
                visits.put(current, 1L);
            }
        }

        long visited = visits.values().size();
        System.out.printf("Solved part 1, house with at least 1 visit: %s%n", visited);

    }
}