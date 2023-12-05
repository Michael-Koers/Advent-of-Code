import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws IOException {

        List<String> lines = Files.readAllLines(Paths.get("2023/day-05/input.txt"));
        Map<Integer, List<MapRange>> maps = parseMaps(lines);
        long[] seeds = parseSeeds(lines);

        long start_p1 = System.currentTimeMillis();

//        solvePart1(seeds, maps);

        long start_p2 = System.currentTimeMillis();
        System.out.printf("Solved part 1, time: %sms%n", System.currentTimeMillis() - start_p1);

        solvePart2(seeds, maps);

        System.out.printf("Solved part 2, time: %sms%n", System.currentTimeMillis() - start_p2);
        System.out.printf("Solved day 5, total duration: %sms%n", System.currentTimeMillis() - start_p1);

    }

    private static long[] parseSeeds(List<String> lines) {
        return Arrays.stream(lines.get(0).split(":")[1].split(" ")).map(String::trim).filter(e -> !e.isBlank()).mapToLong(Long::parseLong).toArray();
    }

    private static void solvePart1(long[] seeds, Map<Integer, List<MapRange>> maps) {

        Long answer = Long.MAX_VALUE;

        for(Long seed : seeds){

            Long soil = mapTranslate(maps.get(0), seed);
            Long fertilizer = mapTranslate(maps.get(1), soil);
            Long water = mapTranslate(maps.get(2), fertilizer);
            Long light = mapTranslate(maps.get(3), water);
            Long temperature = mapTranslate(maps.get(4), light);
            Long humidity = mapTranslate(maps.get(5), temperature);
            Long location = mapTranslate(maps.get(6), humidity);

            if(location < answer) answer = location;
        }

        System.out.printf("Solved, lowest location: %s%n", answer);
    }

    private static void solvePart2(long[] seeds, Map<Integer, List<MapRange>> maps) {

        Long answer = Long.MAX_VALUE;

        for(int i = 0; i < seeds.length; i+=2){
            System.out.printf("Completed seed %s: %s, lowest answer: %s%n", i, seeds[i], answer);
            for(long j = seeds[i] ; j < seeds[i] + seeds[i + 1]; j++){

                if(j % 100_000 == 0) System.out.printf("Seed %s, %s: %s, inc: %s%n", i, seeds[i], j, j - seeds[i]);
                long seed = j;

                Long soil = mapTranslate(maps.get(0), seed);
                Long fertilizer = mapTranslate(maps.get(1), soil);
                Long water = mapTranslate(maps.get(2), fertilizer);
                Long light = mapTranslate(maps.get(3), water);
                Long temperature = mapTranslate(maps.get(4), light);
                Long humidity = mapTranslate(maps.get(5), temperature);
                Long location = mapTranslate(maps.get(6), humidity);

                if(location < answer) answer = location;
            }
        }

        System.out.printf("Solved, lowest location: %s%n", answer);
    }

    private static Long mapTranslate(List<MapRange> maps, Long start) {
         try{
             return maps
                    .stream()
                    .filter(e -> e.inSrcRange(start))
                    .map(e -> e.translate(start))
                    .findFirst().orElseThrow(NoSuchElementException::new);
        } catch (NoSuchElementException e){
            return start;
        }
    }

    private static Map<Integer, List<MapRange>> parseMaps(List<String> readAllLines) {

        // Test input ranges
//        List<MapRange> seedSoil = parseRanges(readAllLines, 3, 4);
//        List<MapRange> soilFertilizer = parseRanges(readAllLines, 7, 9);
//        List<MapRange> fertilizerWater = parseRanges(readAllLines, 12, 15);
//        List<MapRange> waterLight = parseRanges(readAllLines, 18, 19);
//        List<MapRange> lightTemp = parseRanges(readAllLines, 22, 24);
//        List<MapRange> tempHum = parseRanges(readAllLines, 27, 28);
//        List<MapRange> humLocation = parseRanges(readAllLines, 31, 32);

        // Real input ranges
        List<MapRange> seedSoil = parseRanges(readAllLines, 3, 12);
        List<MapRange> soilFertilizer = parseRanges(readAllLines, 15, 30);
        List<MapRange> fertilizerWater = parseRanges(readAllLines, 33, 47);
        List<MapRange> waterLight = parseRanges(readAllLines, 50, 94);
        List<MapRange> lightTemp = parseRanges(readAllLines, 97, 111);
        List<MapRange> tempHum = parseRanges(readAllLines, 114, 136);
        List<MapRange> humLocation = parseRanges(readAllLines, 139, 149);

        return Map.of(0, seedSoil,
                1, soilFertilizer,
                2, fertilizerWater,
                3, waterLight,
                4, lightTemp,
                5, tempHum,
                6, humLocation);
    }

    private static List<MapRange> parseRanges(List<String> readAllLines, int start, int end) {
        return readAllLines.subList(start, end + 1)
                .stream()
                .map(e -> Arrays
                        .stream(e.split(" "))
                        .map(Long::parseLong)
                        .collect(Collectors.toList()))
                .map(e -> new MapRange(e.get(0), e.get(0) + e.get(2) - 1, e.get(1), e.get(1) + e.get(2) - 1, e.get(2)))
                .collect(Collectors.toList());
    }


}


record MapRange(Long destStart, Long destEnd, Long sourceStart, Long sourceEnd, Long offset) {

    boolean inDestRange(int pos) {
        return pos >= destStart && pos <= destEnd;
    }

    boolean inSrcRange(Long pos) {
        return pos >= sourceStart && pos <= sourceEnd;
    }

    Long translate(Long pos){
        return destStart + pos - sourceStart;
    }
}