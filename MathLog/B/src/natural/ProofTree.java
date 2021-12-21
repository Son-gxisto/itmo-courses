package natural;

import expression.Expression;
import expression.Proof;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProofTree {
    /*
    int n = 0;
    ProofNode root;
    public ProofTree(List<Expression> proof) {
        Expression e = proof.get(proof.size() - 1);
        this.root = new ProofNode(e);
        add(proof, root);
    }
    private void add(List<Expression> proof, ProofNode node) {
        Expression e = node.getValue();
        if (e.isMP()) {
            ProofNode f = new ProofNode(proof.get(e.getFromMP()));
            ProofNode to = new ProofNode(proof.get(e.getToMP()));
            add(proof, f);
            add(proof, to);
            node.add(f);
            node.add(to);
        }
    }
    public List<String> getProof() {
        return null;
    }
    /*
    public List<String> getProof(ProofNode pn) {
        List<String> result = new ArrayList<>();
        for (ProofNode p : pn.getNeed()) {
            result.addAll(getProof(p));
        }
        result.add(proofExpr(pn.getValue(), n, result));
        return result;
    }
    List<String> result = new ArrayList<>();
    int n = 0;
        for (int i = 0; i < proof.size(); i++) {
        n = proofExpr(proof.get(i), n, result);
    }
    int finalN = n; /*
        for (int i = 0; i < result.size(); i++) {
            result.set(i, changeNum(result.get(i), n));
        }
        return getRes(new DedLine(), result.stream().map(v -> changeNum(v, finalN)).collect(Collectors.toList())).getProof();
        */
}
