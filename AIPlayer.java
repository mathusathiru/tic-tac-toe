import java.util.ArrayList;
import java.util.List;

public class AIPlayer {

    List<PointsAndScores> rootsChildrenScores;

    private final int difficultyLevel;

    public AIPlayer(int difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    public Point returnBestMove() {

        int MAX = -100000;
        int best = -1;

        for (int i = 0; i < rootsChildrenScores.size(); ++i) {
            if (MAX < rootsChildrenScores.get(i).score) {
                MAX = rootsChildrenScores.get(i).score;
                best = i;
            }
        }

        return rootsChildrenScores.get(best).point;
    }

    public void callMinimax(int depth, int turn, Board b) {
        rootsChildrenScores = new ArrayList<>();
        minimax(depth, Integer.MIN_VALUE, Integer.MAX_VALUE, turn, b);
    }

    public int minimax(int depth, int alpha, int beta, int turn, Board b) {
        if (b.hasXWon()) return Integer.MAX_VALUE;
        if (b.hasOWon()) return Integer.MIN_VALUE;

        List<Point> pointsAvailable = b.getAvailablePoints();

        int maxDepth = getMaxDepth();
        if (pointsAvailable.isEmpty() || depth == maxDepth) {
            return calculateHeuristic(b);
        }

        if (turn == 1) {
            for (Point point : pointsAvailable) {
                b.placeAMove(point, 1);
                int score = minimax(depth + 1, alpha, beta, 2, b);
                b.placeAMove(point, 0);
                alpha = Math.max(alpha, score);
                if (depth == 0)
                    rootsChildrenScores.add(new PointsAndScores(score, point));
                if (beta <= alpha)
                    break;
            }
            return alpha;
        }
        else {
            for (Point point : pointsAvailable) {
                b.placeAMove(point, 2);
                int score = minimax(depth + 1, alpha, beta, 1, b);
                b.placeAMove(point, 0);
                beta = Math.min(beta, score);
                if (beta <= alpha)
                    break;
            }
            return beta;
        }
    }

    public int calculateHeuristic(Board b) {
        int totalHeuristic = 0;

        int movesPlayed = b.gridNumber * b.gridNumber - b.getAvailablePoints().size();
        int centerWeight = (movesPlayed < b.gridNumber * b.gridNumber / 4) ? 100 : 25;
        int edgeWeight = (movesPlayed < b.gridNumber * b.gridNumber / 2) ? 50 : 30;
        int winWeight = (movesPlayed >= b.gridNumber * b.gridNumber / 2) ? 10000 : 5000;

        for (int i = 0; i < b.gridNumber; i++) {
            totalHeuristic += calculateLineValue(b, i, 0, 0, 1);
            totalHeuristic += calculateLineValue(b, 0, i, 1, 0);
        }

        totalHeuristic += calculateLineValue(b, 0, 0, 1, 1);
        totalHeuristic += calculateLineValue(b, 0, b.gridNumber - 1, 1, -1);

        totalHeuristic += checkCorners(b) * centerWeight;
        totalHeuristic += edgeControl(b) * edgeWeight;

        int closeWinCount = closeWin(b, 1);

        if (closeWinCount == 3) {
            totalHeuristic += winWeight;
        }

        else if (closeWinCount == 4) {
            totalHeuristic += winWeight * 2;
            totalHeuristic += endGameStrategy(b);
        }

        for (Point move : b.getAvailablePoints()) {
            if (isForkMove(b, move, 1)) {
                totalHeuristic += 1000;
            }
        }

        return totalHeuristic;
    }

    private int calculateLineValue(Board b, int startX, int startY, int dx, int dy) {
        int xCount = 0;
        int oCount = 0;
        int emptyCount = 0;

        for (int i = 0; i < b.gridNumber; i++) {
            int cellValue = b.board[startX + i * dx][startY + i * dy];
            if (cellValue == 1)
                xCount++;
            else if (cellValue == 2)
                oCount++;
            else
                emptyCount++;
        }

        if (xCount > 0 && oCount > 0)
            return 0;

        if (xCount == 3 && emptyCount == 2)
            return 500;

        if (oCount == 3 && emptyCount == 2)
            return -500;

        if (xCount > 0)
            return (int) Math.pow(10, xCount);

        if (oCount > 0)
            return -(int) Math.pow(10, oCount);

        return 0;
    }

    private int closeWin(Board b, int player) {
        int maxCount = 0;
        for (int i = 0; i < b.gridNumber; i++) {
            maxCount = Math.max(maxCount, closeWinInLine(b, i, 0, 0, 1, player));
            maxCount = Math.max(maxCount, closeWinInLine(b, 0, i, 1, 0, player));
        }
        maxCount = Math.max(maxCount, closeWinInLine(b, 0, 0, 1, 1, player));
        maxCount = Math.max(maxCount, closeWinInLine(b, 0, b.gridNumber - 1, 1, -1, player));
        return maxCount;
    }

    private int closeWinInLine(Board b, int startX, int startY, int dx, int dy, int player) {
        int count = 0, empty = 0;
        for (int i = 0; i < b.gridNumber; i++) {
            int cellValue = b.board[startX + i * dx][startY + i * dy];
            if (cellValue == player) {
                count++;
            } else if (cellValue == 0) {
                empty++;
            }
        }
        if (empty == 1) {
            return count;
        }
        return 0;
    }

    private int endGameStrategy(Board b) {
        int total = 0;

        for (int i = 0; i < b.gridNumber; i++) {
            int xCount = 0, oCount = 0, emptyCount = 0;
            for (int j = 0; j < b.gridNumber; j++) {
                if (b.board[i][j] == 1) {
                    xCount++;
                } else if (b.board[i][j] == 2) {
                    oCount++;
                } else {
                    emptyCount++;
                }
            }
            if (emptyCount == 1 && (xCount == b.gridNumber - 1 || oCount == b.gridNumber - 1)) {
                total += (xCount == b.gridNumber - 1) ? 20000 : -20000;
            }
        }

        for (int j = 0; j < b.gridNumber; j++) {
            int xCount = 0, oCount = 0, emptyCount = 0;
            for (int i = 0; i < b.gridNumber; i++) {
                if (b.board[i][j] == 1) {
                    xCount++;
                } else if (b.board[i][j] == 2) {
                    oCount++;
                } else {
                    emptyCount++;
                }
            }
            if (emptyCount == 1 && (xCount == b.gridNumber - 1 || oCount == b.gridNumber - 1)) {
                total += (xCount == b.gridNumber - 1) ? 20000 : -20000;
            }
        }

        int xCount = 0;
        int oCount = 0;
        int emptyCount = 0;

        for (int i = 0; i < b.gridNumber; i++) {
            if (b.board[i][i] == 1) {
                xCount++;
            } else if (b.board[i][i] == 2) {
                oCount++;
            } else {
                emptyCount++;
            }
        }
        if (emptyCount == 1 && (xCount == b.gridNumber - 1 || oCount == b.gridNumber - 1)) {
            total += (xCount == b.gridNumber - 1) ? 20000 : -20000;
        }

        xCount = 0;
        oCount = 0;
        emptyCount = 0;
        for (int i = 0; i < b.gridNumber; i++) {
            if (b.board[i][b.gridNumber - 1 - i] == 1) {
                xCount++;
            } else if (b.board[i][b.gridNumber - 1 - i] == 2) {
                oCount++;
            } else {
                emptyCount++;
            }
        }
        if (emptyCount == 1 && (xCount == b.gridNumber - 1 || oCount == b.gridNumber - 1)) {
            total += (xCount == b.gridNumber - 1) ? 20000 : -20000;
        }

        return total;
    }

    private int checkCorners(Board b) {
        if (b.gridNumber % 2 == 1) {
            int centerIndex = b.gridNumber / 2;
            if (b.board[centerIndex][centerIndex] == 1) {
                return 1000;
            }
            else if (b.board[centerIndex][centerIndex] == 2) {
                return -1000;
            }
        }
        return 0;
    }

    private int edgeControl(Board b) {
        int total = 0;

        for (int i = 0; i < b.gridNumber; i++) {
            for (int j = 0; j < b.gridNumber; j++) {
                if (i == 0 || i == b.gridNumber - 1 || j == 0 || j == b.gridNumber - 1) {
                    if (b.board[i][j] == 1) {
                        total += 250;
                    }
                    else if (b.board[i][j] == 2) {
                        total -= 250;
                    }
                }
            }
        }

        return total;
    }

    private boolean isForkMove(Board b, Point move, int player) {

        int count = 0;

        for (int i = 0; i < b.gridNumber; i++) {
            if (b.board[i][move.y] == player) {
                count++;
            }
        }
        if (count >= 2)
            return true;

        count = 0;

        for (int j = 0; j < b.gridNumber; j++) {
            if (b.board[move.x][j] == player) {
                count++;
            }
        }
        if (count >= 2)
            return true;

        count = 0;

        if (move.x == move.y) {
            for (int i = 0; i < b.gridNumber; i++) {
                if (b.board[i][i] == player) {
                    count++;
                }
            }
            if (count >= 2)
                return true;
        }

        count = 0;

        if (move.x + move.y == b.gridNumber - 1) {
            for (int i = 0; i < b.gridNumber; i++) {
                if (b.board[i][b.gridNumber - 1 - i] == player) {
                    count++;
                }
            }
            return count >= 2;
        }

        return false;
    }

    public void resetPlayer() {
        rootsChildrenScores = null;
    }

    private int getMaxDepth() {
        if (difficultyLevel == 1) {
            return 2;
        } else if (difficultyLevel == 2) {
            return 4;
        } else if (difficultyLevel == 3) {
            return 6;
        }
        return 4;
    }

}
