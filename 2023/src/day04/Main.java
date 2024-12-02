package day04;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws IOException {

        long start_parse = System.currentTimeMillis();

        Map<Integer, Deck> decks = parseCards(Files.readAllLines(Paths.get("2023/day-04/input.txt")));

        long start_p1 = System.currentTimeMillis();
        System.out.printf("Parsed input, took: %sms%n", System.currentTimeMillis() - start_parse);

        solvePart1(decks);

        long start_p2 = System.currentTimeMillis();
        System.out.printf("Solved part 1, took: %sms%n", System.currentTimeMillis() - start_p1);

        solvePart2(decks);

        System.out.printf("Solved part 2, took: %sms%n", System.currentTimeMillis() - start_p2);
        System.out.printf("Total solve duration: %sms%n", System.currentTimeMillis() - start_parse);
    }

    private static void solvePart2(Map<Integer, Deck> decks) {

        for (Map.Entry<Integer, Deck> indexedDeck : decks.entrySet()) {

            // Make copy because retainAll removes entries
            List<Integer> hits = new ArrayList<>(indexedDeck.getValue().owned());
            hits.retainAll(indexedDeck.getValue().winning());

            int start = indexedDeck.getKey() + 1;
            int end = Math.clamp(indexedDeck.getKey() + hits.size(), 0, decks.size());

            for (int i = start; i <= end; i++) {
                decks.put(i, decks.get(i).addCopies(1 + indexedDeck.getValue().copies()));
            }
        }

        int total = decks.values()
                .stream()
                .mapToInt(e -> e.copies() + 1) // Add original
                .sum();

        System.out.printf("Part 2, total scratchcards won: %s%n", total);

    }

    private static void solvePart1(Map<Integer, Deck> decks) {

        double points = 0;

        for (Deck deck : decks.values()) {

            // Make copy because retainAll removes entries
            List<Integer> hits = new ArrayList<>(deck.owned());
            hits.retainAll(deck.winning());

            if (hits.size() == 0) {
                points += 0;
            } else {
                points += Math.pow(2, (Math.max(hits.size() - 1, 0)));
            }
//            System.out.printf("Card %s has %s hits, winning numbers are %s%n", deck.card(), hits.size(), hits);
        }

        System.out.printf("Part 1, total points: %s%n", points);
    }

    private static Map<Integer, Deck> parseCards(List<String> lines) {

        Map<Integer, Deck> cards = new HashMap<>();

        for (String line : lines) {

            String[] split = line.split(":");
            String[] rawCards = split[1].split("\\|");

            int card = Integer.parseInt(split[0].replace("Card", "").trim());

            List<Integer> winning = Arrays.stream(rawCards[0].split(" "))
                    .filter(e -> !e.isBlank())
                    .map(String::trim)
                    .map(Integer::valueOf)
                    .collect(Collectors.toList());

            List<Integer> owned = Arrays.stream(rawCards[1].split(" "))
                    .filter(e -> !e.isBlank())
                    .map(String::trim)
                    .map(Integer::valueOf)
                    .collect(Collectors.toList());

            cards.put(card, new Deck(card, winning, owned, 0));
        }
        return cards;
    }
}

record Deck(int card, List<Integer> winning, List<Integer> owned, int copies) {

    Deck addCopies(int x) {
        return new Deck(card, winning, owned, copies + x);
    }
}

