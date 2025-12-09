package day09;

import config.Year2025;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import util.Point;

public class Day09 extends Year2025 {

    public static void main(String[] args) throws IOException {
        var d = new Day09();

        var lines = d.readTestInput();

        d.solvePart2(lines);
    }

    @Override
    public void solvePart1(final List<String> lines) {
        List<Point> tiles = new ArrayList<>();

        for (final String line : lines) {
            var parts = line.split(",");

            Point tile = new Point(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
            tiles.add(tile);
        }


        long largestArea = Long.MIN_VALUE;
        for (int i = 0; i < tiles.size(); i++) {

            var t1 = tiles.get(i);

            for (int j = i + 1; j < tiles.size(); j++) {

                var t2 = tiles.get(j);

                var x = Math.abs(t1.x() - t2.x()) + 1;
                var y = Math.abs(t1.y() - t2.y()) + 1;

                var area = x * y;
                if (area > largestArea) {
                    largestArea = area;
                    System.out.printf("Between %s and %s: %s%n", t1, t2, largestArea);
                }

            }
        }

        System.out.println("Part 1: " + largestArea);
    }

    @Override
    public void solvePart2(final List<String> lines) {

        List<Point> corners = new ArrayList<>();
        Set<Point> borders = new HashSet<>();

        for (final String line : lines) {
            var parts = line.split(",");

            Point tile = new Point(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
            corners.add(tile);
        }

        for (int i = 0; i < corners.size(); i++) {
            final Point corner = corners.get(i);
            final Point nextCorner = corners.get(i+1%corners.size()-1);

            if(corner.x() != nextCorner.x()){
                
            }
        }

        long largestArea = Long.MIN_VALUE;
        for (int i = 0; i < corners.size(); i++) {

            var t1 = corners.get(i);
            var t2 = corners.get((i + 2) % corners.size());


            var x = Math.abs(t1.x() - t2.x()) + 1;
            var y = Math.abs(t1.y() - t2.y()) + 1;

            var area = x * y;
            if (area > largestArea) {
                largestArea = area;
                System.out.printf("Between %s and %s: %s%n", t1, t2, largestArea);
            }

        }

        System.out.println("Part 1: " + largestArea);

    }
}
