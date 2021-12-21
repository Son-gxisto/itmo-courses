package expression;

public class Or extends BinaryExpression {

    public Or(Expression left, Expression right) {
        super(left, right);
        operator = "|";
    }
}
