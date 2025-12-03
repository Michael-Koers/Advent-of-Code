package day03;

import config.Year2025;
import java.io.IOException;
import java.util.List;

public class Day03 extends Year2025 {

    public static void main(String[] args) throws IOException {
        var d = new Day03();

        var lines = d.readInput();

        d.solvePart2(lines);
    }

    @Override
    public void solvePart1(final List<String> lines) {

        long total = 0;

        for (final String line : lines) {

            var parts = line.split("");

            int firstHighest = Integer.MIN_VALUE;
            int firstIndex = 0;
            int secondHighest = Integer.MIN_VALUE;

            for (int i = 0; i < parts.length-1; i++) {

                var current = Integer.parseInt(parts[i]);
                if(current > firstHighest) {
                    firstHighest = current;
                    firstIndex = i;
                }
            }

            for(int i = firstIndex+1; i < parts.length; i++) {
                var current = Integer.parseInt(parts[i]);
                if(current > secondHighest) {
                    secondHighest = current;
                }
            }

            Long result = Long.parseLong(String.valueOf(firstHighest).concat(String.valueOf(secondHighest)));

            System.out.println("Largest Joltage: " + result);

            total+= result;

        }


        System.out.println("Part 1: " + total);

    }

    @Override
    public void solvePart2(final List<String> lines) {

        long total = 0;

        for (final String line : lines) {

            var parts = line.split("");

            StringBuilder joltage = new StringBuilder();

            var length = 12;
            var startIndex = 0;

            while(joltage.length() < length) {

                int highest = Integer.MIN_VALUE;

                for(int i = startIndex; i <= parts.length - (length - joltage.length()); i++) {

                    var current = Integer.parseInt(parts[i]);

                    if(current > highest) {
                        highest = current;
                        startIndex = i+1;
                    }
                }

                joltage.append(highest);


            }

            System.out.println("Largest Joltage: " + joltage.toString());

            total += Long.parseLong(joltage.toString());
        }


        System.out.println("Part 2: " + total);

    }
}
