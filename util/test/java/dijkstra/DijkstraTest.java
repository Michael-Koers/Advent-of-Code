//package dijkstra;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//public class DijkstraTest {
//
//    private Graph graph;
//    private Node start;
//
//    /*
//         5    5
//       B - D -- F -
//   5 /   /     /   \ 5
//    A   / 5   / 1   \
//  10 \ /     /       G
//      C --- E ------/
//         1      20
//
//         Shortest Path
//         A -> B = A-B           5
//         A -> C = A-C           10
//         A -> D = A-B-D         10
//         A -> E = A-C-E         11
//         A -> F = A-C-E-F       12
//         A -> G = A-C-E-F-G     17
//
//     */
//    @BeforeEach
//    public void before() {
//
//        Node nodeG = new Node.NodeBuilder()
//                .name("G")
//                .build();
//
//        Node nodeF = new Node.NodeBuilder()
//                .name("F")
//                .addNeighbourNode(nodeG, 5)
//                .build();
//
//        Node nodeE = new Node.NodeBuilder()
//                .name("E")
//                .addNeighbourNode(nodeF, 1)
//                .addNeighbourNode(nodeG, 20)
//                .build();
//
//        Node nodeD = new Node.NodeBuilder()
//                .name("D")
//                .addNeighbourNode(nodeF, 5)
//                .build();
//
//        Node nodeC = new Node.NodeBuilder()
//                .name("C")
//                .addNeighbourNode(nodeD, 5)
//                .addNeighbourNode(nodeE, 1)
//                .build();
//
//        Node nodeB = new Node.NodeBuilder()
//                .name("B")
//                .addNeighbourNode(nodeD, 5)
//                .build();
//
//        Node nodeA = new Node.NodeBuilder()
//                .name("A")
//                .addNeighbourNode(nodeC, 10)
//                .addNeighbourNode(nodeB, 5)
//                .build();
//
//        this.graph = new Graph.GraphBuilder()
//                .addNode(nodeA)
//                .addNode(nodeB)
//                .addNode(nodeC)
//                .addNode(nodeD)
//                .addNode(nodeE)
//                .addNode(nodeF)
//                .addNode(nodeG)
//                .build();
//
//        this.start = nodeA;
//    }
//
//    @Test
//    void testDijkstra() {
//
//        Dijkstra.calculateShortestPath(this.graph, this.start);
//
//       /*
//         Shortest Path
//         A -> B = A-B           5
//         A -> C = A-C           10
//         A -> D = A-B-D         10
//         A -> E = A-C-E         11
//         A -> F = A-C-E-F       12
//         A -> G = A-C-E-F-G     17
//        */
//
//        Node nodeA = this.graph.getNodes().stream().filter(n -> n.getName().equals("A")).findFirst().orElseThrow(NoSuchFieldError::new);
//        Node nodeB = this.graph.getNodes().stream().filter(n -> n.getName().equals("B")).findFirst().orElseThrow(NoSuchFieldError::new);
//        Node nodeC = this.graph.getNodes().stream().filter(n -> n.getName().equals("C")).findFirst().orElseThrow(NoSuchFieldError::new);
//        Node nodeD = this.graph.getNodes().stream().filter(n -> n.getName().equals("D")).findFirst().orElseThrow(NoSuchFieldError::new);
//        Node nodeE = this.graph.getNodes().stream().filter(n -> n.getName().equals("E")).findFirst().orElseThrow(NoSuchFieldError::new);
//        Node nodeF = this.graph.getNodes().stream().filter(n -> n.getName().equals("F")).findFirst().orElseThrow(NoSuchFieldError::new);
//        Node nodeG = this.graph.getNodes().stream().filter(n -> n.getName().equals("G")).findFirst().orElseThrow(NoSuchFieldError::new);
//
//        assertEquals(0, nodeA.getDistance());
//        assertEquals(5, nodeB.getDistance());
//        assertEquals(10, nodeC.getDistance());
//        assertEquals(10, nodeD.getDistance());
//        assertEquals(11, nodeE.getDistance());
//        assertEquals(12, nodeF.getDistance());
//        assertEquals(17, nodeG.getDistance());
//
//        assertEquals(0, nodeA.getShortestPath().size());
//        assertEquals(1, nodeB.getShortestPath().size());
//        assertEquals(1, nodeC.getShortestPath().size());
//        assertEquals(2, nodeD.getShortestPath().size());
//        assertEquals(2, nodeE.getShortestPath().size());
//        assertEquals(3, nodeF.getShortestPath().size());
//        assertEquals(4, nodeG.getShortestPath().size());
//
//        assertEquals(nodeA, nodeG.getShortestPath().get(0));
//        assertEquals(nodeC, nodeG.getShortestPath().get(1));
//        assertEquals(nodeE, nodeG.getShortestPath().get(2));
//        assertEquals(nodeF, nodeG.getShortestPath().get(3));
//    }
//}
