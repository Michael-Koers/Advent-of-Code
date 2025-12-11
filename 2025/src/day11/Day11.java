package day11;

import config.Year2025;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Day11 extends Year2025 {

    public static void main(String[] args) throws IOException {
        var d = new Day11();

        var lines = d.readInput();

        d.warmup(lines, 10);
        d.solve(lines);

    }

    @Override
    public void solvePart1(List<String> lines) {

        Map<String, Set<String>> devices = parse(lines);

        String start = "you";
        String dest = "out";
        Map<String, Long> visited = new HashMap<>();
        visited.put(start, 0L);

        var count = countPaths(start, dest, visited, devices);

        System.out.println("Part 1: " + count);
    }


    @Override
    public void solvePart2(List<String> lines) {
        Map<String, Set<String>> devices = parse(lines);

        var resultDacToFft = 1L;
        resultDacToFft *= countPaths("svr", "dac", new HashMap<>(), devices);
        resultDacToFft *= countPaths("dac", "fft", new HashMap<>(), devices);
        resultDacToFft *= countPaths("fft", "out", new HashMap<>(), devices);

        var resultFftToDac = 1L;
        resultFftToDac *= countPaths("svr", "fft", new HashMap<>(), devices);
        resultFftToDac *= countPaths("fft", "dac", new HashMap<>(), devices);
        resultFftToDac *= countPaths("dac", "out", new HashMap<>(), devices);

        var total = resultDacToFft + resultFftToDac;

        System.out.println("Part 2: " + total);
    }

    private static Map<String, Set<String>> parse(List<String> lines) {
        Map<String, Set<String>> devices = new HashMap<>();

        for (String line : lines) {
            var start = line.split(":")[0].trim();

            var neighbours = Arrays.stream(line.split(":")[1].trim().split(" "))
                    .map(String::trim)
                    .collect(Collectors.toSet());

            devices.put(start, neighbours);
        }
        devices.put("out", Set.of());
        return devices;
    }

    private long countPaths(String current, String dest, Map<String, Long> cache, Map<String, Set<String>> devices) {

        if (current.equals(dest)) {
            return 1L;
        }

        long total = 0;
        for (String next : devices.get(current)) {

            // If we haven't calculated this node to destination before, do it and cache it
            if (!cache.containsKey(next)) {
                var count = countPaths(next, dest, cache, devices);
                cache.put(next, count);
            }

            total += cache.get(next);
        }
        return total;
    }
}


