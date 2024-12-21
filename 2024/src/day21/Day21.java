package day21;

import config.Year2024;
import util.Point;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day21 extends Year2024 {

    public static void main(String[] args) throws IOException {
        var d = new Day21();

        var lines = d.readTestInput();

        d.solvePart1(lines);
    }

    @Override
    public void solvePart1(List<String> lines) {

        List<Inputter> inputters = new ArrayList<>();

        var door = new Inputter(new Point(2, 3), getNumericKeypad(), null);
        var robot1 = new Inputter(new Point(2, 0), getDirectionalKeypad(), door);
        var human = new Inputter(new Point(2, 0), getDirectionalKeypad(), robot1);

        inputters.add(human);
//        inputters.add(robot2);
        inputters.add(robot1);
        inputters.add(door);

        long total = 0;
        for (String instruction : lines) {
            var instructions = determineInstructionCount(instruction, inputters, 0);

            var instrWorth = Integer.parseInt(instruction.replaceAll("[a-zA-Z]", ""));

            total += (long) instrWorth * instructions.size();

            System.out.printf("%s : %s / %s%n", instruction, instructions.size(), instructions.stream().reduce(String::concat).get());

        }

        System.out.println("Part 1: " + total);
    }

    List<String> determineInstructionCount(String instruction, List<Inputter> inputters, int index) {

        var current = inputters.get(index);
        var keypad = current.keypad();

        // Reached door
        if (current.controls() == null) {

            List<String> instructions = new ArrayList<>();

            for (String s : instruction.split("")) {
                var toPoint = keypad.get(s);
                var fromPoint = current.position();

                var toTravel = fromPoint.difference(toPoint);

                instructions.addAll(translatePointToInstruction(toTravel));
                current = current.setPosition(toPoint);
            }

            return instructions;
        } else {

            var inputs = determineInstructionCount(instruction, inputters, index + 1);
            List<String> instructions = new ArrayList<>();

            for (String input : inputs) {
                var toPoint = keypad.get(input);
                var fromPoint = current.position();

                var toTravel = fromPoint.difference(toPoint);

                instructions.addAll(translatePointToInstruction(toTravel));
                current = current.setPosition(toPoint);
            }

            return instructions;
        }
    }

    @Override
    public void solvePart2(List<String> lines) {

    }

    Map<String, Point> getNumericKeypad() {
        Map<String, Point> keypad = new HashMap<>();
        keypad.put("9", new Point(2, 0));
        keypad.put("8", new Point(1, 0));
        keypad.put("7", new Point(0, 0));
        keypad.put("6", new Point(2, 1));
        keypad.put("5", new Point(1, 1));
        keypad.put("4", new Point(0, 1));
        keypad.put("3", new Point(2, 2));
        keypad.put("2", new Point(1, 2));
        keypad.put("1", new Point(0, 2));
        keypad.put("0", new Point(1, 3));
        keypad.put("A", new Point(2, 3));
        return keypad;
    }

    Map<String, Point> getDirectionalKeypad() {
        Map<String, Point> keypad = new HashMap<>();
        keypad.put("A", new Point(2, 0));
        keypad.put("^", new Point(1, 0));
        keypad.put(">", new Point(2, 1));
        keypad.put("v", new Point(1, 1));
        keypad.put("<", new Point(0, 1));
        return keypad;
    }

    List<String> translatePointToInstruction(Point toMove) {
        List<String> inputs = new ArrayList<>();

        if (toMove.x() != 0) {
            if (toMove.x() < 0) {
                for (long i = 0; i > toMove.x(); i--) {
                    inputs.add("<");
                }
            }
            if (toMove.x() > 0) {
                for (long i = 0; i < toMove.x(); i++) {
                    inputs.add(">");
                }
            }
        }
        if (toMove.y() != 0) {
            if (toMove.y() < 0) {
                for (long i = 0; i > toMove.y(); i--) {
                    inputs.add("^");
                }
            }
            if (toMove.y() > 0) {
                for (long i = 0; i < toMove.y(); i++) {
                    inputs.add("v");
                }
            }
        }
        inputs.add("A");
        return inputs;
    }
}

record Inputter(Point position, Map<String, Point> keypad, Inputter controls) {

    Inputter setPosition(Point newPosition) {
        return new Inputter(newPosition, this.keypad, this.controls);
    }
}

