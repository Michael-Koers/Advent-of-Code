package day08;

import config.Year2025;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day08 extends Year2025 {

    public static void main(String[] args) throws IOException {
        var d = new Day08();

        var lines = d.readInput();

        d.solve(lines);
    }


    @Override
    public void solvePart1(final List<String> lines) {

        int limit = 1000;

        List<Position> positions = new ArrayList<>();
        for (final String line : lines) {
            var parts = line.split(",");
            positions.add(new Position(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2])));
        }

        // Create all possible pairs with distances
        List<PairWithDistance> allPairs = new ArrayList<>();
        for (int i = 0; i < positions.size(); i++) {
            for (int j = i + 1; j < positions.size(); j++) {
                double dist = positions.get(i).distance(positions.get(j));
                allPairs.add(new PairWithDistance(i, j, dist));
            }
        }

        // Sort by distance and take the 1000 closest
        allPairs.sort(Comparator.comparingDouble(PairWithDistance::distance));
        List<PairWithDistance> closestPairs = allPairs.stream().limit(limit).toList();

        // Use union-find to group connected boxes
        UnionFind uf = new UnionFind(positions.size());
        for (var pair : closestPairs) {
            uf.union(pair.pos1(), pair.pos2());
        }

        // Count circuit sizes
        Map<Integer, Integer> circuitSizes = new HashMap<>();
        for (int i = 0; i < positions.size(); i++) {
            int root = uf.find(i);
            circuitSizes.put(root, circuitSizes.getOrDefault(root, 0) + 1);
        }

        // Get 3 largest circuits
        List<Integer> sizes = circuitSizes.values().stream()
                .sorted(Comparator.reverseOrder())
                .limit(3)
                .toList();

        long result = sizes.stream().mapToLong(Long::valueOf).reduce(1, (a, b) -> a * b);
        System.out.println("Part 1: " + result);
    }

    @Override
    public void solvePart2(final List<String> lines) {

        List<Position> positions = new ArrayList<>();
        for (final String line : lines) {
            var parts = line.split(",");
            positions.add(new Position(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2])));
        }

        // Create all possible pairs with distances
        List<PairWithDistance> allPairs = new ArrayList<>();
        for (int i = 0; i < positions.size(); i++) {
            for (int j = i + 1; j < positions.size(); j++) {
                double dist = positions.get(i).distance(positions.get(j));
                allPairs.add(new PairWithDistance(i, j, dist));
            }
        }

        // Sort by distance
        allPairs.sort(Comparator.comparingDouble(PairWithDistance::distance));

        // Use union-find and connect pairs until all in one circuit
        UnionFind uf = new UnionFind(positions.size());
        int circuits = positions.size();
        PairWithDistance lastConnection = null;

        for (var pair : allPairs) {
            if (uf.find(pair.pos1()) != uf.find(pair.pos2())) {
                uf.union(pair.pos1(), pair.pos2());
                lastConnection = pair;
                circuits--;
                if (circuits == 1) break;
            }
        }

        // Multiply X coordinates of last connection
        long result = (long) positions.get(lastConnection.pos1()).x() * positions.get(lastConnection.pos2()).x();
        System.out.println("Part 2: " + result);

    }

}


record Position(int x, int y, int z) {

    double distance(Position other) {
        return Math.sqrt(Math.pow(other.x - x, 2) + Math.pow(other.y - y, 2) + Math.pow(other.z - z, 2));
    }
}

record PairWithDistance(int pos1, int pos2, double distance) {}

/**
 * Union-Find (Disjoint Set Union) data structure for efficiently tracking
 * connected components and merging sets.
 *
 * This is used to determine which junction boxes are connected into the same
 * circuit. When two boxes are connected, they're merged into the same set.
 *
 * Time Complexity: Nearly O(1) per operation with path compression
 * Space Complexity: O(n) where n is the number of elements
 */
class UnionFind {
    /** Array where parent[i] points to the parent of element i in its tree */
    int[] parent;

    /**
     * Initializes the Union-Find structure where each element is its own parent
     * (represents n separate circuits, one per junction box).
     *
     * @param n the number of junction boxes
     */
    UnionFind(int n) {
        parent = new int[n];
        for (int i = 0; i < n; i++) {
            parent[i] = i;
        }
    }

    /**
     * Finds the root (representative) of the set containing element x.
     * Uses path compression to flatten the tree and improve future lookups.
     *
     * @param x the element to find the root for
     * @return the root representative of the set containing x
     */
    int find(int x) {
        if (parent[x] != x) {
            parent[x] = find(parent[x]); // Path compression
        }
        return parent[x];
    }

    /**
     * Merges the sets containing x and y into a single set.
     * This represents connecting two junction boxes (or circuits).
     *
     * @param x first element
     * @param y second element
     */
    void union(int x, int y) {
        parent[find(x)] = find(y);
    }
}
