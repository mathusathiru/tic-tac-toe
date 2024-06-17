import java.util.*;

class Board {

    List<Point> availablePoints;

    int gridNumber;

    int[][] board;

    public Board(int gridNumber) {
        this.gridNumber = gridNumber;
        board = new int[gridNumber][gridNumber];
    }

    public boolean isGameOver() {
        return (hasXWon() || hasOWon() || getAvailablePoints().isEmpty());
    }

    private boolean checkDiagonals(int player) {
        boolean leftDiagonalWin = true;
        boolean rightDiagonalWin = true;
        for (int i = 0; i < gridNumber; i++) {
            if (board[i][i] != player) {
                leftDiagonalWin = false;
            }
            if (board[i][gridNumber - 1 - i] != player) {
                rightDiagonalWin = false;
            }
        }
        return leftDiagonalWin || rightDiagonalWin;
    }

    private boolean checkRows(int player) {
        for (int i = 0; i < gridNumber; i++) {
            boolean rowWin = true;
            for (int j = 0; j < gridNumber; j++) {
                if (board[i][j] != player) {
                    rowWin = false;
                    break;
                }
            }
            if (rowWin) {
                return true;
            }
        }
        return false;
    }

    private boolean checkColumns(int player) {
        for (int j = 0; j < gridNumber; j++) {
            boolean colWin = true;
            for (int i = 0; i < gridNumber; i++) {
                if (board[i][j] != player) {
                    colWin = false;
                    break;
                }
            }
            if (colWin) {
                return true;
            }
        }
        return false;
    }

    public boolean hasXWon() {
        return checkDiagonals(1) || checkRows(1) || checkColumns(1);
    }

    public boolean hasOWon() {
        return checkDiagonals(2) || checkRows(2) || checkColumns(2);
    }

    public List<Point> getAvailablePoints() {
        availablePoints = new ArrayList<>();

        for (int i = 0; i < gridNumber; ++i) {
            for (int j = 0; j < gridNumber; ++j) {
                if (board[i][j] == 0) {
                    availablePoints.add(new Point(i, j));
                }
            }
        }

        return availablePoints;
    }

    public int getState(Point point) {
        return board[point.x][point.y];
    }

    public void placeAMove(Point point, int player) {
        board[point.x][point.y] = player;
    }

    public void resetBoard() {
        board = new int[gridNumber][gridNumber];
        availablePoints = null;
    }
}