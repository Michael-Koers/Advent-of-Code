package day03;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws IOException {

        long start = System.currentTimeMillis();

        List<String> lines = Files.readAllLines(Paths.get("2023/day-03/input.txt"));

        List<EnginePart> parts = parseEngineParts(lines);
        List<Symbol> symbols = parseSymbols(lines);

        solvePart1(parts, symbols);
        solvePart2(parts, symbols);

        System.out.printf("Solved in %sms", System.currentTimeMillis() - start);
    }

    private static void solvePart2(List<EnginePart> parts, List<Symbol> symbols) {

        List<Symbol> asteriksSymbols = symbols.stream().filter(e -> e.symbol().equals("*")).collect(Collectors.toList());
        long totalGearRatio = 0;

        for (Symbol asterik : asteriksSymbols) {

            long count = parts.stream()
                    .filter(e -> Math.abs(e.row() - asterik.row()) < 2 && (Math.abs(e.start() - asterik.col()) < 2 || Math.abs(e.end() - asterik.col()) < 2))
                    .count();

            if (count != 2) {
                continue;
            }

            totalGearRatio += parts.stream()
                    .filter(e -> Math.abs(e.row() - asterik.row()) < 2 && (Math.abs(e.start() - asterik.col()) < 2 || Math.abs(e.end() - asterik.col()) < 2))
                    .mapToLong(EnginePart::value)
                    .reduce(1, (a, b) -> a * b);

        }

        System.out.printf("Part 2, total gear ratio: %s%n", totalGearRatio);
    }

    private static void solvePart1(List<EnginePart> parts, List<Symbol> symbols) {

        List<EnginePart> validParts = new ArrayList<>();

        for (EnginePart part : parts) {

            boolean anyAdjacent = symbols.stream().anyMatch(e -> Math.abs(part.row() - e.row()) < 2 && (Math.abs(part.start() - e.col()) < 2 || Math.abs(part.end() - e.col()) < 2));
            List<Symbol> adjacentSymbols = symbols.stream().filter(e -> Math.abs(part.row() - e.row()) < 2 && (Math.abs(part.start() - e.col()) < 2 || Math.abs(part.end() - e.col()) < 2)).toList();

            if (anyAdjacent) {
                System.out.printf("Part %s is adjacent to symbol(s): %s%n", part, adjacentSymbols);
                validParts.add(part);
            } else {
                System.out.printf("Part %s is not adjacent to any symbols%n", part);
            }
        }

        int enginePartSum = validParts.stream().mapToInt(EnginePart::value).sum();
        System.out.printf("Part 1, sum of all engine parts: %s%n", enginePartSum);

    }

    private static List<Symbol> parseSymbols(List<String> lines) {

        List<Symbol> symbols = new ArrayList<>();

        for (int i = 0; i < lines.size(); i++) {

            String line = lines.get(i);
            String[] result = line.split("");

            // Remember end index of last part to prevent problems with duplicate engine part value
            int lastIndex = 0;

            for (String part : result) {

                if (part.isBlank() || isNumeric(part) || part.equals(".")) {
                    continue;
                }

                int col = line.indexOf(part, lastIndex);
                Symbol symbol = new Symbol(part, col, i);
                symbols.add(symbol);

                lastIndex = col + 1;
            }
        }
        return symbols;
    }

    private static List<EnginePart> parseEngineParts(List<String> lines) {

        List<EnginePart> parts = new ArrayList<>();

        for (int i = 0; i < lines.size(); i++) {

            String line = lines.get(i);
            String[] result = line.split("[\\.\\*\\#\\+\\$\\=\\/\\@\\%\\&\\-]");

            // Remember end index of last part to prevent problems with duplicate engine part value
            int lastIndex = 0;

            for (String part : result) {

                if (part.isBlank() || !isNumeric(part)) {
                    continue;
                }

                int start = line.indexOf(part, lastIndex);
                int end = start + part.length() - 1;

                EnginePart newEnginePart = new EnginePart(
                        Integer.parseInt(part), start, end, i
                );

                parts.add(newEnginePart);
                lastIndex = end + 1;
            }
        }
        return parts;
    }

    public static boolean isNumeric(String value) {
        return value.chars().allMatch(Character::isDigit);
    }
}


record EnginePart(int value, int start, int end, int row) {

}

record Symbol(String symbol, int col, int row) {
};