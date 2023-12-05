package util;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class FileInput {

    public static String INPUT = "input.txt";
    public static String INPUT_TEST = "input-test.txt";

    public static List<String> read(String filename, Class clazz) throws URISyntaxException, IOException {
        return Files.readAllLines(Paths.get(clazz.getClassLoader().getResource(filename).toURI()));
    }
}
