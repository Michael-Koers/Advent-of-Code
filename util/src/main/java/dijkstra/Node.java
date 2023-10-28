package dijkstra;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.Singular;

import java.util.LinkedList;
import java.util.Map;

@Builder
@Setter
@Getter
public class Node {

    String name;

    @Builder.Default
    Integer distance = Integer.MAX_VALUE;

    @Singular(value = "addNeighbourNode")
    Map<Node, Integer> adjacentNodes;

    @Builder.Default
    LinkedList<Node> shortestPath = new LinkedList<>();

}
