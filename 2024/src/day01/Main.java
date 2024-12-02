package day01;

import util.FileInput;
import util.Pair;
import util.Stopwatch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {

        List<String> lines = FileInput.read(2024, "01", FileInput.FileType.INPUT_TEST);

        var stopwatch = new Stopwatch();
        solvePart1(lines);
        stopwatch.print();
        solvePart2(lines);
        stopwatch.stop();
        stopwatch.print();

    }

    private static void solvePart2(List<String> lines) {
        List<Integer> leftList = new ArrayList<>();
        List<Integer> rightList = new ArrayList<>();

        for (int i = 0; i < lines.size(); i++) {
            var line = lines.get(i).trim();

            leftList.add(Integer.parseInt(line.split(" {3}")[0].trim()));
            rightList.add(Integer.parseInt(line.split(" {3}")[1].trim()));
        }

        Collections.sort(leftList);
        Collections.sort(rightList);

        Long sum = 0L;

        for (int i = 0; i < leftList.size(); i++) {
            int freq = Collections.frequency(rightList, leftList.get(i));
            sum += (long) freq * leftList.get(i);
        }

        System.out.printf("Similarity score: %s%n", sum);
    }

    private static void solvePart1(List<String> lines) {
        List<Integer> leftList = new ArrayList<>();
        List<Integer> rightList = new ArrayList<>();

        for (int i = 0; i < lines.size(); i++) {
            var line = lines.get(i).trim();

            int left = Integer.parseInt(line.split(" {3}")[0].trim());
            int right = Integer.parseInt(line.split(" {3}")[1].trim());

            leftList.add(left);
            rightList.add(right);
        }

        Collections.sort(leftList);
        Collections.sort(rightList);

        List<Pair> pairs = new ArrayList<>();
        for (int i = 0; i < leftList.size(); i++) {
            pairs.add(new Pair(leftList.get(i), rightList.get(i)));
        }

        long sum = pairs.stream()
                .mapToLong(Pair::apart)
                .sum();

        System.out.printf("Total distance: %s%n", sum);
    }
}

