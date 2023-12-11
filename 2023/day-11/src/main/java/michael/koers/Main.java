package michael.koers;

import util.FileInput;
import util.Point;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

public class Main {
    public static void main(String[] args) throws URISyntaxException, IOException {

        List<String> lines = FileInput.read(FileInput.INPUT_TEST, Main.class);

        parseInput(lines);
    }

    private static String[][] parseInput(List<String> lines) {

        String[][] galaxiesMap = new String[lines.size() * 2][lines.size() * 2];
        List<Point> galaxiesPoints = new ArrayList<>();

        int galaxyNumber = 1;
        int newIndex = 0;

        for (int y = 0; y < lines.size(); y++) {

            String line = lines.get(y);

            // Expand universe horizontally
            if (!line.contains("#")) {
                galaxiesMap[newIndex++] = Arrays.copyOf(line.split(""), galaxiesMap.length);
                galaxiesMap[newIndex++] = Arrays.copyOf(line.split(""), galaxiesMap.length);
            } else {
                String[] split = line.split("");
                for (int x = 0; x < split.length; x++) {

                    if (split[x].equals(".")) {
                        galaxiesMap[newIndex][x] = split[x];
                        continue;
                    }

                    galaxiesPoints.add(new Point(x, y));
                    galaxiesMap[newIndex][x] = String.valueOf(galaxyNumber);
                    galaxyNumber++;
                }
                newIndex++;
            }
        }

        newIndex = 0;
        String[][] vertExpanded = new String[galaxiesMap.length][galaxiesMap.length];
        // Expand vertically
        for (int x = 0; x < lines.get(0).length(); x++) {

            boolean isEmpty = true;

            // "ray casting"
            for (int y = 0; y < galaxiesMap.length; y++) {
                if (galaxiesMap[y][x] == null || galaxiesMap[y][x].equals(".")) continue;
                else {
                    isEmpty = false;
                }

            }
            //expand
            if (isEmpty) {
                for(int y = 0; y < galaxiesMap.length; y++){
                    vertExpanded[y][newIndex] = ".";
                    vertExpanded[y][newIndex+1] = ".";
                }
                newIndex++;

            } else {
                for(int y = 0; y < galaxiesMap.length; y++){
                    vertExpanded[y][newIndex] = galaxiesMap[y][newIndex];
                }
                newIndex++;
            }
        }

        prettyPrint(lines);
        prettyPrint(galaxiesMap);
        prettyPrint(vertExpanded);
        return galaxiesMap;
    }

    public static void prettyPrint(List<String> map){
        for(String line : map){
            System.out.println(line);
        }
        System.out.println();
    }
    public static void prettyPrint(String[][] map){

        for(int i = 0; i < map.length; i++){
            for(int j = 0; j < map[i].length; j++){
                System.out.print(map[i][j]);
            }
            System.out.println();
        }
        System.out.println();
    }
}

record Universe(List<Point> galaxies, String[][] map) {
}