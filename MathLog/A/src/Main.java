import expression.Expression;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        Expression expr = new Parser().parse(in.nextLine());
        System.out.println(expr.toString());
    }
}
