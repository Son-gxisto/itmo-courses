package expression;

public class Impl extends BinaryExpression {

    public Impl(Expression left, Expression right) {
        super(left, right);
        operator = "->";
    }
}
