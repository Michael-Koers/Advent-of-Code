package michael.koers;

import util.Direction;
import util.FileInput;
import util.Point;
import util.Stopwatch;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    private static Map<Point, Pathpiece> pathCache = new HashMap<>();

    public static void main(String[] args) throws URISyntaxException, IOException {

        List<String> read = FileInput.read(FileInput.INPUT, Main.class);

        Field field = parseInput(read);

        // True for part 1, false for part 2
        Stopwatch stopwatch = new Stopwatch();
        solvePart1And2(field, false);
        stopwatch.print();
    }

    private static void solvePart1And2(Field field, boolean part1) {

        Deque<Path> queue = new LinkedList<>();
        queue.add(new Path(field.start(), new HashSet<>()));
        Set<Path> finishedPaths = new HashSet<>();
        Long worst = 0L;

        while (!queue.isEmpty()) {

            Path currentPath = queue.pollFirst();

            // If we already visited this path once before, just skip to the end
            if (pathCache.containsKey(currentPath.current())) {
                Pathpiece cachedPath = pathCache.get(currentPath.current());
                currentPath = currentPath.moveTo(cachedPath.end(), cachedPath.tiles());
            }

            Set<Path> nextPaths = getNextSplit(currentPath, field, part1);

            // If reached end, don't need to do anything more, otherwise, keep on walking
            for (Path nextPath : nextPaths) {
                if (nextPath.current().equals(field.end())) {
                    if(nextPath.previous().size() > worst){
                        System.out.printf("Reached worst end with %s steps%n", nextPath.previous().size());
                        worst = (long) nextPath.previous().size();
                    }
                } else {
                    queue.addFirst(nextPath);
                }
            }
        }

        System.out.printf("Found a total of %s path%n", finishedPaths.size());
//        prettyPrint(field, finishedPaths.stream().map(Path::previous).max((p1, p2) -> p1.size() > p2.size() ? 1 : -1).get());
        System.out.printf("Worst path took %s steps %n", worst);
    }

    private static void prettyPrint(Field field, Set<Point> points) {
        for (int y = 0; y < field.map().length; y++) {
            for (int x = 0; x < field.map()[0].length; x++) {
                if (points.contains(new Point(x, y))) {
                    System.out.print("O");
                } else {
                    System.out.print(field.map()[y][x]);
                }
            }
            System.out.println();
        }
    }

    private static Set<Path> getNextSplit(Path currentPath, Field field, boolean slopey) {

        Queue<Point> nextSteps = new LinkedList<>();
        Point start = currentPath.current();
        nextSteps.add(start);
        Set<Point> visited = new HashSet<>(currentPath.previous());

        Set<Point> segment = new HashSet<>();
        Point finalSegmentStep = start;

        outer:
        while (nextSteps.size() == 1) {

            Point step = nextSteps.poll();
            visited.add(step);
            segment.add(step);
            finalSegmentStep = step;

            for (Direction direction : List.of(Direction.LEFT, Direction.UP, Direction.DOWN, Direction.RIGHT)) {

                Point nextStep = step.moveDirection(direction);

                // Don't walk over same path again
                if (visited.contains(nextStep)) {
                    continue;
                }

                // We reached the end, save this step
                if (nextStep.equals(field.end())) {
                    nextSteps.add(nextStep);
                    break outer;
                }

                // Out of bounds check
                if (nextStep.x() < 0 || nextStep.x() >= field.map().length || nextStep.y() < 0 || nextStep.y() >= field.map()[0].length) {
                    continue;
                }

                // Can't step on rocks
                if (field.map()[(int) nextStep.y()][(int) nextStep.x()].equals("#")) {
                    continue;
                }

                // Make sure we only go down the slope if we come from the right direction
                if (slopey && ((field.map()[(int) nextStep.y()][(int) nextStep.x()].equals(">") && !direction.equals(Direction.RIGHT))
                        || (field.map()[(int) nextStep.y()][(int) nextStep.x()].equals("v") && !direction.equals(Direction.DOWN))
                        || (field.map()[(int) nextStep.y()][(int) nextStep.x()].equals("<") && !direction.equals(Direction.LEFT))
                        || (field.map()[(int) nextStep.y()][(int) nextStep.x()].equals("^") && !direction.equals(Direction.UP)))) {
                    continue;
                }

                nextSteps.add(nextStep);
            }
        }

        pathCache.put(start, new Pathpiece(finalSegmentStep, segment));

        return nextSteps.stream()
                .map(ns -> new Path(ns, visited))
                .collect(Collectors.toSet());
    }

    private static Field parseInput(List<String> read) {
        String[][] map = new String[read.size()][read.get(0).length()];

        for (int i = 0; i < read.size(); i++) {

            map[i] = read.get(i).split("");
        }

        return new Field(new Point(1L, 0L), new Point(read.get(0).length() - 2, read.size() - 1), map);
    }
}

record Field(Point start, Point end, String[][] map) {
};

record Path(Point current, Set<Point> previous) {

    Path moveTo(Point next, Set<Point> tiles) {
        Set<Point> newTiles = new HashSet<>(previous);
        newTiles.addAll(tiles);
        return new Path(next, newTiles);
    }
};

record Pathpiece(Point end, Set<Point> tiles) {
};