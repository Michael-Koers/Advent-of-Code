package util;

import java.io.PrintStream;

public class Stopwatch {

    private Long start;
    private Long end;
    private Long duration;

    public Stopwatch(){
        this.start = System.currentTimeMillis();
    }

    public void print(PrintStream printStream){
        printStream.printf("Duration: %sms%n", System.currentTimeMillis() - this.start);
    }

    public void print(){
        print(System.out);
    }

    public long duration(){
        return this.end == null ? System.currentTimeMillis() - this.start : this.end - this.start;
    }

    public void end(){
        this.end = System.currentTimeMillis();
        this.duration = this.end - this.start;
    }

    public void reset(){
        this.start = System.currentTimeMillis();
    }
}
