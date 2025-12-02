package day02;

import config.Year2025;
import java.io.IOException;
import java.util.List;

public class Day02 extends Year2025 {

    public static void main(String[] args) throws IOException {
        var d = new Day02();

        var lines = d.readInput();

        d.solve(lines);
    }


    @Override
    public void solvePart1(final List<String> lines) {

        long total = 0;

        for (final String line : lines.get(0).split(",")) {

            var parts = line.split("-");
            var start = Long.parseLong(parts[0]);
            var end = Long.parseLong(parts[1]);

            for (long i = start; i <= end; i++) {

                var length = String.valueOf(i).length();

                if (length % 2 != 0) {
                    continue; // uneven, skip
                }

                var leftPart = String.valueOf(i).substring(0, length / 2);
                var rightPart = String.valueOf(i).substring(length / 2);

                if (leftPart.equals(rightPart)) {
                    System.out.println("Mirror: " + i);
                    total += i;
                }
            }

        }


        System.out.println("Part 1: " + total);
    }

    @Override
    public void solvePart2(final List<String> lines) {
        long total = 0;

        for (final String line : lines.get(0).split(",")) {

            var parts = line.split("-");
            var start = Long.parseLong(parts[0]);
            var end = Long.parseLong(parts[1]);

            id:
            for (long i = start; i <= end; i++) {

                var totalLength = String.valueOf(i).length();


                part: for (int subLength = 1; subLength <= totalLength / 2; subLength++) {

                    if(totalLength % subLength != 0) continue; // can't evenly divide

                    var leftPart = String.valueOf(i).substring(0, subLength);
                    var remainder = String.valueOf(i).substring(subLength);

                    var partAmount = remainder.length() / subLength;

                    for(int partIndex = 0; partIndex < partAmount; partIndex++) {
                        var nextPart = remainder.substring(partIndex * subLength, (partIndex + 1) * subLength);
                        if(!nextPart.equals(leftPart)) {
                            continue part; // not all parts are the same
                        }
                    }

                    System.out.println("Mirror: " + i);
                    total += i;
                    continue id;
                }
            }
        }

        System.out.println("Part 2: " + total);
    }
}
