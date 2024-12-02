package day12;

import util.FileInput;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.IntStream;

import static java.util.stream.IntStream.range;

public class Main {

    private static Map<Input, Long> permutationCache = new HashMap<>();

    public static void main(String[] args) throws URISyntaxException, IOException {

        List<String> lines = FileInput.read(FileInput.INPUT, Main.class);

        solvePart1(lines);
        solvePart2(lines);
    }

    private static void solvePart2(List<String> lines) {

        long total = 0L;
        for (String line : lines) {

            String[] parts = line.split(" ");

            List<Integer> groups = unfoldGroups(Arrays.stream(parts[1].split(",")).map(Integer::parseInt).toList(), 5);
            String condition = unfoldCondition(parts[0], 5);

            total += countPermutations(condition, groups);
        }

        System.out.printf("Solved part 2, total permutations: %s%n", total);

    }

    private static String unfoldCondition(String part, int times) {
        return IntStream.range(0, times).mapToObj(i -> {
            return part;
        }).reduce((s, s2) -> s + "?" + s2).get();
    }

    private static List<Integer> unfoldGroups(List<Integer> groups, int times) {
        List<Integer> unfolded = new ArrayList<>();
        for (int i = 0; i < times; i++) {
            unfolded.addAll(groups);
        }
        return unfolded;
    }

    private static long countPermutations(String condition, List<Integer> groups) {

        // Only managed to solve this thanks to: https://github.com/ash42/adventofcode/blob/main/adventofcode2023/src/nl/michielgraat/adventofcode2023/day12/Day12.java
        Input input = new Input(condition, groups);

        // Cache with previously calculated permutations, return if condition and groups have already been calculated
        if (permutationCache.containsKey(input)) {
            return permutationCache.get(input);
        }

        if (condition.isEmpty()) {
            // If condition and remaining groups are both empty, this is a valid permutation so +1
            return groups.isEmpty() ? 1 : 0;
        }

        char firstChar = condition.charAt(0);
        long permutations = 0L;

        if (firstChar == '.') {
            permutations = countPermutations(condition.substring(1), groups);
        } else if (firstChar == '?') {
            permutations = countPermutations("#" + condition.substring(1), groups) +
                    countPermutations("." + condition.substring(1), groups);
        }
        // Fist char is '#', Condition left with groups left
        else if (!groups.isEmpty()) {
            // Get first group size of damaged springs
            int nrDamaged = groups.get(0);
            List<Integer> remainingGroups = groups.subList(1, groups.size());

            // If group is smaller than remaining condition, and first characters of condition equals to group size
            // and are all valid springs, then group size fits in start of condition
            if (nrDamaged <= condition.length() && condition.chars().limit(nrDamaged).allMatch(c -> c == '?' || c == '#')) {

                // Remaning condition length equals next number of damaged springs
                if (nrDamaged == condition.length()) {
                    // Permutation is only valid (because we are at end of condition) if no remaining groups are left
                    // (otherwise there are more groups then there is condition left).
                    permutations = remainingGroups.isEmpty() ? 1 : 0;
                } else if (condition.charAt(nrDamaged) == '.') {
                    // First char after current group is '.', which splits this group from the next, which is valid
                    permutations = countPermutations(condition.substring(nrDamaged + 1), remainingGroups);
                } else if (condition.charAt(nrDamaged) == '?') {
                    // First char after current group is '?', which is only valid if it becomes a '.'
                    // A '#' would mean current group is actually longer, which is invalid
                    permutations = countPermutations("." + condition.substring(nrDamaged + 1), remainingGroups);
                }
            }
        }

        permutationCache.put(input, permutations);
        return permutations;
    }

    private static void solvePart1(List<String> lines) {

        long valid = 0L;

        // Generate all permutations and check wether they abide to their own rules
        for (String line : lines) {

            String springs = line.split(" ")[0];
            List<Integer> criteria = Arrays.stream(line.split(" ")[1].split(",")).map(Integer::parseInt).toList();
            String[] permutations = findAllPermutations(springs);

            for (String permutation : permutations) {

                List<Integer> permSizes = Arrays.stream(permutation.split("\\.")).filter(e -> !e.isBlank()).map(str -> Integer.valueOf(str.length())).toList();
                if (criteria.size() == permSizes.size() && criteria.equals(permSizes)) {
                    valid++;
                }
            }
        }

        System.out.printf("Solved part 1, total of %s valid permutations%n", valid);

    }

    private static String[] findAllPermutations(String springs) {

        // Array of substrings around the unknown numbers
        String[] arr = springs.split("\\?", -1);

        // From: https://stackoverflow.com/questions/65973024/generate-all-possible-string-combinations-by-replacing-the-hidden-number-sig
        return // Loop over all parts
                range(0, arr.length)

                        .mapToObj(i -> i < arr.length - 1 ? new String[]{arr[i] + ".", arr[i] + "#"} : new String[]{arr[i]})

                        // Combine all possibilities on the left, with possibilities on the right, growing the collectiong exponantially
                        .reduce((arr1, arr2) -> Arrays.stream(arr1)
                                .flatMap(str1 -> Arrays.stream(arr2).map(str2 -> str1 + str2))
                                .toArray(String[]::new))
                        .orElse(new String[]{});
    }
}

record Input(String condition, List<Integer> groups) {
};