package michael.koers;

import util.Direction;
import util.FileInput;
import util.Point;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

public class Main {

    public static Set<Light> cachedVisits = new HashSet<>();

    public static void main(String[] args) throws URISyntaxException, IOException {

        List<String> read = FileInput.read(FileInput.INPUT, Main.class);

        String[][] input = parseInput(read);

        solvePart1(input);
    }

    private static void solvePart1(String[][] field) {
        Light start = new Light(new Point(-1, 0), Direction.RIGHT);
        Set<Point> points = continueBeam(start, field);

        System.out.printf("Beam finished travelling, total tiles covered: %s%n", points.size() - 1);
    }

    private static Set<Point> continueBeam(Light beam, String[][] field) {
        System.out.printf("Beam at position %s, going %s%n", beam.position(), beam.direction());

        if (cachedVisits.contains(beam)) {
            System.out.printf("Cache hit, we already walked this point(%s) and direction(%s)%n", beam.position(), beam.direction());
            return new HashSet<>();
        } else {
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
            System.out.printf("Hit out of bounds, end of beam %s%n", beam);
            return hits;
        }

        // Get next position tile
        String nextSpace = field[nextPos.y()][nextPos.x()];

        // Hit nothing
        switch (nextSpace) {
            case "." -> {
                hits.addAll(continueBeam(new Light(nextPos, beam.direction()), field));
                return hits;
            }
            case "/" -> {
                hits.addAll(switch (beam.direction()) {
                    case LEFT -> continueBeam(new Light(nextPos, Direction.DOWN), field);
                    case RIGHT -> continueBeam(new Light(nextPos, Direction.UP), field);
                    case UP -> continueBeam(new Light(nextPos, Direction.RIGHT), field);
                    case DOWN -> continueBeam(new Light(nextPos, Direction.LEFT), field);
                    default -> throw new NoSuchElementException("Direction " + beam.direction() + " unknown");
                });
                return hits;
            }
            case "\\" -> {
                hits.addAll(switch (beam.direction()) {
                    case LEFT -> continueBeam(new Light(nextPos, Direction.UP), field);
                    case RIGHT -> continueBeam(new Light(nextPos, Direction.DOWN), field);
                    case UP -> continueBeam(new Light(nextPos, Direction.LEFT), field);
                    case DOWN -> continueBeam(new Light(nextPos, Direction.RIGHT), field);
                    default -> throw new NoSuchElementException("Direction " + beam.direction() + " unknown");
                });
                return hits;
            }
            case "|" -> {
                System.out.printf("Beam %s splitting%n", beam);
                if (beam.direction().equals(Direction.DOWN) || beam.direction().equals(Direction.UP)) {
                    // nothing special
                    hits.addAll(continueBeam(new Light(nextPos, beam.direction()), field));
                    return hits;
                } else if (beam.direction().equals(Direction.LEFT) || beam.direction().equals(Direction.RIGHT)) {
                    // Send out light to the top
                    hits.addAll(continueBeam(new Light(nextPos, Direction.UP), field));
                    // Send out light to the bottom
                    hits.addAll(continueBeam(new Light(nextPos, Direction.DOWN), field));
                    return hits;
                }
            }
            case "-" -> {
                if (beam.direction().equals(Direction.LEFT) || beam.direction().equals(Direction.RIGHT)) {
                    // nothing special
                    hits.addAll(continueBeam(new Light(nextPos, beam.direction()), field));
                    return hits;
                } else if (beam.direction().equals(Direction.UP) || beam.direction().equals(Direction.DOWN)) {
                    // Send out light to the top
                    hits.addAll(continueBeam(new Light(nextPos, Direction.LEFT), field));
                    // Send out light to the bottom
                    hits.addAll(continueBeam(new Light(nextPos, Direction.RIGHT), field));
                    return hits;
                }
            }
        }
        System.out.printf("nextSpace %s didn't hit any cases%n", nextSpace);
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