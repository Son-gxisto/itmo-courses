package Rubik;

public class Cube {
    private Front u, d, r, l, f, b;
    Cube() {
        u = new Front('W');
        d = new Front('Y');
        r = new Front('O');
        l = new Front('R');
        b = new Front('G');
        f = new Front('B');
    }

    private void rotate(Front f, Front u, Front l, Front d, Front r) {
        f.rotate90();
        Character[] temp = new Character[f.n];
        for(int i = 0; i < u.n; i++)
            temp[i] = u.getCells()[u.n-1][i];
        for(int i = 0; i < u.n; i++)
            u.setCell(u.n - 1, i, l.getCells()[u.n - i - 1][u.n - 1]);
        for(int i = 0; i <u.n; i++)
            l.setCell(u.n - i - 1, u.n - 1, d.getCells()[0][i]);
        for(int i = 0; i < u.n; i++)
            d.setCell(0, i, r.getCells()[i][0]);
        for(int i = 0; i < u.n; i++)
            r.setCell(i, 0, temp[i]);
    }

    public void rotate90 (Character ind) {
        switch(ind) {
            case 'U':
                rotate(u, d, l, f, r);
                break;
            case 'F':
                rotate(f, u, l, d, r);
                break;
            case 'R':
                rotate(r, u, f, d, b);
                break;
            case 'B':
                rotate(b, u, r, d, l);
                break;
            case 'L':
                rotate(l, u, b, d, f);
                break;
            case 'D':
                rotate(d, f, l, b, r);
                break;
        }
    }
    private void rotate90nTimes(Character ind, int count) {
        for (int i = 0; i < count; i++)
            rotate90(ind);
    }
    public void rotateCounter90(Character ind) {
        rotate90nTimes(ind, 3);
    }
    public void rotate180(Character ind) {
        rotate90nTimes(ind, 2);
    }
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            sb.append("   ").append(u.getCI(i)).append('\n');
        }
        for (int i = 0; i < 3; i++) {
            sb.append(l.getCI(i)).append(f.getCI(i)).append(r.getCI(i)).append(b.getCI(i)).append('\n');
        }
        for (int i = 0; i < 3; i++) {
            sb.append("   ").append(d.getCI(i)).append('\n');
        }
        return sb.toString();
    }
}
