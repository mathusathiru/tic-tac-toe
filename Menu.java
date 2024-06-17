import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Menu extends JFrame {
    private JLabel bodyTextLabel;
    private int selectedBoardSize;
    private int selectedDifficulty;

    public Menu() {
        initializeUI();
    }

    private void initializeUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Tic-Tac-Toe");
        setPreferredSize(new Dimension(600, 400));

        JPanel headingPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel headingLabel = new JLabel("Welcome to Tic-Tac-Toe");
        headingPanel.add(headingLabel);

        JPanel boardSizePanel = new JPanel(new GridLayout(2, 2, 10, 10));
        JButton button3x3 = new JButton("3x3");
        button3x3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedBoardSize = 3;
                updateBodyText();
            }
        });
        boardSizePanel.add(button3x3);

        JButton button4x4 = new JButton("4x4");
        button4x4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedBoardSize = 4;
                updateBodyText();
            }
        });
        boardSizePanel.add(button4x4);

        JButton button5x5 = new JButton("5x5");
        button5x5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedBoardSize = 5;
                updateBodyText();
            }
        });
        boardSizePanel.add(button5x5);

        JButton button6x6 = new JButton("6x6");
        button6x6.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedBoardSize = 6;
                updateBodyText();
            }
        });
        boardSizePanel.add(button6x6);

        JPanel difficultyPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        JButton easyButton = new JButton("Easy");
        easyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedDifficulty = 1;
                updateBodyText();
            }
        });
        difficultyPanel.add(easyButton);

        JButton mediumButton = new JButton("Medium");
        mediumButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedDifficulty = 2;
                updateBodyText();
            }
        });
        difficultyPanel.add(mediumButton);

        JButton hardButton = new JButton("Hard");
        hardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedDifficulty = 3;
                updateBodyText();
            }
        });
        difficultyPanel.add(hardButton);

        JPanel startPanel = new JPanel(new GridBagLayout());
        JButton startButton = new JButton("Start");
        startButton.setPreferredSize(new Dimension(200, 50));
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedBoardSize == 0 && selectedDifficulty != 0) {
                    String difficulty = getDifficultyString(selectedDifficulty);
                    JOptionPane.showMessageDialog(null, "Cannot start game. Choose a board size",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    bodyTextLabel.setText("Cannot start game. Board size unselected. Difficulty " + difficulty +
                            " selected.");
                } else if (selectedBoardSize != 0 && selectedDifficulty == 0) {
                    JOptionPane.showMessageDialog(null, "Cannot start game. Choose a difficulty",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    bodyTextLabel.setText("Cannot start game. Difficulty unselected. Board size " + selectedBoardSize +
                            " selected.");
                } else if (selectedBoardSize != 0) {
                    startGame(selectedBoardSize, selectedDifficulty);
                } else {
                    JOptionPane.showMessageDialog(null, "Cannot start game. Choose a board " +
                                    "size and difficulty",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    bodyTextLabel.setText("Please select a board size. Please select a difficulty.");
                }
            }
        });

        GridBagConstraints startButtonConstraints = new GridBagConstraints();
        startButtonConstraints.gridx = 0;
        startButtonConstraints.gridy = 0;
        startButtonConstraints.weightx = 1.0;
        startButtonConstraints.fill = GridBagConstraints.HORIZONTAL;
        startPanel.add(startButton, startButtonConstraints);

        JPanel bodyTextPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bodyTextLabel = new JLabel("Please select a board size. Please select a difficulty.");
        bodyTextPanel.add(bodyTextLabel);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 5, 10);
        mainPanel.add(headingPanel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5, 10, 5, 10);
        mainPanel.add(boardSizePanel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 10, 5, 10);
        mainPanel.add(difficultyPanel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 10, 5, 10);
        mainPanel.add(startPanel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 10, 10, 10);
        mainPanel.add(bodyTextPanel, gbc);

        add(mainPanel);

        pack();
        setLocationRelativeTo(null);
    }

    private void startGame(int boardSize, int difficultyLevel) {
        dispose();
        Board board = new Board(boardSize);
        AIPlayer aiPlayer = new AIPlayer(difficultyLevel);
        TicTacToe gui = new TicTacToe(board, aiPlayer);
    }

    private void updateBodyText() {
        if (selectedBoardSize == 0 && selectedDifficulty == 0) {
            bodyTextLabel.setText("Please select a board size. Please select a difficulty.");
        } else if (selectedBoardSize != 0 && selectedDifficulty == 0) {
            bodyTextLabel.setText("Board size " + selectedBoardSize + " selected. Please select a difficulty.");
        } else if (selectedBoardSize == 0) {
            String difficulty = getDifficultyString(selectedDifficulty);
            bodyTextLabel.setText("Difficulty " + difficulty + " selected. Please select a board size.");
        } else {
            String difficulty = getDifficultyString(selectedDifficulty);
            bodyTextLabel.setText("Board size " + selectedBoardSize + " selected. Difficulty " + difficulty +
                    " selected.");
        }
    }

    private String getDifficultyString(int difficulty) {
        return switch (difficulty) {
            case 1 -> "Easy";
            case 2 -> "Medium";
            case 3 -> "Hard";
            default -> "";
        };
    }


}