package michael.koers;

import util.FileInput;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws URISyntaxException, IOException {

        List<String> read = FileInput.read(FileInput.INPUT, Main.class);

        List<Field> fields = parseInput(read);

//        solvePart1(fields);
        solvePart2(fields);
    }

    private static void solvePart2(List<Field> fields) {
        List<Integer> hors = new ArrayList<>();
        List<Integer> verts = new ArrayList<>();

        field:
        for (Field field : fields) {

            List<String> prevs = new ArrayList<>();
            List<String> currentField = field.layout();

            // Check horizontal
            for (int i = 0; i < field.layout().size(); i++) {

                String current = currentField.get(i);

                if (prevs.isEmpty()) {
                    prevs.add(current);
                    continue;
                }

                boolean match = true;
                boolean foundSmudge = false;
                for (int d = 0; d < Math.min(prevs.size(), currentField.size() - i); d++) {
                    if(!foundSmudge && smudgeEquals(prevs.get(i - d - 1), currentField.get(i + d))){
                        foundSmudge = true;
                    } else if (!prevs.get(i - d - 1).equals(currentField.get(i + d))) {
                        match = false;
                        break;
                    }
                }

                if (match && foundSmudge) {
                    hors.add(i);
                    System.out.printf("Field %s has horizontal mirror between %s and %s%n", field.index(), i, i + 1);
                    continue field;
                }
                prevs.add(current);
            }

            // Check vertical
            prevs.clear();

            // Loop over x-axis
            for (int x = 0; x < currentField.get(0).length(); x++) {

                // Create vertical line
                String current = getLineAtX(currentField, x);

                if (prevs.isEmpty()) {
                    prevs.add(current);
                    continue;
                }

                boolean match = true;
                boolean foundSmudge = false;
                for (int d = 0; d < Math.min(prevs.size(), currentField.get(0).length() - x); d++) {
                    if(!foundSmudge && smudgeEquals(prevs.get(x - d - 1), getLineAtX(currentField, x + d))){
                        foundSmudge = true;
                    } else if (!prevs.get(x - d - 1).equals(getLineAtX(currentField, x + d))) {
                        match = false;
                        break;
                    }
                }

                if (match && foundSmudge) {
                    verts.add(x);
                    System.out.printf("Field %s has vertical mirror between %s and %s%n", field.index(), x, x + 1);
                    continue field;
                }
                prevs.add(current);
            }
        }

        long rowValue = hors.stream().mapToLong(i -> i * 100L).sum();
        long colValue = verts.stream().mapToLong(Integer::longValue).sum();

        System.out.printf("Solved part 2, total of notes: %s%n", rowValue + colValue);
    }

    private static boolean smudgeEquals(String s, String s1) {
        int diffCount = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) != s1.charAt(i)) {
                diffCount++;
            }
        }
        return diffCount == 1;
    }

    private static void solvePart1(List<Field> fields) {

        List<Integer> hors = new ArrayList<>();
        List<Integer> verts = new ArrayList<>();

        field:
        for (Field field : fields) {

            List<String> prevs = new ArrayList<>();
            List<String> currentField = field.layout();

            // Check horizontal
            for (int i = 0; i < field.layout().size(); i++) {

                String current = currentField.get(i);

                if (prevs.isEmpty()) {
                    prevs.add(current);
                    continue;
                }

                boolean match = true;
                for (int d = 0; d < Math.min(prevs.size(), currentField.size() - i); d++) {
                    if (!prevs.get(i - d - 1).equals(currentField.get(i + d))) {
                        match = false;
                        break;
                    }
                }

                if (match) {
                    hors.add(i);
                    System.out.printf("Field %s has horizontal mirror between %s and %s%n", field.index(), i, i + 1);
                    continue field;
                }
                prevs.add(current);
            }

            // Check vertical
            prevs.clear();

            // Loop over x-axis
            for (int x = 0; x < currentField.get(0).length(); x++) {

                // Create vertical line
                String current = getLineAtX(currentField, x);

                if (prevs.isEmpty()) {
                    prevs.add(current);
                    continue;
                }

                boolean match = true;
                for (int d = 0; d < Math.min(prevs.size(), currentField.get(0).length() - x); d++) {
                    if (!prevs.get(x - d - 1).equals(getLineAtX(currentField, x + d))) {
                        match = false;
                        break;
                    }
                }

                if (match) {
                    verts.add(x);
                    System.out.printf("Field %s has vertical mirror between %s and %s%n", field.index(), x, x + 1);
                    continue field;
                }
                prevs.add(current);
            }
        }

        long rowValue = hors.stream().mapToLong(i -> i * 100L).sum();
        long colValue = verts.stream().mapToLong(Integer::longValue).sum();

        System.out.printf("Solved part 1, total of notes: %s%n", rowValue + colValue);
    }

    private static String getLineAtX(List<String> currentField, int x) {
        StringBuilder sb = new StringBuilder();
        for (String s : currentField) {
            sb.append(s.charAt(x));
        }
        return sb.toString();
    }

    private static List<Field> parseInput(List<String> lines) {

        List<Field> fields = new ArrayList<>();
        List<String> tmp = new ArrayList<>();
        Integer index = 0;
        for (String line : lines) {
            if (line.isBlank()) {
                fields.add(new Field(index++, new ArrayList<>(tmp)));
                tmp.clear();
                continue;
            }
            tmp.add(line);
        }
        fields.add(new Field(index++, new ArrayList<>(tmp)));

        return fields;
    }
}

record Field(Integer index, List<String> layout) {
};