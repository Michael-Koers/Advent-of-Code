package day12;

import config.Year2025;

import java.io.IOException;
import java.util.List;

public class Day12 extends Year2025 {

    public static void main(String[] args) throws IOException {
        var d = new Day12();

        // Removed the shapes from the input, as they are irrelevant to solving this part
        var lines = d.readInput();

        d.solvePart1(lines);
    }


    @Override
    public void solvePart1(List<String> lines) {

        long possible = 0L;
        for (String line : lines) {
            var parts = line.split(":");
            var length = Integer.parseInt(parts[0].split("x")[0]);
            var width = Integer.parseInt(parts[0].split("x")[1]);

            int nrOfShapes = 0;
            for (String s : parts[1].trim().split(" ")) {
                nrOfShapes += Integer.parseInt(s);
            }

            if (nrOfShapes <= length / 3 * width / 3) {
                possible++;
            }
        }

        System.out.println("Part 2: " + possible);
    }

    @Override
    public void solvePart2(List<String> lines) {

    }
}
