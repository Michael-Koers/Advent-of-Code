package day11;

import config.Year2024;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Day11 extends Year2024 {

    Map<Blinks, Long> cache = new HashMap<>();
    List<Stone> stones = new ArrayList<>();

    public static void main(String[] args) throws IOException {

        var d = new Day11();

        var input = d.readInput();

        d.stopwatch.start();
        d.solvePart1(input);
        d.solvePart2(input);
        d.stopwatch.prettyPrint();
    }

    @Override
    public void solvePart1(final List<String> lines) {

        stones = Arrays.stream(lines.getFirst().split(" ")).map(Long::parseLong).map(Stone::new).collect(Collectors.toCollection(ArrayList::new));

        long sum = 0;
        for (final Stone stone : stones) {
            sum += blink(stone, 25);
        }

        System.out.println("Part 1: " + sum);
    }

    @Override
    public void solvePart2(final List<String> lines) {

        long sum = 0;
        for (final Stone stone : stones) {
            sum += blink(stone, 75);
        }

        System.out.println("Part 2: " + sum);
    }

    public Long blink(Stone stone, int blinksLeft) {
        var blinks = new Blinks(stone, blinksLeft);
        if (cache.containsKey(blinks)) {
//            System.out.println(" CACHE HIT!");
            return cache.get(blinks);
        }

        if (blinksLeft == 0) {
            return 1L;
        }

        Long result;
        if (stone.value() == 0) {
            result = blink(new Stone(1L), blinksLeft - 1);
        } else if (stone.isSplittable()) {
            String currentvalue = stone.value().toString();
            String leftValue = currentvalue.substring(0, currentvalue.length() / 2);
            String rightValue = currentvalue.substring(currentvalue.length() / 2).trim();

            result = blink(new Stone(Long.valueOf(leftValue)), blinksLeft - 1) + blink(new Stone(Long.valueOf(rightValue)), blinksLeft - 1);
        } else {
            result = blink(new Stone(stone.value() * 2024L), blinksLeft - 1);
        }

        cache.put(blinks, result);
        return result;
    }


}

record Stone(Long value) {

    boolean isSplittable() {
        return this.value.toString().length() % 2 == 0;
    }
}

record Blinks(Stone stone, int blinksLeft) {}


