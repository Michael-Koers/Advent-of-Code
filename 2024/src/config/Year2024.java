package config;

import util.Year;

public abstract class Year2024 implements Year {

    @Override
    public String getPackage() {
        return this.getClass().getPackageName();
    }

    @Override
    public int getYear() {
        return 2024;
    }
}
