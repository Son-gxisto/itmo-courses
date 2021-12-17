package ru.itmo.wp.web.page;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@SuppressWarnings({"unused", "RedundantSuppression"})
public class TicTacToePage {
    private void action(HttpServletRequest request, Map<String, Object> view) {
        if (request.getSession().getAttribute("state") == null) {
            newGame(request, view);
            return;
        }
        State state = (State) request.getSession().getAttribute("state");
        view.put("state", state);
    }
    private void onMove(HttpServletRequest request, Map<String, Object> view) {
        State state = (State) request.getSession().getAttribute("state");
        for (Map.Entry<String, String[]> par : request.getParameterMap().entrySet()) {
            if (par.getKey().startsWith("cell")) {
                String cell = par.getKey();
                int row = cell.charAt(cell.length() - 2) - '0';
                int col = cell.charAt(cell.length() - 1) - '0';
                state.move(row, col);
                break;
            }
        }
        view.put("state", state);
    }

    private void newGame(HttpServletRequest request, Map<String, Object> view) {
        State state = new State();
        request.getSession().setAttribute("state", state);
        view.put("state", state);
    }
    public class State {
        private int size;
        private Character[][] cells;
        private boolean crossesMove;
        private String phase;
        private char turn = 'X';
        private State() {
            size = 3;
            cells = new Character[size][size];
            crossesMove = true;
            phase="RUNNING";
        }
        private void newGame(Map<String, Object> view) {
            view.put("state", new State());
        }
        private void action(Map<String, Object> view) {
            view.put("state", new State());
        }
        public int getSize() {
            return size;
        }
        public Character[][] getCells() {
            return cells;
        }
        public boolean getCrossesMove() {
            return crossesMove;
        }
        public String getPhase() {
            return phase;
        }
        private void move(int row, int col) {
            if (col < size && row < size && cells[row][col] == null && phase.equals("RUNNING")) {
                cells[row][col] = turn;
                checkWin(turn);
                crossesMove = !crossesMove;
                if (turn == 'X') {
                    turn = 'O';
                } else {
                    turn = 'X';
                }
            }
        }
        private void checkWin(Character turn) {
            int free = 0;
            int d1 = 0, d2 = 0;
            for (int i = 0; i < size; i++) {
                int ch = 0, cw = 0;
                if (cells[i][i] == turn) d1++;
                if (cells[i][size - i - 1] == turn) d2++;
                for (int j = 0; j < size; j++) {
                    if (cells[i][j] == turn) ch++;
                    if (cells[j][i] == turn) cw++;
                    if (cells[i][j] == null) free++;
                }
                if (Math.max(Math.max(d1, d2), Math.max(ch, cw)) == size) {
                    phase="WON_" + turn;
                    return;
                }
            }
            if (free == 0) {
                phase="DRAW";
            }
        }
    }
}
