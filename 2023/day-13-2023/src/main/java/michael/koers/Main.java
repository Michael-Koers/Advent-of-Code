package michael.koers;

import util.FileInput;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws URISyntaxException, IOException {


        List<String> read = FileInput.read(FileInput.INPUT_TEST, Main.class);

        solvePart1(read);

    }

    private static void solvePart1(List<String> read) {

        List<String> temp = new ArrayList<>();
        List<Integer> hors = new ArrayList<>();
        int startIndex = 0;
        boolean found = false;
        outer:
        for (int i = 0; i < read.size(); i++) {

            String s = read.get(i);

            // Next field reached
            if (s.isEmpty()) {
                temp.clear();
                startIndex = i+2;
                found = false;
                continue;
            }
            // Continue to next field
            else if (found) {
                continue;
            }
            // If there is stuff to check
            else if (!temp.isEmpty()) {
                for (int d = 0; d < temp.size(); d++) {
                    // Horizontal check
                    // Make sure we don't go out of bounds
                    if (i - d - 1 < 0 || i - d > read.size()) {
                        System.out.println("Reached bounds, no horizontal line found");

                    }
                    // Execute check
                    else if (temp.get(startIndex + i - d - 1  ).equals(read.get(i - d))) {
                        found = true;
                        hors.add(i);
                        System.out.printf("Horizontal line found at %s%n", i);
                        continue outer;
                    }
                }
            }
            temp.add(s);


        }
    }
}

record Field(String[][] layout) {
}