package expression;

public abstract class Expression {

    private int type, hypNumber, axiomNumber;
    private Expression fromMP, toMP;

    public Expression getFromMP() {
        return fromMP;
    }

    public void setFromMP(Expression fromMP) {
        this.fromMP = fromMP;
    }

    public Expression getToMP() {
        return toMP;
    }

    public void setToMP(Expression toMP) {
        this.toMP = toMP;
    }

    private Expression a, b, c;

    public int getType() {
        return type;
    }
    public void setVars(Expression a, Expression b, Expression c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public void setVars(Expression a, Expression b) {
        this.a = a;
        this.b = b;
        this.c = null;
    }

    public Expression getA() {
        return a;
    }

    public Expression getB() {
        return b;
    }

    public Expression getC() {
        return c;
    }

    public abstract String toStringNatural();

    public boolean isAxiom() {
        return type == 1;
    }

    public void setAxiom() {
        type = 1;
    }

    public void setAxiomNumber(int axiomNumber) {
        this.axiomNumber = axiomNumber;
    }

    public int getAxiomNumber() {
        return axiomNumber;
    }

    public boolean isHyp() {
        return type == 2;
    }

    public void setHyp() {
        type = 2;
    }

    public int getHypNumber() {
        return hypNumber;
    }

    public void setHypNumber(int hypNumber) {
        this.hypNumber = hypNumber;
    }

    public boolean isMP() {
        return type == 3;
    }

    public void setMP() {
        type = 3;
    }

    @Override
    public abstract String toString();

    @Override
    public abstract int hashCode();

    @Override
    public abstract boolean equals(Object obj);
}