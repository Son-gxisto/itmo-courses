package expression;

public class False extends Expression {

    @Override
    public String toStringNatural() {
        return "_|_";
    }

    @Override
    public String toString() {
        return "_|_";
    }

    @Override
    public int hashCode() {
        return 247;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof False;
    }
}
