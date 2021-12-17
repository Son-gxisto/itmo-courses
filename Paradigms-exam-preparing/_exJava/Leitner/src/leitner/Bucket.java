package leitner;
import javafx.util.Pair;
import java.util.ArrayDeque;
import java.util.Deque;
/*
apple яблоко
кровать bed
плохо bad
хорошо good
check(perevod)
хорошо -
 */
public class Bucket {
    private Deque<Pair<String, String>> queue = new ArrayDeque<>();

    public void add(String key, String value) {
        queue.add(new Pair<>(key, value));
    }
    public void add(Pair<String, String> value) {
        queue.add(value);
    }
    public String toString() {
        StringBuilder sb = new StringBuilder();
        while (!queue.isEmpty()) {
            Pair<String, String> p = remove();
            sb.append(p.getKey()).append(" ").append(p.getValue()).append("\n");
        }
        return sb.toString();
    }

    Pair<String, String> remove() {
        if (queue.isEmpty())
            return null;
        return queue.remove();
    }

}
