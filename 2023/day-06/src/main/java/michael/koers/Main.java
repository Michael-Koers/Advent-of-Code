package michael.koers;

import util.Stopwatch;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws Exception {

        // test input
//        List<Race> races = List.of(new Race(7, 9), new Race(15, 40), new Race(30, 200));
        // real input
        List<Race> races = List.of(new Race(53L, 250L), new Race(91L, 1330L), new Race(67L, 1081L), new Race(68L, 1025L));

        // test input
//        Race bigRace = new Race(71530L, 940200L);
        // real input
        Race bigRace = new Race(53916768L, 250133010811025L);

        Stopwatch stopwatch = new Stopwatch();
        solvePart1(races);
        System.out.printf("Part 1 took %sms%n", stopwatch.duration());
        stopwatch.reset();
        solvePart2(bigRace);
        System.out.printf("Part 1 took %sms%n", stopwatch.duration());
    }

    private static void solvePart2(Race bigRace) {

        Long distanceToBeat = bigRace.distance();
        Long speed = 0L;
        Long possibleWins = 0L;

        for(Long timeLeft = bigRace.duration(); timeLeft > 0; timeLeft--){

            if(speed * timeLeft > distanceToBeat) {
                possibleWins++;
            }

            speed++;

        }

        System.out.printf("Part 2, total wins: %s%n", possibleWins);
    }

    private static void solvePart1(List<Race> races) {

        long totalWins = 1;

        for (Race race : races) {
            List<Long> raceTimes = calculateRace(race, race.duration(), 0);

            long wins = raceTimes.stream()
                    .filter(e -> e > race.distance())
                    .count();

            totalWins *= wins;
            System.out.printf("Race %s has %s possible wins%n", race, wins);
        }

        System.out.printf("Part 1, total wins: %s%n", totalWins);
    }

    private static List<Long> calculateRace(Race race, long timeLeft, long speed) {

        if (timeLeft == 0) {
            return List.of();
        }

        List<Long> results = new ArrayList<>();

        results.add(timeLeft * speed);

        results.addAll(calculateRace(race, timeLeft - 1, speed + 1));

        return results;
    }


}

record Race(Long duration, Long distance) {
}
