import java.util.Scanner;
import java.util.TreeSet;

public class Main {
    static int first;
    static String num;
    static long value;
    static int length;
    static TreeSet<Long> res0 = new TreeSet<>(), res1 = new TreeSet<>();
    static Scanner cin = new Scanner(System.in);
    static void gen1(int x, int ind, int v) {
        char[] t = new char[length];
        for (int i = length - 1; i >= 0; i--) {
            if (i != ind) {
                t[i] = (char) ('0' + x);
            }
        }
        t[ind] = ((char) ('0' + v));
        if (value <= Long.parseLong(String.valueOf(t))) {
            res1.add(Long.parseLong(String.valueOf(t)));
        }
    }

    static void gen0(int v) {
        char[] t = new char[length];
        for (int i = 0; i < length; i++) {
            t[i] = (char) ('0' + v);
        }
        Long a = Long.parseLong(String.valueOf(t));
        if (value <= a) {
            res0.add(a);
        }
    }

    static void generate() {
        gen0(first);
        for (int i = 0; i <= 9; i++) {
            for (int j = 0; j < length; j++) {
                for (int k = 0; k <= 9; k++) {
                    gen1(i, j, k);
                }
            }
        }
        if (first != 9) {
            gen0(first + 1);
        }
    }
    public static void main(String[] args) {
        value = cin.nextLong();
        int k = cin.nextInt();
        num = String.valueOf(value);
        first = Integer.parseInt("" + num.charAt(0));
        length = num.length();
        generate();
        if (k == 0) {
            System.out.println(res0.first());
        } else {
            res1.addAll(res0);
            System.out.println(res1.first());
        }
    }
}
