package michael.koers;

import util.Direction;
import util.FileInput;
import util.Point;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Main {
    public static void main(String[] args) throws URISyntaxException, IOException {

        List<String> read = FileInput.read(FileInput.INPUT, Main.class);

        Field field = parseInput(read);

//        solvePart1(field, 64);
        solvePart2(field, 26501365L);
    }

    private static void solvePart2(Field field, long steps) {
        // We will have even/odd grids that are fully reachable, and some grids at the far reaches that we can't fully cover

        // Field is a perfect square
        assert field.rows() == field.cols() : "Assertion is false";
        int grid_size = field.cols();

        // We start in the middle of the grid
        assert field.start().y() == field.start().x() && field.start().x() == Math.floorDiv(grid_size, 2) : "Assertion is false";

        // Number of steps equals to 1 half grid + full grid sizes, with which we prove that we will perfectly reach
        // the end of a grid with our final step in straight horizontal / vertical direction
        assert steps % grid_size == Math.floorDiv(grid_size, 2) : "Assertion is false";

        // Number of grids that we will be able to fully cover with our number of steps
        long grid_width = Math.floorDiv(steps, grid_size) -1;

        // Number of odd grids (meaning, starting grid with odd number of steps)
        long odd_grids = (long) Math.pow(Math.floorDiv(grid_width, 2) * 2 + 1, 2);
        // Number of even grids (meaning, starting grid with even number of steps)
        long even_grids = (long) Math.pow(Math.floorDiv(grid_width+1, 2) * 2, 2);

        // These are the odds/even grids we will fully cover, so calculate to total number of steps once, starting point doesn't matter
        // Just make sure that we fully cover the grid with our steps
        long odd_grid_steps = calculatePlotsReached(field.start(), field, grid_size *2+1);
        long even_grid_steps = calculatePlotsReached(field.start(), field, grid_size *2);

        // Calculate number of plots reached for outermost grids on top/left/bottom/right
        long steps_t = calculatePlotsReached(new Point(field.start().x(), grid_size-1), field, grid_size-1);
        long steps_r = calculatePlotsReached(new Point(0, field.start().y()), field, grid_size-1);
        long steps_b = calculatePlotsReached(new Point(field.start().x(), 0), field, grid_size-1);
        long steps_l = calculatePlotsReached(new Point(grid_size-1, field.start().y()), field, grid_size-1);

        // Calculate small corner fields reached
        long small_tr = calculatePlotsReached(new Point(0 , grid_size-1), field, Math.floorDiv(grid_size, 2) - 1);
        long small_tl = calculatePlotsReached(new Point(grid_size-1 , grid_size-1), field, Math.floorDiv(grid_size, 2) - 1);
        long small_br = calculatePlotsReached(new Point(0 , 0), field, Math.floorDiv(grid_size, 2) - 1);
        long small_bl = calculatePlotsReached(new Point(grid_size-1 , 0), field, Math.floorDiv(grid_size, 2) - 1);

        // Calculate large corner fields reached
        long large_tr = calculatePlotsReached(new Point(0 , grid_size-1), field, Math.floorDiv(grid_size * 3, 2) - 1);
        long large_tl = calculatePlotsReached(new Point(grid_size-1 , grid_size-1), field, Math.floorDiv(grid_size * 3, 2) - 1);
        long large_br = calculatePlotsReached(new Point(0 , 0), field, Math.floorDiv(grid_size * 3, 2) - 1);
        long large_bl = calculatePlotsReached(new Point(grid_size-1 , 0), field, Math.floorDiv(grid_size * 3, 2) - 1);

        long total =
                odd_grids * odd_grid_steps                  // All odd grid steps
                + even_grids * even_grid_steps              // All even grid steps
                + steps_t + steps_r + steps_b + steps_l     // All straight outermost grids steps
                + (grid_width + 1) * (small_tr + small_tl + small_br + small_bl) // All small outer grids steps
                + grid_width * (large_tr + large_tl + large_br + large_bl); // All large outer grids steps

        System.out.printf("Solved part 2, total plots reached : %s%n", total);
    }

    private static void solvePart1(Field field, int steps) {
        long totalSteps = calculatePlotsReached(field.start(), field, steps);
        System.out.printf("Solved part 1, total garden plots reached: %s%n", totalSteps);
    }

    private static Long calculatePlotsReached(Point start, Field field, int steps) {
        Set<Point> nextSteps = new HashSet<>();
        Set<Point> currentSteps = new HashSet<>();
        currentSteps.add(start);

        for (int i = 0; i < steps; i++) {

            for (Point currentStep : currentSteps) {

                for (Direction direction : List.of(Direction.UP, Direction.LEFT, Direction.RIGHT, Direction.DOWN)) {
                    if (isDirectionFree(field, currentStep, direction)) {
                        nextSteps.add(currentStep.moveDirection(direction));
                    }
                }
            }
            currentSteps = new HashSet<>(nextSteps);
            nextSteps.clear();
        }
        return (long) currentSteps.size();
    }

    private static void prettyPrint(Field field, Set<Point> currentSteps) {
        for (int y = 0; y < field.rows(); y++) {
            for (int x = 0; x < field.cols(); x++) {
                if (currentSteps.contains(new Point(x, y))) {
                    System.out.print("O");
                } else if (field.rocks().contains(new Point(x, y))) {
                    System.out.print("#");
                } else {
                    System.out.print(".");
                }
            }
            System.out.println();
        }
    }

    private static boolean isDirectionFree(Field field, Point currentStep, Direction direction) {
        Point next = currentStep.moveDirection(direction);
        return next.x() >= 0
                && next.x() < field.cols()
                && next.y() >= 0
                && next.y() < field.rows()
                && !field.rocks().contains(next);
    }

    private static Field parseInput(List<String> read) {

        Set<Point> rocks = new HashSet<>();
        Point start = null;
        for (int y = 0; y < read.size(); y++) {
            for (int x = 0; x < read.get(y).length(); x++) {

                char c = read.get(y).charAt(x);

                if (c == '#') {
                    rocks.add(new Point(x, y));
                } else if (c == 'S') {
                    start = new Point(x, y);
                }
            }
        }
        return new Field(start, rocks, read.size(), read.get(0).length());
    }
}

record Field(Point start, Set<Point> rocks, int rows, int cols) {
};