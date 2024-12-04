package util;

public enum Direction {

    LEFT(new Point(-1, 0)),
    RIGHT(new Point(1, 0)),
    UP(new Point(0, -1)),
    DOWN(new Point(0, 1)),

    LEFT_UP(new Point(-1, -1)),
    LEFT_DOWN(new Point(-1, 1)),
    RIGHT_UP(new Point(1, -1)),
    RIGHT_DOWN(new Point(1, 1)),

    NONE(new Point(0, 0));
    public final Point movement;

    Direction(Point movement) {
        this.movement = movement;
    }

    public Direction opposite() {
        return switch (this) {
            case RIGHT -> LEFT;
            case UP -> DOWN;
            case DOWN -> UP;
            case LEFT -> RIGHT;
            case LEFT_UP -> RIGHT_DOWN;
            case LEFT_DOWN -> RIGHT_UP;
            case RIGHT_UP -> LEFT_DOWN;
            case RIGHT_DOWN -> LEFT_UP;
            case NONE -> NONE;
        };
    }

    public Direction turnLeft() {
        return switch (this) {
            case RIGHT -> UP;
            case UP -> LEFT;
            case LEFT -> DOWN;
            case DOWN -> RIGHT;
            default -> throw new UnsupportedOperationException("Direction " + this + " is not supported yet");
        };
    }

    public Direction turnRight() {
        return switch (this) {
            case RIGHT -> DOWN;
            case DOWN -> LEFT;
            case LEFT -> UP;
            case UP -> RIGHT;
            default -> throw new UnsupportedOperationException("Direction " + this + " is not supported yet");
        };
    }
}
