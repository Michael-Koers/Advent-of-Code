package day25;

import config.Year2024;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Day25 extends Year2024 {

    public static void main(String[] args) throws IOException {

        var d = new Day25();

        var lines = d.readInput();

        d.solve(lines);

    }


    @Override
    public void solvePart1(List<String> lines) {

        List<List<Integer>> locks = new ArrayList<>();
        List<List<Integer>> keys = new ArrayList<>();

        int i = 0;
        while (i < lines.size()) {

            List<String> lockOrKey = List.of(
                    lines.get(i),
                    lines.get(i + 1),
                    lines.get(i + 2),
                    lines.get(i + 3),
                    lines.get(i + 4),
                    lines.get(i + 5),
                    lines.get(i + 6)
            );

            var isLock = lockOrKey.getFirst().contains("#");

            if (isLock) {
                locks.add(findLockHeights(lockOrKey));
            } else {
                keys.add(findKeyHeights(lockOrKey));
            }

            i += 8;
        }

        int fit = 0;
        for (List<Integer> key : keys) {
            nextLock:
            for (List<Integer> lock : locks) {
                for (int c = 0; c < key.size(); c++) {
                    if (key.get(c) + lock.get(c) >= 6) {
                        continue nextLock;
                    }
                }
                fit++;
            }
        }

        System.out.println("Part 1: " + fit);
    }

    public static List<Integer> findLockHeights(List<String> grid) {
        int numRows = grid.size();
        int numCols = grid.getFirst().length();
        List<Integer> results = new ArrayList<>();

        // Iterate through each column
        for (int col = 0; col < numCols; col++) {
            int lowestRow = -1; // Default if no '#' is found

            // Check from the bottom row upwards
            for (int row = numRows - 1; row >= 0; row--) {
                if (grid.get(row).charAt(col) == '#') {
                    lowestRow = row;
                    break;
                }
            }

            results.add(lowestRow);
        }

        return results;
    }

    public static List<Integer> findKeyHeights(List<String> grid) {
        int numRows = grid.size();
        int numCols = grid.getFirst().length();
        List<Integer> results = new ArrayList<>();

        // Iterate through each column
        for (int col = 0; col < numCols; col++) {
            int highestRow = -1; // Default if no '#' is found

            // Check from the bottom row upwards
            for (int row = 0; row <= numRows; row++) {
                if (grid.get(row).charAt(col) == '#') {
                    highestRow = numRows - row - 1;
                    break;
                }
            }

            results.add(highestRow);
        }

        return results;
    }

    @Override
    public void solvePart2(List<String> lines) {

    }
}
