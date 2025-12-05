package day19;

import util.FileInput;
import util.Range;
import util.Stopwatch;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

public class Main {
    public static void main(String[] args) throws URISyntaxException, IOException, NoSuchFieldException {

        List<String> read = FileInput.read(FileInput.INPUT, Main.class);

        PartSystem system = parseInput(read);

//        solvePart1(system);

        Stopwatch stopwatch = new Stopwatch();
        solvePart2(system);
        stopwatch.print();
    }

    private static void solvePart2(PartSystem system) throws NoSuchFieldException {

        Combination combination = new Combination("in",
                new Range(1, 4000),
                new Range(1, 4000),
                new Range(1, 4000),
                new Range(1, 4000));

        Queue<Combination> queue = new LinkedList<>();
        queue.add(combination);

        List<Combination> accepted = new ArrayList<>();

        while (!queue.isEmpty()) {

            Combination current = queue.poll();

            // If this combination reached some kind of end, don't continue
            if (current.step().equals("A")) {
                System.out.printf("Accepted combination: %s%n", current);
                accepted.add(current);
                continue;
            } else if (current.step().equals("R")) {
                continue;
            }

            String unparsedRule = system.rules().get(current.step());

            for (String rule : unparsedRule.split(",")) {

                if (!rule.contains(":")) {
                    queue.add(current.nextStep(rule));
                } else {
                    String field = rule.substring(0, 1);
                    String op = rule.substring(1, 2);
                    String next = rule.split(":")[1];
                    long value = Long.parseLong(rule.substring(2, rule.indexOf(":")));

                    // X field check
                    if (field.equals("x") && combination.x().contains(value)) {
                        if (op.equals("<")) {
                            // Have to split, everying that complies goes on queue, everything else continues
                            queue.add(new Combination(next, new Range(current.x().start(), value - 1), current.m(), current.a(), current.s()));
                            current = new Combination(current.step(), new Range(value, current.x().end()), current.m(), current.a(), current.s());
                        } else {
                            // Have to split, everying that complies goes on queue, everything else continues
                            queue.add(new Combination(next, new Range(value + 1, current.x().end()), current.m(), current.a(), current.s()));
                            current = new Combination(current.step(), new Range(current.x().start(), value), current.m(), current.a(), current.s());
                        }
                    }
                    // M field check
                    else if (field.equals("m") && combination.m().contains(value)) {
                        if (op.equals("<")) {
                            // Have to split, everying that complies goes on queue, everything else continues
                            queue.add(new Combination(next, current.x(), new Range(current.m().start(), value - 1), current.a(), current.s()));
                            current = new Combination(current.step(), current.x(), new Range(value, current.m().end()), current.a(), current.s());
                        } else {
                            // Have to split, everying that complies goes on queue, everything else continues
                            queue.add(new Combination(next, current.x(), new Range(value + 1, current.m().end()), current.a(), current.s()));
                            current = new Combination(current.step(), current.x(), new Range(current.m().start(), value), current.a(), current.s());
                        }
                    }
                    // A field check
                    else if (field.equals("a") && combination.a().contains(value)) {
                        if (op.equals("<")) {
                            // Have to split, everying that complies goes on queue, everything else continues
                            queue.add(new Combination(next, current.x(), current.m(), new Range(current.a().start(), value - 1), current.s()));
                            current = new Combination(current.step(), current.x(), current.m(), new Range(value, current.a().end()), current.s());
                        } else {
                            // Have to split, everying that complies goes on queue, everything else continues
                            queue.add(new Combination(next, current.x(), current.m(), new Range(value + 1, current.a().end()), current.s()));
                            current = new Combination(current.step(), current.x(), current.m(), new Range(current.a().start(), value), current.s());
                        }
                    }
                    // S field check
                    else if (field.equals("s") && combination.s().contains(value)) {
                        if (op.equals("<")) {
                            // Have to split, everying that complies goes on queue, everything else continues
                            queue.add(new Combination(next, current.x(), current.m(), current.a(), new Range(current.s().start(), value - 1)));
                            current = new Combination(current.step(), current.x(), current.m(), current.a(), new Range(value, current.s().end()));
                        } else {
                            // Have to split, everying that complies goes on queue, everything else continues
                            queue.add(new Combination(next, current.x(), current.m(), current.a(), new Range(value + 1, current.s().end())));
                            current = new Combination(current.step(), current.x(), current.m(), current.a(), new Range(current.s().start(), value));
                        }
                    }
                }
            }
        }

        long combinations = accepted.stream().mapToLong(Combination::value).sum();
        System.out.printf("Solved part 2, total distinct combinations: %s%n", combinations);
    }

    private static void solvePart1(PartSystem system) throws NoSuchFieldException {

        List<Part> accepted = new ArrayList<>();
        List<Part> rejected = new ArrayList<>();

        part:
        for (Part part : system.parts()) {
            String flowName = "in";

            System.out.printf("%nPart %s:", part);

            sort:
            while (true) {
                System.out.printf(" -> %s ", flowName);
                String unparsedRules = system.rules().get(flowName);

                for (String s : unparsedRules.split(",")) {
                    // The Default action at the end
                    if (!s.contains(":")) {
                        if (s.equals("A")) {
                            accepted.add(part);
                            System.out.print(" -> A ");
                            continue part;
                        } else if (s.equals("R")) {
                            rejected.add(part);
                            System.out.print(" -> R ");
                            continue part;
                        } else {
                            flowName = s;
                            continue sort;
                        }
                    } else {
                        String field = s.substring(0, 1);
                        String op = s.substring(1, 2);
                        String next = s.split(":")[1];
                        Long value = Long.parseLong(s.substring(2, s.indexOf(":")));

                        Long toEvaluate = switch (field) {
                            case "x" -> part.x();
                            case "m" -> part.m();
                            case "a" -> part.a();
                            case "s" -> part.s();
                            default -> throw new NoSuchFieldException();
                        };

                        if (op.equals(">")) {
                            if (toEvaluate > value) {
                                if (next.equals("A")) {
                                    accepted.add(part);
                                    System.out.print(" -> A ");
                                    continue part;
                                } else if (next.equals("R")) {
                                    rejected.add(part);
                                    System.out.print(" -> R ");
                                    continue part;
                                } else {
                                    flowName = next;
                                    continue sort;
                                }
                            }
                        } else {
                            if (toEvaluate < value) {
                                if (next.equals("A")) {
                                    accepted.add(part);
                                    System.out.print(" -> A ");
                                    continue part;
                                } else if (next.equals("R")) {
                                    rejected.add(part);
                                    System.out.print(" -> R ");
                                    continue part;
                                } else {
                                    flowName = next;
                                    continue sort;
                                }
                            }
                        }
                    }
                }
            }
        }

        System.out.println();
        System.out.println();
        System.out.printf("Total parts accepted: %s%n", accepted.size());
        System.out.printf("Total parts rejected: %s%n", rejected.size());

        long sum = accepted.stream().mapToLong(Part::value).sum();
        System.out.printf("Solved part 1, sum accepted parts: %s%n", sum);
    }

    private static PartSystem parseInput(List<String> read) {

        Map<String, String> flows = new HashMap<>();
        List<Part> parts = new ArrayList<>();

        boolean readingFlows = true;

        for (String s : read) {

            if (s.isBlank()) {
                readingFlows = false;
                continue;
            }

            if (readingFlows) {
                String[] flowSplit = s.split("\\{");
                flows.put(flowSplit[0], flowSplit[1].substring(0, flowSplit[1].length() - 1));
            } else {
                String[] partsSplit = s.split(",");
                parts.add(new Part(
                        Long.parseLong(partsSplit[0].replace("{", "")
                                .substring(2)),
                        Long.parseLong(partsSplit[1].substring(2)),
                        Long.parseLong(partsSplit[2].substring(2)),
                        Long.parseLong(partsSplit[3].replace("}", "")
                                .substring(2))));
            }
        }

        return new PartSystem(parts, flows);
    }
}

record PartSystem(List<Part> parts, Map<String, String> rules) {
}

record Part(long x, long m, long a, long s) {

    public long value() {
        return this.x + this.m + this.a + this.s;
    }
};

record Workflow(String[] rules) {
};

record Combination(String step, Range x, Range m, Range a, Range s) {

    Combination nextStep(String step) {
        return new Combination(step, this.x, this.m, this.a, this.s);
    }

    long value() {
        return this.x.value() * this.m.value() * this.a.value() * this.s.value();
    }
};
