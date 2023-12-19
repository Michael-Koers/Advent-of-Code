package util;

public record Range(long start, long end) {

    public boolean inRange(long position) {
        return position >= start && position <= end;
    }

    public long value(){
        return this.end - this.start+1;
    }

}
