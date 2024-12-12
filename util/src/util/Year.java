package util;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public interface Year {

    default List<String> readInput() throws IOException {
        return FileInput.read(
                getYear()
                , getPackage()
                , FileInput.FileType.INPUT.getType()
        );
    }

    default List<String> readTestInput() throws IOException {
        return FileInput.read(
                getYear()
                , getPackage()
                , FileInput.FileType.INPUT_TEST.getType()
        );
    }

    default List<String> readInput(String filename) throws IOException {
        return FileInput.read(
                getYear()
                , getPackage()
                , filename
        );
    }

    /**
     * For reading files when running from native environment. This method will not find the file specified
     * when running in a Jar or IDE.
     */
    default List<String> readNative(String filename, Class<?> clazz) throws IOException, URISyntaxException {
        return FileInput.readNative(filename, clazz);
    }

    String getPackage();

    int getYear();

    void solvePart1(List<String> lines);

    void solvePart2(List<String> lines);
}
