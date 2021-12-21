package natural;

import expression.*;

public class NaturalRules {
    /*Ax,
     Il| Ilor
     Ir| Iror
     Iand I&
     Iimpl I->
     Eland El&
     Erand Er&
     Eimpl E->
     Enot E_|_
     */
    public static String Ax(DedLine line, Expression e) {
        //line.addHypo(e);
        line.setRight(e);
        line.setSym("Ax");
       // line.upNum();
        return line.toString();
    }

    public static String Iimpl(DedLine line, Expression a, Expression b) {
        line.setRight(new Impl(a, b));
        line.removeHypo(a);
        line.setSym("I->");
        line.upNum();
        return line.toString();
    }

    public static String Iimpl(DedLine line, Expression l) {
        line.setRight(new Impl(l, line.getRight()));
        line.removeHypo(l);
        line.setSym("I->");
        line.upNum();
        return line.toString();
    }

    public static String Eimpl(DedLine line, Expression e) {
        line.setSym("E->");
        line.setRight(e);
        line.upNum();
        return line.toString();
    }

    public static String Iand(DedLine line, Expression a, Expression b) {
        line.setSym("I&");
        line.upNum();
        line.setRight(new And(a, b));
        return line.toString();
    }

    public static String Eland(DedLine line, Expression a) {
        line.setSym("El&");
        line.upNum();
        line.setRight(a);
        return line.toString();
    }

    public static String Erand(DedLine line, Expression b) {
        line.setSym("Er&");
        line.upNum();
        line.setRight(b);
        return line.toString();
    }

    public static String Ilor(DedLine line, Expression b) {
        line.setSym("Il|");
        line.upNum();
        line.setRight(new Or(line.getRight(), b));
        return line.toString();
    }

    public static String Iror(DedLine line, Expression a) {
        line.setSym("Ir|");
        line.upNum();
        line.setRight(new Or(a, line.getRight()));
        return line.toString();
    }

    public static String Eor(DedLine line, Expression a, Expression b, Expression c) {
        line.setSym("E|");
        line.upNum();
        line.removeHypo(a);
        line.removeHypo(b);
        line.setRight(c);
        return line.toString();
    }

    public static String Enot(DedLine line, Expression b) {
        line.setSym("E_|_");
        line.setRight(b);
        line.upNum();
        return line.toString();
    }
}
