package config;

import java.util.List;
import util.Stopwatch;
import util.Year;

public abstract class Year2025 implements Year {

    public void solve(List<String> lines) {
        this.stopwatch.start();
        this.solvePart1(lines);
        this.stopwatch.time();
        this.solvePart2(lines);
        this.stopwatch.time();
        this.stopwatch.prettyPrint();
    }

    public void warmup(List<String> lines, int iterations) {
        for (int i = 0; i < iterations; i++) {
            this.solvePart1(lines);
            this.solvePart2(lines);
        }
    }

    public Stopwatch stopwatch;

    public Year2025() {
        stopwatch = new Stopwatch(this.getPackage());
    }

    @Override
    public String getPackage() {
        return this.getClass().getPackageName();
    }

    @Override
    public int getYear() {
        return 2025;
    }
}
