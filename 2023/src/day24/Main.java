package day24;

import util.FileInput;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

    public static PathCrossing IDENTICAL = new PathCrossing(true, 0, 0);

    public static void main(String[] args) throws URISyntaxException, IOException {

        List<String> read = FileInput.read(FileInput.INPUT, Main.class);
        List<Hailstone> hailstones = parseInput(read);

        // Test input
//        solvePart1(hailstones, 7L, 27L);

        // Real input
//        solvePart1(hailstones, 200000000000000L, 400000000000000L);
        solvePart2(hailstones);
    }

    private static void solvePart2(List<Hailstone> hailstones) {

        // Straight up stolen from internet: https://pastebin.com/tFJ40WH6

        Hailstone h1 = hailstones.get(0);
        Hailstone h2 = hailstones.get(1);

        int range = 500;
        for (int vx = -range; vx <= range; vx++) {
            for (int vy = -range; vy <= range; vy++) {
                for (int vz = -range; vz <= range; vz++) {

                    if (vx == 0 || vy == 0 || vz == 0) {
                        continue;
                    }

                    // Find starting point for rock that will intercept first two hailstones (x,y) on this trajectory

                    // simultaneous linear equation (from part 1):
                    // H1:  x = A + a*t   y = B + b*t
                    // H2:  x = C + c*u   y = D + d*u
                    //
                    //  t = [ d ( C - A ) - c ( D - B ) ] / ( a * d - b * c )
                    //
                    // Solve for origin of rock intercepting both hailstones in x,y:
                    //     x = A + a*t - vx*t   y = B + b*t - vy*t
                    //     x = C + c*u - vx*u   y = D + d*u - vy*u

                    long A = h1.px(), a = h1.vx() - vx;
                    long B = h1.py(), b = h1.vy() - vy;
                    long C = h2.px(), c = h2.vx() - vx;
                    long D = h2.py(), d = h2.vy() - vy;

                    // skip if division by 0
                    if (c == 0 || (a * d) - (b * c) == 0) {
                        continue;
                    }

                    // Rock intercepts H1 at time t
                    long t = (d * (C - A) - c * (D - B)) / ((a * d) - (b * c));

                    // Calculate starting position of rock from intercept point
                    long x = h1.px() + h1.vx() * t - vx * t;
                    long y = h1.py() + h1.vy() * t - vy * t;
                    long z = h1.pz() + h1.vz() * t - vz * t;


                    // check if this rock throw will hit all hailstones

                    boolean hitall = true;
                    for (int i = 0; i < hailstones.size(); i++) {

                        Hailstone h = hailstones.get(i);
                        long u;
                        if (h.vx() != vx) {
                            u = (x - h.px()) / (h.vx() - vx);
                        } else if (h.vy() != vy) {
                            u = (y - h.py()) / (h.vy() - vy);
                        } else if (h.vz() != vz) {
                            u = (z - h.pz()) / (h.vz() - vz);
                        } else {
                            throw new RuntimeException();
                        }

                        if ((x + u * vx != h.px() + u * h.vx()) || (y + u * vy != h.py() + u * h.vy()) || (z + u * vz != h.pz() + u * h.vz())) {
                            hitall = false;
                            break;
                        }
                    }

                    if (hitall) {
                        System.out.printf("%d %d %d   %d %d %d   %d %n", x, y, z, vx, vy, vz, x + y + z);
                    }
                }
            }
        }
    }

    private static void solvePart1(List<Hailstone> hailstones, long min, long max) {

        long count = 0L;

        // Compare current hailstone with all future hailstones
        for (int i = 0; i < hailstones.size(); i++) {
            for (int j = i + 1; j < hailstones.size(); j++) {

                System.out.printf("Hailstone %s : %s%n", i, hailstones.get(i).toString());
                System.out.printf("Hailstone %s : %s%n", j, hailstones.get(j).toString());
                PathCrossing crossing = findIntersection(hailstones.get(i), hailstones.get(j));

                if (crossing == null) {
                    System.out.println("Lines are parallel to eachother");
                } else if (crossing == IDENTICAL) {
                    System.out.println("Lines are identical to eachother");
                    count++;
                } else {

                    if (!crossing.inFuture()) {
                        System.out.println("Lines crossed eachother in the past");
                    } else if ((crossing.cx() >= min && crossing.cx() <= max) && (crossing.cy() >= min && crossing.cy() <= max)) {
                        System.out.printf("Lines crossed at %s, %s %n", crossing.cy(), crossing.cx());
                        count++;
                    }

                }
            }
        }

        System.out.printf("Final line cross count: %s%n", count);
    }

    private static PathCrossing findIntersection(Hailstone h1, Hailstone h2) {

        // Some linear algebra-ing?
        double a1 = (double) h1.vy() / h1.vx();
        double b1 = h1.py() - a1 * h1.px();

        double a2 = (double) h2.vy() / h2.vx();
        double b2 = h2.py() - a2 * h2.px();

        if (a1 == a2) {
            if (b1 == b2) {
                // Lijnen zijn identiek
                return IDENTICAL;
            }
            // Lijnen zijn parallel
            return null;

        }
        double crossingX = (b2 - b1) / (a1 - a2);
        double crossingY = crossingX * a1 + b1;
        boolean inFuture = crossingX > h1.px() == h1.vx() > 0 && crossingX > h2.px() == h2.vx() > 0;
        return new PathCrossing(inFuture, crossingX, crossingY);
    }


    private static List<Hailstone> parseInput(List<String> read) {
        return read.stream().map(Hailstone::fromString).collect(Collectors.toList());
    }
}


record Hailstone(long px, long py, long pz, long vx, long vy, long vz) {

    public static Hailstone fromString(String line) {
        String[] parts = line.split("@");

        return new Hailstone(
                Long.parseLong(parts[0].split(", ")[0].trim()),
                Long.parseLong(parts[0].split(", ")[1].trim()),
                Long.parseLong(parts[0].split(", ")[2].trim()),
                Long.parseLong(parts[1].split(", ")[0].trim()),
                Long.parseLong(parts[1].split(", ")[1].trim()),
                Long.parseLong(parts[1].split(", ")[2].trim())
        );
    }
};

record PathCrossing(boolean inFuture, double cx, double cy) {
};