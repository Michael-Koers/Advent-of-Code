package util;

import java.io.IOException;
import java.util.List;

public interface Year {

    default List<String> readInput() throws IOException {
        return FileInput.read(
                getYear()
                , getPackage()
                , FileInput.FileType.INPUT
        );
    }

    default List<String> readTestInput() throws IOException {
        return FileInput.read(
                getYear()
                , getPackage()
                , FileInput.FileType.INPUT_TEST
        );
    }

    String getPackage();

    int getYear();

    void solvePart1(List<String> lines);

    void solvePart2(List<String> lines);
}
