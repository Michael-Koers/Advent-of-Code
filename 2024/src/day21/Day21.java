package day21;

import config.Year2024;
import util.Direction;
import util.Point;

import java.io.IOException;
import java.util.*;

public class Day21 extends Year2024 {

    Map<String, Long> cache = new HashMap<>();

    public static void main(String[] args) throws IOException {
        var d = new Day21();

        var lines = d.readInput();

        d.solvePart1(lines);
    }

    @Override
    public void solvePart1(List<String> lines) {

        int nrRobots = 25;

        long complexity = 0L;
        for (String code : lines) {
            String current = "A";
            Long steps = 0L;

            for (String c : code.split("")) {
                steps += solveKeypad(current, c, nrRobots);
                current = c;
            }

            System.out.printf("%s: %s%n", code, steps);
            complexity += steps * Long.parseLong(code.substring(0, 3));
        }

        System.out.println("Part 1: " + complexity);
    }

    private Long solveKeypad(String current, String code, int nrRobots) {
        Set<String> paths = determinePathNumerical(current, code);

        long bestPath = Long.MAX_VALUE;
        for (String path : paths) {
            String pathStart = "A";
            Long next = 0L;

            for (String s : path.split("")) {
                next += solveDirectional(pathStart, s, nrRobots);
                pathStart = s;
            }

            if (next < bestPath) {
                bestPath = next;
            }
        }

        return bestPath;
    }

    private Long solveDirectional(String start, String code, int remainingRobots) {
        String cacheKey = "%s-%s-%s".formatted(remainingRobots, start, code);

        if (cache.containsKey(cacheKey)) {
//            System.out.println("Cache hit!");
            return cache.get(cacheKey);
        }

        Set<String> paths = determinePathDirectional(start, code);

        long bestPath = Long.MAX_VALUE;
        for (String path : paths) {
            String pathStart = "A";
            Long next = 0L;

            if (remainingRobots > 1) {
                for (String s : path.split("")) {
                    next += solveDirectional(pathStart, s, remainingRobots - 1);
                    pathStart = s;
                }
            } else {
                next = (long) path.length();
            }

            if (next < bestPath) {
                bestPath = next;
            }
        }

        cache.put(cacheKey, bestPath);
        return bestPath;
    }

    private Set<String> determinePathDirectional(String from, String to) {
        var fromPoint = getDirectionalKeypad().get(from);
        var toPoint = getDirectionalKeypad().get(to);

        var toMove = fromPoint.difference(toPoint);

        Set<String> instructions = new HashSet<>();

        if (fromPoint.y() == 1 || toPoint.x() != 0) {
            String instruction = "";
            if (toMove.x() < 0) {
                instruction = instruction.concat("<".repeat((int) -toMove.x()));
            }
            if (toMove.x() > 0) {
                instruction = instruction.concat(">".repeat((int) toMove.x()));
            }
            if (toMove.y() < 0) {
                instruction = instruction.concat("^".repeat((int) -toMove.y()));
            }
            if (toMove.y() > 0) {
                instruction = instruction.concat("v".repeat((int) toMove.y()));
            }
            instruction = instruction.concat("A");
            instructions.add(instruction);
        }

        if (fromPoint.x() > 0 || toPoint.y() != 0) {
            var instruction = "";
            if (toMove.y() < 0) {
                instruction = instruction.concat("^".repeat((int) -toMove.y()));
            }
            if (toMove.y() > 0) {
                instruction = instruction.concat("v".repeat((int) toMove.y()));
            }
            if (toMove.x() < 0) {
                instruction = instruction.concat("<".repeat((int) -toMove.x()));
            }
            if (toMove.x() > 0) {
                instruction = instruction.concat(">".repeat((int) toMove.x()));
            }
            instruction = instruction.concat("A");
            instructions.add(instruction);

        }
        return instructions;
    }

    private Set<String> determinePathNumerical(String from, String to) {
        var fromPoint = getNumericKeypad().get(from);
        var toPoint = getNumericKeypad().get(to);

        var toMove = fromPoint.difference(toPoint);

        Set<String> instructions = new HashSet<>();

        if (fromPoint.y() < 3 || toPoint.x() != 0) {
            String instruction = "";

            if (toMove.x() < 0) {
                instruction = instruction.concat("<".repeat((int) -toMove.x()));
            }
            if (toMove.x() > 0) {
                instruction = instruction.concat(">".repeat((int) toMove.x()));
            }
            if (toMove.y() < 0) {
                instruction = instruction.concat("^".repeat((int) -toMove.y()));
            }
            if (toMove.y() > 0) {
                instruction = instruction.concat("v".repeat((int) toMove.y()));
            }
            instruction = instruction.concat("A");
            instructions.add(instruction);
        }

        if (fromPoint.x() > 0 || toPoint.y() != 3) {
            String instruction = "";

            if (toMove.y() < 0) {
                instruction = instruction.concat("^".repeat((int) -toMove.y()));
            }
            if (toMove.y() > 0) {
                instruction = instruction.concat("v".repeat((int) toMove.y()));
            }
            if (toMove.x() < 0) {
                instruction = instruction.concat("<".repeat((int) -toMove.x()));
            }
            if (toMove.x() > 0) {
                instruction = instruction.concat(">".repeat((int) toMove.x()));
            }
            instruction = instruction.concat("A");
            instructions.add(instruction);

        }
        return instructions;
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
        keypad.put("A", new Point(2, 3));
        keypad.put("0", new Point(1, 3));
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
}

record Inputter(Point position, Map<String, Point> keypad, Inputter controls) {

    Inputter setPosition(Point newPosition) {
        return new Inputter(newPosition, this.keypad, this.controls);
    }

    Inputter moveDirection(Direction direction) {
        return new Inputter(this.position.moveDirection(direction), this.keypad, this.controls);
    }
}

