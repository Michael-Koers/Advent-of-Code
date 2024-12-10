package day10;

import config.Year2024;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import util.Direction;
import util.Point;

public class Day10 extends Year2024 {

    public static void main(String[] args) throws IOException {

        var d = new Day10();

        var lines = d.readInput();

        d.stopwatch.start();
        d.solvePart1(lines);
//        d.solvePart2(lines);
        d.stopwatch.prettyPrint();
    }

    @Override
    public void solvePart1(final List<String> lines) {

        List<Point> trailheads = getTrailheads(lines);

        int part1 = 0;
        int part2 = 0;

        for (final Point trailhead : trailheads) {

            int nrOfPaths = 0;
            Set<Point> maxHeights = new HashSet<>();
            Queue<Point> paths = new ArrayDeque<>();
            paths.add(trailhead);

            while (!paths.isEmpty()) {

                Point current = paths.remove();

                for (Direction d : List.of(Direction.LEFT, Direction.DOWN, Direction.RIGHT, Direction.UP)) {

                    Point next = current.moveDirection(d);

                    // out of bounds
                    if (next.x() < 0 || next.y() < 0
                            || next.x() >= lines.getFirst().length() || next.y() >= lines.size()) {
                        continue;
                    }


                    var heightCurrent = getHeightOnPoint(current, lines);
                    var heightNext = getHeightOnPoint(next, lines);


                    if ((heightNext - heightCurrent) != 1) {continue;}

                    if (heightNext == 9) {
                        maxHeights.add(next);
                        nrOfPaths++;
                        continue;
                    }

                    paths.add(next);
                }
            }

            part1 += maxHeights.size();
            part2 += nrOfPaths;
        }

        System.out.println("Part 1: " + part1);
        System.out.println("Part 2: " + part2);
    }

    private List<Point> getTrailheads(final List<String> lines) {
        List<Point> trailheads = new ArrayList<>();

        for (int y = 0; y < lines.size(); y++) {
            var split = lines.get(y).split("");

            for (int x = 0; x < split.length; x++) {
                if (split[x].equals("0")) {
                    trailheads.add(new Point(x, y));
                }
            }
        }
        return trailheads;
    }

    private int getHeightOnPoint(Point point, List<String> lines) {
        return Integer.parseInt(String.valueOf(lines.get((int) point.y()).charAt((int) point.x())));

    }

    @Override
    public void solvePart2(final List<String> lines) {
        // Part 1 needed simple change for part 2 solution
    }
}
