package dijkstra;

import java.util.Set;

public class Graph {

    private Set<Node> nodes;

    public void addNode(Node node) {
        this.nodes.add(node);
    }

    public Set<Node> getNodes() {
        return this.nodes;
    }
}
