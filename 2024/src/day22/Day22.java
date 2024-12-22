package day22;

import config.Year2024;

import java.io.IOException;
import java.util.*;

public class Day22 extends Year2024 {

    Map<Sequence, Long> sequenceScores = new HashMap<>();

    public static void main(String[] args) throws IOException {
        var d = new Day22();

        var lines = d.readInput();

        d.solve(lines);
    }

    @Override
    public void solvePart1(List<String> lines) {
        var secrets = lines.stream().map(Long::parseLong).toList();

        long iterations = 2000;

        long total = 0L;

        for (Long secret : secrets) {
            for (int i = 0; i < iterations; i++) {
                secret = generateSecretNumber(secret);
            }
            total += secret;
        }

        System.out.println("Part 1: " + total);
    }

    long generateSecretNumber(long input) {
        long tmp = mixAndPrune(input * 64L, input);
        tmp = mixAndPrune(Math.floorDiv(tmp, 32L), tmp);
        tmp = mixAndPrune(tmp * 2048L, tmp);
        return tmp;
    }

    long mixAndPrune(long value, long secret) {
        return (secret ^ value) % 16777216;
    }

    @Override
    public void solvePart2(List<String> lines) {
        var secrets = lines.stream().map(Long::parseLong).toList();

//        Map<Sequence, Long> sequenceScores = new HashMap<>();
        for (Long secret : secrets) {

            Sequence sequence = new Sequence();
            Set<Sequence> seen = new HashSet<>();

            for (int i = 0; i < 2000; i++) {
                var newSecret = generateSecretNumber(secret);
                var price = newSecret % 10;
                var diff = price - (secret % 10);

                sequence = sequence.pushDiff((int) diff);

                secret = newSecret;

                if (seen.contains(sequence)) continue;
                seen.add(sequence);

                // My hack-y way of ignoring sequences that would start too early
                if (sequence.isValid()) {
                    Long sequenceScore = sequenceScores.getOrDefault(sequence, 0L);
                    sequenceScore += price;
                    sequenceScores.put(sequence, sequenceScore);
                }
            }

        }

        Map.Entry<Sequence, Long> highest = sequenceScores.entrySet().stream().sorted(Comparator.comparingLong(Map.Entry::getValue)).toList().reversed().getFirst();
        System.out.printf("Part 2: %s (%s)%n", highest.getValue(), highest.getKey());
    }
}

record Sequence(int first, int second, int third, int fourth) {

    Sequence() {
        this(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE);
    }

    Sequence pushDiff(int newDiff) {
        return new Sequence(newDiff, this.first, this.second, this.third);
    }

    boolean isValid() {
        return this.first != Integer.MIN_VALUE
                && this.second != Integer.MIN_VALUE
                && this.third != Integer.MIN_VALUE
                && this.fourth != Integer.MIN_VALUE;
    }
}
