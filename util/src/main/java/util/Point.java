package util;

public record Point(int x, int y) {

    boolean isAdjacent(Point other) {
        return (Math.abs(this.x - other.x) < 2) && (Math.abs(this.y - other.y) < 2);
    }

    Point moveTo(Point destination) {
        return new Point(destination.x, destination.y);
    }

    Point moveDistance(Point distance) {
        return new Point(this.x + distance.x, this.y + distance.y);
    }

    Point moveDirection(Direction direction) {
        return new Point(this.x + direction.movement.x, this.y + direction.movement.y);
    }
}
