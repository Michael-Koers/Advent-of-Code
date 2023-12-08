package michael.koers;

import util.FileInput;
import util.MathUtil;
import util.Stopwatch;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws URISyntaxException, IOException {

        List<String> read = FileInput.read(FileInput.INPUT, Main.class);
//        List<String> read = FileInput.read("input-test3.txt", Main.class);

        ElfMap map = parseNodes(read);

        var stopwatch = new Stopwatch();
        solvePart1(map);

        System.out.printf("Solved part 1, took: %sms%n", stopwatch.duration());
        stopwatch.reset();

        solvePart2(map);
        System.out.printf("Solved part 2, took: %sms%n", stopwatch.duration());

    }

    private static void solvePart2(ElfMap map) {

        boolean stepping = true;
        int steps = 0;
        String[] instructions = map.instructions().split("");
        List<String> currentNodes = map.nodes().keySet().stream().filter(e -> e.endsWith("A")).collect(Collectors.toCollection(ArrayList::new));

        List<Integer> lcm = new ArrayList<>();
        currentNodes.forEach(e -> lcm.add(0));

        Long lcmSteps = 0L;

        while (stepping) {

            String instruction = instructions[steps % instructions.length];

            for (int i = 0; i < currentNodes.size(); i++) {

                // Already found shortest path, use LCM to find path to reach all ends
                if(lcm.size() > i && lcm.get(i) > 0){
                    continue;
                }

                String next;
                if (instruction.equals("L")) {
                    next = String.valueOf(map.nodes().get(currentNodes.get(i)).keySet().toArray()[0]);
                } else {
                    next = String.valueOf(map.nodes().get(currentNodes.get(i)).values().toArray()[0]);
                }
                if(next.endsWith("Z")){
                    lcm.set(i, steps+1);
                }

                currentNodes.set(i, next);
            }

            steps++;

            // If all on Z, stop
            if (currentNodes.stream().allMatch(e -> e.endsWith("Z"))) {
                stepping = false;
                lcmSteps = MathUtil.lcm(lcm.stream().map(Integer::longValue).toArray(Long[]::new));
            }
        }

        System.out.printf("Solved Part 2, reached 'Z' on all nodes, steps taken: %s%n", lcmSteps);
    }

    private static void solvePart1(ElfMap map) {

        String current = "AAA";
        int steps = 0;
        String[] instructions = map.instructions().split("");

        while (!current.equals("ZZZ")) {

            String instruction = instructions[steps % instructions.length];

            if (instruction.equals("L")) {
                current = String.valueOf(map.nodes().get(current).keySet().toArray()[0]);
            } else {
                current = String.valueOf(map.nodes().get(current).values().toArray()[0]);
            }

            steps++;

        }

        System.out.printf("Solved part 1, total steps to 'ZZZ': %s%n", steps);
    }

    private static ElfMap parseNodes(List<String> read) {

        Map<String, Map<String, String>> nodes = new HashMap<>();

        String instructions = read.remove(0);
        // Remove blank line after instructions
        read.remove(0);

        for (String line : read) {

            String start = line.split(" = ")[0];

            String left = line.split(" = ")[1].split(",")[0].replace("(", "").trim();
            String right = line.split(" = ")[1].split(",")[1].replace(")", "").trim();

            var leftRight = Map.of(left, right);
            nodes.put(start, leftRight);
        }

        return new ElfMap(instructions, nodes);
    }
}

record ElfMap(String instructions, Map<String, Map<String, String>> nodes) {
};