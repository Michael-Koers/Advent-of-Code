package day07;

import config.Year2024;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Day07 extends Year2024 {
    public static void main(String[] args) throws IOException {
        var d = new Day07();

        var input = d.readInput();

        d.stopwatch.start();
        d.solvePart1(input);
        d.solvePart2(input);
        d.stopwatch.prettyPrint();
    }

    @Override
    public void solvePart1(List<String> lines) {
        long sum = 0L;
        for (String line : lines) {
            long target = Long.parseLong(line.split(":")[0]);
            Long[] available = Arrays.stream(line.split(":")[1].trim().split(" ")).map(Long::parseLong).toArray(Long[]::new);
            if (canReachDesiredCalibrationPart1(0L, target, available, 0)) {
                sum += target;
            }
        }
        System.out.printf("Total: %s%n", sum);
    }

    boolean canReachDesiredCalibrationPart1(long current, long target, Long[] longs, int index) {

        // Stop at end of array or if current calculation has surpassed the goal
        if (current > target || index >= longs.length) {
            return current == target;
        }

        long next = current + longs[index];

        if (next <= target && canReachDesiredCalibrationPart1(next, target, longs, index + 1)) {
            return true;
        }

        next = current * longs[index];
        if (next <= target && canReachDesiredCalibrationPart1(next, target, longs, index + 1)) {
            return true;
        }

        return false;
    }

    boolean canReachDesiredCalibrationPart2(long current, long target, Long[] longs, int index) {

        // Stop at end of array or if current calculation has surpassed the goal
        if (current > target || index >= longs.length) {
            return current == target;
        }

        long next = current + longs[index];

        if (next <= target && canReachDesiredCalibrationPart2(next, target, longs, index + 1)) {
            return true;
        }

        next = current * longs[index];
        if (next <= target && canReachDesiredCalibrationPart2(next, target, longs, index + 1)) {
            return true;
        }

        next = Long.parseLong(String.valueOf(current).concat(String.valueOf(longs[index])));
        if (next <= target && canReachDesiredCalibrationPart2(next, target, longs, index + 1)) {
            return true;
        }

        return false;
    }

    @Override
    public void solvePart2(List<String> lines) {
        long sum = 0L;
        for (String line : lines) {
            long target = Long.parseLong(line.split(":")[0]);
            Long[] available = Arrays.stream(line.split(":")[1].trim().split(" ")).map(Long::parseLong).toArray(Long[]::new);

            if (canReachDesiredCalibrationPart2(0L, target, available, 0)) {
                sum += target;
            }
        }
        System.out.printf("Total: %s%n", sum);
    }
}