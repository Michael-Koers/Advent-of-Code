package michael.koers;

import lombok.Getter;
import lombok.Setter;
import util.Direction;
import util.FileInput;
import util.Point;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;

// Needed a proper solution for part 2, see: https://www.youtube.com/watch?v=NTLYL7Mg2jU
public class Main {

    private static Set<Point> seenCache = new HashSet<>();

    public static void main(String[] args) throws URISyntaxException, IOException {

        List<String> read = FileInput.read(FileInput.INPUT, Main.class);

        Graph graph = parseInput(read);
        connectNeighbours(read, graph, false);

        System.out.println(dfs(graph, graph.start()));
    }

    private static long dfs(Graph g, Node node) {
        if (g.end().equals(node)) {
            return 0;
        }

        long m = -Long.MAX_VALUE;

        seenCache.add(node.getPos());
        for (Map.Entry<Point, Integer> neighbour : node.getNeighbours().entrySet()) {
            if (!seenCache.contains(neighbour.getKey())) {
                m = Math.max(m, dfs(g, g.getNode(neighbour.getKey())) + neighbour.getValue());
            }
        }
        seenCache.remove(node.getPos());

        return m;
    }

    private static void connectNeighbours(List<String> read, Graph graph, boolean isPart1) {

        Set<Point> points = graph.nodes().stream().map(n -> n.pos).collect(Collectors.toSet());

        Map<Character, List<Direction>> charDirections = new HashMap<>();
        charDirections.put('^', List.of(Direction.UP));
        charDirections.put('v', List.of(Direction.DOWN));
        charDirections.put('<', List.of(Direction.LEFT));
        charDirections.put('>', List.of(Direction.RIGHT));
        charDirections.put('.', List.of(Direction.UP, Direction.RIGHT, Direction.DOWN, Direction.LEFT));

        for (Node node : graph.nodes()) {

            Queue<Step> stack = new LinkedList<>();
            stack.add(new Step(0, (int) node.pos.y(), (int) node.pos.x()));

            Set<Point> seen = new HashSet<>();
            seen.add(node.pos);

            while (!stack.isEmpty()) {

                Step nextStep = stack.poll();

                if (nextStep.steps() != 0 && points.contains(nextStep.getPoint())) {
                    node.addNeighbour(nextStep.getPoint(), nextStep.steps());
                    continue;
                }

                char nextchar;

                // In part one, we listen to the directions, in part 2, we consider everything '.'
                if (isPart1) {
                    nextchar = read.get(nextStep.row()).charAt(nextStep.col());
                } else {
                    nextchar = '.';
                }

                for (Direction direction : charDirections.get(nextchar)) {
                    int ny = (int) (nextStep.row() + direction.movement.y());
                    int nx = (int) (nextStep.col() + direction.movement.x());
                    Point newPoint = new Point(nx, ny);
                    if (ny >= 0 && ny < read.size()
                            && nx >= 0 && nx < read.get(0).length()
                            && read.get(ny).charAt(nx) != '#'
                            && !seen.contains(newPoint)) {
                        stack.add(new Step(nextStep.steps() + 1, (int) newPoint.y(), (int) newPoint.x()));
                        seen.add(newPoint);
                    }
                }
            }
        }

        System.out.println();
    }

    private static Graph parseInput(List<String> read) {

        Node start = new Node(new Point(read.get(0).indexOf("."), 0));
        Node end = new Node(new Point(read.getLast().lastIndexOf("."), read.size() - 1));
        List<Point> points = new ArrayList<>();

        for (int y = 0; y < read.size(); y++) {
            for (int x = 0; x < read.get(y).length(); x++) {

                if (read.get(y).charAt(x) == '#') {
                    continue;
                }

                int neighbours = 0;

                for (Direction direction : List.of(Direction.UP, Direction.RIGHT, Direction.DOWN, Direction.LEFT)) {
                    int ny = (int) (y + direction.movement.y());
                    int nx = (int) (x + direction.movement.x());

                    if (ny >= 0 && ny < read.size() && nx >= 0 && nx < read.get(y).length() && read.get(ny).charAt(nx) != '#') {
                        neighbours += 1;
                    }
                }
                if (neighbours >= 3) {
                    points.add(new Point(x, y));
                }
            }
        }
        List<Node> collect = points.stream()
                .map(Node::new)
                .collect(Collectors.toList());
        collect.add(start);
        collect.add(end);
        return new Graph(collect, start, end);
    }

}

record Graph(List<Node> nodes, Node start, Node end) {

    Node getNode(Point p) {
        return nodes.stream().filter(node -> node.pos.equals(p)).findFirst().orElseThrow(NoSuchElementException::new);
    }
};

@Getter
@Setter
class Node {
    Point pos;
    Map<Point, Integer> neighbours = new HashMap<>();

    public Node(Point pos) {
        this.pos = pos;
    }

    public void addNeighbour(Point neighbour, Integer weight) {
        this.neighbours.put(neighbour, weight);
    }
};

record Step(int steps, int row, int col) {

    Point getPoint() {
        return new Point(col, row);
    }
};