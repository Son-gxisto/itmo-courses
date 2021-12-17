package leitner;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/*
яблоко apple
кровать bed
плохо bad
хорошо good
 */
public class Main {
    public static void main(String[] args) throws IOException {
        Leitner leitner = new Leitner("test.txt");
        for (int i = 0; i < 5; i++) {
           // leitner.check();
        }
        System.out.println(leitner.buckets[0].toString());
        leitner.save();
    }
}
