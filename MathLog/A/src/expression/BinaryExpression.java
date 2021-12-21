package expression;

public abstract class BinaryExpression extends Expression {

    public Expression leftO;
    public Expression rightO;
    String operator;

    BinaryExpression(Expression leftO, Expression rightO) {
        this.leftO = leftO;
        this.rightO = rightO;
    }

    @Override
    public String toString() {
        return "(" + operator + "," + leftO + "," + rightO + ")";
    }

    public String toStringNatural() {
        return toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof BinaryExpression) {
            BinaryExpression other = (BinaryExpression) obj;
            if (other.operator.equals(operator)) {
                return (leftO.equals(other.leftO) && rightO.equals(other.rightO));
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return leftO.hashCode() * 41 + rightO.hashCode() * 59 + operator.hashCode();
    }
}