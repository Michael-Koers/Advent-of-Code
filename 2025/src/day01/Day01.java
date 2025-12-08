package day01;

import config.Year2025;
import java.io.IOException;
import java.util.List;

public class Day01 extends Year2025 {

    public static void main(String[] args) throws IOException {
        var d = new Day01();
        var lines = d.readInput();

//        d.warmup(lines, 1);
        d.solve(lines);
    }

    @Override
    public void solvePart1(final List<String> lines) {

        int start = 50;
        int max = 100;
        int count = 0;

//        System.out.println(start);

        for (final String line : lines) {
            var amount = Integer.parseInt(line.substring(1));
            var dir = line.charAt(0);

            if (dir == 'L') {
                start -= amount;
            } else if (dir == 'R') {
                start += amount;
            }
            start = ((start % max) + max) % max;
            if (start == 0) {count++;}

//            System.out.println(start);

        }

        System.out.println("Part 1: " + count);

    }

    @Override
    public void solvePart2(final List<String> lines) {

        int pointer = 50;
        int max = 100;
        int count = 0;


        for (final String line : lines) {
            var amount = Integer.parseInt(line.substring(1));
            var dir = line.charAt(0);

            for(int i = 0; i < amount; i++) {

                if(dir == 'L') {
                    pointer--;
                } else if (dir == 'R') {
                    pointer++;
                }

                if(pointer < 0) {
                    pointer = max;
                }
                if(pointer > max){
                    pointer = 0;
                }

                if(pointer == 0) {count++;}
            }
        }

        System.out.println("Part 2: " + count);

    }
}
