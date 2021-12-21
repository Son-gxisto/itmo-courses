package natural;

import expression.*;

import java.util.ArrayList;
import java.util.List;

import static natural.NaturalRules.*;

public class NaturalAxiom {
    private static String changeNum(String line, int num) {
        int rb = line.indexOf(']');
        int cur = Integer.parseInt(line.substring(1, rb));
        return "[" + (num - cur) + line.substring(rb);
    }
    public static Proof getRes(DedLine line, List<String> list) {
        List<String> result = new ArrayList<>();
        String t = list.get(0);
        int rb = t.indexOf(']');
        int cur = Integer.parseInt(t.substring(1, rb));
        t = list.get(list.size() - 1);
        rb = t.indexOf(']');
        cur += Integer.parseInt(t.substring(1, rb));
        for (int i = 0; i < list.size(); i++) {
            result.add(changeNum(list.get(list.size() - 1 - i), cur));
        }
        return new Proof(line.getNum(), String.join("\n", result));
    }
    public static Proof axiom1(List<Expression> hypo, Expression a, Expression b, int n) {
        List<String> result = new ArrayList<>();
        DedLine line = new DedLine(n);
        line.setHypo(hypo);
        line.addHypo(a);
        line.addHypo(b);
        result.add(Ax(line, a));   //Г, a, b |- a [Ax]
        result.add(Iimpl(line, b));//Г, a |- b -> a [I->]
        result.add(Iimpl(line, a));//Г |- a -> b -> a [I->]
        return getRes(line, result);
    }
    public static Proof axiom2(List<Expression> hypo, Expression a, Expression b, Expression c, int n) {
        List<String> result = new ArrayList<>();
        DedLine line = new DedLine(n);
        line.setHypo(hypo);
        Expression abc = new Impl(a, new Impl(b, c)); //abc = a->b->c
        line.addAllHypo(List.of(a, new Impl(a, b), abc));
        result.add(Ax(line, a)); //Г, a, a->b, a->b->c |- a [Ax]
        result.add(Ax(line, new Impl(a, b))); //Г, a, a->b, a->b->c |- a->b [Ax]
        result.add(Eimpl(line, b)); //Г, a, a->b, a->b->c |- b [E->]
        line.setNum(line.getNum() - 1);
        result.add(Ax(line, abc)); //Г, a, a->b, a->b->c |- a->b->c [Ax]
        result.add(Ax(line, a)); //Г, a, a->b, a->b->c |- a [Ax]
        result.add(Eimpl(line, new Impl(b, c))); //Г, a, a->b, b->c, a->b->c |- b->c [E->]
        result.add(Eimpl(line, c)); //Г, a, a->b, a->b->c |- c [E->]
        result.add(Iimpl(line, a, c)); //Г, a->b, a->b->c |- a->c [I->]
        result.add(Iimpl(line, abc)); //Г, a->b |-  (a->b->c)->(a->c) [I->]
        result.add(Iimpl(line, hypo.get(hypo.size() - 1))); //Г |- (a->b)-> (a->b->c)->(a->c) [I->]
        return getRes(line, result);
    }
    public static Proof axiom3(List<Expression> hypo, Expression a, Expression b, int n) {
        List<String> result = new ArrayList<>();
        DedLine line = new DedLine(n);
        line.setHypo(hypo);
        line.addHypo(a);
        line.addHypo(b);
        result.add(Ax(line, a)); //Г, a, b |- b [Ax]
        result.add(Ax(line, b)); //Г, a, b |- a [Ax]
        result.add(Iand(line, a, b)); //Г, a, b |- a & b [I&]
        result.add(Iimpl(line, b)); //Г, a |- b -> a & b [I->]
        result.add(Iimpl(line, a)); //Г |- a -> b -> a & b [I->]
        return getRes(line, result);
    }

    public static Proof axiom4(List<Expression> hypo, Expression a, Expression b, int n) {
        List<String> result = new ArrayList<>();
        DedLine line = new DedLine(n);
        line.setHypo(hypo);
        Expression ab = new And(a, b); //ab = a & b
        line.addHypo(ab);
        result.add(Ax(line, ab)); //Г, a & b |- a & b [Ax]
        result.add(Eland(line, a)); //Г, a & b |- a [El&]
        result.add(Iimpl(line, ab)); //Г |- a & b -> a [I->]
        return getRes(line, result);
    }

    public static Proof axiom5(List<Expression> hypo, Expression a, Expression b, int n) {
        List<String> result = new ArrayList<>();
        DedLine line = new DedLine(n);
        line.setHypo(hypo);
        Expression ab = new And(a, b); //ab = a & b
        line.addHypo(ab);
        result.add(Ax(line, ab)); //Г, a & b |- a & b [Ax]
        result.add(Erand(line, b)); //Г, a & b |- b [Er&]
        result.add(Iimpl(line, ab)); //Г |- a & b -> a [I->]
        return getRes(line, result);
    }

    public static Proof axiom6(List<Expression> hypo, Expression a, Expression b, int n) {
        List<String> result = new ArrayList<>();
        DedLine line = new DedLine(n);
        line.setHypo(hypo);
        line.addHypo(a);
        result.add(Ax(line, a)); //Г, a |- a [Ax]
        result.add(Ilor(line, b)); //Г, a |- a | b [Il|]
        result.add(Iimpl(line, a)); //Г |- a -> a | b [I->]
        return getRes(line, result);
    }

    public static Proof axiom7(List<Expression> hypo, Expression a, Expression b, int n) {
        List<String> result = new ArrayList<>();
        DedLine line = new DedLine(n);
        line.setHypo(hypo);
        line.addHypo(b);
        result.add(Ax(line, b)); //Г, b |- b [Ax]
        result.add(Iror(line, a)); //Г, b |- a | b [Ir|]
        result.add(Iimpl(line, b)); //Г |- b -> a | b [I->]
        return getRes(line, result);
    }

    public static Proof axiom8(List<Expression> hypo, Expression a, Expression b, Expression c, int n) {
        List<String> result = new ArrayList<>();
        DedLine line = new DedLine(n);
        line.setHypo(hypo);
        Expression ac = new Impl(a, c), bc = new Impl(b, c), aorb = new Or(a, b);
        line.addAllHypo(List.of(a, b, ac, bc, aorb));
        result.add(Ax(line, ac));       //Г, a -> c, b -> c, a | b, a, b |- a -> c [Ax]
        result.add(Ax(line, bc));       //Г, a -> c, b -> c, a | b, a, b |- b -> c [Ax]
        result.add(Ax(line, aorb));     //Г, a -> c, b -> c, a | b, a, b |- a | b [Ax]
        result.add(Eor(line, a, b, c)); //Г, a -> c, b -> c, a | b |- c [E|]
        result.add(Iimpl(line, aorb));  //Г, a -> c, b -> c |- (a | b -> c) [I->]
        result.add(Iimpl(line, bc));    //Г, a -> c |- (b -> c) -> (a | b -> c) [I->]
        result.add(Iimpl(line, ac));    //Г |- (a -> c) -> (b -> c) -> (a | b -> c) [I->]
        return getRes(line, result);
    }

    public static Proof axiom9(List<Expression> hypo, Expression a, Expression b, int n) {
        List<String> result = new ArrayList<>();
        DedLine line = new DedLine(n);
        line.setHypo(hypo);
        Expression ab = new Or(a, b);
        Expression aNotb = new Impl(a, new Not(b));
        line.addAllHypo(List.of(a, ab, aNotb));
        result.add(Ax(line, a)); //Г, a |- a [Ax]
        result.add(Ax(line, ab)); //Г, a, a -> b |- a -> b [Ax]
        result.add(Eimpl(line, b)); //Г, a, a -> b, a -> (b -> _|_) |- b -> _|_ [E->]
        line.setNum(line.getNum() - 1);
        result.add(Ax(line, aNotb)); //Г, a, a -> b, a -> (b -> _|_) |- a -> (b -> _|_) [Ax]
        result.add(Ax(line, a)); // Г, a, a -> b, a -> (b -> _|_) |- a [Ax]
        result.add(Eimpl(line, new Not(b))); //Г, a, a -> b, a -> (b -> _|_) |- b -> _|_ [E->]
        result.add(Eimpl(line, new False())); //Г, a, a -> b, a -> (b -> _|_) |- _|_ [E->]
        result.add(Iimpl(line, a)); //Г, a -> b, a -> (b -> _|_) |- a -> _|_ [I->]
        result.add(Iimpl(line, aNotb)); //Г, a -> b |- (a -> (b -> _|_)) -> (a -> _|_) [I->]
        result.add(Iimpl(line, ab)); //Г |- (a -> b) -> (a -> (b -> _|_)) -> (a -> _|_) [I->]
        return getRes(line, result);
    }

    public static Proof axiom10(List<Expression> hypo, Expression a, Expression b, int n) {
        List<String> result = new ArrayList<>();
        DedLine line = new DedLine(n);
        line.setHypo(hypo);
        Expression na = new Not(a);
        line.addHypo(a);
        line.addHypo(na);
        result.add(Ax(line, a)); //Г, a |- a [Ax]
        result.add(Ax(line, na)); //Г, a, (a -> _|_) |- (a -> _|_) [Ax]
        result.add(Eimpl(line, new False())); //Г, a, (a -> _|_) |- _|_ [E->]
        result.add(Enot(line, b)); //Г, a, (a -> _|_) |- b [E_|_]
        result.add(Iimpl(line, na)); //Г, a |- (a -> _|_) -> b [I->]
        result.add(Iimpl(line, a)); //Г |- a -> (a -> _|_) -> b [I->]
        return getRes(line, result);
    }
}
