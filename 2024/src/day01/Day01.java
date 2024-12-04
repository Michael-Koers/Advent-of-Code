package day01;

import config.Year2024;
import util.Pair;
import util.Stopwatch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Day01 extends Year2024 {
    public static void main(String[] args) throws IOException {
        var d = new Day01();

        d.stopwatch.start();
        d.solvePart1(d.readInput());
        d.solvePart2(d.readInput());
        d.stopwatch.prettyPrint();

    }

    public void solvePart2(List<String> lines) {
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

    public void solvePart1(List<String> lines) {
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

