package floydwarshall;

/*
    Floyd Warshall is used for pre-calculating all paths from every to every other node.
    Unknown paths should be a very large number to make sure known paths will be registered.
 */
public class FloydWarshall {

    private FloydWarshall() {
    }



    public static int[][] calculatePaths(int[][] graph) {

        int dimension = graph.length;
        int[][] matrix = new int[dimension][dimension];

        // copy graph
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                matrix[i][j] = graph[i][j];
            }
        }

        for (int k = 0; k < dimension; k++) {
            for (int i = 0; i < dimension; i++) {
                for (int j = 0; j < dimension; j++) {
                    if (matrix[i][k] + matrix[k][j] < matrix[i][j]) {
                        matrix[i][j] = matrix[i][k] + matrix[k][j];
                    }
                }
            }
        }

        return matrix;
    }
}
