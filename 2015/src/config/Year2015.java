package config;

import util.Year;

public interface Year2015 extends Year {

    default int getYear() {
        return 2015;
    }

}
