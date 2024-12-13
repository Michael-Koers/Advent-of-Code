package day13;

import config.Year2024;
import util.Point;

import java.io.IOException;
import java.util.List;

public class Day13 extends Year2024 {

    public static void main(String[] args) throws IOException {
        var d = new Day13();
        d.solve(d.readInput());
    }

    @Override
    public void solvePart1(List<String> lines) {
        var tokens = calculateTokens(lines, 0);
        System.out.println("Part 1: " + tokens);
    }

    private long calculateTokens(List<String> lines, long offset) {
        long tokens = 0;

        for (int i = 0; i < lines.size(); i += 4) {

            var buttonA = getButton(lines.get(i));
            var buttonB = getButton(lines.get(i + 1));
            var prize = getPrize(lines.get(i + 2), offset);

            // Next 3 lines are stolen, it's been 10 years since I've had maths
            long d = buttonA.x() * buttonB.y() - buttonA.y() * buttonB.x();
            double a = (double) (prize.x() * buttonB.y() - prize.y() * buttonB.x()) / d;
            double b = (double) (prize.y() * buttonA.x() - prize.x() * buttonA.y()) / d;

            // Quick way to check if doubles are whole
            if (a % 1 == 0 && b % 1 == 0) {
                tokens += (long) ((a * 3) + b);
            }
        }

        return tokens;
    }

    private Point getButton(String line) {
        return new Point(Integer.parseInt(line.substring(line.indexOf("+"), line.indexOf("+") + 3))
                , Integer.parseInt(line.substring(line.lastIndexOf("+"), line.lastIndexOf("+") + 3)));
    }

    private Point getPrize(String line, long offset) {
        return new Point(Long.parseLong(line.substring(line.indexOf("=") + 1, line.indexOf(","))) + offset,
                Long.parseLong(line.substring(line.lastIndexOf("=") + 1)) + offset);
    }

    @Override
    public void solvePart2(List<String> lines) {
        var tokens = calculateTokens(lines, 10000000000000L);
        System.out.println("Part 2: " + tokens);
    }
}

