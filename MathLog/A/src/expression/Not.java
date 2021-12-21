package expression;

public class Not extends Expression {

    public Expression expr;

    public Not(Expression expr) {
        this.expr = expr;
    }

    @Override
    public String toString() {
        return "(!" + expr + ")";
    }

    @Override
    public String toStringNatural() {
        return "(" + expr + "->" + "_|_)";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Not) {
            return expr.equals(((Not) obj).expr);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return expr.hashCode();
    }
}