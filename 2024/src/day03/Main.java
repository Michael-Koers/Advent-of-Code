package day03;

import config.Year2024;

import java.io.IOException;
import java.util.List;

class Day03 extends Year2024 {
    public static void main(String[] args) throws IOException {
        Day03 d = new Day03();

        d.solvePart1(d.readInput());
        d.solvePart2(d.readTestInput());
    }

    @Override
    public void solvePart1(List<String> lines) {
        System.out.println(lines.getFirst());
    }

    @Override
    public void solvePart2(List<String> lines) {
        System.out.println(lines.getFirst());
    }
}



