package leitner;

import javafx.util.Pair;

import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.Scanner;
import java.io.*;

public class Leitner {
    private final int size = 10;
    Bucket[] buckets = new Bucket[size];
    private Random random = new Random();
    private Scanner in = new Scanner(System.in);

    private boolean isEnglish(String v) {
        char c = v.charAt(0);
        return 'a' <= c && c <= 'z' || 'A' <= c && c <= 'Z';
    }

    Leitner(String fileName) throws FileNotFoundException, UnsupportedEncodingException {
        for (int i = 0; i < size; i++) {
            buckets[i] = new Bucket();
        }
        Scanner file = new Scanner(new File(fileName));
        int num = 0;
        StringBuilder first = null;
        while (file.hasNext()) {
            String v = file.next();
            if (Character.isDigit(v.charAt(0))) {
                num = Integer.parseInt(v);
            } else {
                if (first == null) {
                    first = new StringBuilder(v);
                } else {
                    if (isEnglish(v)) {
                        first.append(" ").append(v);
                    } else {
                        buckets[num].add(first.toString(), v);
                        first = null;
                    }
                }
            }
        }
        file.close();
    }

    public void check() {
        Pair<String, String> p = null;
        int num = 0;
        while (p == null) {
            //another random function
            num = random.nextInt(size);
            p = buckets[num].remove();
        }
        //System.out.println();
        System.out.print(p.getKey() + ": ");
        String word = in.next();
        if (word.equals(p.getValue())) {
            if (num == buckets.length - 1) {
                buckets[num].add(p);
            } else {
                buckets[num + 1].add(p);
            }
        } else {
            buckets[0].add(p);
        }
    }
    public void save() throws IOException {
        BufferedWriter out = new BufferedWriter(
                new OutputStreamWriter(
                        new FileOutputStream(new File("buffer.txt")),
                        StandardCharsets.UTF_8
                )
        );
        for (int i = 0; i < size; i++) {
            out.write(i + "\n");
            out.write(buckets[i].toString());
        }
        out.close();
    }
}
