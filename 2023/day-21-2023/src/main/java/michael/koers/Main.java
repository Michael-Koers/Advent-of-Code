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

        solvePart1(field, 64);
    }

    private static void solvePart1(Field field, int steps) {

        Set<Point> nextSteps = new HashSet<>();
        Set<Point> currentSteps = new HashSet<>();
        currentSteps.add(field.start());

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
        prettyPrint(field, currentSteps);
        System.out.printf("Solved part 1, total garden plots reached: %s%n", currentSteps.size());
    }

    private static void prettyPrint(Field field, Set<Point> currentSteps) {
        for (int y = 0; y < field.rows(); y++) {
            for (int x = 0; x < field.cols(); x++) {
                if(currentSteps.contains(new Point(x, y))){
                    System.out.print("O");
                } else if (field.rocks().contains(new Point(x, y))){
                    System.out.print("#");
                } else{
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