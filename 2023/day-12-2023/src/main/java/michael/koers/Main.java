package michael.koers;

import util.FileInput;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.stream.IntStream.range;

public class Main {
    public static void main(String[] args) throws URISyntaxException, IOException {

        List<String> lines = FileInput.read(FileInput.INPUT_TEST, Main.class);

//        solvePart1(lines);
        solvePart2(lines);
    }

    private static void solvePart2(List<String> lines) {

        long valid = 0L;

        // Generate all permutations and check wether they abide to their own rules
        for (String line : lines) {

            String springs = line.split(" ")[0];
            List<Integer> criteria = Arrays.stream(line.split(" ")[1].split(",")).map(Integer::parseInt).collect(Collectors.toCollection(ArrayList::new));
            List<Integer> dupe = new ArrayList<>(criteria);
            criteria.addAll(dupe);
            criteria.addAll(dupe);
            criteria.addAll(dupe);
            criteria.addAll(dupe);

            String unfoldedSpring = range(0, 5).mapToObj(i -> {
                return springs;
            }).reduce((s1, s2) -> s1 + "?" + s2).get();

            long count = countAllPermutations(unfoldedSpring, criteria);
            System.out.printf("String %s(%s) has %s valid permutations for %s(%s)%n", unfoldedSpring, springs, count, criteria, dupe);
            valid += count;
        }

        System.out.printf("Solved part 1, total of %s valid permutations%n", valid);

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

    private static long countAllPermutations(String springs, List<Integer> sizes) {

        // Array of substrings around the unknown numbers
        String[] arr = springs.split("\\?", -1);

        // From: https://stackoverflow.com/questions/65973024/generate-all-possible-string-combinations-by-replacing-the-hidden-number-sig
        String[] x = // Loop over all parts
                Arrays.stream(range(0, arr.length)

                        .mapToObj(i -> i < arr.length - 1 ? new String[]{arr[i] + ".", arr[i] + "#"} : new String[]{arr[i]})

                        // Combine all possibilities on the left, with possibilities on the right, growing the collectiong exponantially
                        .reduce((arr1, arr2) -> Arrays.stream(arr1)
                                .flatMap(str1 -> Arrays.stream(arr2)
                                        .map(str2 -> str1 + str2))
                                .toArray(String[]::new))
                        .get())
                        .filter(perm -> Arrays.stream(perm.split("\\."))
                                .filter(s -> !s.isBlank())
                                .map(String::length)
                                .allMatch(sizes::contains))
                        .filter(perm -> Arrays.stream(perm.split("\\."))
                                .filter(s -> !s.isBlank())
                                .map(String::length)
                                .toList().equals(sizes)).toArray(String[]::new);
                        /*
                        .filter(perm -> Arrays.stream(perm.split("\\."))
                .filter(s -> !s.isBlank())
                .map(String::length)
                .allMatch(sizes::contains))
                .filter(perm -> Arrays.stream(perm.split("\\."))
                        .filter(s -> !s.isBlank())
                        .map(String::length)
                        .toList().equals(sizes)))
*/
        return x.length;
    }
}
