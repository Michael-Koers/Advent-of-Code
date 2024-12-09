package day04;

import config.Year2024;
import util.Direction;

import java.io.IOException;
import java.util.List;

public class Day04 extends Year2024 {
    public static void main(String[] args) throws IOException {
        var d = new Day04();

        var input = d.readInput();

        d.stopwatch.start();
        d.solvePart1(input);
        d.solvePart2(input);
        d.stopwatch.prettyPrint();
    }

    @Override
    public void solvePart1(List<String> lines) {

        int counter = 0;
        for (int y = 0; y < lines.size(); y++) {

            String[] horizontal = lines.get(y).split("");
            for (int x = 0; x < horizontal.length; x++) {

                // Only check possibilities when we are on an 'X'
                if (!horizontal[x].equals("X")) continue;

                for (Direction d : Direction.values()) {

                    String result = "X";

                    int newPosY = y;
                    int newPosX = x;

                    for (int t = 0; t < 3; t++) {
                        // 'Move' in a direction
                        newPosY += (int) d.movement.y();
                        newPosX += (int) d.movement.x();

                        // Prevent out of bounds
                        if (newPosY < 0 || newPosY > lines.size() - 1
                                || newPosX < 0 || newPosX > horizontal.length - 1) {
                            break;
                        }

                        var nextChar = String.valueOf(lines.get(newPosY).charAt(newPosX));

                        // Don't continue dead ends
                        if (!String.valueOf("XMAS".charAt(1 + t)).equals(nextChar)) break;

                        result = result.concat(String.valueOf(lines.get(newPosY).charAt(newPosX)));

                    }

                    if (result.equals("XMAS")) {
                        counter++;
                    }
                }
            }
        }

        System.out.println("XMAS appears " + counter + " times");
    }

    @Override
    public void solvePart2(List<String> lines) {
        int counter = 0;
        for (int y = 0; y < lines.size(); y++) {

            String[] horizontal = lines.get(y).split("");
            hor:
            for (int x = 0; x < horizontal.length; x++) {

                if (!horizontal[x].equals("A")) continue;

                String resultLeftDiagonal = "A";

                for (Direction d : List.of(Direction.LEFT_UP, Direction.RIGHT_DOWN)) {

                    int newPosY = (int) (y + d.movement.y());
                    int newPosX = (int) (x + d.movement.x());

                    // Prevent out of bounds
                    if (newPosY < 0 || newPosY > lines.size() - 1
                            || newPosX < 0 || newPosX > horizontal.length - 1) {
                        continue hor;
                    }

                    var nextChar = String.valueOf(lines.get(newPosY).charAt(newPosX));

                    // If 1 character doesn't match M/S from MAS, this 'A' is already completely invalid
                    if (!"MS".contains(nextChar)) continue hor;

                    resultLeftDiagonal = resultLeftDiagonal.concat(nextChar);

                }

                // Check left diagonal contains all the necessary characters, we don't have to check for order
                // We already now 'A' is in the middle, and order of M/S doesn't matter as long as all characters
                // are at least once present
                if (!resultLeftDiagonal.contains("M")
                        || !resultLeftDiagonal.contains("A")
                        || !resultLeftDiagonal.contains("S")) continue;

                String resultRightDiagonal = "A";

                for (Direction d : List.of(Direction.RIGHT_UP, Direction.LEFT_DOWN)) {

                    int newPosY = (int) (y + d.movement.y());
                    int newPosX = (int) (x + d.movement.x());

                    // Prevent out of bounds
                    if (newPosY < 0 || newPosY > lines.size() - 1
                            || newPosX < 0 || newPosX > horizontal.length - 1) {
                        continue hor;
                    }

                    var nextChar = String.valueOf(lines.get(newPosY).charAt(newPosX));

                    // If 1 character doesn't match M/S from MAS, this 'A' is already completely invalid
                    if (!"MS".contains(nextChar)) continue hor;

                    resultRightDiagonal = resultRightDiagonal.concat(nextChar);

                }

                if (!resultRightDiagonal.contains("M")
                        || !resultRightDiagonal.contains("A")
                        || !resultRightDiagonal.contains("S")) continue;

                // We have a valid X-MAS!
                counter++;
//                System.out.printf("Valid X-Mas: (y:%s, x:%s)%n", y, x);
            }
        }

        System.out.println("XMAS appears " + counter + " times");
    }
}
