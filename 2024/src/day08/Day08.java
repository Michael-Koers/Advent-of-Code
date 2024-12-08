package day08;

import config.Year2024;
import util.Point;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day08 extends Year2024 {

    public static void main(String[] args) throws IOException {

        var d = new Day08();

        d.stopwatch.start();
        d.solvePart1(d.readInput());
        d.solvePart2(d.readInput());
        d.stopwatch.prettyPrint();
    }

    @Override
    public void solvePart1(List<String> lines) {
        List<Antenna> antennas = getAntennas(lines);
        Set<Point> antinodes = new HashSet<>();

        for (Antenna current : antennas) {
            for (Antenna other : antennas) {

                if (current == other) continue;
                if (!current.freq().equals(other.freq())) continue;

                int xOffsetCurrent = (int) (current.position().x() - other.position().x());
                int yOffsetCurrent = (int) (current.position().y() - other.position().y());

                int xOffsetOther = (int) (other.position().x() - current.position().x());
                int yOffsetOther = (int) (other.position().y() - current.position().y());

                var antinode1 = new Point(current.position().x() + xOffsetCurrent, current.position().y() + yOffsetCurrent);
                var antinode2 = new Point(other.position().x() + xOffsetOther, other.position().y() + yOffsetOther);

                // Within area/bounds check
                if (antinode1.x() >= 0 && antinode1.x() < lines.getFirst().length()
                        && antinode1.y() >= 0 && antinode1.y() < lines.size()) {
                    antinodes.add(antinode1);
                }

                if (antinode2.x() >= 0 && antinode2.x() < lines.getFirst().length()
                        && antinode2.y() >= 0 && antinode2.y() < lines.size()) {
                    antinodes.add(antinode2);
                }
            }
        }

        System.out.println("Anti nodes found: " + antinodes.size());
    }

    @Override
    public void solvePart2(List<String> lines) {
        List<Antenna> antennas = getAntennas(lines);
        Set<Point> antinodes = new HashSet<>();

        for (Antenna current : antennas) {
            for (Antenna other : antennas) {

                if (current == other) continue;
                if (!current.freq().equals(other.freq())) continue;

                // Same antennas, so they create a pattern, which always repeats on their own position
                // so their own position always contains a anti-node
                antinodes.add(current.position());
                antinodes.add(other.position());

                int xOffsetCurrent = (int) (current.position().x() - other.position().x());
                int yOffsetCurrent = (int) (current.position().y() - other.position().y());

                int xOffsetOther = (int) (other.position().x() - current.position().x());
                int yOffsetOther = (int) (other.position().y() - current.position().y());

                var accruingXOffsetCurrent = 0;
                var accruingYOffsetCurrent = 0;
                var accruingXOffsetOther = 0;
                var accruingYOffsetOther = 0;

                // Quick dirty hack to check if anti-nodes are still being added
                // , and so we haven't gone out of bounds on both sides yet
                boolean keepAdding = true;

                while (keepAdding) {

                    keepAdding = false;
                    accruingXOffsetCurrent += xOffsetCurrent;
                    accruingYOffsetCurrent += yOffsetCurrent;
                    accruingXOffsetOther += xOffsetOther;
                    accruingYOffsetOther += yOffsetOther;

                    var antinode1 = new Point(current.position().x() + accruingXOffsetCurrent, current.position().y() + accruingYOffsetCurrent);
                    var antinode2 = new Point(other.position().x() + accruingXOffsetOther, other.position().y() + accruingYOffsetOther);

                    // Within area/bounds check
                    if (antinode1.x() >= 0 && antinode1.x() < lines.getFirst().length()
                            && antinode1.y() >= 0 && antinode1.y() < lines.size()) {
                        antinodes.add(antinode1);
                        keepAdding = true;
                    }

                    if (antinode2.x() >= 0 && antinode2.x() < lines.getFirst().length()
                            && antinode2.y() >= 0 && antinode2.y() < lines.size()) {
                        antinodes.add(antinode2);
                        keepAdding = true;
                    }
                }


            }
        }

        System.out.println("Anti nodes found: " + antinodes.size());

    }

    private List<Antenna> getAntennas(List<String> lines) {

        List<Antenna> list = new ArrayList<>();

        for (int y = 0; y < lines.size(); y++) {
            var line = lines.get(y).split("");

            for (int x = 0; x < line.length; x++) {

                var dot = line[x];

                if (dot.equals(".")) continue;

                list.add(new Antenna(dot, new Point(x, y)));
            }
        }
        return list;
    }

}

record Antenna(String freq, Point position) {
};
