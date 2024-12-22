package util;

import java.io.PrintStream;

public class Stopwatch {

    private long start;
    private long durationPart1;
    private long durationPart2;
    private String day = "not set";

    public Stopwatch() {
    }

    public Stopwatch(String day) {
        this();
        this.day = day;
    }

    public void start() {
        this.start = System.currentTimeMillis();
    }

    public void print() {
        print(System.out);
    }

    public void print(PrintStream printStream) {
        printStream.printf("Duration: %sms%n", System.currentTimeMillis() - this.start);
    }

    public void prettyPrint() {
        prettyPrint(System.out);
    }

    public void prettyPrint(PrintStream printStream) {
        printStream.printf("""
                        🎄 Day: %s | ☕ Java: %s %s | 🌟 Part 1: %sms | 🌟 Part 2: %sms | ⌚ Total: %sms
                        """,
                this.day.replace("day", "")
                , System.getProperty("java.vendor")
                , System.getProperty("java.version")
                , this.durationPart1
                , this.durationPart2
                , this.durationPart1 + this.durationPart2
        );
    }

    public void time() {
        if (this.durationPart1 == 0) {
            this.durationPart1 = System.currentTimeMillis() - this.start;
            this.start = System.currentTimeMillis();
        } else {
            this.durationPart2 = System.currentTimeMillis() - this.start;
        }
    }

    public void reset() {
        this.start = System.currentTimeMillis();
    }
}