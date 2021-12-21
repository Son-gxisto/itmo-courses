package natural;

import expression.Expression;

import java.util.List;

public class ProofNode {
    public ProofNode(Expression value) {
        this.value = value;
        this.need = null;
    }
    private Expression value;

    public Expression getValue() {
        return value;
    }

    public void setValue(Expression value) {
        this.value = value;
    }

    public List<ProofNode> getNeed() {
        return need;
    }

    public void setNeed(List<ProofNode> need) {
        this.need = need;
    }

    private List<ProofNode> need;
    public void add(ProofNode v) {
        need.add(v);
    }
}
