package Rubik;
import java.util.Scanner;
//use U L F R B D for set Front, ' for "counterClock" and 2 for 180
//example FF'UU'BB'FRLD'2
//LL'FF'DD'F'2F2
// FUBRLDFRLBBDURLFUBRLDFRLBBDURLURLBFD
public class Main {
    public static void main(String[] args) {
        //!!!!!!!!
        boolean LOG = true;
        Cube cube = new Cube();
        Scanner in = new Scanner(System.in);
        String line = in.nextLine();
        for (int i = 0; i < line.length(); i++) {
            char cur = line.charAt(i);
            if (Character.isLetter(cur)) {
                if (i + 1 < line.length()) {
                    char c = line.charAt(i + 1);
                    boolean not = c == '\'';
                    if (not) {
                        if (i + 2 < line.length() && line.charAt(i + 2) == '2') {
                            cube.rotate180(cur);
                        } else {
                            cube.rotateCounter90(cur);
                        }
                    } else {
                        if (c == '2') {
                            cube.rotate180(cur);
                        } else {
                            cube.rotate90(cur);
                        }
                    }
                }
                if (LOG) {
                    System.out.println(cube.toString());
                }
            }
        }

        System.out.println("result:");
        System.out.println(cube.toString());
    }
}
