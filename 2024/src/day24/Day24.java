package day24;

import config.Year2024;

import java.io.IOException;
import java.util.*;

public class Day24 extends Year2024 {

    public static void main(String[] args) throws IOException {

        var d = new Day24();

        var lines = d.readInput();

        d.solve(lines);

    }

    @Override
    public void solvePart1(List<String> lines) {

        Map<String, Boolean> known = new HashMap<>();
        var wires = lines.stream().takeWhile(s -> !s.isBlank()).toList();
        for (String s : wires) {
            var parts = s.split(": ");
            known.put(parts[0], parts[1].equals("1"));
        }

        var originalKnown = new HashMap<>(known);

        Long x = mapToLong(known, "x");
        Long y = mapToLong(known, "y");

        var originalGates = lines.stream().skip(wires.size() + 1).toList();
        var gates = new ArrayDeque<>(originalGates);
        while (!gates.isEmpty()) {

            var gate = gates.poll();

            var parts = gate.split(" ");
            var left = parts[0];
            var right = parts[2];

            if (!known.containsKey(left) || !known.containsKey(right)) {
                gates.addLast(gate);
                continue;
            }

            var operand = parts[1];
            var output = parts[4];

            var leftValue = known.get(left);
            var rightValue = known.get(right);

            var result = executeOperand(operand, leftValue, rightValue);

            known.put(output, result);
        }

        long output = mapToLong(known, "z");


        System.out.println("Part 1: " + output);

        // Part 2
        String desiredOutput = Long.toBinaryString(x + y);
        System.out.printf("%s <- actual%n", Long.toBinaryString(output), output);
        System.out.printf("%s <- desired%n", desiredOutput, x + y);

        List<String> list = known.keySet().stream().filter(k -> k.startsWith("z")).sorted().toList();
        System.out.println("actual vs desired");

        List<Node> trees = new ArrayList<>();
        for (String zKey : list) {
            Node root = new Node(zKey);
            makeTree(root, originalGates, originalKnown);
            trees.add(root);
        }

        Map<String, Integer> occurances = new HashMap<>();

        for (int i = 0; i < trees.size(); i++) {
            Node tree = trees.get(i);

            String actual = tree.value ? "1" : "0";
            String desired = String.valueOf(desiredOutput.charAt(desiredOutput.length() - 1 - i));
            System.out.printf("%s - %s", actual, desired);

            var occ = occurances.getOrDefault(tree.name, 0);
            occ++;
            occurances.put(tree.name, occ);


        }
        System.out.println();
    }

    private static boolean executeOperand(String operand, Boolean leftValue, Boolean rightValue) {
        return switch (operand) {
            case "AND" -> leftValue & rightValue;
            case "XOR" -> leftValue ^ rightValue;
            case "OR" -> leftValue | rightValue;
            default -> throw new IllegalArgumentException("Operation " + operand + " not supported");
        };
    }

    private Node makeTree(Node node, List<String> gates, HashMap<String, Boolean> original) {

        var gate = gates.stream().filter(g -> g.endsWith(node.name)).findFirst().get();

        var split = gate.split(" ");

        var left = split[0];
        var right = split[2];
        var operand = split[1];

        var leftNode = new Node(left);
        var rightNode = new Node(right);
        node.setLeft(leftNode);
        node.setRight(rightNode);
        node.setOperand(operand);

        if (!original.containsKey(left)) {
            makeTree(leftNode, gates, original);
        } else {
            leftNode.setValue(original.get(left));
        }

        if (!original.containsKey(right)) {
            makeTree(rightNode, gates, original);
        } else {
            rightNode.setValue(original.get(right));
        }

        node.setValue(executeOperand(node.operand, node.left.value, node.right.value));
        return node;
    }

    private static Long mapToLong(Map<String, Boolean> known, String prefix) {
        return known.entrySet()
                .stream()
                .filter(k -> k.getKey().startsWith(prefix))
                .map(k -> Map.entry(Integer.valueOf(k.getKey().replaceAll("[a-zA-Z]", "")), k.getValue()))
                .sorted(Comparator.comparingInt(Map.Entry::getKey))
                .map((k) -> k.getValue() ? "1" : "0")
                .reduce((s1, s2) -> s2.concat(s1))
                .map(s -> Long.parseLong(s, 2)).get();
    }


    @Override
    public void solvePart2(List<String> lines) {

    }
}

class BinaryTree {
    Node root;

    BinaryTree(Node root) {
        this.root = root;
    }
}

class Node {
    String name;
    String operand;
    boolean value;
    Node left;
    Node right;

    Node(String value) {
        this.name = value;
    }

    void setLeft(Node left) {
        this.left = left;
    }

    void setRight(Node right) {
        this.right = right;
    }

    void setOperand(String operand) {
        this.operand = operand;
    }

    void setValue(boolean value) {
        this.value = value;
    }
}