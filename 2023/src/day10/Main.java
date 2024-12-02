package day10;

import util.FileInput;
import util.Point;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

public class Main {
    public static void main(String[] args) throws URISyntaxException, IOException {

        List<String> read = FileInput.read(FileInput.INPUT, Main.class);

        solvePart1(read);
        solvePart2(read);
    }

    private static void solvePart2(List<String> read) {

        Map<Point, List<Point>> points = new HashMap<>();
        Point start = null;
        String start_s = "";
        for (int y = 0; y < read.size(); y++)
            for (int x = 0; x < read.get(y).length(); x++) {

                String s = read.get(y).split("")[x];

                switch (s) {
                    case "|" -> points.put(new Point(x, y), List.of(new Point(x, y - 1), new Point(x, y + 1)));
                    case "-" -> points.put(new Point(x, y), List.of(new Point(x - 1, y), new Point(x + 1, y)));
                    case "L" -> points.put(new Point(x, y), List.of(new Point(x + 1, y), new Point(x, y - 1)));
                    case "J" -> points.put(new Point(x, y), List.of(new Point(x - 1, y), new Point(x, y - 1)));
                    case "7" -> points.put(new Point(x, y), List.of(new Point(x - 1, y), new Point(x, y + 1)));
                    case "F" -> points.put(new Point(x, y), List.of(new Point(x + 1, y), new Point(x, y + 1)));
                    case "S" -> {
                        start = new Point(x, y);
                        Point point1 = null;
                        Point point2 = null;
                        boolean top = false;
                        boolean left = false;
                        boolean bottom = false;
                        boolean right = false;

                        if ((y - 1 >= 0) && "\\|7F".contains(read.get(y - 1).split("")[x])) {
                            point1 = new Point(x, y - 1);
                            top = true;
                        }
                        if ((y + 1 < read.size()) && "\\|LJ".contains(read.get(y + 1).split("")[x])) {
                            if (point1 == null) {
                                point1 = new Point(x, y + 1);
                            } else {
                                point2 = new Point(x, y + 1);
                            }
                            bottom = true;
                        }
                        if ((x - 1 >= 0) && "-LF".contains(read.get(y).split("")[x - 1])) {
                            if (point1 == null) {
                                point1 = new Point(x - 1, y);
                            } else {
                                point2 = new Point(x - 1, y);
                            }
                            left = true;
                        }
                        if ((x + 1 < read.get(y).length()) && "-J7".contains(read.get(y).split("")[x + 1])) {
                            if (point1 == null) {
                                point1 = new Point(x + 1, y);
                            } else {
                                point2 = new Point(x + 1, y);
                            }
                            right = true;
                        }
                        if (top && left) start_s = "J";
                        else if (top && right) start_s = "L";
                        else if (top && bottom) start_s = "|";
                        else if (bottom && left) start_s = "7";
                        else if (bottom && right) start_s = "F";
                        else if (left && right) start_s = "-";
                        points.put(start, List.of(point1, point2));
                    }
                }
            }

        LinkedList<Point> queue = new LinkedList<>();
        queue.add(start);

        Set<Point> pipeGrid = new HashSet<>();

        while (!queue.isEmpty()) {
            Point current = queue.pop();
            for (Point adj : points.get(current)) {
                if (!pipeGrid.contains(adj)) {
                    pipeGrid.add(adj);
                    queue.add(adj);
                }
            }
        }

        String[][] map = new String[read.size()][read.get(0).length()];
        read.set((int) start.y(), read.get((int) start.y()).replace("S", start_s));
        int tiles = 0;

        // Send out a horizontal ray
        for (int y = 0; y < read.size(); y++) {
            int hits = 0;
            for (int x = 0; x < read.get(y).length(); x++) {
                String c = read.get(y).split("")[x];

                if (pipeGrid.contains(new Point(x, y))) {
                    if ("LJ|".contains(c)) {
                        hits++;
                    }
                } else if (hits % 2 != 0) {
                    tiles++;
                }
            }
        }

        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                System.out.print(map[i][j]);
            }
            System.out.println();
        }
        System.out.printf("Solved part 2, number of enclosed tiles: %s%n", tiles);
    }

    private static void solvePart1(List<String> read) {

        Map<Point, List<Point>> points = new HashMap<>();
        Point start = null;

        for (int y = 0; y < read.size(); y++)
            for (int x = 0; x < read.get(y).length(); x++) {

                String s = read.get(y).split("")[x];

                switch (s) {
                    case "|" -> points.put(new Point(x, y), List.of(new Point(x, y - 1), new Point(x, y + 1)));
                    case "-" -> points.put(new Point(x, y), List.of(new Point(x - 1, y), new Point(x + 1, y)));
                    case "L" -> points.put(new Point(x, y), List.of(new Point(x + 1, y), new Point(x, y - 1)));
                    case "J" -> points.put(new Point(x, y), List.of(new Point(x - 1, y), new Point(x, y - 1)));
                    case "7" -> points.put(new Point(x, y), List.of(new Point(x - 1, y), new Point(x, y + 1)));
                    case "F" -> points.put(new Point(x, y), List.of(new Point(x + 1, y), new Point(x, y + 1)));
                    case "S" -> {
                        start = new Point(x, y);
                        Point point1 = null;
                        Point point2 = null;

                        if ((y - 1 >= 0) && "\\|7F".contains(read.get(y - 1).split("")[x])) {
                            point1 = new Point(x, y - 1);
                        }
                        if ((y + 1 < read.size()) && "\\|LJ".contains(read.get(y + 1).split("")[x])) {
                            if (point1 == null) {
                                point1 = new Point(x, y + 1);
                            } else {
                                point2 = new Point(x, y + 1);
                            }
                        }
                        if ((x - 1 >= 0) && "-LF".contains(read.get(y).split("")[x - 1])) {
                            if (point1 == null) {
                                point1 = new Point(x - 1, y);
                            } else {
                                point2 = new Point(x - 1, y);
                            }
                        }
                        if ((x + 1 < read.get(y).length()) && "-J7".contains(read.get(y).split("")[x + 1])) {
                            if (point1 == null) {
                                point1 = new Point(x + 1, y);
                            } else {
                                point2 = new Point(x + 1, y);
                            }
                        }
                        points.put(start, List.of(point1, point2));
                    }
                }
            }


        LinkedList<Point> queue = new LinkedList<>();
        queue.add(start);

        Map<Point, Integer> cost = new HashMap<>();
        cost.put(start, 0);

        while (!queue.isEmpty()) {
            Point current = queue.pop();
            for (Point adj : points.get(current)) {
                if (!cost.containsKey(adj)) {
                    cost.put(adj, cost.get(current) + 1);
                    queue.add(adj);
                }
            }
        }
        System.out.printf("Solved part 1, max number of steps: %s%n", cost.values().stream().mapToInt(Integer::intValue).max().getAsInt());
    }
}

record Pipe(String s, int x, int y) {
}