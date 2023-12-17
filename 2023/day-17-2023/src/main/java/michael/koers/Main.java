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

        solvePart1(read);
    }

    private static void solvePart1(List<String> read) {

        Path start = new Path(new Point(0, 0), new HashSet<>(), Direction.NONE, 0, 0, 0);
        Point end = new Point(read.get(0).length() - 1, read.size() - 1);

        PriorityQueue<Path> unsettled = new PriorityQueue<>(Comparator.comparingInt(Path::depth));
        unsettled.add(start);

        int best = Integer.MAX_VALUE;

        while (!unsettled.isEmpty()) {
            Path path = unsettled.remove();

            if (path.cost() >= best) {
                continue;
            }

            Set<Path> neighbours = getNeighbours(path, read);

            // Reached end?
            if (neighbours.stream().anyMatch(p -> p.position().equals(end))) {
                Path endPath = neighbours.stream().filter(p -> p.position().equals(end)).findFirst().get();
                System.out.printf("Path reached end at depth %s at a cost of %s heat loss%n", endPath.depth(), endPath.cost());
                best = path.cost();
            }
            unsettled.addAll(neighbours);
        }
    }

    private static Set<Path> getNeighbours(Path path, List<String> read) {
        List<Direction> dirs = List.of(Direction.DOWN, Direction.RIGHT, Direction.LEFT, Direction.UP);
        Set<Path> neighbours = new HashSet<>();

        for (Direction dir : dirs) {

            // Don't revisit same nodes
            if (path.visited().contains(path.position().moveDirection(dir))) {
                continue;
            }

            // Can't go out of bounds
            if ((path.position().y() == 0 && dir.equals(Direction.UP))
                    || (path.position().y() == read.size() - 1 && dir.equals(Direction.DOWN))
                    || (path.position().x() == 0 && dir.equals(Direction.LEFT))
                    || (path.position().x() == read.get(0).length() - 1 && dir.equals(Direction.RIGHT))) {
                continue;
            }

            // Can't do 180 degree turn
            if ((dir.equals(Direction.LEFT) && path.direction().equals(Direction.RIGHT))
                    || (dir.equals(Direction.UP) && path.direction().equals(Direction.DOWN))
                    || (dir.equals(Direction.RIGHT) && path.direction().equals(Direction.LEFT))
                    || (dir.equals(Direction.DOWN) && path.direction().equals(Direction.UP))) {
                continue;
            }

            // Can't make more than 3 consecutive moves
            if (path.moveDirection(dir, 0).consecutive() >= 4) {
                continue;
            }

            // Get cost of next tile
            int cost = Integer.parseInt(String.valueOf(read.get(path.position().y() + dir.movement.y()).charAt(path.position().x() + dir.movement.x())));
            neighbours.add(path.moveDirection(dir, cost));
        }

        return neighbours;
    }
}

record Path(Point position, Set<Point> visited, Direction direction, int consecutive, int depth, int cost) {
    Path moveDirection(Direction direction, int cost) {
        int consecutive = 0;
        if (this.direction.equals(direction)) {
            consecutive = this.consecutive + 1;
        }
        this.visited.add(this.position);
        return new Path(this.position.moveDirection(direction), this.visited, direction, consecutive, this.depth + 1, this.cost + cost);
    }
};