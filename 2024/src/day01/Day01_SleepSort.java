package day01;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Day01_SleepSort {
    public static void main(String[] args) throws IOException {
        var d = new Day01_SleepSort();

        var lines = Files.readAllLines(Path.of("2024/src/day01/input.txt"));

        long start = System.currentTimeMillis();
        d.solve(lines);
        System.out.printf("Runtime: %sms%n", System.currentTimeMillis() - start);
    }

    public void solve(List<String> lines) {
        List<Integer> leftList = new ArrayList<>();
        List<Integer> rightList = new ArrayList<>();

        for (String s : lines) {
            var split = s.split(" {3}");
            leftList.add(Integer.parseInt(split[0]));
            rightList.add(Integer.parseInt(split[1]));
        }

        // Sorting our lists using.... SleepSort!
        // Why use precious resources like CPU and/or RAM, when we can use the limitless resource
        // that is... TIME!
        var sorted = sleepSort(leftList, rightList);
        leftList = sorted.left();
        rightList = sorted.right();

        long part1 = 0L;
        long part2 = 0L;

        for (int i = 0; i < leftList.size(); i++) {
            // Part 1
            part1 += Math.abs(leftList.get(i) - rightList.get(i));

            // Part 2
            int freq = Collections.frequency(rightList, leftList.get(i));
            part2 += (long) freq * leftList.get(i);
        }

        System.out.printf("Part 1: %s, Part 2: %s%n", part1, part2);
    }

    Result sleepSort(List<Integer> left, List<Integer> right) {
        List<Integer> sortedLeft = Collections.synchronizedList(new ArrayList<>());
        List<Integer> sortedRight = Collections.synchronizedList(new ArrayList<>());
        List<Thread> threads = new ArrayList<>();

        // Start threads for 'sorting' left
        for (Integer number : left) {
            // Using the new fancy VirtualThreads in Java 21+
            Thread thread = Thread.ofVirtual().start(() -> {
                try {
                    // Pause virtual thread, using micro or nanoseconds gave... inconsistent results.. :)
                    Thread.sleep(number);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                // Add number to the sorted list after sleep
                sortedLeft.add(number);
            });
            threads.add(thread);
        }

        // Start threads for 'sorting' right
        for (Integer number : right) {
            // Using the new fancy VirtualThreads in Java 21+
            Thread thread = Thread.ofVirtual().start(() -> {
                try {
                    // Pause virtual thread, using micro or nanoseconds gave... inconsistent results.. :)
                    Thread.sleep(number);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                // Add number to the sorted list after sleep
                sortedRight.add(number);
            });
            threads.add(thread);
        }

        // Join threads back together, because we put them all in the same list, we only need to wait once
        // Big performance increase!
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        return new Result(sortedLeft, sortedRight);
    }


}

record Result(List<Integer> left, List<Integer> right) {
}
