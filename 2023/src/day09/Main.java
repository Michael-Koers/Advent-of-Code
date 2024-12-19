package day09;

import util.FileInput;
import util.Stopwatch;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ∑∑∑Main {
    public static void main(String[] args) throws URISyntaxException, IOException {

        List<String> lines = FileInput.read(FileInput.INPUT, Main.class);
        List<List<Long>> allValues = parseInput(lines);

        Stopwatch stopwatch = new Stopwatch();

        solvePart1(allValues);

        System.out.printf("Solved part 1 in: %sms%n", stopwatch.duration());
        stopwatch.reset();

        solvePart2(allValues);

        System.out.printf("Solved part 2 in: %sms%n", stopwatch.duration());
    }

    private static void solvePart2(List<List<Long>> allValues) {
        List<Long> extrapolated = new ArrayList<>();
        for (List<Long> values : allValues) {
            extrapolated.add(values.getFirst() - extrapolateLeft(values));
        }

        Long totalExtrapolated = extrapolated.stream().mapToLong(Long::longValue).sum();
        System.out.printf("Solved part 2, sum of extrapolated values: %s%n", totalExtrapolated);
    }


    private static void solvePart1(List<List<Long>> allValues) {
        List<Long> extrapolated = new ArrayList<>();
        for (List<Long> values : allValues) {
            extrapolated.add(values.getLast() + extrapolateRight(values));
        }

        Long totalExtrapolated = extrapolated.stream().mapToLong(Long::longValue).sum();
        System.out.printf("Solved part 1, sum of extrapolated values: %s%n", totalExtrapolated);
    }

    public static Long extrapolateRight(List<Long> values) {
        List<Long> diff = calculateDiffs(values);
        if (diff.stream().allMatch(d -> d == 0)) {
            return diff.getLast();
        } else {
            return extrapolateRight(diff) + diff.getLast();
        }
    }

    public static Long extrapolateLeft(List<Long> values) {
        List<Long> diff = calculateDiffs(values);
        if (diff.stream().allMatch(d -> d == 0)) {
            return diff.getFirst();
        } else {
            return diff.getFirst() - extrapolateLeft(diff);
        }
    }

    private static List<Long> calculateDiffs(List<Long> values) {
        return IntStream.range(0, values.size() - 1)
                .mapToObj(i -> values.get(i + 1) - values.get(i))
                .toList();
    }

    private static List<List<Long>> parseInput(List<String> lines) {
        return lines.stream()
                .map(e -> e.split(" "))
                .map(s -> Arrays.stream(s)
                        .map(Long::parseLong)
                        .collect(Collectors.toList()))
                .collect(Collectors.toList());
    }
}
