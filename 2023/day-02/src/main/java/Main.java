import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws IOException {

        long start = System.currentTimeMillis();

        Map<Integer, List<Score>> gameResults = parseGameResults(Files.readAllLines(Paths.get("2023/day-02/input.txt")));

        solvePart1(gameResults);
        solvePart2(gameResults);

        System.out.printf("Total duration: %sms%n", System.currentTimeMillis() - start);
    }

    private static void solvePart1(Map<Integer, List<Score>> gameResults) {
        int maxRed = 12;
        int maxGreen = 13;
        int maxBlue = 14;


        Map<Integer, List<Score>> possibleGames = gameResults.entrySet()
                .stream()
                .filter(e -> e.getValue().stream().allMatch(s -> s.red() <= maxRed && s.green() <= maxGreen && s.blue() <= maxBlue))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        int sumId = possibleGames.keySet().stream().mapToInt(Integer::intValue).sum();

        System.out.printf("Done! Total games: %s, possible games: %s, total ID: %d%n", gameResults.size(), possibleGames.size(), sumId);

    }

    private static void solvePart2(Map<Integer, List<Score>> gameResults) {

        int totalPower = 0;

        System.out.printf("Done! Total games: %s, total power: %d%n", gameResults.size(), totalPower);
    }

    private static Map<Integer, List<Score>> parseGameResults(List<String> lines) {
        Map<Integer, List<Score>> gameResults = new HashMap<>();

        for (String line : lines) {

            Integer gameId = Integer.parseInt(line.split(":")[0].replace("Game ", ""));

            String game = line.split(":")[1];
            String[] sets = game.split(";");


            List<Score> scores = new ArrayList<>();

            for (String set : sets) {

                Score cubeCount = new Score(0, 0, 0);
                for (String hand : set.split(",")) {

                    int number = Integer.parseInt(hand.trim().split(" ")[0]);
                    String color = hand.trim().split(" ")[1];

                    cubeCount = switch (color) {
                        case "blue" -> cubeCount.setBlue(number);
                        case "green" -> cubeCount.setGreen(number);
                        case "red" -> cubeCount.setRed(number);
                        default -> throw new IllegalStateException("Unexpected value: " + color);
                    };

                }

                scores.add(cubeCount);
            }
            // put hashmap
            gameResults.put(gameId, scores);
        }
        return gameResults;
    }
}


record Score(int red, int green, int blue) {

    Score setRed(int x) {
        return new Score(red + x, green, blue);//, maxRed, maxGreen, maxBlue);
    }

    Score setGreen(int x) {
        return new Score(red, green + x, blue);//, maxRed, maxGreen, maxBlue);
    }

    Score setBlue(int x) {
        return new Score(red, green, blue + x);//, maxRed, maxGreen, maxBlue);
    }
};