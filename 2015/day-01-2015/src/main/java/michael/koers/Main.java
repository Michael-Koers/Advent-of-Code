package michael.koers;

import util.FileInput;
import util.MathUtil;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws URISyntaxException, IOException {
//        List<String> lines = FileInput.read(FileInput.INPUT, Main.class);

//        solvePart1And2(lines);
        System.out.println(MathUtil.gcd(42L, 12L));
    }

    private static void solvePart1And2(List<String> lines) {
        String line = lines.get(0);

        long total = 0L;
        boolean reachedBasement = false;

        String[] split = line.split("");
        for(int i = 0; i < split.length; i++){
            String s = split[i];
            switch (s){
                case "(" -> total++;
                case ")" -> total--;
            }

            if (total == -1 && !reachedBasement) {
                System.out.printf("Solved part 2, reached basement at position: %s%n", i+1);
                reachedBasement = true;
            }
        }

        System.out.printf("Solved part 1, floor: %s%n", total);
    }
}