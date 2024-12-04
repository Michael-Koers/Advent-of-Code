package config;

import util.Stopwatch;
import util.Year;

public abstract class Year2024 implements Year {

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
