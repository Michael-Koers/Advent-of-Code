package day17;

import config.Year2024;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day17 extends Year2024 {

    long success = Long.MAX_VALUE;

    public static void main(String[] args) {

        var d = new Day17();

        d.solve(null);
    }

    @Override
    public void solvePart1(List<String> lines) {

        List<Long> program = Arrays.stream("2,4,1,3,7,5,0,3,4,1,1,5,5,5,3,0".split(",")).map(Long::parseLong).toList();

        System.out.println(execute(45483412L, program));
    }

    private List<Long> execute(Long a, List<Long> program) {
        long registerA = a;
        Long registerB = 0L;
        Long registerC = 0L;

        List<Long> codes = new ArrayList<>();
        int i = 0;

        while (i < program.size()) {

            long opcode = program.get(i);
            long combo = program.get(i + 1);
            long literal = switch ((int) combo) {
                case 4 -> registerA;
                case 5 -> registerB;
                case 6 -> registerC;
                default -> combo;
            };

            switch ((int) opcode) {
                case 0 -> registerA = (long) (registerA / Math.pow(2, literal));
                case 1 -> registerB ^= combo;
                case 2 -> registerB = literal % 8;
                case 3 -> {
                    if (registerA != 0) {
                        i = (int) combo;
                        continue;
                    }
                }
                case 4 -> registerB ^= registerC;
                case 5 -> codes.add(literal % 8);
                case 6 -> registerB = (long) (registerA / Math.pow(2, literal));
                case 7 -> registerC = (long) (registerA / Math.pow(2, literal));
            }
            i += 2;
        }
        return codes;
    }

    @Override
    public void solvePart2(List<String> lines) {
        List<Long> program = Arrays.stream("2,4,1,3,7,5,0,3,4,1,1,5,5,5,3,0".split(",")).map(Long::parseLong).toList();
        dfs(program, 0, 0);
        System.out.println(success);
    }

    void dfs(List<Long> program, long cur, int pos) {
        for (int i = 0; i < 8; i++) {
            // All operations are done on the last 3 bits of the byte
            long nextNum = (cur << 3) + i;
            List<Long> execResult = execute(nextNum, program);
            if (!execResult.equals(program.subList(program.size() - pos - 1, program.size()))) {
                continue;
            }
            if (pos == program.size() - 1) {
                success = Math.min(success, nextNum);
                return;
            }
            dfs(program, nextNum, pos + 1);
        }
    }

}
