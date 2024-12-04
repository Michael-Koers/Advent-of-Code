package util;

import java.io.PrintStream;

public class Stopwatch {

    private Long start;
    private Long end;
    private Long duration;
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
        printStream.printf("Day: %s | Duration: %sms | ☕ JDK: %s %s%n",
                this.day.replace("day", "")
                , System.currentTimeMillis() - this.start
                , System.getProperty("java.vendor")
                , System.getProperty("java.version"));
    }


    public long duration() {
        return this.end == null ? System.currentTimeMillis() - this.start : this.duration;
    }

    public void stop() {
        this.end = System.currentTimeMillis();
        this.duration = this.end - this.start;
    }

    public void reset() {
        this.start = System.currentTimeMillis();
    }
}