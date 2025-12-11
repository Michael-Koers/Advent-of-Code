package day10;

import config.Year2025;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
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


        List<Machine> machines = new ArrayList<>();

        for (String line : lines) {

            Matcher matcher = pattern.matcher(line);
            matcher.find();

            var binary = matcher.group().replace("[", "").replace("]", "");
            BitSet destination = new BitSet(binary.length());

            for (int i = 0; i < binary.length(); i++) {
                if (binary.charAt(i) == '#') {
                    destination.set(i);
                }
            }

            List<BitSet> buttons = new ArrayList<>();

            while (matcher.find()) {
                var part = matcher.group();

                if (part.startsWith("{")) continue;

                part = part.replace("(", "").replace(")", "");
                var trimmed = Arrays.stream(part.split(",")).map(Integer::valueOf).toList();

                BitSet buttonsBitSet = new BitSet(binary.length());
                for (Integer button : trimmed) {
                    buttonsBitSet.set(button);
                }

                buttons.add(buttonsBitSet);
            }


            var machine = new Machine(new BitSet(binary.length()), destination, buttons);
            machines.add(machine);
        }

        long total = 0L;

        for (Machine machine : machines) {

            var sequence = findFewestPressed(machine.current(), machine.destination(), machine.buttons(), new ArrayList<>());
            System.out.println(sequence);
            total += sequence.size();
        }

        System.out.println("Part 1: " + total);
    }

    private List<BitSet> findFewestPressed(BitSet current, BitSet destination, List<BitSet> buttons, List<BitSet> pressed) {

        if (current.equals(destination)) {
            return pressed;
        }

        var remaining = new ArrayList<>(buttons);
        remaining.removeAll(pressed);

        List<BitSet> shortest = new ArrayList<>(buttons);

        for (BitSet button : remaining) {
            var next = (BitSet) current.clone();
            next.xor(button);

            var newPressed = new ArrayList<>(pressed);
            newPressed.add(button);

            var sequence = findFewestPressed(next, destination, remaining, newPressed);

            if (sequence.size() < shortest.size()) {
                shortest = sequence;
            }
        }

        return shortest;
    }

    @Override
    public void solvePart2(List<String> lines) {

    }
}

record Machine(BitSet current, BitSet destination, List<BitSet> buttons) {
}