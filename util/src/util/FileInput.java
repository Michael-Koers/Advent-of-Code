package util;

import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FileInput {

    public static final String INPUT = "";
    public static final String INPUT_TEST = "";

    public static List<String> readNative(String filename, Class<?> clazz) {
        List<String> lines = new ArrayList<>();

        try (FileSystem filesystem = FileSystems.newFileSystem(URI.create("resource:/"), Collections.singletonMap("create", "true"))) {
            Path path = filesystem.getPath(clazz.getPackageName(), filename);
            lines.addAll(Files.readAllLines(path));
        } catch (Exception e) {
            System.out.printf("File not found at: %s/%s%n%s", clazz.getPackageName(), filename, e);
        }

        return lines;

    }

    public static List<String> read(String path) throws IOException {
        return Files.readAllLines(Path.of(path));
    }

    public static List<String> read(int year, String day, FileType file) throws IOException {
        return Files.readAllLines(Path.of(
                "".concat(String.valueOf(year))
                        .concat("/src/")
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