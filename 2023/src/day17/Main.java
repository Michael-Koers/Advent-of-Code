package day17;

import util.Direction;
import util.FileInput;
import util.Point;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;

public class Main {

    public static void main(String[] args) throws URISyntaxException, IOException {

        List<String> read = FileInput.read(FileInput.INPUT, Main.class);
        long[][] city = parseInput(read);

        solvePart1(city, false);
        solvePart2(city, true);
    }

    private static void solvePart2(long[][] city, boolean ultra) {
        Path path = dijkstra(city, ultra);
        System.out.printf("Solved part 2, found cheapest path: %s%n", path.cost());
    }

    private static void solvePart1(long[][] city, boolean ultra) {
        Path path = dijkstra(city, ultra);
        System.out.printf("Solved part 1, found cheapest path: %s%n", path.cost());
    }

    private static Path dijkstra(long[][] city, boolean ultra) {

        int rows = city.length;
        int cols = city[0].length;

        HashSet<Node> visited = new HashSet<>();
        PriorityQueue<Path> unvisited = new PriorityQueue<>(Path::compare);

        unvisited.add(new Path(new Node(new Point(0, 1), Direction.DOWN, -1), city[1][0]));
        unvisited.add(new Path(new Node(new Point(1, 0), Direction.RIGHT, -1), city[0][1]));

        while (!unvisited.isEmpty()) {

            Path current = unvisited.remove();

            if (visited.contains(current.node())) {
                continue;
            }

            visited.add(current.node());
            // Reached end point
            if (current.node().point().equals(new Point(cols - 1, rows - 1))) {
                return current;
            }

            if (ultra) {
                // Check if same direction is in bounds
                Point next = current.node().point().moveDirection(current.node().direction());
                if (isInBounds(city, next) && current.node().steps() < 9) {
                    unvisited.add(createPath(city, next, current, current.node().direction()));
                }

                // Check if left is in bounds
                next = current.node().point().moveDirection(current.node().direction().left());
                if (isInBounds(city, next) && current.node().steps() > 2) {
                    unvisited.add(createPath(city, next, current, current.node().direction().left()));
                }

                // Check if right is in bounds
                next = current.node().point().moveDirection(current.node().direction().right());
                if (isInBounds(city, next) && current.node().steps() > 2) {
                    unvisited.add(createPath(city, next, current, current.node().direction().right()));
                }
            } else {
                // Check if same direction is in bounds
                Point next = current.node().point().moveDirection(current.node().direction());
                if (isInBounds(city, next) && current.node().steps() < 2) {
                    unvisited.add(createPath(city, next, current, current.node().direction()));
                }

                // Check if left is in bounds
                next = current.node().point().moveDirection(current.node().direction().left());
                if (isInBounds(city, next)) {
                    unvisited.add(createPath(city, next, current, current.node().direction().left()));
                }

                // Check if right is in bounds
                next = current.node().point().moveDirection(current.node().direction().right());
                if (isInBounds(city, next)) {
                    unvisited.add(createPath(city, next, current, current.node().direction().right()));
                }
            }
        }
        return null;
    }

    private static Path createPath(long[][] city, Point next, Path current, Direction direction) {
        return new Path(
                new Node(new Point(next.x(), next.y()),
                        direction, current.node().direction() == direction ? current.node().steps() + 1 : 0)
                , current.cost() + city[(int) next.y()][(int) next.x()]);
    }


    private static boolean isInBounds(long[][] field, Point next) {
        return next.x() >= 0 && next.x() < field[0].length && next.y() >= 0 && next.y() < field.length;
    }

    private static long[][] parseInput(List<String> read) {
        long[][] cave = new long[read.size()][read.get(0).length()];
        for (int y = 0; y < read.size(); y++) {
            for (int x = 0; x < read.get(y).length(); x++) {
                cave[y][x] = Long.parseLong(String.valueOf(read.get(y).charAt(x)));
            }
        }
        return cave;
    }
}


record Node(Point point, Direction direction, int steps) {
};

record Path(Node node, long cost) {
    public int compare(Path o) {
        int diff = (int) (cost - o.cost);
        if (diff == 0 && node.direction() == o.node.direction()) {
            diff = node.steps() - o.node.steps();
        }
        if (diff == 0) {
            diff = (int) (o.node.point().y() + o.node.point().x() - node.point().y() - node.point().x());
        }
        return diff;
    }
};