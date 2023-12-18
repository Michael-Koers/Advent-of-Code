package util;

public record Point(long x, long y) {

    public boolean isAdjacent(Point other) {
        return (Math.abs(this.x - other.x) < 2) && (Math.abs(this.y - other.y) < 2);
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
}
