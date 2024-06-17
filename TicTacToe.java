import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TicTacToe extends JFrame {
    private JButton[][] boardButtons;
    private JLabel statusLabel;
    private Board board;
    private AIPlayer aiPlayer;

    public TicTacToe(Board board, AIPlayer aiPlayer) {
        this.board = board;
        this.aiPlayer = aiPlayer;
        initializeUI();
    }


    private void initializeUI() {
        boardButtons = new JButton[board.gridNumber][board.gridNumber];
        JPanel boardPanel = new JPanel(new GridLayout(board.gridNumber, board.gridNumber));
        for (int row = 0; row < board.gridNumber; row++) {
            for (int col = 0; col < board.gridNumber; col++) {
                JButton button = new JButton();
                button.addActionListener(new CellClickListener(row, col));
                boardButtons[row][col] = button;
                boardPanel.add(button);
            }
        }

        JPanel outerPanel = new JPanel(new BorderLayout());
        outerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        outerPanel.add(boardPanel, BorderLayout.CENTER);

        statusLabel = new JLabel("Player 1's turn");
        JButton resetButton = new JButton("Reset");
        resetButton.addActionListener(new ResetButtonListener());

        JButton menuButton = new JButton("Menu");
        menuButton.addActionListener(new MenuButtonListener());

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        topPanel.add(statusLabel);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.add(resetButton);
        bottomPanel.add(menuButton);

        add(outerPanel, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);
        add(bottomPanel, BorderLayout.SOUTH);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Tic-Tac-Toe");
        setPreferredSize(new Dimension(600, 400));
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void updateBoardDisplay() {
        for (int row = 0; row < board.gridNumber; row++) {
            for (int col = 0; col < board.gridNumber; col++) {
                String text = "";
                if (board.board[row][col] == 1) {
                    text = "X";
                } else if (board.board[row][col] == 2) {
                    text = "O";
                }
                boardButtons[row][col].setText(text);
            }
        }
    }

    private void handleCellClick(int row, int col) {
        if (board.getState(new Point(row, col)) != 0) {
            return;
        }

        board.placeAMove(new Point(row, col), 2);
        updateBoardDisplay();

        if (board.hasOWon()) {
            statusLabel.setText("Player 2 wins!");
            disableBoardButtons();
        } else if (board.isGameOver()) {
            statusLabel.setText("It's a draw!");
            disableBoardButtons();
        } else {
            aiPlayer.callMinimax(0, 1, board);
            Point aiMove = aiPlayer.returnBestMove();
            board.placeAMove(aiMove, 1);
            updateBoardDisplay();

            if (board.hasXWon()) {
                statusLabel.setText("Player 1 wins!");
                disableBoardButtons();
            } else if (board.isGameOver()) {
                statusLabel.setText("It's a draw!");
                disableBoardButtons();
            } else {
                statusLabel.setText("Player 2's turn");
            }
        }
    }

    private void resetGame() {
        board.resetBoard();
        aiPlayer.resetPlayer();
        updateBoardDisplay();
        enableBoardButtons();
        statusLabel.setText("Player 1's turn");
    }

    private class MenuButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            dispose();
            new Menu().setVisible(true);
        }
    }

    private void disableBoardButtons() {
        for (int row = 0; row < board.gridNumber; row++) {
            for (int col = 0; col < board.gridNumber; col++) {
                boardButtons[row][col].setEnabled(false);
            }
        }
    }

    private void enableBoardButtons() {
        for (int row = 0; row < board.gridNumber; row++) {
            for (int col = 0; col < board.gridNumber; col++) {
                boardButtons[row][col].setEnabled(true);
            }
        }
    }

    private class CellClickListener implements ActionListener {
        private int row;
        private int col;

        public CellClickListener(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            handleCellClick(row, col);
        }
    }

    private class ResetButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            resetGame();
        }
    }
}