package michael.koers;

import util.FileInput;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws URISyntaxException, IOException, NoSuchFieldException {

        List<String> read = FileInput.read(FileInput.INPUT, Main.class);

        PartSystem system = parseInput(read);

        solvePart1(system);
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
        String startFlow = "";
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