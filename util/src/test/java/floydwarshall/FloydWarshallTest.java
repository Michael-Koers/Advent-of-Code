package floydwarshall;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class FloydWarshallTest {

    @Test
    public void testFloydWarshall(){

        // Example / test data taken from: https://www.programiz.com/dsa/floyd-warshall-algorithm
        int INF = 99999;

        int[][] graph = { { 0, 3, INF, 5 }, { 2, 0, INF, 4 }, { INF, 1, 0, INF }, { INF, INF, 2, 0 } };

        int[][] matrix = FloydWarshall.calculatePaths(graph);

        System.out.println(Arrays.deepToString(matrix));

        System.out.println(matrix[0][2]);
    }
}
