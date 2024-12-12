package day12;

import config.Year2024;
import util.Direction;
import util.Point;

import java.io.IOException;
import java.util.*;

public class Day12 extends Year2024 {

    public static void main(String[] args) throws IOException {

        var d = new Day12();
        d.solvePart2(d.readTestInput());


    }

    @Override
    public void solvePart1(final List<String> lines) {

        var areas = determineAreas(lines);

        long totalPrice = 0;
        for (Area area : areas) {

            int totalFences = 0;

            for (final Point plant : area.plants()) {
                int fences = getFences(plant, area.plants());
                totalFences += fences;
            }

            var areaPrice = totalFences * area.plants().size();
            totalPrice += areaPrice;
        }

        System.out.println("Part 1: " + totalPrice);
    }

    private List<Area> determineAreas(List<String> lines) {
        Set<Plant> plants = new HashSet<>();
        for (int y = 0; y < lines.size(); y++) {
            var split = lines.get(y).split("");

            for (int x = 0; x < split.length; x++) {

                var plant = split[x];
                var newPoint = new Point(x, y);
                plants.add(new Plant(plant, newPoint));

            }
        }

        var areas = floodFillAreas(plants);
        return areas;
    }

    private List<Area> floodFillAreas(Set<Plant> plants) {

        List<Area> areas = new ArrayList<>();
        for (Plant plant : plants) {

            // Skip points already found
            if (areas.stream().anyMatch(a -> a.plants().contains(plant.point()))) continue;

            var start = plant.point();
            var label = plant.label();

            Queue<Point> toVisit = new ArrayDeque<>();
            toVisit.add(start);

            List<Point> area = new ArrayList<>();
            area.add(start);

            while (!toVisit.isEmpty()) {
                var current = toVisit.poll();
                for (Direction direction : List.of(Direction.UP, Direction.LEFT, Direction.RIGHT, Direction.DOWN)) {

                    var next = current.moveDirection(direction);

                    // Dont visit invalid locations
                    if (!plants.contains(new Plant(label, next))) continue;

                    // Dont go backwards
                    if (area.contains(next)) continue;

                    area.add(next);
                    toVisit.add(next);
                }
            }

            areas.add(new Area(label, area));
        }
        return areas;
    }


    private static int getFences(final Point plant, final List<Point> plants) {
        int fences = 4;

        for (final Point plant2 : plants) {
            if (plant == plant2) {
                continue;
            }
            if (!plant.isAdjacent(plant2)) {
                continue;
            }

            fences--;
        }
        return fences;
    }

    @Override
    public void solvePart2(final List<String> lines) {
        var areas = determineAreas(lines);

        long totalPrice = 0;
        for (Area area : areas) {

            int minX = (int) area.plants().stream().mapToLong(Point::x).min().getAsLong()-1;
            int maxX = (int) area.plants().stream().mapToLong(Point::x).max().getAsLong()+1;

            int minY = (int) area.plants().stream().mapToLong(Point::y).min().getAsLong()-1;
            int maxY = (int) area.plants().stream().mapToLong(Point::y).max().getAsLong()+1;

            boolean inside = false;
            int sides = 0;

            for (int y = minY; y <= maxY; y++) {
                for (int x = minX; x <= maxX; x++) {
                    if (area.plants().contains(new Point(x, y))) {
                        if (inside) continue;

                        inside = true;
                        sides++;
                    } else {
                        if (inside) sides++;
                        inside = false;
                    }
                }
            }

            inside = false;

            for (int x = minX; x <= maxX; x++) {
                for (int y = minY; y <= maxY; y++) {
                    if (area.plants().contains(new Point(x, y))) {
                        if (inside) continue;

                        inside = true;
                        sides++;
                    } else {
                        if (inside) sides++;
                        inside = false;
                    }
                }
            }

            System.out.printf("Area %s has %s sides %n", area.label(), sides);
        }
    }
}

record Plant(String label, Point point) {
}

record Area(String label, List<Point> plants) {

    boolean isInside(Point point) {
        return plants.contains(point);
    }
}
