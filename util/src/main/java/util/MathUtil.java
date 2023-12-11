package util;

public class MathUtil {

    /**
     * In mathematics, the greatest common divisor (GCD) of two or more integers, which are not all zero, is the largest positive integer that divides each of the integers.
     * <p>
     * Example:
     * gcd(5, 25) = 5
     * </p>
     *
     * @param a long 1 to compare
     * @param b long 2 to compare
     * @return Value indicating the GCD
     */
    public static long gcd(long a, long b) {
        while (b > 0) {
            long temp = b;
            b = a % b; // % is remainder
            a = temp;
        }
        return a;
    }

    /**
     * In mathematics, the greatest common divisor (GCD) of two or more integers, which are not all zero, is the largest positive integer that divides each of the integers.
     * <p>
     * Example:
     * gcd(8, 12, 20, 80, 120) = 4
     * </p>
     *
     * @param input Array to GCD
     * @return Value indicating the GCD
     */
    public static long gcd(Long[] input) {
        long result = input[0];
        for (int i = 1; i < input.length; i++) {
            result = gcd(result, input[i]);
        }
        return result;
    }

    /**
     * In arithmetic and number theory, the least common multiple, lowest common multiple, or smallest common multiple of two integers a and b,
     * usually denoted by lcm(a, b), is the smallest positive integer that is divisible by both a and b
     * <p>
     * Example:
     * lcm(5,4) = 20
     * </p>
     *
     * @return The first common multiple of long a and long b
     */
    public static long lcm(long a, long b) {
        return a * (b / gcd(a, b));
    }

    /**
     * In arithmetic and number theory, the least common multiple, lowest common multiple, or smallest common multiple of two integers a and b,
     * usually denoted by lcm(a, b), is the smallest positive integer that is divisible by both a and b
     * <p>
     * Example:
     * lcm(5,4,3,2) = 60
     * </p>
     *
     * @return The first common multiple of long a and long b
     */
    public static long lcm(Long[] input) {
        long result = input[0];
        for (int i = 1; i < input.length; i++) result = lcm(result, input[i]);
        return result;
    }
}
