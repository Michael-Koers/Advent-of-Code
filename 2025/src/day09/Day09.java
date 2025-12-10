package day09;

import config.Year2025;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import util.Direction;
import util.Point;

public class Day09 extends Year2025 {

    public static void main(String[] args) throws IOException {
        var d = new Day09();

        var lines = d.readInput();

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
        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;

        for (final String line : lines) {
            var parts = line.split(",");

            Point tile = new Point(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
            corners.add(tile);

            maxX = Math.max(maxX, (int) tile.x());
            minX = Math.min(minX, (int) tile.x());
            maxY = Math.max(maxY, (int) tile.y());
            minY = Math.min(minY, (int) tile.y());
        }

        for (int i = 0; i < corners.size(); i++) {
            final Point corner = corners.get(i);
            final Point nextCorner = corners.get((i + 1) % (corners.size()));

            if (corner.x() < nextCorner.x()) {
                for (long x = corner.x(); x < nextCorner.x() + 1; x++) {
                    borders.add(new Point(x, nextCorner.y()));
                }
            }

            if (nextCorner.x() < corner.x()) {
                for (long x = nextCorner.x(); x < corner.x() + 1; x++) {
                    borders.add(new Point(x, nextCorner.y()));
                }
            }

            if (corner.y() < nextCorner.y()) {
                for (long y = corner.y(); y < nextCorner.y() + 1; y++) {
                    borders.add(new Point(nextCorner.x(), y));
                }
            }

            if (nextCorner.y() < corner.y()) {
                for (long y = nextCorner.y(); y < corner.y() + 1; y++) {
                    borders.add(new Point(nextCorner.x(), y));
                }
            }
        }


        var start = corners.getFirst().moveDirection(Direction.RIGHT_DOWN); // Works for test input
        var bitmap = initEmptyBitmap(borders, minX, minY, maxX, maxY);
        // DFS THAT SHIT

        prettyPrint(bitmap);

        dfs(start, borders, bitmap);

        prettyPrint(bitmap);
        //


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

    private static boolean[][] initEmptyBitmap(Set<Point> borders, final int minX, final int minY, final int maxX, final int maxY) {
        var bitmap = new boolean[maxY + 2][maxX + 2];
        for (int x = minX-1; x <= maxX; x++) {
            for (int y = minY-1; y <= maxY; y++) {
                if (borders.contains(new Point(x, y))) {
                    bitmap[y][x] = true;
                } else {
                    bitmap[y][x] = false;
                }
            }
        }

        return bitmap;
    }

    private void dfs(final Point start, final Set<Point> borders, final boolean[][] bitmap) {

        bitmap[(int) start.y()][(int) start.x()] = true;

        for (final Point next : start.getDirectNeighbours()) {
            if (borders.contains(next)) {
                bitmap[(int) next.y()][(int) next.x()] = true;
                continue;
            }

            // If true -> already visisted
            if (bitmap[(int) next.y()][(int) next.x()]) {
                continue;
            }

            // Keep dfs'ing
            dfs(next, borders, bitmap);

        }


    }

    static void prettyPrint(boolean[][] map) {
        for (int y = 0; y < map.length - 1; y++) {
            for (int x = 0; x < map[y].length - 1; x++) {
                System.out.print(map[y][x] ? "#" : ".");
            }
            System.out.println("");
        }
        System.out.println("");
    }

    static void prettyPrint(Set<Point> points, int maxX, int maxY) {
        for (int y = 0; y <= maxY + 1; y++) {
            for (int x = 0; x <= maxX + 2; x++) {
                var p = new Point(x, y);
                System.out.print(points.contains(p) ? "#" : ".");
            }
            System.out.println("");
        }
        System.out.println("");
    }

}
