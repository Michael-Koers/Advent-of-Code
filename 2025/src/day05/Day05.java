package day05;

import config.Year2025;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import util.Range;

public class Day05 extends Year2025 {

    public static void main(String[] args) throws IOException {
        var d = new Day05();

        var lines = d.readInput();

        d.solve(lines);
    }

    @Override
    public void solvePart1(final List<String> lines) {

        var split = lines.indexOf("");

        var ranges = lines.stream()
                .limit(split)
                .map(l -> l.split("-"))
                .map(p -> new Range(Long.parseLong(p[0]), Long.parseLong(p[1])))
                .collect(Collectors.toSet());


        var ids = lines.stream()
                .skip(split+1)
                .map(Long::parseLong)
                .filter(id -> ranges.stream().anyMatch(r -> r.contains(id)))
                .collect(Collectors.toSet());



        System.out.println("Part 1: " + ids.size());

    }

    @Override
    public void solvePart2(final List<String> lines) {
        Set<Range> ranges = new HashSet<>();
        Set<Long> ids = new HashSet<>();

        boolean idParse = false;
        for (final String line : lines) {

            if (line.isBlank()) {
                idParse = true;
                continue;
            }

            if (!idParse) {
                var parts = line.split("-");
                var start = Long.parseLong(parts[0]);
                var end = Long.parseLong(parts[1]);

                var newRange = new Range(start, end);

                var overlappingRanges = ranges.stream()
                        .filter(r2 -> r2.overlap(newRange))
                        .collect(Collectors.toSet());

                var mergedRange = newRange.merge(overlappingRanges);

                // Remove before adding in case we didn't merge, otherwise we would be removing our newly added range
                ranges.removeAll(overlappingRanges);
                ranges.add(mergedRange);

            } else {
                ids.add(Long.parseLong(line));
            }
        }


        Long count = 0L;

        for (final Range range : ranges) {
            count += range.value();
        }

        System.out.println("Part 2: " + count);


    }
}
