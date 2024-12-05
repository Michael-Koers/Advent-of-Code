package day05;

import config.Year2024;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day05 extends Year2024 {
    public static void main(String[] args) throws IOException {

        var d = new Day05();

        d.stopwatch.start();
        d.solvePart1(d.readInput());
        d.stopwatch.prettyPrint();
    }


    @Override
    public void solvePart1(List<String> lines) {
        var sortingRules = getSortingRules(lines);

        int sum = 0;
        int sumRetries = 0;

        for (int i = sortingRules.size() + 1; i < lines.size(); i++) {

            final List<Integer> split = new ArrayList<>(Arrays.stream(lines.get(i).split(",")).map(Integer::valueOf).toList());

            // Part 1 check
            if (!validLine(split, sortingRules)) {

                // Part 2 sorting
                split.sort((left, right) ->
                        sortingRules
                                .stream()
                                .anyMatch(sr -> sr.start() == left && sr.end() == right) ? -1 : 1);

                sumRetries += split.get((split.size() - 1) / 2);
                continue;

            }
            System.out.printf("Valid line: %s%n", lines.get(i));
            sum += split.get((split.size() - 1) / 2);
        }
        System.out.println(sum);
        System.out.println(sumRetries);
    }

    private List<Pair> getSortingRules(List<String> lines) {
        return lines
                .stream()
                .takeWhile(s -> s.contains("|"))
                .map(s -> new Pair(Integer.parseInt(s.split("\\|")[0])
                        , Integer.parseInt(s.split("\\|")[1])))
                .toList();
    }

    private boolean validLine(List<Integer> split, List<Pair> pairs) {
        List<Integer> prevs = new ArrayList<>();
        prevs.add(split.getFirst());

        for (int j = 1; j < split.size(); j++) {

            int curr = split.get(j);

            var prevIncorrect = isPrevIncorrect(pairs, curr, prevs);
            var nextIncorrect = isNextIncorrect(pairs, curr, prevs);

            if (prevIncorrect || nextIncorrect) {
                return false;
            }

            prevs.add(curr);
        }
        return true;
    }


    private boolean isNextIncorrect(final List<Pair> pairs, final int curr, final List<Integer> prevs) {
        return pairs.stream()
                .filter(p -> p.start() == curr)
                .anyMatch(p -> prevs.contains(p.end()));
    }

    private boolean isPrevIncorrect(final List<Pair> pairs, final int curr, final List<Integer> prevs) {
        return pairs.stream()
                .filter(p -> p.end() == curr)
                .noneMatch(p -> prevs.contains(p.start()));
    }

    @Override
    public void solvePart2(List<String> lines) {

    }
}

record Pair(int start, int end) {
}