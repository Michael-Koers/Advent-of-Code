package day14;

import config.Year2024;
import util.Point;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Day14 extends Year2024 {
    public static void main(String[] args) throws IOException {
        var d = new Day14();

        var lines = d.readInput();

        d.solvePart2(lines);
    }

    @Override
    public void solvePart1(List<String> lines) {
        List<Robot> robots = parseRobots(lines);

        // test = 11, real = 101
        int width = 101;

        // test = 7, real = 103
        int length = 103;

        int seconds = 100;

        List<Robot> moved = new ArrayList<>();
        for (Robot robot : robots) {

            int newX = (int) (((robot.location().x() + robot.velocity().x() * seconds) % width) + width) % width;
            int newY = (int) (((robot.location().y() + robot.velocity().y() * seconds) % length) + length) % length;

            moved.add(new Robot(new Point(newX, newY), robot.velocity()));
        }

        var topleft = moved.stream()
                .filter(r -> r.location().x() < (width - 1) / 2)
                .filter(r -> r.location().y() < (length - 1) / 2)
                .count();

        var topRight = moved.stream()
                .filter(r -> r.location().x() > (width - 1) / 2)
                .filter(r -> r.location().y() < (length - 1) / 2)
                .count();

        var bottomLeft = moved.stream()
                .filter(r -> r.location().x() < ((width - 1) / 2))
                .filter(r -> r.location().y() > (length - 1) / 2)
                .count();

        var bottomRight = moved.stream()
                .filter(r -> r.location().x() > (width - 1) / 2)
                .filter(r -> r.location().y() > (length - 1) / 2)
                .count();

        long result = topleft * topRight * bottomLeft * bottomRight;

        prettyPrint(moved, width, length);

        System.out.println("Part 1 : " + result);
    }

    private void prettyPrint(List<Robot> moved, int width, int length) {
        for (int y = 0; y < length; y++) {
            for (int x = 0; x < width; x++) {

                if (y == (length - 1) / 2) {
                    System.out.print("#");
                    continue;
                } else if (x == (width - 1) / 2) {
                    System.out.print("#");
                    continue;
                }
                var point = new Point(x, y);

                var count = moved.stream()
                        .filter(r -> r.location().equals(point))
                        .count();

                System.out.print(count == 0 ? "." : count);

            }
            System.out.println();
        }
    }

    private List<Robot> parseRobots(List<String> lines) {
        List<Robot> robots = new ArrayList<>();
        for (String line : lines) {

            var position = line.split(" ")[0].replace("p=", "").trim().split(",");
            var velocity = line.split(" ")[1].replace("v=", "").trim().split(",");

            var location = new Point(Integer.parseInt(position[0]), Integer.parseInt(position[1]));
            var speed = new Point(Integer.parseInt(velocity[0]), Integer.parseInt(velocity[1]));

            robots.add(new Robot(location, speed));
        }
        return robots;
    }

    @Override
    public void solvePart2(List<String> lines) {
        List<Robot> robots = parseRobots(lines);

        // test = 11, real = 101
        int width = 101;

        // test = 7, real = 103
        int length = 103;

        int seconds = 0;
        boolean isChristmasTree = false;

        while (!isChristmasTree) {

            List<Robot> tmp = new ArrayList<>();
            seconds++;

            for (Robot robot : robots) {

                int newX = (int) (((robot.location().x() + robot.velocity().x() * seconds) % width) + width) % width;
                int newY = (int) (((robot.location().y() + robot.velocity().y() * seconds) % length) + length) % length;

                tmp.add(new Robot(new Point(newX, newY), robot.velocity()));
            }

            Set<Point> collect = tmp.stream().map(Robot::location).collect(Collectors.toSet());

            // Apparently at the moment of forming the Christmas tree, none of the robots overlap...
            if (collect.size() == robots.size()) {
                prettyPrint(robots, width, length);
                isChristmasTree = true;
            }
        }

        System.out.println("Part 2 : " + seconds);
    }
}

record Robot(Point location, Point velocity) {
}
