package util;

import java.util.List;

public record Point(long x, long y) {

    public boolean isAdjacentDiagonally(Point other) {
        return (Math.abs(this.x - other.x) < 2) && (Math.abs(this.y - other.y) < 2);
    }

    public boolean isAdjacent(Point other) {
        return (Math.abs(this.x - other.x) == 1) && (Math.abs(this.y - other.y) == 0)
                || (Math.abs(this.x - other.x) == 0) && (Math.abs(this.y - other.y) == 1);
    }

    public boolean anyAdjacent(List<Point> other) {
        return other.stream().anyMatch(this::isAdjacent);
    }

    public Point moveTo(Point destination) {
        return new Point(destination.x, destination.y);
    }

    public Point moveDistance(Point distance) {
        return new Point(this.x + distance.x, this.y + distance.y);
    }

    public Point moveDirection(Direction direction) {
        return new Point(this.x + direction.movement.x, this.y + direction.movement.y);
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }
}
