package day12;

import config.Year2024;
import util.Direction;
import util.Point;

import java.io.IOException;
import java.util.*;

public class Day12 extends Year2024 {

    public static void main(String[] args) throws IOException {
        var d = new Day12();
        d.solvePart2(d.readInput());
    }

    @Override
    public void solvePart1(final List<String> lines) {

        var areas = determineAreas(lines);

        long totalPrice = 0;
        for (Area area : areas) {

            int totalFences = 0;

            for (final Plant plant : area.plants()) {
                int fences = getFences(plant.point(), area.plants().stream().map(Plant::point).toList());
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

        return floodFillAreas(plants);
    }

    private List<Area> floodFillAreas(Set<Plant> plants) {

        List<Area> areas = new ArrayList<>();
        for (Plant plant : plants) {

            // Skip points already found
            if (areas.stream().anyMatch(a -> a.plants().stream().map(Plant::point).toList().contains(plant.point())))
                continue;

            var start = plant.point();
            var label = plant.label();

            Queue<Point> toVisit = new ArrayDeque<>();
            toVisit.add(start);

            List<Plant> area = new ArrayList<>();
            area.add(plant);

            while (!toVisit.isEmpty()) {
                var current = toVisit.poll();
                for (Direction direction : List.of(Direction.UP, Direction.LEFT, Direction.RIGHT, Direction.DOWN)) {

                    var next = current.moveDirection(direction);
                    var nextPlant = new Plant(label, next);
                    // Dont visit invalid locations
                    if (!plants.contains(nextPlant)) continue;

                    // Dont go backwards
                    if (area.contains(nextPlant)) continue;

                    area.add(nextPlant);
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

        System.out.printf("Part 2: %s%n",
                areas.stream().mapToLong(Area::price).sum());
    }
}

record Plant(String label, Point point) implements Comparable<Plant> {
    Collection<Plant> edges() {
        return Set.of(
                new Plant(this.label, new Point(point.x(), point.y())),
                new Plant(this.label, new Point(point.x(), point.y() + 1)),
                new Plant(this.label, new Point(point.x() + 1, point.y())),
                new Plant(this.label, new Point(point.x() + 1, point.y() + 1))
        );
    }

    @Override
    public int compareTo(Plant o) {
        return this.point.y() == o.point.y() ? Long.compare(this.point.x(), o.point.x()) : Long.compare(this.point.y(), o.point.y());
    }
}

record Area(String label, List<Plant> plants) {

    boolean isInside(Point point) {
        return plants.stream().map(Plant::point).toList().contains(point);
    }

    int area() {
        return plants.size();
    }

    long edges() {
        Map<Plant, SortedSet<Plant>> edges = new HashMap<>();
        for (Plant plant : plants) {
            for (Plant edge : plant.edges()) {
                edges.computeIfAbsent(edge, c -> new TreeSet<>());
                edges.get(edge).add(plant);
            }

        }
        long hidden = 2 * edges.values().stream().filter(s -> s.size() == 2 && !s.first().point().isAdjacent(s.last().point())).count();
        return edges.values().stream().filter(s -> (s.size() & 1) == 1).count() + hidden;
    }

    long price() {
        return area() * edges();
    }
}
