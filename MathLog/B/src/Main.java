import expression.*;
import natural.DedLine;
import natural.NaturalRules;

import java.util.*;
import java.util.stream.Collectors;

import static natural.NaturalAxiom.*;

public class Main {
    private static Parser parser = new Parser();
    private static Expression first;
    private static List<Expression> proof = new ArrayList<>();
    private static List<Expression> hypo = new ArrayList<>();
    private static Set<Expression> uniqueProof = new LinkedHashSet<>();
    private static Map<Expression, Expression> rightImpl = new HashMap<>();
    private static Expression[] axiom = new Expression[10];

    private static Expression parse(String s) {
        return parser.parse(s);
    }

    private static void initAxioms() {
        axiom[0] = parse("A->B->A");
        axiom[1] = parse("(A->B)->(A->B->C)->(A->C)");
        axiom[2] = parse("A->B->A&B");
        axiom[3] = parse("A&B->A");
        axiom[4] = parse("A&B->B");
        axiom[5] = parse("A->A|B");
        axiom[6] = parse("B->A|B");
        axiom[7] = parse("(A->C)->(B->C)->(A|B->C)");
        axiom[8] = parse("(A->B)->(A->!B)->!A");
        axiom[9] = parse("A->!A->B");
    }

    //2, 7
    private static Proof axSwitch(int i, Expression e, int n) {
        return switch (i) {
            case 1 -> axiom1(hypo, e.getA(), e.getB(), n);
            case 2 -> axiom2(hypo, e.getA(), e.getB(), e.getC(), n);
            case 3 -> axiom3(hypo, e.getA(), e.getB(), n);
            case 4 -> axiom4(hypo, e.getA(), e.getB(), n);
            case 5 -> axiom5(hypo, e.getA(), e.getB(), n);
            case 6 -> axiom6(hypo, e.getA(), e.getB(), n);
            case 7 -> axiom7(hypo, e.getA(), e.getB(), n);
            case 8 -> axiom8(hypo, e.getA(), e.getB(), e.getC(), n);
            case 9 -> axiom9(hypo, e.getA(), e.getB(), n);
            case 10 -> axiom10(hypo, e.getA(), e.getB(), n);
            default -> new Proof(0, "");
        };
    }

    private static boolean checkMP(Expression e, int n) {
        if (!rightImpl.containsKey(e)) {
            return false;
        }
        Expression l;
        l = ((BinaryExpression) rightImpl.get(e)).leftO;
        for (int i = 0; i < n; i++) {
            if (l.equals(proof.get(i))) {
                e.setMP();
                e.setFromMP(proof.get(i));
                e.setToMP(rightImpl.get(e));
                return true;
            }
        }
        for (Expression expression : hypo) {
            if (l.equals(expression)) {
                e.setMP();
                e.setFromMP(expression);
                e.setToMP(rightImpl.get(e));
                return true;
            }
        }
        return false;
    }

    private static boolean checkHypo(Expression e) {
        for (Expression h : hypo) {
            if (e.equals(h)) {
                e.setHyp();
                return true;
            }
        }
        return false;
    }

    private static boolean checkAxioms(Expression e) {
        for (int i = 0; i < axiom.length; i++) {
            Map<String, Expression> vars = new HashMap<>();
            if (checkAxiom(axiom[i], e, vars)) {
                e.setAxiom();
                e.setAxiomNumber(i + 1);
                e.setVars(vars.get("A"), vars.get("B"), vars.get("C"));
                return true;
            }
        }
        return false;
    }

    private static boolean checkAxiom(Expression l, Expression r, Map<String, Expression> subExpr) {
        if (l instanceof Variable) {
            String variable = ((Variable) l).name;
            if (subExpr.containsKey(variable)) {
                return subExpr.get(variable).equals(r);
            } else {
                subExpr.put(variable, r);
            }
        }
        if (l instanceof Not) {
            if (l.getClass() == r.getClass()) {
                return checkAxiom(((Not) l).expr, ((Not) r).expr, subExpr);
            }
            return false;
        }
        if (l instanceof BinaryExpression) {
            if (l.getClass() == r.getClass()) {
                BinaryExpression leftOperator = (BinaryExpression) l;
                BinaryExpression rightOperator = (BinaryExpression) r;
                return (checkAxiom(leftOperator.leftO, rightOperator.leftO, subExpr) &&
                        checkAxiom(leftOperator.rightO, rightOperator.rightO, subExpr));
            }
            return false;
        }
        return true;
    }

    public static boolean checkProof(List<Expression> proof) {
        for (int i = 0; i < proof.size(); i++) {
            Expression e = proof.get(i);
            if (!(checkAxioms(e) || checkHypo(e) || checkMP(e, i))) {
                if (i == proof.size() - 1 && !proof.get(proof.size() - 1).equals(first)) {
                    System.out.println("The proof does not prove the required expression");
                } else {
                    System.out.println("The proof is incorrect at line " + (i + 1));
                }
                return false;
            }
        }
        //System.out.println("Correct");
        return true;
    }

    public static void main(String[] args) {
        //System.out.println(axiom2(new ArrayList<>(), new Variable("a"), new Variable("b"), new Variable("c")));
        initAxioms();
        Scanner in = new Scanner(System.in);
        //BufferedReader in = Files.newBufferedReader(Path.of("B.in"));
        String s = in.nextLine();
        if (s.contains("|-")) {
            s = s.replaceAll("\\s", "").replace("\n", "");
            if (s.indexOf("|-") > 0) {
                hypo = Arrays.stream(s.substring(0, s.indexOf("|-")).split(",")).map(parser::parse).collect(Collectors.toList());
            }
            first = parse(s.substring(s.indexOf("|-") + 2));
            for (int i = 0; i < hypo.size(); i++) {
                hypo.get(i).setHyp();
                if (hypo.get(i) instanceof Impl) {
                    rightImpl.put(((Impl) hypo.get(i)).rightO, hypo.get(i));
                }
            }
        }
        // proof.add(parse(s));
        //s = in.nextLine();
        while (in.hasNextLine() && s != null && !s.isEmpty()) {
            s = in.nextLine();
            Expression e = parse(s);
            if (!uniqueProof.contains(e)) {
                proof.add(e);
                uniqueProof.add(e);
                if (e instanceof Impl) {
                    rightImpl.put(((Impl) e).rightO, e);
                }
            }
        }
        //proof.forEach(System.out::println);
        if (checkProof(proof)) {
            System.out.println(naturalProof(proof));
        }
    }

    private static int proofExpr(Expression e, int n, List<String> result) {
        DedLine dl;
        switch (e.getType()) {
            case 1 -> {
                Proof proof = axSwitch(e.getAxiomNumber(), e, n);
                result.addAll(List.of(proof.getProof().split("\n")));
                return proof.getNum();
            }
            case 3 -> {
                dl = new DedLine(n);
                dl.setHypo(hypo);
                dl.setNum(dl.getNum() - 1);
                result.add(NaturalRules.Eimpl(dl, e));
                return dl.getNum() + 1;
            }
            case 2 -> {
                dl = new DedLine(n);
                dl.setHypo(hypo);
                result.add(NaturalRules.Ax(dl, e));
                return dl.getNum();
            }
        }
        return 0;
    }

    private static String naturalProof(List<Expression> proof) {
        List<String> result = new ArrayList<>();
        int finalN = proof(proof.get(proof.size() - 1), 0, proof, result);
        //return getRes(new DedLine(), result.stream().map(v -> changeNum(v, finalN)).collect(Collectors.toList())).getProof();
        List<String> rt = new ArrayList<>();
        for (int i = 0; i < result.size(); i++) {
            rt.add(result.get(result.size() - 1 - i));
        }
        return String.join("\n", rt);
        //return getRes(new DedLine(), result).getProof();
    }
    private static int proof(Expression e, int n, List<Expression> proof, List<String> res) {
        int rn = proofExpr(e, n, res);
        if (e.isMP()) {
            proof(e.getFromMP(), rn, proof, res);
            proof(e.getToMP(), rn, proof, res);
        }
        return rn;
    }
}
