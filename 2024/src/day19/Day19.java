package day19;

import config.Year2024;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day19 extends Year2024 {

    Map<String, Long> cache = new HashMap<>();

    public static void main(String[] args) throws IOException {

        var d = new Day19();

        var lines = d.readInput();

        d.stopwatch.start();
        d.solvePart1(lines);
        d.stopwatch.print();
    }


    @Override
    public void solvePart1(final List<String> lines) {

        List<String> towels = Arrays.stream(lines.getFirst().split(", ")).toList();

        List<String> patterns = lines.stream().skip(2).toList();

        int possible = 0;
        long total = 0;

        for (final String pattern : patterns) {

            var poss = isPatternPossible(pattern, towels);

            if (poss > 0) {
                possible++;
            }

            total += poss;
        }

        System.out.println("Part 1: " + possible);
        System.out.println("Part 2: " + total);
    }

    long isPatternPossible(String pattern, List<String> towels) {
        if (cache.containsKey(pattern)) {
            return cache.get(pattern);
        }

        if (pattern.isEmpty()) {return 1;}

        long possible = 0;

        for (final String towel : towels) {
            if (!pattern.startsWith(towel)) {continue;}
            var newPattern = pattern.replaceFirst(towel, "");
            var count = isPatternPossible(newPattern, towels);
            cache.put(newPattern, count);
            possible += count;
        }

        return possible;
    }

    @Override
    public void solvePart2(final List<String> lines) {

    }
}
