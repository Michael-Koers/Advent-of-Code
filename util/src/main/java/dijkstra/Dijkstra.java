package dijkstra;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.*;

/**
 * Calculate the shortest path from source to all possible destinations.
 * Each node contains information about the distance between it and the source node, and the path required to take.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Dijkstra {


    public static Graph calculateShortestPath(Graph graph, Node start) {

        start.setDistance(0);

        Set<Node> visitedNodes = new HashSet<>();
        Set<Node> unvisitedNodes = new HashSet<>();

        unvisitedNodes.add(start);

        while (!unvisitedNodes.isEmpty()) {
            Node nextNode = getLowestDistanceNode(unvisitedNodes);
            unvisitedNodes.remove(nextNode);

            for (Map.Entry<Node, Integer> pair : nextNode.getAdjacentNodes().entrySet()) {
                Node adjacentNode = pair.getKey();
                Integer distance = pair.getValue();

                // If we already visited this node, don't go there again
                if (!visitedNodes.contains(adjacentNode)) {
                    calculateShortestDistance(nextNode, adjacentNode, distance);
                    unvisitedNodes.add(adjacentNode);
                }
            }
            visitedNodes.add(nextNode);
        }

        return graph;
    }

    private static void calculateShortestDistance(Node sourceNode, Node nextNode, Integer distance) {
        Integer sourceDistance = sourceNode.getDistance();

        // If this path via source node to next node is faster than current known route to next node
        // update next node distance and shortest path
        if (sourceDistance + distance < nextNode.getDistance()) {
            nextNode.setDistance(sourceDistance + distance);
            LinkedList<Node> shortestPath = new LinkedList<>(sourceNode.getShortestPath());
            shortestPath.add(sourceNode);
            nextNode.setShortestPath(shortestPath);
        }
    }

    private static Node getLowestDistanceNode(Set<Node> unvisitedNodes) {
        return unvisitedNodes.stream().min(Comparator.comparing(Node::getDistance)).orElseThrow(NoSuchElementException::new);
    }
}
