import expression.*;

public class Parser {
//P1'->!QQ->!R10&S|!T&U&V
    public Expression parse(String s) {
        s = s.replaceAll("\\s", "").replace("\n", "");
        Result result = parseImpl(s, 0);
        return result.acc;
    }

    private Result parseVar(String s, int begin) {
        StringBuilder curVar = new StringBuilder();
        int i = 0;
        while (begin + i < s.length() && (Character.isLetter(s.charAt(begin + i)) ||
                Character.isDigit(s.charAt(begin + i)) || s.charAt(begin + i) == '\'')) {
            curVar.append(s.charAt(begin + i));
            ++i;
        }
        Expression res = new Variable(curVar.toString());
        return new Result(res, begin + i);
    }

    private Result parseOr(String s, int begin) {
        Result res = parseAnd(s, begin);
        Expression acc = res.acc;
        while (res.rest < s.length()) {
            if (s.charAt(res.rest) != '|') {
                break;
            }
            res = parseAnd(s, res.rest + 1);
            acc = new Or(acc, res.acc);
        }
        return new Result(acc, res.rest);
    }

    private Result parseAnd(String s, int begin) {
        Result res = expr(s, begin);
        Expression acc = res.acc;
        while (true) {
            if (res.rest == s.length()) {
                return res;
            }
            char sign = s.charAt(res.rest);
            if (sign != '&') {
                return res;
            }
            Result right = expr(s, res.rest + 1);
            acc = new And(acc, right.acc);
            res = new Result(acc, right.rest);
        }
    }

    private Result parseImpl(String s, int start) {
        Result res = parseOr(s, start);
        Expression acc = res.acc;
        while (res.rest < s.length()) {
            if (s.charAt(res.rest) != '-') {
                break;
            }
            res = parseImpl(s, res.rest + 2);
            acc = new Impl(acc, res.acc);
        }
        return new Result(acc, res.rest);
    }

   private Result expr(String s, int start) {
        char first = s.charAt(start);
        if (first == '!') {
            Result next = expr(s, start + 1);
            Expression result = new Not(next.acc);
            return new Result(result, next.rest);
        }
        if (first == '(') {
            Result r = parseImpl(s, start + 1);
            if (s.charAt(r.rest) == ')') {
                r.setRest(r.rest + 1);
            }
            return r;
        }
        return parseVar(s, start);
    }

    private class Result {

        private Expression acc;
        private int rest;

        Result(Expression acc, int rest) {
            this.acc = acc;
            this.rest = rest;
        }

        public void setRest(int rest) {
            this.rest = rest;
        }
    }
}