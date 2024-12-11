package config;

import util.Stopwatch;
import util.Year;

import java.util.List;

public abstract class Year2024 implements Year {

    public void solve(List<String> lines) {
        this.stopwatch.start();
        this.solvePart1(lines);
        this.stopwatch.time();
        this.solvePart2(lines);
        this.stopwatch.time();
        this.stopwatch.prettyPrint();
    }

    public Stopwatch stopwatch;

    public Year2024() {
        stopwatch = new Stopwatch(this.getPackage());
    }

    @Override
    public String getPackage() {
        return this.getClass().getPackageName();
    }

    @Override
    public int getYear() {
        return 2024;
    }
}
