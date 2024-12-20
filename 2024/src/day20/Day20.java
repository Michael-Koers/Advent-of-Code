package day20;

import config.Year2024;
import util.Direction;
import util.Point;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class Day20 extends Year2024 {

    // 'Caching' for part 2, don't re-calculate etc.
    List<TrackTile> trackTiles;
    Track track;

    public static void main(String[] args) throws IOException {
        var d = new Day20();

        var lines = d.readInput();

        d.solve(lines);
    }


    @Override
    public void solvePart1(List<String> lines) {

        track = parseTrack(lines);

        trackTiles = raceThroughTrack(track);
        List<Integer> cheats = findCheats(trackTiles, 2);

        System.out.println("Part 1: " + cheats.stream().filter(c -> c >= 100).toList().size());
    }

    private List<Integer> findCheats(List<TrackTile> trackTiles, int maxCheatDistance) {
        List<Integer> cheats = new ArrayList<>();

        for (TrackTile trackTile : trackTiles) {
            List<Integer> cheatable = trackTiles.stream()
                    // Don't cheat backwards
                    .filter(t -> t.picoseconds() > trackTile.picoseconds())
                    // Check if there are tracks within cheating distance by using Manhattan Distance
                    .filter(t -> t.point().manhattanDistance(trackTile.point()) > 1 && t.point().manhattanDistance(trackTile.point()) <= maxCheatDistance)
                    // Remember the save we can cheat (positions are irrelevant for the answer)
                    .map(t -> (int) (t.picoseconds() - trackTile.picoseconds() - t.point().manhattanDistance(trackTile.point())))
                    .toList();

            cheats.addAll(cheatable);
        }
        return cheats;
    }

    private List<TrackTile> raceThroughTrack(Track track) {
        List<TrackTile> trackTiles = new ArrayList<>();
        trackTiles.add(new TrackTile(track.start(), 0));

        var direction = determineDirection(track, track.start().getNeighbours()
                .stream()
                .filter(p -> !track.walls().contains(p))
                .findFirst().get());

        Queue<Point> path = new ArrayDeque<>();
        path.add(track.start());

        int picoseconds = 0;

        outer:
        while (!path.isEmpty()) {

            picoseconds++;
            var current = path.poll();

            for (Direction d : List.of(direction, direction.turnLeft(), direction.turnRight())) {

                var next = current.moveDirection(d);

                // Don't run into walls (not yet)
                if (track.walls().contains(next)) continue;

                // Reached end
                if (track.end().equals(next)) {
                    trackTiles.add(new TrackTile(next, picoseconds));
                    break outer;
                }

                direction = d;
                trackTiles.add(new TrackTile(next, picoseconds));
                path.add(next);
            }

        }

        System.out.println("Track length: " + trackTiles.stream().filter(t -> t.point().equals(track.end())).findFirst().get().picoseconds());
        return trackTiles;
    }

    private Direction determineDirection(Track track, Point next) {
        Direction startingDirection;
        if (next.y() == track.start().y()) {
            if (next.x() < track.start().x()) {
                startingDirection = Direction.LEFT;
            } else {
                startingDirection = Direction.RIGHT;
            }
        } else {
            if (next.y() < track.start().y()) {
                startingDirection = Direction.UP;
            } else {
                startingDirection = Direction.DOWN;
            }
        }

        return startingDirection;
    }

    private Track parseTrack(List<String> lines) {
        Point start = null;
        Point end = null;
        List<Point> walls = new ArrayList<>();

        for (int y = 0; y < lines.size(); y++) {

            var line = lines.get(y).split("");

            for (int x = 0; x < line.length; x++) {

                var tile = line[x];

                if (tile.equals("#")) {
                    walls.add(new Point(x, y));
                } else if (tile.equals("S")) {
                    start = new Point(x, y);
                } else if (tile.equals("E")) {
                    end = new Point(x, y);
                }
            }
        }

        return new Track(start, end, walls);
    }

    @Override
    public void solvePart2(List<String> lines) {
        List<Integer> bigCheats = findCheats(trackTiles, 20);
        System.out.println("Part 2: " + bigCheats.stream().filter(c -> c >= 100).toList().size());
    }
}

record Track(Point start, Point end, List<Point> walls) {
}

record TrackTile(Point point, int picoseconds) {
}
