package michael.koers;

import util.Direction;
import util.FileInput;
import util.Point;
import util.Stopwatch;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

public class Main {

    public static Set<Light> cachedVisits = new HashSet<>();

    public static void main(String[] args) throws URISyntaxException, IOException {

        List<String> read = FileInput.read(FileInput.INPUT_TEST, Main.class);

        String[][] input = parseInput(read);

        Stopwatch stopwatch = new Stopwatch();
        solvePart1(input);
        solvePart2(input);
        stopwatch.print();
    }

    private static void solvePart2(String[][] input) {

        long max = 0L;

        // START TOP
        for (int i = 0; i < input[0].length; i++) {
            Light beam = new Light(new Point(i, -1), Direction.DOWN);
            System.out.printf("Calculating beam %s%n", beam);
            Set<Point> points = continueBeam(beam, input);
            cachedVisits.clear();
            if (points.size() > max) max = points.size();
        }

        // START BOTTOM
        for (int i = 0; i < input[0].length; i++) {
            Light beam = new Light(new Point(i, input.length), Direction.UP);
            System.out.printf("Calculating beam %s%n", beam);
            Set<Point> points = continueBeam(beam, input);
            cachedVisits.clear();
            if (points.size() > max) max = points.size();
        }
        // START LEFT
        for (int i = 0; i < input.length; i++) {
            Light beam = new Light(new Point(-1, i), Direction.RIGHT);
            System.out.printf("Calculating beam %s%n", beam);
            Set<Point> points = continueBeam(beam, input);
            cachedVisits.clear();
            if (points.size() > max) max = points.size();
        }
        // START RIGHT
        for (int i = 0; i < input.length; i++) {
            Light beam = new Light(new Point(input[0].length, i), Direction.LEFT);
            System.out.printf("Calculating beam %s%n", beam);
            Set<Point> points = continueBeam(beam, input);
            cachedVisits.clear();
            if (points.size() > max) max = points.size();
        }

        System.out.printf("Checked all paths, most covered tiles: %s%n", max - 1);
    }

    private static void solvePart1(String[][] field) {
        Light start = new Light(new Point(-1, 0), Direction.RIGHT);
        Set<Point> tiles = continueBeam(start, field);
        System.out.printf("Beam finished travelling, total tiles covered: %s%n", tiles.size() - 1);
    }

    private static Set<Point> continueBeam(Light beam, String[][] field) {
//        System.out.printf("Beam at position %s, going %s%n", beam.position(), beam.direction());

        if(cachedVisits.contains(beam)){
            return Set.of(beam.position());
        } else{
            cachedVisits.add(beam);
        }

        // Remember the tiles we hit
        Set<Point> hits = new HashSet<>();
        hits.add(beam.position());

        // Determine beam's next position
        Point nextPos = beam.position().moveDirection(beam.direction());

        // Out of bounds checks
        if ((nextPos.x() < 0 && beam.direction() == Direction.LEFT)
                || (nextPos.x() >= field.length && beam.direction() == Direction.RIGHT)
                || (nextPos.y() < 0 && beam.direction() == Direction.UP)
                || (nextPos.y() >= field[0].length && beam.direction() == Direction.DOWN)) {
//            System.out.printf("Hit out of bounds, end of beam %s%n", beam);
            return hits;
        }

        // Get next position tile
        String nextSpace = field[nextPos.y()][nextPos.x()];

        // Hit nothing
        switch (nextSpace) {
            case "/" -> hits.addAll(switch (beam.direction()) {
                case LEFT -> continueBeam(new Light(nextPos, Direction.DOWN), field);
                case RIGHT -> continueBeam(new Light(nextPos, Direction.UP), field);
                case UP -> continueBeam(new Light(nextPos, Direction.RIGHT), field);
                case DOWN -> continueBeam(new Light(nextPos, Direction.LEFT), field);
                default -> throw new NoSuchElementException("Direction " + beam.direction() + " unknown");
            });
            case "\\" -> hits.addAll(switch (beam.direction()) {
                case LEFT -> continueBeam(new Light(nextPos, Direction.UP), field);
                case RIGHT -> continueBeam(new Light(nextPos, Direction.DOWN), field);
                case UP -> continueBeam(new Light(nextPos, Direction.LEFT), field);
                case DOWN -> continueBeam(new Light(nextPos, Direction.RIGHT), field);
                default -> throw new NoSuchElementException("Direction " + beam.direction() + " unknown");
            });
            case "|" -> {
//                System.out.printf("Beam %s splitting%n", beam);
                if (beam.direction().equals(Direction.DOWN) || beam.direction().equals(Direction.UP)) {
                    // nothing special
                    hits.addAll(continueBeam(new Light(nextPos, beam.direction()), field));
                } else if (beam.direction().equals(Direction.LEFT) || beam.direction().equals(Direction.RIGHT)) {
                    // Send out light to the top & bottom
                    hits.addAll(continueBeam(new Light(nextPos, Direction.UP), field));
                    hits.addAll(continueBeam(new Light(nextPos, Direction.DOWN), field));
                }
            }
            case "-" -> {
                if (beam.direction().equals(Direction.LEFT) || beam.direction().equals(Direction.RIGHT)) {
                    // nothing special
                    hits.addAll(continueBeam(new Light(nextPos, beam.direction()), field));
                } else if (beam.direction().equals(Direction.UP) || beam.direction().equals(Direction.DOWN)) {
                    // Send out light to the left & right
                    hits.addAll(continueBeam(new Light(nextPos, Direction.LEFT), field));
                    hits.addAll(continueBeam(new Light(nextPos, Direction.RIGHT), field));
                }
            }
            default -> hits.addAll(continueBeam(new Light(nextPos, beam.direction()), field));
        }
        return hits;
    }


    private static String[][] parseInput(List<String> read) {
        String[][] input = new String[read.size()][read.get(0).length()];
        for (int i = 0; i < read.size(); i++) {
            input[i] = read.get(i).split("");
        }
        return input;
    }
}

record Light(Point position, Direction direction) {
};