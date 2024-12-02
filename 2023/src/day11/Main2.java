package day11;

import util.FileInput;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class Main2 {
    public static void main(String[] args) throws URISyntaxException, IOException {

        List<String> lines = FileInput.read("input-stefan.txt", Main.class);

        List<Point> map = parseInput(lines, 1L);
        solvePart1(map);

        map = parseInput(lines, 1_000_000L - 1L);
        solvePart1(map);
    }

    private static List<Point> parseInput(List<String> lines, long expansion) {

        List<Point> galaxies = new ArrayList<>();

        List<Integer> emptyVerts = new ArrayList<>();
        List<Integer> emptyHors = new ArrayList<>();

        for (int y = 0; y < lines.size(); y++) {
            if (!lines.get(y).contains("#")) {
                emptyHors.add(y);
            }
        }

        col:
        for (int x = 0; x < lines.get(0).length(); x++) {
            for (int y = 0; y < lines.size(); y++) {
                if (lines.get(y).charAt(x) == '#') {
                    continue col;
                }
            }
            emptyVerts.add(x);
        }

        System.out.printf("Empty horizontal lines: %s%n", emptyHors);
        System.out.printf("Empty vertical lines: %s%n", emptyVerts);

        long padding_y = 0L;

        row:
        for (int y = 0; y < lines.size(); y++) {
            if (emptyHors.contains(y)) {
                padding_y += expansion;
                continue row;
            }

            long padding_x = 0L;

            col:
            for (int x = 0; x < lines.get(y).length(); x++) {
                if (emptyVerts.contains(x)) {
                    padding_x += expansion;
                    continue col;
                }

                if (lines.get(y).charAt(x) == '#') {
                    galaxies.add(new Point(y + padding_y, x + padding_x));
                }
            }
        }
        return galaxies;
    }

    private static void solvePart1(List<Point> galaxies) {
        long total = 0L;

        List<Point> others = new ArrayList<>(galaxies);
        for (Point galaxy_src : galaxies) {
            others.remove(galaxy_src);
            for (Point galaxy_dest : others) {
                if (galaxy_src == galaxy_dest) {
                    continue;
                }
                // Manhatten distance
                total += Math.abs(galaxy_dest.x() - galaxy_src.x()) + Math.abs(galaxy_dest.y() - galaxy_src.y());
            }
        }

        System.out.printf("Solved part 1, sum of shortest paths: %s%n", total);
    }


}


record Point(long y, long x) {
}