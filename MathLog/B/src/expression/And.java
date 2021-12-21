package expression;

public class And extends BinaryExpression {

    public And(Expression left, Expression right) {
        super(left, right);
        operator = "&";
    }
}