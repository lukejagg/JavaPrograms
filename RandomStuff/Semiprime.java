package RandomStuff;

import java.math.BigInteger;
import java.util.Random;

public class Semiprime extends Thread {

    static BigInteger semiprime = new BigInteger("1009881397871923546909564894309468582818233821955573955141120516205831021338528545374366109757154363664913380084917065169921701524733294389270280234380960909804976440540711201965410747553824948672771374075011577182305398340606162079");
    static Random rnd = new Random();
    static int amount = 0;

    public static BigInteger sqrt(BigInteger x) {
        BigInteger div = BigInteger.ZERO.setBit(x.bitLength()/2);
        BigInteger div2 = div;
        for(;;) {
            BigInteger y = div.add(x.divide(div)).shiftRight(1);
            if (y.equals(div) || y.equals(div2))
                return y;
            div2 = div;
            div = y;
        }
    }

    public static void print(Object o) {
        System.out.println(o.toString());
    }

    public static String randomNumber(int amount) {
        String n = "" + (rnd.nextInt(9) + 1);
        for (int i = 1; i < amount - 1; i++) {
            n += rnd.nextInt(10);
        }
        n += "9";
        return n;
    }

    public static void main(String[] args) {
        for (int i = 0; i < 3; i++) {
            new Semiprime().start();
        }

        double lt = System.nanoTime();
        for (;;) {
            if (System.nanoTime() - lt >= 1000000000) {
                amount = 0;
                lt = System.nanoTime();
            }
        }
    }

    public void run() {
        for (int i = 0; true; i++) {
            BigInteger num = new BigInteger(randomNumber(116));
            if (semiprime.remainder(num).compareTo(new BigInteger("0")) == 0) {
                print(num + " WOW THIS WORKS");
            }
        }
    }

}
