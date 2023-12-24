package michael.koers;

import lombok.Getter;
import lombok.Setter;
import util.Direction;
import util.FileInput;
import util.Point;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

public class Main_Dijkstra {

    public static void main(String[] args) throws URISyntaxException, IOException {

        List<String> read = FileInput.read(FileInput.INPUT_TEST, Main_Dijkstra.class);

        Graph graph = parseInput(read, true);

        solvePart1(graph);
    }

    private static void solvePart1(Graph graph) {

        Set<Node> settledNodes = new HashSet<>();
        Set<Node> unsettledNodes = new HashSet<>();

        unsettledNodes.add(graph.start());

        while (!unsettledNodes.isEmpty()) {
            Node currentNode = getMaxiumumDistanceNode(unsettledNodes);
            unsettledNodes.remove(currentNode);

            for (Edge neighbour : currentNode.getNeighbours()) {
                Node neighbourNode = graph.nodeMap().get(neighbour.destination());
                Integer weight = neighbour.weight();
                if (!settledNodes.contains(graph.nodeMap().get(neighbour.destination()))) {
                    calculateLongestDistance(currentNode, neighbourNode, weight);
                    unsettledNodes.add(neighbourNode);
                }
            }
            settledNodes.add(currentNode);
        }

        System.out.printf("Longest hike: %s%n", graph.nodeMap().get(graph.end().position).getDistance());
    }

    private static void calculateLongestDistance(Node sourceNode, Node nextNode, Integer distance) {
        Integer sourceDistance = sourceNode.getDistance();

        // If this path via source node to next node is faster than current known route to next node
        // update next node distance and shortest path
        if (sourceDistance + distance > nextNode.getDistance()) {
            nextNode.setDistance(sourceDistance + distance);
        }
    }

    private static Node getMaxiumumDistanceNode(Set<Node> unvisitedNodes) {
        return unvisitedNodes.stream().max(Comparator.comparing(Node::getDistance)).orElseThrow(NoSuchElementException::new);
    }

    private static Graph parseInput(List<String> read, boolean isPart1) {

        String[][] map = read.stream().map(s -> s.split("")).toArray(String[][]::new);

        Queue<Node> newNodes = new LinkedList<>();
        Node startNode = new Node("start", new Point(1, 0));
        newNodes.add(startNode);
        Node end = new Node("end", new Point(read.get(0).length() - 2, read.size() - 1));

        Map<Point, Node> graph = new HashMap<>();

        while (!newNodes.isEmpty()) {
            Node current = newNodes.poll();

            if (graph.containsKey(current.position)) continue;

            graph.put(current.position, current);
            newNodes.addAll(getNeighbours(current, end, map, isPart1));
        }

        return new Graph(graph, startNode, end);
    }

    private static Set<Node> getNeighbours(Node start, Node end, String[][] map, boolean slopey) {

        Queue<Point> pathsToWalk = new LinkedList<>(findPaths(start, map));
        Set<Node> newNodes = new HashSet<>();

        while (!pathsToWalk.isEmpty()) {

            Queue<Point> nextSteps = new LinkedList<>();
            nextSteps.add(pathsToWalk.poll());
            Deque<Point> visited = new LinkedList<>();

            stepping:
            while (nextSteps.size() == 1) {

                Point current = nextSteps.poll();
                visited.add(current);

                for (Direction direction : List.of(Direction.LEFT, Direction.UP, Direction.DOWN, Direction.RIGHT)) {

                    Point nextStep = current.moveDirection(direction);

                    // Can't go back
                    if ((map[(int) current.y()][(int) current.x()].equals(">") && !direction.equals(Direction.RIGHT))
                            || (map[(int) current.y()][(int) current.x()].equals("<") && !direction.equals(Direction.LEFT))
                            || (map[(int) current.y()][(int) current.x()].equals("^") && !direction.equals(Direction.UP))
                            || (map[(int) current.y()][(int) current.x()].equals("v") && !direction.equals(Direction.DOWN))) {
                        continue;
                    }

                    // Don't walk over same path again
                    if (visited.contains(nextStep)) {
                        continue;
                    }

                    // Out of bounds check
                    if (nextStep.x() < 0 || nextStep.x() >= map.length || nextStep.y() < 0 || nextStep.y() >= map[0].length) {
                        continue;
                    }

                    // TODO Check if end
                    if (nextStep.equals(end.position)) {
                        visited.add(nextStep);
                        break stepping;
                    }

                    // Can't step on rocks
                    if (map[(int) nextStep.y()][(int) nextStep.x()].equals("#")) {
                        continue;
                    }

                    nextSteps.add(nextStep);
                }
            }
            // Lay connection with start
            newNodes.add(new Node(visited.getLast().toString(), visited.getLast()));
            start.addNeighbour(visited.size(), visited.getLast());
        }
        return newNodes;
    }

    private static Set<Point> findPaths(Node start, String[][] map) {

        Set<Point> paths = new HashSet<>();
        for (Direction direction : List.of(Direction.UP, Direction.RIGHT, Direction.DOWN, Direction.LEFT)) {

            Point newPath = start.position.moveDirection(direction);

            // Out of bounds check
            if (newPath.x() < 0 || newPath.x() >= map.length || newPath.y() < 0 || newPath.y() >= map[0].length) {
                continue;
            }

            if (direction.equals(Direction.UP) && map[(int) newPath.y()][(int) newPath.x()].equals("^")) {
                paths.add(newPath);
            }
            if (direction.equals(Direction.RIGHT) && map[(int) newPath.y()][(int) newPath.x()].equals(">")) {
                paths.add(newPath);
            }
            if (direction.equals(Direction.DOWN) && map[(int) newPath.y()][(int) newPath.x()].equals("v")) {
                paths.add(newPath);
            }
            if (direction.equals(Direction.LEFT) && map[(int) newPath.y()][(int) newPath.x()].equals("<")) {
                paths.add(newPath);
            }

        }
        if (paths.isEmpty()) paths.add(start.position);

        return paths;
    }
}

record Graph(Map<Point, Node> nodeMap, Node start, Node end) {
};

@Setter
@Getter
class Node {

    String name;
    Point position;
    List<Edge> neighbours = new ArrayList<>();
    Integer distance = 0;

    Node(String name, Point position) {
        this.name = name;
        this.position = position;
    }

    public void addNeighbour(int weight, Point position) {
        this.neighbours.add(new Edge(weight, position));
    }
}

record Edge(int weight, Point destination) {
};
