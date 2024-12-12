//package day12;
//
//import config.Year2024;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Optional;
//import util.Point;
//
//public class Day12 extends Year2024 {
//
//    List<Area> points = new ArrayList<>();
//
//    public static void main(String[] args) throws IOException {
//
//        var d = new Day12();
//        d.solve(d.readTestInput());
//
//
//    }
//
//    @Override
//    public void solvePart1(final List<String> lines) {
//
//        for (int y = 0; y < lines.size(); y++) {
//            var split = lines.get(y).split("");
//
//            for (int x = 0; x < split.length; x++) {
//
//                var plant = split[x];
//                var newPoint = new Point(x, y);
//
//                // If new plant, first create collection
//                if (points.stream().noneMatch(a -> a.label().equals(plant))) {
//                    points.add(new Area(plant, List.of(newPoint)));
//                    continue;
//                }
//
//                var sameTypePlants = points.stream().filter(p -> p.label().equals(plant)).toList();
//
//                // If existing, check if same area
//                final Optional<Area> first = sameTypePlants.stream()
//                        .filter(a -> a.points().stream().anyMatch(p -> p.isAdjacent(newPoint)))
//                        .findFirst();
//
//
//            }
//        }
//
//        System.out.println();
//
//        long totalPrice = 0;
//        for (final Map.Entry<String, List<Point>> plantType : points.entrySet()) {
//
//            var plants = plantType.getValue();
//
//            int totalFences = 0;
//
//            for (final Point plant : plants) {
//
//                int fences = getFences(plant, plants);
//
//                totalFences += fences;
//            }
//
//            var areaPrice = totalFences * plants.size();
//            System.out.printf("Area %s has total price %s%n", plantType.getKey(), areaPrice);
//            totalPrice += areaPrice;
//
//        }
//
//        System.out.println("Part 1: " + totalPrice);
//    }
//
//    private static int getFences(final Point plant, final List<Point> plants) {
//        int fences = 4;
//
//        for (final Point plant2 : plants) {
//            if (plant == plant2) {continue;}
//            if (!plant.isAdjacent(plant2)) {continue;}
//
//            fences--;
//        }
//        return fences;
//    }
//
//    @Override
//    public void solvePart2(final List<String> lines) {
//
//    }
//}
//
//record Area(String label, List<Point> points) {}
