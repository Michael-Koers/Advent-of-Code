package dijkstra;


import java.util.LinkedList;
import java.util.Map;

public class Node {

    String name;

    Integer distance = Integer.MAX_VALUE;

    Map<Node, Integer> adjacentNodes;

    LinkedList<Node> shortestPath = new LinkedList<>();

    public void addAdjacentNode(Node node, Integer cost) {
        this.adjacentNodes.put(node, cost);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    public Map<Node, Integer> getAdjacentNodes() {
        return adjacentNodes;
    }

    public void setAdjacentNodes(Map<Node, Integer> adjacentNodes) {
        this.adjacentNodes = adjacentNodes;
    }

    public LinkedList<Node> getShortestPath() {
        return shortestPath;
    }

    public void setShortestPath(LinkedList<Node> shortestPath) {
        this.shortestPath = shortestPath;
    }
}
