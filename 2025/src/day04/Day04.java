package day04;

import config.Year2025;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import util.Point;

public class Day04 extends Year2025 {

    public static void main(String[] args) throws IOException {
        var d = new Day04();

        var lines = d.readInput();

        d.solve(lines);
    }


    @Override
    public void solvePart1(final List<String> lines) {

        Set<Point> rolls = new HashSet<>();

        for (int y = 0; y < lines.size(); y++) {

            String[] horizontal = lines.get(y).split("");

            for (int x = 0; x < horizontal.length; x++) {

                if (horizontal[x].equals("@")) {
                    rolls.add(new Point(x, y));
                }
            }

        }

        int count = 0;
        for (Point roll : rolls) {
            var neighbours = new ArrayList<>(roll.getAllNeighbours());

            neighbours.retainAll(rolls);

            if (neighbours.size() < 4) {
                count++;
            }
        }

        System.out.println("Part 1: " + count);

    }

    @Override
    public void solvePart2(final List<String> lines) {

        Set<Point> rolls = new HashSet<>();

        for (int y = 0; y < lines.size(); y++) {

            String[] horizontal = lines.get(y).split("");

            for (int x = 0; x < horizontal.length; x++) {

                if (horizontal[x].equals("@")) {
                    rolls.add(new Point(x, y));
                }
            }

        }

        int total = 0;
        Set<Point> remove;

        do {
            remove = new HashSet<>();
            for (Point roll : rolls) {
                var neighbours = new ArrayList<>(roll.getAllNeighbours());

                neighbours.retainAll(rolls);

                if (neighbours.size() < 4) {
                    remove.add(roll);
                }
            }
            rolls.removeAll(remove);
            total += remove.size();
        } while (!remove.isEmpty());


        System.out.println("Part 2: " + total);
    }
}
