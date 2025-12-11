package day11;

import config.Year2025;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Day11 extends Year2025 {

    public static void main(String[] args) throws IOException {
        var d = new Day11();

        var lines = d.readInput();

        d.solvePart2(lines);

    }

    @Override
    public void solvePart1(List<String> lines) {

        Map<String, Set<String>> devices = new HashMap<>();

        for (String line : lines) {
            var start = line.split(":")[0].trim();

            var neighbours = Arrays.stream(line.split(":")[1].trim().split(" "))
                    .map(String::trim)
                    .collect(Collectors.toSet());

            devices.put(start, neighbours);
        }

        String start = "you";
        String dest = "out";
        Set<String> visited = new HashSet<>();
        visited.add(start);

        var count = countPathsP1(start, dest, visited, devices);

        System.out.println("Part 1: " + count);
    }

    private long countPathsP1(String current, String dest, Set<String> visited, Map<String, Set<String>> devices) {

        if (current.equals(dest)) {
            return 1L;
        }

        long count = 0;
        for (String next : devices.get(current)) {

            if (visited.contains(next)) {
                continue;
            }

            var newVisited = new HashSet<>(visited);
            newVisited.add(next);

            count += countPathsP1(next, dest, newVisited, devices);
        }
        return count;
    }

    @Override
    public void solvePart2(List<String> lines) {
        Map<String, Set<String>> devices = new HashMap<>();

        for (String line : lines) {
            var start = line.split(":")[0].trim();

            var neighbours = Arrays.stream(line.split(":")[1].trim().split(" "))
                    .map(String::trim)
                    .collect(Collectors.toSet());

            devices.put(start, neighbours);
        }

        String start = "svr";
        String dest = "out";
        Set<String> visited = new HashSet<>();
        visited.add(start);

        var count = countPathsP2(start, dest, visited, devices);

        System.out.println("Part 2: " + count);
    }

    private long countPathsP2(String current, String dest, Set<String> visited, Map<String, Set<String>> devices) {

        if (current.equals(dest)) {
            if (visited.contains("dac") && visited.contains("fft")) {
                return 1L;
            } else {
                return 0L;
            }
        }

        long count = 0;
        for (String next : devices.get(current)) {

            if (visited.contains(next)) {
                continue;
            }

            var newVisited = new HashSet<>(visited);
            newVisited.add(next);

            count += countPathsP2(next, dest, newVisited, devices);
        }
        return count;
    }
}


