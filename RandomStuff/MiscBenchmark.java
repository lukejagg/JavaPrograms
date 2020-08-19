package RandomStuff;

import java.util.ArrayList;
import java.util.HashMap;

public class MiscBenchmark {

    public static int[] test(int i, int j) {
        return new int[] {i, j};
    }

    public static String test2(int i, int j) {
        return i + "," + j;
    }

    public static void main(String[] args) {
        final int n = 1000;
        double n0 = 0.0;
        double n1 = 0.0;
        long t = System.nanoTime();
        ArrayList<Boolean> t1 = new ArrayList<Boolean>();
        for (int i = 0; i < 40000; i++) {
            t1.add(i, true);
        }
        System.out.println((System.nanoTime() - t) / 1000000000.0);
        t = System.nanoTime();
        ArrayList<Boolean> t2 = new ArrayList<Boolean>();
        for (int i = 0; i < 40000; i++) {
            t2.add(true);
        }
        System.out.println((System.nanoTime() - t) / 1000000000.0);
//        while (true) {
//            long t = System.nanoTime();
//            HashMap<int[], Boolean> cells = new HashMap();
//            for (int i = 0; i < n; i++) {
//                for (int j = 0; j < n; j++) {
//                    cells.put(test(i, j), true);
//                }
//            }
//            cells.forEach((p, b) -> {
//                int[] lol = p;
//            });
//            n0 = n0 + 0.5 * ((System.nanoTime() - t) / 100000 0000.0 - n0);
////            System.out.println("i" + n0);
//
//            t = System.nanoTime();
//            HashMap<String, Boolean> cells2 = new HashMap();
//            for (int i = 0; i < n; i++) {
//                for (int j = 0; j < n; j++) {
//                    cells2.put(test2(i, j), true);
//                }
//            }
//            cells2.forEach((p, b) -> {
//                String[] lol = p.split(",");
//                int[] d = {Integer.parseInt(lol[0]), Integer.parseInt(lol[1])};
//            });
//            n1 = n1 + 0.5 * ((System.nanoTime() - t) / 1000000000.0 - n1);
////            System.out.println("s" + n1);
//            if (n0 > n1)
//                System.out.println("IT'S FASTER");
//            else
//                System.out.println("IT'S SLOWER");
//        }
    }
}
