package day20;

import util.FileInput;
import util.MathUtil;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;

enum Pulse {
    HIGH,
    LOW;

    public Pulse flip() {
        return switch (this) {
            case HIGH -> LOW;
            case LOW -> HIGH;
        };
    }
}

public class Main {
    public static void main(String[] args) throws URISyntaxException, IOException {

        List<String> read = FileInput.read(FileInput.INPUT, Main.class);

        Map<String, Module> modules = parseInput(read);

        solvePart1(modules);
        solvePart2(modules);
    }

    private static void solvePart2(Map<String, Module> modules) {
        long buttonCount = 1000L;

        // The next four feed into ft, which feeds into rx (important: these are all conjunctions!)
        // vz, bq, qh, lt
        // All high pulses -> low output
        // When all 4 send high pulse to ft, we get low signal to rx
        Map<String, Long> destCount = new HashMap<>();

        outer:
        while (true) {

            Module button = new Button("button", new String[]{"broadcaster"});
            Deque<Signal> queue = new LinkedList<>(button.receiveAndSend(new Signal("", "", Pulse.LOW)));
            buttonCount++;

            while (!queue.isEmpty()) {

                Signal signal = queue.pollFirst();
                if (signal.pulse().equals(Pulse.HIGH) && signal.destination().equals("ft")) {
                    destCount.putIfAbsent(signal.source(), buttonCount);
                    if (destCount.size() >= 4) break outer;
                }
                Module module = modules.get(signal.destination());
                if (module == null) continue;
                queue.addAll(module.receiveAndSend(signal));
            }
        }
        System.out.printf("Solved part 2, button presses required: %s%n", MathUtil.lcm(destCount.values().toArray(Long[]::new)));
    }

    private static void solvePart1(Map<String, Module> modules) {
        long low = 0L;
        long high = 0L;
        int i = 0;

        while (i < 1000) {

            Module button = new Button("button", new String[]{"broadcaster"});
            Deque<Signal> queue = new LinkedList<>(button.receiveAndSend(new Signal("", "", Pulse.LOW)));

            while (!queue.isEmpty()) {

                Signal signal = queue.pollFirst();
                if (signal.pulse().equals(Pulse.LOW)) {
                    low++;
                } else {
                    high++;
                }

                Module module = modules.get(signal.destination());
                if (module == null) continue;

                queue.addAll(module.receiveAndSend(signal));
            }
            i++;
        }
        System.out.printf("Solved part 1, %s high pulses, %s low pulses, multiplying: %s%n", high, low, high * low);
    }

    private static Map<String, Module> parseInput(List<String> read) {

        Map<String, Module> modules = new HashMap<>();

        for (String s : read) {

            String[] parts = s.split("->");
            String broadcaster = parts[0].trim();
            String[] destinations = Arrays.stream(parts[1].split(",")).map(String::trim).toArray(String[]::new);

            Module module = switch (broadcaster.charAt(0)) {
                case '%' -> new FlipFlop(broadcaster.substring(1), destinations);
                case '&' ->
                        new Conjunction(broadcaster.substring(1), destinations, findAllConjunctionInputs(read, broadcaster.substring(1)));
                case 'b' -> new Broadcaster(broadcaster, destinations);
                default -> throw new NoSuchElementException();
            };
            modules.put(module.name, module);
        }
        return modules;
    }

    private static List<String> findAllConjunctionInputs(List<String> read, String conjunction) {

        List<String> inputs = new ArrayList<>();

        for (String s : read) {
            String dest = s.split("->")[1];
            String input = s.split("->")[0].trim().substring(1);
            if (dest.contains(conjunction)) inputs.add(input);
        }
        return inputs;
    }
}

class Button extends Module {

    public Button(String name, String[] next) {
        super(name, next);
    }

    @Override
    public List<Signal> receiveAndSend(Signal signal) {
//        Arrays.stream(this.next).forEach(n -> System.out.printf("%s -%s -> %s%n", this.name, this.pulse, n));
        return List.of(new Signal(this.name, this.next[0], Pulse.LOW));
    }

}

class Broadcaster extends Module {

    public Broadcaster(String name, String[] next) {
        super(name, next);
    }

    @Override
    public List<Signal> receiveAndSend(Signal signal) {
        this.pulse = signal.pulse();

//        Arrays.stream(this.next).forEach(n -> System.out.printf("%s -%s -> %s%n", this.name, this.pulse, n));
        return Arrays.stream(this.next)
                .map(next -> new Signal(this.name, next, this.pulse))
                .collect(Collectors.toList());
    }
}

class Conjunction extends Module {

    Map<String, Pulse> inputs = new HashMap<>();

    public Conjunction(String name, String[] next, List<String> inputs) {
        super(name, next);
        for (String input : inputs) {
            this.inputs.put(input, Pulse.LOW);
        }
    }

    @Override
    public List<Signal> receiveAndSend(Signal signal) {
        this.inputs.put(signal.source(), signal.pulse());

//        Arrays.stream(this.next).forEach(n -> System.out.printf("%s -%s -> %s%n", this.name, this.pulse, n));
        return Arrays.stream(this.next)
                .map(next -> new Signal(this.name, next, allHighPulses() ? Pulse.LOW : Pulse.HIGH))
                .collect(Collectors.toList());
    }

    private boolean allHighPulses() {
        return this.inputs.values().stream().allMatch(p -> p.equals(Pulse.HIGH));
    }
}

class FlipFlop extends Module {

    public FlipFlop(String name, String[] next) {
        super(name, next);
    }

    @Override
    public List<Signal> receiveAndSend(Signal signal) {
        if (signal.pulse().equals(Pulse.LOW)) {
            this.pulse = this.pulse.flip();

//            Arrays.stream(this.next).forEach(n -> System.out.printf("%s -%s -> %s%n", this.name, this.pulse, n));
            return Arrays.stream(this.next)
                    .map(next -> new Signal(this.name, next, this.pulse))
                    .collect(Collectors.toList());
        }
        return new LinkedList<>();
    }
}

abstract class Module {

    Pulse pulse = Pulse.LOW;
    String name;
    String[] next;

    public Module(String name, String[] next) {
        this.name = name;
        this.next = next;
    }


    public abstract List<Signal> receiveAndSend(Signal signal);
}

record Signal(String source, String destination, Pulse pulse) {
};