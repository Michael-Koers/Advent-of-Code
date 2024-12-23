package day23;

import config.Year2024;

import java.io.IOException;
import java.util.*;

public class Day23 extends Year2024 {

    public static void main(String[] args) throws IOException {
        var d = new Day23();

        var lines = d.readInput();

        d.solve(lines);
    }

    @Override
    public void solvePart1(List<String> lines) {

        Map<String, Set<String>> connections = new HashMap<>();

        for (String line : lines) {
            var parts = line.split("-");

            var con1 = connections.getOrDefault(parts[0], new HashSet<>());
            con1.add(parts[1]);
            connections.putIfAbsent(parts[0], con1);

            var con2 = connections.getOrDefault(parts[1], new HashSet<>());
            con2.add(parts[0]);
            connections.putIfAbsent(parts[1], con2);
        }

        Set<String> circularConnections = new HashSet<>();

        for (Map.Entry<String, Set<String>> con1 : connections.entrySet()) {

            for (String con2 : con1.getValue()) {

                var node2 = connections.get(con2);

                for (String con3 : node2) {

                    var node3 = connections.get(con3);

                    if (node3.contains(con1.getKey())) {
                        var connected = List.of(con1.getKey(), con2, con3);

                        if (connected.stream().anyMatch(s -> s.startsWith("t"))) {
                            // Sort to remove duplicates in set
                            var result = connected.stream()
                                    .sorted()
                                    .reduce((s1, s2) -> s1.concat(",").concat(s2))
                                    .get();
                            circularConnections.add(result);
                        }
                    }
                }
            }
        }

        System.out.println("Part 1: " + circularConnections.size());

    }

    @Override
    public void solvePart2(List<String> lines) {
        Map<String, Set<String>> connections = new HashMap<>();

        for (String line : lines) {
            var parts = line.split("-");

            var con1 = connections.getOrDefault(parts[0], new HashSet<>());
            con1.add(parts[1]);
            connections.putIfAbsent(parts[0], con1);

            var con2 = connections.getOrDefault(parts[1], new HashSet<>());
            con2.add(parts[0]);
            connections.putIfAbsent(parts[1], con2);
        }

        var finder = new MaxCliqueFinder(connections);
        var result = finder.findLargestClique();

        var password = result.stream().sorted()
                .reduce((s1, s2) -> s1.concat(",").concat(s2))
                .get();

        System.out.println("Part 2: " + password);

    }
}

// This class was written with the help of ChatGPT, worked first try!
class MaxCliqueFinder {
    private Map<String, Set<String>> graph;

    public MaxCliqueFinder(Map<String, Set<String>> graph) {
        this.graph = graph;
    }

    Set<String> findLargestClique() {
        List<Set<String>> cliques = new ArrayList<>();

        // Currently growing clique
        Set<String> R = new HashSet<>();

        // Potential nodes to be added to the clique
        Set<String> P = new HashSet<>(graph.keySet());

        // Nodes already considered
        Set<String> X = new HashSet<>();

        bronKerbosch(R, P, X, cliques);

        return cliques.stream()
                .max(Comparator.comparingInt(Set::size))
                .orElse(Collections.emptySet());
    }

    void bronKerbosch(Set<String> R, Set<String> P, Set<String> X, List<Set<String>> cliques) {
        if (P.isEmpty() && X.isEmpty()) {
            cliques.add(new HashSet<>(R));
            return;
        }

        Set<String> Pcopy = new HashSet<>(P);
        for (String v : Pcopy) {
            R.add(v);

            bronKerbosch(R,
                    intersection(P, graph.get(v)),
                    intersection(X, graph.get(v)),
                    cliques);

            R.remove(v);
            P.remove(v);
            X.add(v);
        }
    }

    private Set<String> intersection(Set<String> a, Set<String> b) {
        Set<String> result = new HashSet<>(a);
        result.retainAll(b);
        return result;
    }

}