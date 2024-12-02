package day02;

import util.FileInput;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws URISyntaxException, IOException {

        List<String> lines = FileInput.read(FileInput.INPUT, Main.class);

        List<Box> boxes = parseInput(lines);

        solvePart1And2(boxes);
    }

    private static void solvePart1And2(List<Box> boxes) {

        long wrapping = 0L;
        long ribbon = 0L;

        for (Box box : boxes) {

            long surfaceA = (long) box.length() * box.width();
            long surfaceB = (long) box.width() * box.height();
            long surfaceC = (long) box.height() * box.length();

            // Calculate wrapping paper
            long extra = Math.min(Math.min(surfaceA, surfaceB), surfaceC);
            wrapping += 2 * surfaceA + 2 * surfaceB + 2 * surfaceC + extra;

            // Calculate ribbon
            long maxSide = Math.max(Math.max(box.length(), box.height()), box.width());
            long sides = 2L * (box.length() + box.width() + box.height()) - 2L * (maxSide);
            ribbon += sides + ((long) box.height() * box.length() * box.width());

        }

        System.out.printf("Solved part 1, wrapping paper: %s, ribbon: %s%n", wrapping, ribbon);
    }

    private static List<Box> parseInput(List<String> lines) {
        List<Box> boxes = new ArrayList<>();
        for (String line : lines) {
            String[] s = line.split("x");
            boxes.add(new Box(Integer.parseInt(s[0]), Integer.parseInt(s[1]), Integer.parseInt(s[2])));
        }
        return boxes;
    }
}


record Box(int length, int width, int height) {
}