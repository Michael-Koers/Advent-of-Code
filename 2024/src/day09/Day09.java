package day09;

import config.Year2024;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Day09 extends Year2024 {

    public static void main(String[] args) throws IOException {
        var d = new Day09();

        var input = d.readInput();

        d.stopwatch.start();
        d.solvePart1(input);
        d.solvePart2(input);
        d.stopwatch.prettyPrint();
    }

    @Override
    public void solvePart1(final List<String> lines) {

        var split = lines.getFirst().split("");
        List<Space> uncompacted = new ArrayList<>();
        int id = 0;

        for (int i = 0; i < split.length; i++) {

            var character = Integer.parseInt(split[i]);

            if (i % 2 == 1) {
                for (int j = 0; j < character; j++) {
                    uncompacted.add(new Empty());
                }
            } else {
                for (int j = 0; j < character; j++) {
                    uncompacted.add(new File(id));
                }
                id++;
            }
        }

        // Compact
        for (int i = 0; i < uncompacted.size(); i++) {
            if (uncompacted.get(i) instanceof File) {
                continue;
            }

            var move = uncompacted.removeLast();
            while (move instanceof Empty) {
                move = uncompacted.removeLast();
            }

            uncompacted.set(i, move);
        }

        long sum = 0;

        for (int i = 0; i < uncompacted.size(); i++) {
            sum += (long) i * ((File) uncompacted.get(i)).id();
        }

        System.out.println("Part 1: " + sum);
    }

    @Override
    public void solvePart2(final List<String> lines) {

        var split = lines.getFirst().split("");
        List<Block> blocks = new ArrayList<>();

        int index = 0;
        int id = 0;

        for (int i = 0; i < split.length; i++) {

            var character = Integer.parseInt(split[i]);

            if (character == 0) continue;

            // Empty
            if (i % 2 == 1) {
                blocks.add(new Block(new Empty(), character, index));
                index += character;
            }
            // Disk usage
            else {
                blocks.add(new Block(new File(id), character, index));
                index += character;
                id++;
            }
        }

        // debug printing
//        debugPrint(blocks);

//        System.out.println();

        List<Block> fileBlocks = new ArrayList<>(blocks.reversed().stream().filter(b -> b.space() instanceof File).toList());

        for (Block file : fileBlocks) {

            var emptyFieldOptional = blocks.stream()
                    .filter(b -> b.space() instanceof Empty)
                    .filter(b -> b.index() < file.index())
                    .filter(b -> b.length() >= file.length())
                    .findFirst();

            // Current file can't be moved forward
            if (emptyFieldOptional.isEmpty()) continue;

            Block emptyBlock = emptyFieldOptional.get();
            blocks.remove(emptyBlock);
            blocks.remove(file);

            // Move positions, and shorten empty space
            var newFile = file.moveIndex(emptyBlock.index());
            var oldEmpty = emptyBlock.removeLength(file.length());

            blocks.add(newFile);
            blocks.add(oldEmpty);


            blocks.sort((a, b) -> a.index() > b.index() ? 1 : -1);
//            debugPrint(blocks);
        }

        // debug printing
//        debugPrint(blocks);

        // Correct answer for test input
//        System.out.println("00992111777.44.333....5555.6666.....8888..");

        long total = 0;
        for (Block block : blocks) {
            total += block.getValue();
        }
        System.out.println("Part 2: " + total);
    }

    private static void debugPrint(List<Block> blocks) {
        for (Block block : blocks) {
            for (int i = 0; i < block.length(); i++) {
                if (block.space() instanceof File f) {
                    System.out.print(f.id());
                } else {
                    System.out.print(".");
                }
            }
        }
        System.out.println();
    }


}

interface Space {
}

record Empty() implements Space {
}

record File(int id) implements Space {

}

record Block(Space space, int length, int index) {

    Block removeLength(int length) {
        return new Block(this.space, this.length - length, this.index + length);
    }

    Block moveIndex(int index) {
        return new Block(this.space, this.length, index);
    }

    Block appendEmpty(Block right, int extraSpace) {
        return new Block(this.space, this.length + right.length + extraSpace, this.index);
    }

    long getValue() {
        long value = 0;

        if (this.space() instanceof File f) {
            for (int i = 0; i < this.length; i++) {
                value += (long) (i + index) * f.id();
            }
        }
        return value;
    }
}
