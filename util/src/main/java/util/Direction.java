package util;

public enum Direction {

    LEFT(new Point(-1, 0)),
    RIGHT(new Point(1, 0)),
    UP(new Point(0, 1)),
    DOWN(new Point(0, -1)),

    LEFT_UP(new Point(-1, 1)),
    LEFT_DOWN(new Point(-1, -1)),
    RIGHT_UP(new Point(1, 1)),
    RIGHT_DOWN(new Point(1, -1));

    public final Point movement;

    Direction(Point movement) {
        this.movement = movement;
    }
}
