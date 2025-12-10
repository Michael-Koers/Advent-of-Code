package day09;

import config.Year2025;
import util.Point;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Day09 extends Year2025 {

    public static void main(String[] args) throws IOException {
        var d = new Day09();

        var lines = d.readInput();

        d.warmup(lines, 1);
        d.solve(lines);
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
//                    System.out.printf("Between %s and %s: %s%n", t1, t2, largestArea);
                }

            }
        }

        System.out.println("Part 1: " + largestArea);
    }

    @Override
    public void solvePart2(final List<String> lines) {

        List<Point> corners = new ArrayList<>();
        List<Point> tiles = new ArrayList<>();
        List<Area> areas = new ArrayList<>();

        for (final String line : lines) {
            var parts = line.split(",");

            Point tile = new Point(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
            corners.add(tile);
        }


        for (int i = 0; i < corners.size(); i++) {
            tiles.addAll(getTilesBetween(corners.get(i), corners.get((i + 1) % corners.size())));

            for (int j = i + 1; j < corners.size(); j++) {
                areas.add(new Area(corners.get(i), corners.get(j)));
            }
        }

        // Sort areas from largest to smallest
        areas.sort(Comparator.comparingLong(Area::value).reversed());

        long result = 0L;
        for (Area area : areas) {
            if (validArea(area, tiles)) {
                result = area.value();
                break;
            }
        }

        System.out.println("Part 2: " + result);

    }

    private boolean validArea(Area area, List<Point> tiles) {
        long minX = Math.min(area.p1().x(), area.p2().x());
        long maxX = Math.max(area.p1().x(), area.p2().x());
        long minY = Math.min(area.p1().y(), area.p2().y());
        long maxY = Math.max(area.p1().y(), area.p2().y());

        for (Point tile : tiles) {
            // Check if any tiles crosses our value
            // If tiles X/Y is between value's min and max, it's inside the value
            if (minX < tile.x() && maxX > tile.x() && minY < tile.y() && maxY > tile.y()) {
                return false;
            }
        }
        return true;
    }

    private List<Point> getTilesBetween(Point p1, Point p2) {
        List<Point> points = new ArrayList<>();

        if (p1.x() == p2.x()) {
            for (long y = Math.min(p1.y(), p2.y()); y < Math.max(p1.y(), p2.y()); y++) {
                points.add(new Point(p1.x(), y));
            }
        } else {
            for (long x = Math.min(p1.x(), p2.x()); x < Math.max(p1.x(), p2.x()); x++) {
                points.add(new Point(x, p1.y()));
            }
        }

        return points;
    }


}

record Area(Point p1, Point p2, long value) {
    Area(Point p1, Point p2) {
        this(p1, p2, (Math.abs(p1.x() - p2.x()) + 1L) * (Math.abs(p1.y() - p2.y()) + 1L));
    }
}
