package day10;

import config.Year2025;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.stream.Collectors.joining;

public class Day10 extends Year2025 {


    public static void main(String[] args) throws IOException {
        var d = new Day10();

        var lines = d.readInput();

        d.solvePart2(lines);
    }

    @Override
    public void solvePart1(List<String> lines) {
        List<Machine> machines = parseMachines(lines);

        long total = 0L;

        for (Machine machine : machines) {

            var sequence = findFewestPressed(machine.current(), machine.destination(), machine.buttons(), new ArrayList<>());
            System.out.println(sequence);
            total += sequence;
        }

        System.out.println("Part 1: " + total);
    }


    @Override
    public void solvePart2(List<String> lines) {
        List<Machine> machines = parseMachines(lines);

        var z3program = machines.stream().map(Machine::z3program).collect(joining());

        var output = "";

        try {
            var process = new ProcessBuilder(List.of("C:\\Users\\Michael\\Downloads\\z3-4.15.4-x64-win\\z3-4.15.4-x64-win\\bin\\z3.exe", "-in")).start();
            process.outputWriter().write(z3program);
            process.outputWriter().close();
            process.waitFor();
            output = new String(process.getInputStream().readAllBytes());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        var result = Pattern.compile("\\(total (\\pN+)\\)")
                .matcher(output)
                .results()
                .map(r -> r.group(1))
                .mapToInt(Integer::parseInt)
                .sum();

        System.out.println("Part 2: " + result);
    }

    private static List<Machine> parseMachines(List<String> lines) {
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
            List<Integer> joltages = new ArrayList<>();

            while (matcher.find()) {
                var part = matcher.group();

                if (part.startsWith("(")) {
                    part = part.replace("(", "").replace(")", "");
                    var trimmed = Arrays.stream(part.split(",")).map(Integer::valueOf).toList();

                    BitSet buttonsBitSet = new BitSet(binary.length());
                    for (Integer button : trimmed) {
                        buttonsBitSet.set(button);
                    }

                    buttons.add(buttonsBitSet);
                } else if (part.startsWith("{")) {
                    part = part.replace("{", "").replace("}", "");
                    joltages = Arrays.stream(part.split(",")).map(Integer::valueOf).toList();

                }
            }


            var machine = new Machine(new BitSet(binary.length()), destination, buttons, joltages);
            machines.add(machine);
        }
        return machines;
    }

    private Long findFewestPressed(BitSet start, BitSet destination, List<BitSet> buttons, List<BitSet> pressed) {

        Set<BitSet> seen = new HashSet<>();
        Queue<Step> states = new LinkedList<>();
        states.add(new Step(start, 0));

        while (!states.isEmpty()) {
            var state = states.poll();
            if (state.state().equals(destination)) {
                return state.steps();
            }

            if (seen.contains(state.state())) {
                continue;
            }

            seen.add(state.state());
            for (BitSet button : buttons) {
                BitSet nextState = (BitSet) state.state().clone();
                nextState.xor(button);
                states.add(new Step(nextState, state.steps() + 1));
            }
        }

        throw new IllegalArgumentException("Expected to have an answer by now");
    }

}

record Machine(BitSet current, BitSet destination, List<BitSet> buttons, List<Integer> joltages) {

    CharSequence z3program() {
        var builder = new StringBuilder();
        builder.append("(reset)\n");
        for (int i = 0; i < buttons.size(); i++) {
            builder.append("(declare-const k%d Int)\n".formatted(i));
            builder.append("(assert (>= k%d 0))\n".formatted(i));
        }
        for (int j = 0; j < joltages.size(); j++) {
            builder.append("(assert (= (+");
            for (int i = 0; i < buttons.size(); i++)
                if (buttons.get(i).get(j)) builder.append(" k" + i);
            builder.append(") %d))\n".formatted(joltages.get(j)));
        }
        builder.append("(declare-const total Int)\n");
        builder.append("(assert (= total (+");
        for (int i = 0; i < buttons.size(); i++) builder.append(" k" + i);
        builder.append(")))\n");
        builder.append("(minimize total)\n");
        builder.append("(check-sat)\n");
        builder.append("(get-objectives)\n");
        return builder;
    }
}

record Step(BitSet state, long steps) {
};