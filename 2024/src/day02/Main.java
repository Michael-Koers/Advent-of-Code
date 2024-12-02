package day02;

import util.FileInput;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        List<String> lines = FileInput.read(2024, "02", FileInput.FileType.INPUT);

        solve(lines);
    }

    private static void solve(List<String> lines) {

        int safeReports = 0;
        int safeReportsProblemDampener = 0;

        outer:
        for (String line : lines) {

            int[] s = Arrays.stream(line.split(" ")).mapToInt(Integer::parseInt).toArray();

            // For safe report checks it's much easier to work with the difference than the actual numbers themselves.
            int[] diffs = calculateDifferences(s);

            // Part 1
            if (isSafe(diffs)) {
                safeReports++;
//                System.out.printf("Line: %s is SAFE%n", line);
            }
            // Part 2 - Problem Dampener
            else {
                // Basically brute forcing every possibility by removing 1 element somewhere in the sequence
                // and check if it passed the safe validations.
                for (int i = 0; i < s.length; i++) {
                    int[] leftNumbers = Arrays.copyOfRange(s, 0, i);
                    int[] rightNumbers = Arrays.copyOfRange(s, i + 1, s.length);

                    // Beautiful array copying stuff...
                    int[] newNumbers = new int[leftNumbers.length + rightNumbers.length];
                    System.arraycopy(leftNumbers, 0, newNumbers, 0, leftNumbers.length);
                    System.arraycopy(rightNumbers, 0, newNumbers, leftNumbers.length, rightNumbers.length);

                    int[] newDiffs = calculateDifferences(newNumbers);

                    if (isSafe(newDiffs)) {
                        safeReportsProblemDampener++;
//                        System.out.printf("Line dampened: %s is SAFE%n", line);
                        continue outer;
                    }
                }
            }
        }
        System.out.printf("Total SAFE lines: %s%n", safeReports);
        System.out.printf("Total DAMPENED lines: %s%n", safeReportsProblemDampener);
        System.out.printf("Total lines: %s%n", safeReports + safeReportsProblemDampener);
    }

    private static int[] calculateDifferences(int[] s) {
        int[] diffs = new int[s.length - 1];
        for (int j = 0; j < s.length - 1; j++) {
            diffs[j] = (s[j + 1] - s[j]);
        }
        return diffs;
    }

    private static boolean isSafe(int[] diffs) {
        boolean safeDiffs = Arrays.stream(diffs).allMatch(Main::isSafe);

        boolean allAscending = Arrays.stream(diffs).allMatch(d -> d < 0);
        boolean allDescending = Arrays.stream(diffs).allMatch(d -> d > 0);

        return safeDiffs && (allAscending || allDescending);
    }

    public static boolean isSafe(int diff) {
        return Math.abs(diff) > 0 && Math.abs(diff) < 4;
    }
}