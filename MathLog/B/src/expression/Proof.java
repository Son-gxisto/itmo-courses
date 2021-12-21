package expression;

public class Proof {
    public Proof(int num, String proof) {
        this.num = num;
        this.proof = proof;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getProof() {
        return proof;
    }

    public void setProof(String proof) {
        this.proof = proof;
    }

    private int num;
    private String proof;
}
