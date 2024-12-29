package day11;

import config.Year2024;

import java.io.IOException;
import java.util.*;

public class Day11 extends Year2024 {

    Map<String, Long> cache = new HashMap<>();
    List<Long> stones = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        var d = new Day11();
        d.solve(d.readInput());
    }

    @Override
    public void solvePart1(final List<String> lines) {

        stones = Arrays.stream(lines.getFirst().split(" ")).map(Long::parseLong).toList();

        long sum = 0;
        for (final Long stone : stones) {
            sum += blink(stone, 25);
        }

        System.out.println("Part 1: " + sum);
    }

    @Override
    public void solvePart2(final List<String> lines) {

        long sum = 0;
        for (final Long stone : stones) {
            sum += blink(stone, 75);
        }

        System.out.println("Part 2: " + sum);
    }

    public Long blink(Long stone, int blinksLeft) {
        var cacheKey = "%s-%s".formatted(stone, blinksLeft);

        if (cache.containsKey(cacheKey)) {
            return cache.get(cacheKey);
        }

        if (blinksLeft == 0) {
            return 1L;
        }

        Long result;
        if (stone == 0) {
            result = blink(1L, blinksLeft - 1);
        } else if (isSplittable(stone)) {
            String currentValue = stone.toString();
            String leftValue = currentValue.substring(0, currentValue.length() / 2);
            String rightValue = currentValue.substring(currentValue.length() / 2).trim();

            result = blink(Long.valueOf(leftValue), blinksLeft - 1)
                    + blink(Long.valueOf(rightValue), blinksLeft - 1);
        } else {
            result = blink(stone * 2024L, blinksLeft - 1);
        }

        cache.put(cacheKey, result);
        return result;
    }


    boolean isSplittable(Long value) {
        return value.toString().length() % 2 == 0;
    }
}