package day14;

import config.Year2024;
import util.Point;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Day14 extends Year2024 {

    // Grid size
    // test = 11, real = 101
    int width = 101;
    // test = 7, real = 103
    int length = 103;

    public static void main(String[] args) throws IOException {
        var d = new Day14();
        d.solve(d.readInput());
    }

    @Override
    public void solvePart1(List<String> lines) {
        List<Robot> robots = parseRobots(lines);

        int seconds = 100;

        List<Robot> moved = new ArrayList<>();
        for (Robot robot : robots) {

            // Instead of simulating movement, we just calculate where the robots will be after 100 seconds
            int newX = (int) (((robot.location().x() + robot.velocity().x() * seconds) % width) + width) % width;
            int newY = (int) (((robot.location().y() + robot.velocity().y() * seconds) % length) + length) % length;

            moved.add(new Robot(new Point(newX, newY), robot.velocity()));
        }

        var topLeft = moved.stream()
                .filter(r -> r.location().x() < (width - 1) / 2)
                .filter(r -> r.location().y() < (length - 1) / 2)
                .count();

        var topRight = moved.stream()
                .filter(r -> r.location().x() > (width - 1) / 2)
                .filter(r -> r.location().y() < (length - 1) / 2)
                .count();

        var bottomLeft = moved.stream()
                .filter(r -> r.location().x() < (width - 1) / 2)
                .filter(r -> r.location().y() > (length - 1) / 2)
                .count();

        var bottomRight = moved.stream()
                .filter(r -> r.location().x() > (width - 1) / 2)
                .filter(r -> r.location().y() > (length - 1) / 2)
                .count();

        long result = topLeft * topRight * bottomLeft * bottomRight;

        System.out.println("Part 1 : " + result);
    }

    @Override
    public void solvePart2(List<String> lines) {
        List<Robot> robots = parseRobots(lines);

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

            // Use inherent behaviour of Sets to remove duplicates
            Set<Point> collect = tmp.stream().map(Robot::location).collect(Collectors.toSet());

            // Apparently at the moment of forming the Christmas tree, none of the robots overlap.
            if (collect.size() == robots.size()) {
                isChristmasTree = true;
            }
        }

        System.out.println("Part 2 : " + seconds);
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
}

record Robot(Point location, Point velocity) {
}
