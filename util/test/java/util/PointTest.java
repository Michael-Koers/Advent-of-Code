//package util;
//
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.CsvSource;
//import org.junit.jupiter.params.provider.EnumSource;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//public class PointTest {
//
//    @ParameterizedTest
//    @CsvSource({"9,9", "9,10", "9,11", "10,9", "10,10", "10,11", "11,9", "11,10", "11,11"})
//    void testIsAdjacent(int otherX, int otherY) {
//
//        Point point = new Point(10, 10);
//        Point other = new Point(otherX, otherY);
//
//        assertTrue(point.isAdjacent(other));
//    }
//
//    @ParameterizedTest
//    @CsvSource({"0,0", "8,8", "8,9", "8,10", "8,11", "12,12", "100,100", "-10,-10"})
//    void testIsNotAdjacent(int otherX, int otherY) {
//
//        Point point = new Point(10, 10);
//        Point other = new Point(otherX, otherY);
//
//        assertFalse(point.isAdjacent(other));
//    }
//
//    @ParameterizedTest
//    @EnumSource(Direction.class)
//    void testMoveDirection(Direction direction) {
//
//        Point start = new Point(10, 10);
//
//        Point end = start.moveDirection(direction);
//
//        assertTrue(start.isAdjacent(end));
//    }
//
//    @ParameterizedTest
//    @CsvSource({"-100,-100", "0,0", "100,100", "2147483647, 2147483647", "-2147483648, -2147483648"})
//    void testMoveTo(int newX, int newY) {
//
//        Point start = new Point(10, 10);
//
//        Point destination = new Point(newX, newY);
//
//        Point end = start.moveTo(destination);
//
//        assertEquals(newX, end.x());
//        assertEquals(newY, end.y());
//    }
//
//    @ParameterizedTest
//    @CsvSource({"10,10", "-10,10", "-10,-10", "10,-10", "0,0", "500,500"})
//    void testMoveDistance(int distX, int distY) {
//
//        Point start = new Point(10, 10);
//
//        Point distance = new Point(distX, distY);
//
//        Point end = start.moveDistance(distance);
//
//        assertEquals(start.x() + distX, end.x());
//        assertEquals(start.y() + distY, end.y());
//    }
//}
