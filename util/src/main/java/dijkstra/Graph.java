package dijkstra;

import lombok.Builder;
import lombok.Getter;
import lombok.Singular;

import java.util.Set;

@Getter
@Builder
public class Graph {

    @Singular(value = "addNode")
    private Set<Node> nodes;

}
