package day07;

import util.FileInput;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

enum Type {
    FIVE(10),
    FOUR(9),
    FULL_HOUSE(8),
    THREE(7),
    TWO_PAIR(6),
    ONE_PAIR(5),
    HIGH(0);

    public int value;

    private Type(int value) {
        this.value = value;
    }
}

public class Main {
    public static void main(String[] args) throws URISyntaxException, IOException {

        List<String> lines = FileInput.read(2023, "07", FileInput.FileType.INPUT);

        List<String> sorted = parseHands(lines, false);
        solvePart1(sorted);

        List<String> sorted_jokers = parseHands(lines, true);
        // We can rerun part 1 solver for part 2 output
        solvePart1(sorted_jokers);
    }

    private static void solvePart1(List<String> sorted) {

        Long total = 0L;

        for (int i = 0; i < sorted.size(); i++) {
            total += Long.parseLong(sorted.get(i).split(" ")[1]) * (i + 1);
        }

        System.out.printf("Part 1, total winnings: %s%n", total);
    }

    private static List<String> parseHands(List<String> lines, boolean jokersEnabled) {

        lines.sort((o1, o2) ->
        {
            String order = jokersEnabled ? "AKQT98765432J" : "AKQJT98765432";
            // -1 = o1 smaller than 02, 0 = 01 equals 02, 1 = 01 greater than 02

            String hand_1 = o1.split(" ")[0];
            String hand_2 = o2.split(" ")[0];
            Type hand_1_type = determineHand(hand_1, jokersEnabled);
            Type hand_2_type = determineHand(hand_2, jokersEnabled);

            // If both hands are of some type, like FOUR == FOUR
            if (hand_1_type.value == hand_2_type.value) {
                for (int i = 0; i < hand_1.length(); i++) {

                    char char_1 = hand_1.charAt(i);
                    char char_2 = hand_2.charAt(i);

                    if (order.indexOf(char_1) == order.indexOf(char_2)) {
                        continue;
                    }
                    return order.indexOf(char_1) < order.indexOf(char_2) ? 1 : -1;
                }

            }

            return hand_1_type.value > hand_2_type.value ? 1 : -1;
        });


        return lines;
    }

    public static Type determineHand(String hand, boolean jokersEnabled) {
        Map<Character, Long> charOccurences = new HashMap<>();

        for (char s : hand.toCharArray()) {
            if (jokersEnabled) {
                charOccurences.put(s, hand.chars()
                        .filter(e -> e != 'J')
                        .filter(e -> e == s).count());

            } else {
                charOccurences.put(s, hand.chars().filter(e -> e == s).count());
            }
        }

        List<Long> occurences = charOccurences.values().stream()
                .filter(e -> e > 1).collect(Collectors.toList());

        long jokerCount = hand.chars().filter(c -> c == 'J').count();
        if (jokersEnabled && jokerCount > 0) {
            if (occurences.isEmpty()) {
                occurences.add(jokerCount + 1);
            } else {
                long max = occurences.stream().mapToLong(Long::longValue).max().getAsLong();
                int max_index = occurences.indexOf(max);
                occurences.set(max_index, max + jokerCount);
            }
        }

        // FIVE OF A KIND, >=5 is to account for 5 J's, with now give occurences count of 6, which is a bug I'm not going to fix
        if (occurences.size() == 1 && occurences.get(0) >= 5) {
            return Type.FIVE;
        }

        // FOUR OF A KIND
        else if (occurences.size() == 1 && occurences.get(0) == 4) {
            return Type.FOUR;
        }

        // THREE OF A KIND
        else if (occurences.size() == 1 && occurences.get(0) == 3) {
            return Type.THREE;
        }

        // FULL HOUSE
        else if (occurences.size() == 2 && occurences.stream().filter(e -> e == 3).count() == 1 && occurences.stream().filter(e -> e == 2).count() == 1) {
            return Type.FULL_HOUSE;
        }
        // TWO PAIR
        else if (occurences.size() == 2 && occurences.stream().filter(e -> e == 2).count() == 2) {
            return Type.TWO_PAIR;
        }
        // ONE PAIR
        else if (occurences.size() == 1 && occurences.stream().filter(e -> e == 2).count() == 1) {
            return Type.ONE_PAIR;
        }
        // TYPE = HIGHEST
        else {
            return Type.HIGH;
        }
    }
};

record Hand(Integer value, Type type, String orig) {
}