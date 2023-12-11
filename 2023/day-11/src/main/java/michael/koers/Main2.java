package michael.koers;

import util.FileInput;
import util.Point;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class Main2 {
    public static void main(String[] args) throws URISyntaxException, IOException {

        List<String> lines = FileInput.read(FileInput.INPUT_TEST, Main.class);

        List<String> map = parseInput(lines);

        solvePart1(map);
    }

    private static void solvePart1(List<String> map) {
        List<Galaxy> galaxies = new ArrayList<>();

        // find points
        for(int y = 0; y < map.size(); y++){

            for(int x = 0; x < map.get(y).length(); x++){

                if(map.get(y).charAt(x) != '.'){

                    galaxies.add(new Galaxy(map.get(y).charAt(x) - '0', new Point(x, y)));
                }
            }
        }

        Long total = 0L;

        List<Galaxy> others = new ArrayList<>(galaxies);
        for(Galaxy galaxy_src : galaxies){
            others.remove(galaxy_src);
            for(Galaxy galaxy_dest : others){
                if(galaxy_src == galaxy_dest){
                    continue;
                }
                // Manhatten distance
                total += Math.abs(galaxy_dest.point().x() - galaxy_src.point().x()) + Math.abs(galaxy_dest.point().y() - galaxy_src.point().y());
            }
        }

        System.out.printf("Solved part 1, sum of shortest paths: %s%n", total);
    }

    private static List<String> parseInput(List<String> lines) {

        System.out.println("Input");
        prettyPrint(lines);

        List<String> map = new ArrayList<>();

        int galaxyNumber = 1;

        for (String line : lines) {
            // Expand horizontally
            if (!line.contains("#")) {
                map.add(line);
                map.add(line);
            } else {
                StringBuilder sb = new StringBuilder();
                for(String s : line.split("")){
                    if(s.equals("#")){
                        s = String.valueOf(galaxyNumber++);
                    }
                    sb.append(s);
                }
                map.add(sb.toString());
            }
        }

        System.out.println("Before");
        prettyPrint(map);

        // Expand vertically
        for(int x = 0; x < map.get(0).length(); x++){
            boolean isEmpty = true;
            for(int y = 0; y < map.size(); y++){

                if(map.get(y).charAt(x) != '.'){
                    isEmpty = false;
                    break;
                }
            }
            if(isEmpty){
                for(int y = 0; y < map.size(); y++){
                    String newLine = new StringBuilder(map.get(y)).insert(x, ".").toString();
                    map.set(y, newLine);
                }
                x++;
            }
        }

        System.out.println("After");
        prettyPrint(map);

        return map;
    }

    public static void prettyPrint(List<String> map){
        for(String line : map){
            System.out.println(line);
        }
        System.out.println();
    }
}


record Galaxy(int number, Point point){};