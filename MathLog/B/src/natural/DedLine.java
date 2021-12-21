package natural;

import expression.Expression;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DedLine {
    private Expression right;
    private List<Expression> hypo = new ArrayList<>();

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public void upNum() {
        num++;
    }

    public String getSym() {
        return sym;
    }

    public void setSym(String sym) {
        this.sym = sym;
    }

    public Expression getRight() {
        return right;
    }

    public void setRight(Expression right) {
        this.right = right;
    }

    public void setHypo(List<Expression> hypo) {
        this.hypo = hypo;
    }

    private int num;
    private String sym;
    public DedLine(Expression e) {
        this.right = e;
    }

    public DedLine() {this.num = 0;}

    public DedLine(int num) {
        this.num = num;
    }

    public String toString() {
        String t = hypo.stream().map(Expression::toStringNatural).collect(Collectors.joining(","));
        return "[" + num + "] " + //"Г" +
                (t.isEmpty()? t : t + " ") +
                "|- " + (right == null ? "_|_" : right.toStringNatural()) + " [" + sym + "]";
    }
    public void addHypo(Expression e) {
        hypo.add(e);
    }
    public void addAllHypo(List<Expression> e) {
        hypo.addAll(e);
    }
    public boolean removeHypo(Expression e) {
        if (!hypo.contains(e)) {
            return false;
        }
        hypo.remove(e);
        return true;
    }
}
