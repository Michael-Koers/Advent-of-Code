package michael.koers;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import util.Direction;
import util.FileInput;
import util.Point;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;

public class Main_2 {

    public static void main(String[] args) throws URISyntaxException, IOException {

        List<String> read = FileInput.read(FileInput.INPUT_TEST, Main_2.class);

        Map<Point, Node> nodeMap = parseNodeMap(read);
        connectNeightbours(nodeMap, read);

        solvePart1(nodeMap, read);
    }

    private static void solvePart1(Map<Point, Node> nodeMap, List<String> read) {

        Node start = nodeMap.get(new Point(0, 0));
        Node end = nodeMap.get(new Point(read.size() - 1, read.get(0).length() - 1));

        start.setDistance(0);

        Set<Node> unvisitedNodes = new HashSet<>();

        unvisitedNodes.add(start);

        while (!unvisitedNodes.isEmpty()) {

            Node next = getLowestDistanceNode(unvisitedNodes);
            unvisitedNodes.remove(next);

            for (Map.Entry<Direction, Node> pair : next.getNeighbours().entrySet()) {

                Direction direction = pair.getKey();
                Node neighbour = pair.getValue();

                // Can't do the same moves more than 3 times in a row
                if (next.getSameDirectionCount() >= 3 && (next.getDirection().equals(direction))) {
                    continue;
                }

                // If this path is already slower than this one, skip
                if (next.getDistance() > end.getDistance()) {
                    continue;
                }

                if (next.getDistance() + neighbour.getCost() < neighbour.getDistance()) {
                    updateNeighbourNode(next, neighbour, direction);
                    unvisitedNodes.add(neighbour);
                }
            }
        }

        prettyPrint(end, read);
        System.out.printf("Solved part 1, total heat loss: %s%n", end.getDistance() + end.getCost() - start.getCost());
    }

    private static void prettyPrint(Node end, List<String> read) {
        Map<Point, Direction> path = end.getShortestPath().stream().collect(Collectors.toMap(Node::getPosition, Node::getDirection));

        for (int y = 0; y < read.size(); y++) {
            for (int x = 0; x < read.get(y).length(); x++) {
                if (path.containsKey(new Point(x, y))) {
                    switch (path.get(new Point(x, y))) {
                        case LEFT -> System.out.print("<");
                        case RIGHT -> System.out.print(">");
                        case UP -> System.out.print("^");
                        case DOWN -> System.out.print("v");
                        case NONE -> System.out.print("S");
                        default -> System.out.print("?");
                    }
                } else {
                    System.out.print(read.get(y).charAt(x));
                }
            }
            System.out.println();
        }
    }

    private static void updateNeighbourNode(Node sourceNode, Node nextNode, Direction direction) {
        Integer sourceDistance = sourceNode.getDistance();
        Integer distance = nextNode.getCost();

        // If this path via source node to next node is faster than current known route to next node
        // update next node distance and shortest path
        if (sourceDistance + distance < nextNode.getDistance()) {
            nextNode.setDistance(sourceDistance + distance);
            nextNode.setDirection(direction);

            // If we still going in same direction, up the same direction count
            if (sourceNode.getDirection().equals(nextNode.getDirection())) {
                nextNode.setSameDirectionCount(sourceNode.getSameDirectionCount() + 1);
            } else {
                nextNode.setSameDirectionCount(1);
            }

            LinkedList<Node> shortestPath = new LinkedList<>(sourceNode.getShortestPath());
            shortestPath.add(sourceNode);
            nextNode.setShortestPath(shortestPath);
        }
    }

    private static Node getLowestDistanceNode(Set<Node> unvisitedNodes) {
        return unvisitedNodes.stream().min(Comparator.comparing(Node::getDistance)).orElseThrow(NoSuchElementException::new);
    }


    private static void connectNeightbours(Map<Point, Node> nodeMap, List<String> read) {
        for (Map.Entry<Point, Node> pointNodeEntry : nodeMap.entrySet()) {

            Point currentPosition = pointNodeEntry.getKey();
            Node currentNode = pointNodeEntry.getValue();
            // Get left
            if (currentPosition.x() > 0) {
                Node left = nodeMap.get(currentPosition.moveDirection(Direction.LEFT));
                currentNode.addNeighbour(Direction.LEFT, left);
            }
            // Get right
            if (currentPosition.x() < read.get(0).length() - 1) {
                Node right = nodeMap.get(currentPosition.moveDirection(Direction.RIGHT));
                currentNode.addNeighbour(Direction.RIGHT, right);
            }
            // Get up
            if (currentPosition.y() > 0) {
                Node top = nodeMap.get(currentPosition.moveDirection(Direction.UP));
                currentNode.addNeighbour(Direction.UP, top);
            }
            // Get down
            if (currentPosition.y() < read.size() - 1) {
                Node bottom = nodeMap.get(currentPosition.moveDirection(Direction.DOWN));
                currentNode.addNeighbour(Direction.DOWN, bottom);
            }
        }
    }

    private static Map<Point, Node> parseNodeMap(List<String> read) {

        Map<Point, Node> nodeMap = new HashMap<>();

        for (int y = 0; y < read.size(); y++) {
            for (int x = 0; x < read.get(y).length(); x++) {

                Point point = new Point(x, y);
                int cost = Integer.parseInt(String.valueOf(read.get(y).charAt(x)));
                Node node = new Node(point, cost, Integer.MAX_VALUE, Direction.NONE, 0, new HashMap<>(), new LinkedList<>());
                nodeMap.put(point, node);
            }
        }

        return nodeMap;
    }
}


@Getter
@Setter
@AllArgsConstructor
final class Node {
    private Point position;
    private int cost;
    private int distance;
    private Direction direction;
    private int sameDirectionCount;
    private Map<Direction, Node> neighbours;
    private LinkedList<Node> shortestPath;

    public void addNeighbour(Direction direction, Node node) {
        this.neighbours.put(direction, node);
    }

    public int pathLength() {
        return this.shortestPath.size();
    }
};