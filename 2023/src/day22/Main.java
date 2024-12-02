package day22;

import util.FileInput;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws URISyntaxException, IOException {

        List<String> lines = FileInput.read(FileInput.INPUT, Main.class);

        List<Integer[]> bricks = parseInput(lines);

        solvePart1AndPart2(bricks);
    }

    private static void solvePart1AndPart2(List<Integer[]> bricks) {
        // Array is x1,y1,z1, x2, y2, z2
        // Remember, z = vertical height, not y
        // Sort our bricks by height first
        bricks.sort(Comparator.comparing(b -> b[2]));

        // Now drop down the bricks
        // If they overlapse in x,y, z of brick above is other brick + 1

        for (int i = 0; i < bricks.size(); i++) {
            int max_z = 1;
            // Get current brick and its index
            Integer[] brick = bricks.get(i);
            int index = bricks.indexOf(brick);

            // Sorted all bricks by height, and we only need to check bricks below us
            for (int j = 0; j < index; j++) {
                if (overlapse(brick, bricks.get(j))) {
                    // Place this brick above brick below us, we use end z because that's highest z?
                    max_z = Math.max(max_z, bricks.get(j)[5] + 1);
                }
                // Calculate new Z position,
                brick[5] -= brick[2] - max_z; // Basically subtract drop height from current height
                brick[2] = max_z;
            }
        }

        // Now that everything has dropped, sort again from low to higher
        bricks.sort(Comparator.comparing(b -> b[2]));

        Map<Integer, List<Integer>> supportedBy = defaultMap(bricks.size());
        Map<Integer, List<Integer>> supportingOther = defaultMap(bricks.size());

        // Loop from bottom to top, if current and previous overlap (and max z diff of 1), current is supported by previous

        for (int i = 0; i < bricks.size(); i++) {

            Integer[] upperBrick = bricks.get(i);
            for (int j = 0; j < bricks.indexOf(upperBrick); j++) {
                Integer[] lowerBrick = bricks.get(j);
                // upperbrick[2] = lowest vertical position of brick on top
                // lowerbrick[5] = highest vertical position of brick on bottom
                if (overlapse(upperBrick, lowerBrick) && upperBrick[2] == lowerBrick[5] + 1) {
                    // Update list of bricks upper brick is being supported by
                    supportedBy.get(i).add(j);
                    // Update list of bricks lower brick is supporting
                    supportingOther.get(j).add(i);
                }
            }
        }
        long totalPart1 = 0L;

        // Loop over all bricks
        for (int i = 0; i < bricks.size(); i++) {
            // Stream over all bricks supported by this brick
            // if THEY get supported by more than 1 brick
            // we can disintegrate current brick
            if (supportingOther.get(i)
                    .stream()
                    .allMatch(other -> supportedBy.get(other).size() >= 2)) {
                totalPart1++;
            }
        }

        System.out.printf("Solved part 1, a total of %s bricks can be disintegrated%n", totalPart1);

        long totalPart2 = 0L;
        // Loop over each brick,
        for (int i = 0; i < bricks.size(); i++) {

            // Get all bricks only supported by this brick, because they will fall
            Deque<Integer> queue = supportingOther.get(i).stream()
                    .filter(b -> supportedBy.get(b).size() == 1)
                    .collect(Collectors.toCollection(LinkedList::new));

            // Remember all bricks that are falling in this chain reaction, because maybe a brick being upheld
            // by 2 or more bricks will eventually fall if everything below does fall
            Set<Integer> falling = new HashSet<>(queue);
            falling.add(i);

            // Start the chain reaction
            while (!queue.isEmpty()) {

                // Get next brick
                var j = queue.pollFirst();
                // Make copy so we can use .removeAll() without changing the original collection
                var copy = new ArrayList<>(supportingOther.get(j));
                // Get all bricks that aren't already falling
                copy.removeAll(falling);
                // Loop over each next brick
                for (Integer integer : copy) {
                    // If current brick is supported by bricks all contained in the falling set, this brick will also fall now
                    if (falling.containsAll(new HashSet<>(supportedBy.get(integer)))) {
                        queue.add(integer);
                        falling.add(integer);
                    }
                }
            }

            totalPart2 += falling.size() - 1;

        }
        System.out.printf("Solved part 2, a total of %s bricks would fall%n", totalPart2);

    }

    private static Map<Integer, List<Integer>> defaultMap(int size) {
        Map<Integer, List<Integer>> defaultMap = new HashMap<>();

        for (int i = 0; i < size; i++) {
            defaultMap.put(i, new ArrayList<>());
        }
        return defaultMap;
    }

    private static boolean overlapse(Integer[] brick, Integer[] other) {
        return Math.max(brick[0], other[0]) <= Math.min(brick[3], other[3])
                && Math.max(brick[1], other[1]) <= Math.min(brick[4], other[4]);
    }

    private static List<Integer[]> parseInput(List<String> lines) {
        List<Integer[]> bricks = new ArrayList<>();
        for (String line : lines) {
            bricks.add(Arrays.stream(line.replace("~", ",").split(","))
                    .map(Integer::parseInt)
                    .toArray(Integer[]::new));
        }
        return bricks;
    }
}