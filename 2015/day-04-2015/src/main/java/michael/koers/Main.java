package michael.koers;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Main {
    public static void main(String[] args) throws NoSuchAlgorithmException {

        String key = "abcdef609043";

        solvePart1(key);
    }

    private static void solvePart1(String key) throws NoSuchAlgorithmException {

        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(key.getBytes(StandardCharsets.UTF_8));
        byte[] digest = md.digest();
        BigInteger bigInt = new BigInteger(1, digest);
        String hash = bigInt.toString(32);

        System.out.println(hash);


//        System.out.printf("Solved part 1, secret value is: %s%n");
    }
}