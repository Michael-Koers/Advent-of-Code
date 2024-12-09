package day09;

import config.Year2024;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Day09 extends Year2024 {

    public static void main(String[] args) throws IOException {
        var d = new Day09();

        d.solvePart1(d.readInput());
    }

    @Override
    public void solvePart1(final List<String> lines) {

        var split = lines.getFirst().split("");
        List<Block> uncompacted = new ArrayList<>();
        int id = 0;

        for (int i = 0; i < split.length; i++) {

            var character = Integer.parseInt(split[i]);

            if (i % 2 == 1) {
                for (int j = 0; j < character; j++) {
                    uncompacted.add(new Empty());
                }
            } else {
                for (int j = 0; j < character; j++) {
                    uncompacted.add(new File(id));
                }
                id++;
            }
        }

        // Print uncompacted
//        uncompacted.forEach(u -> {
//            if (u instanceof File f) {
//                System.out.print(f.id());
//            } else {
//                System.out.print(".");
//
//            }
//        });
//        System.out.println();

        // Compact
        for (int i = 0; i < uncompacted.size(); i++) {

            if (uncompacted.get(i) instanceof File) {continue;}

            var move = uncompacted.removeLast();
            while (move instanceof Empty) {
                move = uncompacted.removeLast();
            }

            uncompacted.set(i, move);
        }

        // Print compacted
//        uncompacted.forEach(u -> {
//            if (u instanceof File f) {
//                System.out.print(f.id());
//            } else {
//                System.out.print(".");
//
//            }
//        });
//        System.out.println();

        long sum = 0;

        for (int i = 0; i < uncompacted.size(); i++) {
            sum += (long) i * ((File) uncompacted.get(i)).id();
        }

        System.out.println("Part 1: " + sum);

    }

    @Override
    public void solvePart2(final List<String> lines) {

    }
}

interface Block {}

record Empty() implements Block {}

record File(int id) implements Block {}
