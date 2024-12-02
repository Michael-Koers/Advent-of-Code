package day01;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Main {

    public static void main(String[] args) throws IOException {
        solvePart1(Files.readAllLines(Paths.get("2023/day-01/input.txt")));
        solvePart2(Files.readAllLines(Paths.get("2023/day-01/input.txt")));
    }

    private static void solvePart1(List<String> lines) {
        List<Integer> results = new ArrayList<>();

        for (String line : lines) {

            String first = "";
            String last = "";

            for (char character : line.toCharArray()) {
                if (Character.isDigit(character)) {
                    if (first.isEmpty()) {
                        first = String.valueOf(character);
                    }
                    last = String.valueOf(character);
                }
            }

            results.add(Integer.parseInt(first.concat(last)));

        }

        int result = results.stream()
                .mapToInt(Integer::intValue)
                .sum();

        System.out.printf("Part 1 final result: %d%n", result);
    }


    private static void solvePart2(List<String> lines) {

        Map<String, Integer> values = Map.of("one", 1,
                "two", 2,
                "three", 3,
                "four", 4,
                "five", 5,
                "six", 6,
                "seven", 7,
                "eight", 8,
                "nine", 9);

        List<Integer> results = new ArrayList<>();

        for (String line : lines) {

            // position, value
            Map<Integer, Integer> positions = new HashMap<>();

            for (Map.Entry<String, Integer> pair : values.entrySet()) {
                if (line.contains(pair.getKey())) {
                    // indexOf gives first occurence, lastIndexOf gives final occurence, because same String can occur more than once
                    positions.put(line.indexOf(pair.getKey()), pair.getValue());
                    positions.putIfAbsent(line.lastIndexOf(pair.getKey()), pair.getValue());
                }
            }

            for (int i = 0; i < line.toCharArray().length; i++) {
                if (Character.isDigit(line.charAt(i))) {
                    positions.put(i, Character.getNumericValue(line.charAt(i)));
                }
            }

            int lowestIndex = Collections.min(positions.keySet());
            int highestIndex = Collections.max(positions.keySet());

            results.add(Integer.parseInt(positions.get(lowestIndex) + "" + positions.get(highestIndex)));
        }

        int result = results.stream()
                .mapToInt(Integer::intValue)
                .sum();

        System.out.printf("Part 2 final result: %d%n", result);
    }
}