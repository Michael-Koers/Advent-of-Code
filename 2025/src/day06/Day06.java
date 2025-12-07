package day06;

import config.Year2025;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day06 extends Year2025 {

    // Little bit of cheating, add a "-" operand at the end of the input file (required to get the correct padding for numbers in last column)
    public static void main(String[] args) throws IOException {
        var d = new Day06();
        var lines = d.readInput();
        d.solve(lines);
    }

    @Override
    public void solvePart1(final List<String> lines) {

        Map<Integer, List<Integer>> map = new HashMap<>();
        Map<Integer, String> ops = new HashMap<>();

        for (final String line : lines) {
            var parts = line.trim().split("\\s+");

            for (int i = 0; i < parts.length; i++) {
                if (parts[i].contains("-")) {continue;}
                if (parts[i].trim().contains("*") || parts[i].trim().contains("+")) {
                    ops.put(i, parts[i].trim());
                } else {

                    var number = Integer.parseInt(parts[i]);

                    map.putIfAbsent(i, new ArrayList<>());

                    map.get(i).add(number);
                }
            }
        }

        long total = 0;
        for (int i = 0; i < map.size(); i++) {

            var numbers = map.get(i);
            var op = ops.get(i);

            long result = 0;
            if (op.equals("*")) {
                if (result == 0) {result = 1;} ;
                for (int number : numbers) {
                    result *= number;
                }
            } else if (op.equals("+")) {
                for (int number : numbers) {
                    result += number;
                }
            }

            total += result;
        }


        System.out.println("Part 2:" + total);


    }

    @Override
    public void solvePart2(final List<String> lines) {
        Map<Integer, List<String>> map = new HashMap<>();
        Map<Integer, String> ops = new HashMap<>();

        // index to length
        Map<Integer, Integer> indexLength = new HashMap<>();

        var opParts = lines.getLast().split(" ");
        int length = 0;
        int index = 0;
        for (final String part : opParts) {
            if (part.isBlank()) {
                length++;
            } else if (length > 0) {
                indexLength.put(index, length);
                index++;
                length = 0;
            }
        }

        opParts = lines.getLast().split("\\s+");
        for (int i = 0; i < opParts.length; i++) {
            if (opParts[i].equals("-")) {continue;}

            ops.put(i, opParts[i]);
        }

        lines.removeLast(); // already processsed

        for (final String line : lines) {
            int offset = 0;
            for (final Map.Entry<Integer, Integer> entry : indexLength.entrySet()) {
                var start = entry.getKey() + offset;
                var partlength = entry.getValue() + 1;

                var part = line.substring(start, start + partlength);

                map.putIfAbsent(entry.getKey(), new ArrayList<>());

                map.get(entry.getKey()).add(part);
                offset += partlength;
            }
        }

        long total = 0L;

        for (int i = 0; i < map.size(); i++) {

            var numbers = map.get(i);
            var size = numbers.getFirst().length();
            List<Integer> newNumbers = new ArrayList<>();
            for (int j = size - 1; j >= 0; j--) {

                String newNumber = "";
                for (final String number : numbers) {
                    var charAt = number.charAt(j);
                    if (charAt != 32) {
                        newNumber = newNumber.concat(String.valueOf(charAt));
                    }
                }

                newNumbers.add(Integer.parseInt(newNumber));
            }


            long result = 0;
            var op = ops.get(i);

            if (op.equals("*")) {
                if (result == 0) {result = 1;} ;
                for (int number : newNumbers) {
                    result *= number;
                }
            } else if (op.equals("+")) {
                for (int number : newNumbers) {
                    result += number;
                }
            }

            total += result;
        }

        System.out.println("Part 2: " + total);
    }
}
