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

        List<String> lines = FileInput.read(FileInput.INPUT, Main.class);

        Field field = parseInput(lines);

        Stopwatch stopwatch = new Stopwatch();

//        solvePart1(field);
        solvePart2(field);

        stopwatch.print();
    }

    private static void solvePart2(Field field) {

        List<Direction> directions = List.of(Direction.UP, Direction.LEFT, Direction.DOWN, Direction.RIGHT);

        long cycles = 1_000_000_000;
        Map<Set<Point>, Long> cache = new HashMap<>();
        boolean cacheSkipped = false;

        for (long x = 0; x < cycles; x++) {
            if (cache.containsKey(field.round()) && !cacheSkipped) {
                long cadance = x - cache.get(field.round());
                // Can't include first x number of cycles, because they might not be part of the repeating cycle yet
                long remainderCycles = (cycles-x) % cadance;
                // Make it so we only cycle the remainder amount of times
                x = cycles - remainderCycles;
                System.out.println("Found repeating pattern!");
                cacheSkipped = true;
            } else {
                cache.put(field.round(), x);
            }

            for (Direction direction : directions) {
                long moving = 1L;
                while (moving > 0) {

                    moving = 0L;

                    List<Point> newCubes = new ArrayList<>();
                    for (Iterator<Point> i = field.round().iterator(); i.hasNext(); ) {
                        Point roundCube = i.next();
                        Point nextCube = roundCube.moveDirection(direction);
                        if ((roundCube.y() == 0 && direction.equals(Direction.UP))
                                || (roundCube.y() == field.rows() - 1 && direction.equals(Direction.DOWN))
                                || (roundCube.x() == 0 && direction.equals(Direction.LEFT))
                                || (roundCube.x() == field.cols() - 1 && direction.equals(Direction.RIGHT))
                                || field.cube().contains(nextCube)
                                || field.round().contains(nextCube)
                                || newCubes.contains(nextCube)) {
                            continue;
                        } else {
                            moving++;
                            newCubes.add(nextCube);
                            i.remove();
                        }
                    }
                    field.round().addAll(newCubes);
                }

            }
        }

        System.out.printf("Final%n");
        prettyPrint(field);
        System.out.println("");

        long weight = field.round().stream()
                .mapToLong(r -> field.rows() - r.y())
                .sum();

        System.out.printf("Solved part 2, total weight: %s%n", weight);
    }

    private static void solvePart1(Field field) {

        long moving = 1L;

        while (moving > 0) {

            moving = 0L;

            List<Point> newCubes = new ArrayList<>();
            for (Iterator<Point> i = field.round().iterator(); i.hasNext(); ) {
                Point roundCube = i.next();
                Point nextCube = roundCube.moveDirection(Direction.UP);
                if (roundCube.y() == 0 || field.cube().contains(nextCube) || field.round().contains(nextCube)
                        || newCubes.contains(nextCube)) {
                    continue;
                } else {
                    moving++;
                    newCubes.add(nextCube);
                    i.remove();
                }
            }
            field.round().addAll(newCubes);
        }

        prettyPrint(field);

        long weight = field.round().stream()
                .mapToLong(r -> field.rows() - r.y())
                .sum();

        System.out.printf("Solved part 1, total weight: %s%n", weight);
    }

    private static void prettyPrint(Field field) {
        for (int y = 0; y < field.rows(); y++) {
            for (int x = 0; x < field.cols(); x++) {
                if (field.cube().contains(new Point(x, y))) {
                    System.out.print("#");
                } else if (field.round().contains(new Point(x, y))) {
                    System.out.print("O");
                } else {
                    System.out.print(".");
                }
            }
            System.out.println("");
        }
    }

    private static Field parseInput(List<String> lines) {

        Set<Point> rounds = new HashSet<>();
        Set<Point> cubes = new HashSet<>();

        for (int y = 0; y < lines.size(); y++) {

            for (int x = 0; x < lines.get(y).length(); x++) {

                char current = lines.get(y).charAt(x);
                if (current == '.') {
                    continue;
                }

                if (current == '#') {
                    cubes.add(new Point(x, y));
                } else {
                    rounds.add(new Point(x, y));
                }
            }
        }
        return new Field(rounds, cubes, lines.size(), lines.get(0).length());

    }
}


record Field(Set<Point> round, Set<Point> cube, int rows, int cols) {
};