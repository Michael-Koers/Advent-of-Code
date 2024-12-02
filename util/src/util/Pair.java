package util;

public record Pair(long left, long right) {

    public long apart() {
        return Math.abs(this.left() - this.right());
    }
}
