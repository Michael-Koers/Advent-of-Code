package util;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class FileInput {

    public static final String INPUT = "";
    public static final String INPUT_TEST = "";

    public static List<String> read(String filename, Class clazz) throws URISyntaxException, IOException {
        return Files.readAllLines(Paths.get(clazz.getClassLoader().getResource(filename).toURI()));
    }

    public static List<String> read(String path) throws IOException {
        return Files.readAllLines(Path.of("src/".concat(path)));
    }

    public static List<String> read(int year, String day, FileType file) throws IOException {
        return Files.readAllLines(Path.of(
                "".concat(String.valueOf(year))
                        .concat("/src/day")
                        .concat(day)
                        .concat("/")
                        .concat(file.getType())));
    }

    public enum FileType {
        INPUT("input.txt"),
        INPUT_TEST("input-test.txt");

        private final String filetype;

        FileType(String filetype) {
            this.filetype = filetype;
        }

        String getType() {
            return this.filetype;
        }
    }
}