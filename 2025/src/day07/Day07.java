package day07;

import config.Year2025;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import util.Direction;
import util.Point;

public class Day07 extends Year2025 {

    public static void main(String[] args) throws IOException {
        var d = new Day07();
        var lines = d.readInput();

        d.warmup(lines, 2);
        d.solve(lines);
    }

    @Override
    public void solvePart1(final List<String> lines) {

        Set<Point> splitters = new HashSet<>();
        Queue<Point> streams = new ArrayDeque<>();

        for (int y = 0; y < lines.size(); y++) {
            var line = lines.get(y).split("");

            for (int x = 0; x < line.length; x++) {
                if (line[x].equals("S")) {
                    streams.add(new Point(x, y));
                } else if (line[x].equals("^")) {
                    splitters.add(new Point(x, y));
                }
            }
        }

        int maxHeight = splitters.stream().mapToInt(p -> (int) p.y()).max().getAsInt() + 1;

        Set<Point> visited = new HashSet<>();
        long part1 = 0L;

        while (!streams.isEmpty()) {

            var current = streams.remove();

            if (visited.contains(current)) {continue;}
            visited.add(current);

            var down = current.moveDirection(Direction.DOWN);

            if (splitters.contains(down)) {

                var leftDown = current.moveDirection(Direction.LEFT_DOWN);
                var rightDown = current.moveDirection(Direction.RIGHT_DOWN);

                streams.add(leftDown);
                streams.add(rightDown);

                part1++;
                continue;
            }

            if (down.y() < maxHeight) {
                streams.add(down);
            }
        }

        System.out.println("Part 1: " + part1);
    }

    @Override
    public void solvePart2(final List<String> lines) {
        Set<Point> splitters = new HashSet<>();
        Queue<Point> streams = new ArrayDeque<>();

        for (int y = 0; y < lines.size(); y++) {
            var line = lines.get(y).split("");

            for (int x = 0; x < line.length; x++) {
                if (line[x].equals("S")) {
                    streams.add(new Point(x, y));
                } else if (line[x].equals("^")) {
                    splitters.add(new Point(x, y));
                }
            }
        }

        int maxHeight = splitters.stream().mapToInt(p -> (int) p.y()).max().getAsInt() + 1;

        Map<Point, Long> streamCount = new HashMap<>();
        streamCount.put(streams.peek(), 1L);

        while (streams.stream().anyMatch(p -> p.y() < maxHeight)) {

            var current = streams.remove();
            var down = current.moveDirection(Direction.DOWN);

            // Reached bottom
            if (down.y() >= maxHeight) {
                streamCount.put(down, streamCount.get(current));
                continue;
            }

            // Split the stream
            if (splitters.contains(down)) {

                var leftDown = current.moveDirection(Direction.LEFT_DOWN);
                var rightDown = current.moveDirection(Direction.RIGHT_DOWN);

                if (!streamCount.containsKey(leftDown)) {
                    streamCount.put(leftDown, 0L);
                    streams.add(leftDown);
                }

                streamCount.put(leftDown, streamCount.get(current) + streamCount.get(leftDown));

                if (!streamCount.containsKey(rightDown)) {
                    streamCount.putIfAbsent(rightDown, 0L);
                    streams.add(rightDown);
                }

                streamCount.put(rightDown, streamCount.get(current) + streamCount.get(rightDown));

                continue;
            }

            // simply move down
            if (!streamCount.containsKey(down)) { // This means we are reaching this point for the first time
                streamCount.put(down, 0L);
                streams.add(down);  // So we want to continue traversing this path
            }

            streamCount.put(down, streamCount.get(current) + streamCount.get(down));

        }

        var total = streamCount.entrySet().stream()
                .filter(e -> e.getKey().y() >= maxHeight)
                .mapToLong(Map.Entry::getValue)
                .sum();
        System.out.println("Part 2: " + total);
    }
}
