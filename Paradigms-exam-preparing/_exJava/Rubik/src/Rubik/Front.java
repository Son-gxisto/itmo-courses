package Rubik;

import java.util.Arrays;

public class Front {
    final int n = 3;
    private char[][] cells;
    Front(Character color) {
        cells = new char[n][n];
        for(int i = 0; i < n; i++)
            for(int j = 0; j < n ; j++)
                cells[i][j] = color;
    }
    char[][] getCells() {
        return cells;
    }
    void setCell(int i, int j, Character v) {
        cells[i][j] = v;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < n; i++) {
            for(int j = 0; j < n; j++)
                sb.append(cells[i][j]);
            sb.append('\n');
        }
        return sb.toString();
    }
    public String getCI(int i) {
        return new String(cells[i]);
    }
    void rotate90() {
        char[][] t = new char[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                t[i][j] = cells[n - 1 - j][i];
            }
        }
        for (int i = 0; i < n; i++) {
            System.arraycopy(t[i], 0, cells[i], 0, n);
        }
    }
}