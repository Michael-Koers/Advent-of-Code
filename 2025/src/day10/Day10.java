package day10;

import config.Year2025;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day10 extends Year2025 {

    public static void main(String[] args) throws IOException {
        var d = new Day10();

        var lines = d.readTestInput();

        d.solvePart1(lines);
    }

    @Override
    public void solvePart1(List<String> lines) {
        Pattern pattern = Pattern.compile("(\\[.*?]|\\(.*?\\)|\\{.*?})");


        for (String line : lines) {
            Matcher matcher = pattern.matcher(line);
            while (matcher.find()) {
                System.out.println(matcher.group());
            }
        }
    }

    @Override
    public void solvePart2(List<String> lines) {

    }
}
