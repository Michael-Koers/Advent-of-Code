package util;

import java.util.Set;

/**
 * Record to represent a range of numbers, end is inclusive!
 * @param start inclusive!
 * @param end inclusive!
 */
public record Range(long start, long end) {

    public boolean contains(long position) {
        return position >= start && position <= end;
    }

    public boolean overlap(Range other){
        return this.end >= other.start && this.start <= other.end;
    }

    public Range merge(Range other){
        return new Range(Math.min(this.start, other.start), Math.max(this.end, other.end));
    }

    public Range merge(Set<Range> others){
        long newStart = this.start;
        long newEnd = this.end;

        for (Range other : others) {
            newStart = Math.min(newStart, other.start);
            newEnd = Math.max(newEnd, other.end);
        }

        return new Range(newStart, newEnd);
    }

    public long value() {
        return this.end - this.start + 1;
    }

}
