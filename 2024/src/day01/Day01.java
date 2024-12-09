package day01;

import config.Year2024;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Day01 extends Year2024 {
    public static void main(String[] args) throws IOException {
        var d = new Day01();

        var input = d.readInput();

        d.stopwatch.start();
        d.solvePart1(input);
        d.solvePart2(input);
        d.stopwatch.prettyPrint();
    }

    public void solvePart2(List<String> lines) {
        List<Integer> leftList = new ArrayList<>();
        List<Integer> rightList = new ArrayList<>();

        for (String s : lines) {
            leftList.add(Integer.parseInt(s.split(" {3}")[0].trim()));
            rightList.add(Integer.parseInt(s.split(" {3}")[1].trim()));
        }

        Collections.sort(leftList);
        Collections.sort(rightList);

        long sum = 0L;

        for (Integer integer : leftList) {
            int freq = Collections.frequency(rightList, integer);
            sum += (long) freq * integer;
        }

        System.out.printf("Similarity score: %s%n", sum);
    }

    public void solvePart1(List<String> lines) {
        List<Integer> leftList = new ArrayList<>();
        List<Integer> rightList = new ArrayList<>();

        for (String s : lines) {
            int left = Integer.parseInt(s.substring(0, 5));
            int right = Integer.parseInt(s.substring(8, 13));

            leftList.add(left);
            rightList.add(right);
        }

        Collections.sort(leftList);
        Collections.sort(rightList);

        long sum = 0L;
        for (int i = 0; i < leftList.size(); i++) {
            sum += Math.abs(leftList.get(i) - rightList.get(i));
        }

        System.out.printf("Total distance: %s%n", sum);
    }
}