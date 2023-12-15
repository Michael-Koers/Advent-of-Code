package michael.koers;

import util.FileInput;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class Main {
    public static void main(String[] args) throws URISyntaxException, IOException {


        List<String> read = FileInput.read(FileInput.INPUT, Main.class);
        String[] lines = read.get(0).split(",");

        solvePart1(lines);
        solvePart2(lines);
    }

    private static void solvePart2(String[] lines) {
        Map<Long, Stack<Box>> boxes = new HashMap<>();
        for (String line : lines) {
            long boxIndex = 0L;
            for (int i = 0; i < line.length(); i++) {
                char c = line.charAt(i);
                // =/- are operations
                if (c == '=') {
                    String label = line.substring(0,i);
                    Box box = new Box(label, Integer.parseInt(String.valueOf(line.charAt(i + 1))), line);
                    Stack<Box> boxStack = boxes.getOrDefault(boxIndex, new Stack<>());

                    // Already present? replace
                    int index = searchIndexBox(boxStack, label);
                    if (index >= 0) {
                        boxStack.set(index, box);
                    } else {
                        boxStack.add(box);
                    }

                    boxes.putIfAbsent(boxIndex, boxStack);
                    System.out.printf("Label %s went inside box %s, box: %s%n", line, boxIndex, box);
                    break;
                } else if (c == '-') {
                    // Remove box, move everything 'forward'
                    String label = line.substring(0,i);
                    Stack<Box> boxStack = boxes.getOrDefault(boxIndex, new Stack<>());

                    // No stacks in this box yet, nothing to remove, skip
                    if (boxStack.isEmpty()) break;

                    int index = searchIndexBox(boxStack, label);
                    if(index >= 0){
                        boxStack.remove(index);
                    } else{
                        System.out.printf("Label %s removed no lenses box %s was empty%n", label, boxIndex);
                    }

                    break;
                } else {
                    boxIndex += (int) c;
                    boxIndex = boxIndex * 17;
                    boxIndex = boxIndex % 256;
                }
            }
        }

        long total = 0L;

        for (Map.Entry<Long, Stack<Box>> longStackEntry : boxes.entrySet()) {
            Long boxIndex = longStackEntry.getKey();
            Stack<Box> boxStack_1 = longStackEntry.getValue();
            for (int j = 0; j < boxStack_1.size(); j++) {
                long value = (boxIndex + 1) * (j+1) * boxStack_1.get(j).focalLength();
                System.out.printf("Box %s has focusing power of %s%n", boxStack_1.get(j).label(), value);
                total += value;
            }
        }
        System.out.printf("Solved part 2, total focussing power: %s%n", total);
    }

    private static int searchIndexBox(Stack<Box> boxes, String label) {
        for (Box box : boxes) {
            if (box.label().equals(label)) {
                return boxes.indexOf(box);
            }
        }
        return -1;
    }


    private static void solvePart1(String[] lines) {

        long sum = 0L;
        for (String line : lines) {
            long hashValue = 0L;
            for (char c : line.toCharArray()) {
                hashValue += (int) c;
                hashValue = hashValue * 17;
                hashValue = hashValue % 256;
            }
            sum += hashValue;
            //System.out.printf("HASH value of %s = %s%n", line, hashValue);
        }

        System.out.printf("Solved part 1, total HASH sum: %s%n", sum);
    }
}

record Box(String label, Integer focalLength, String line) {
};