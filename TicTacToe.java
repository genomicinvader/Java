import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class TicTacToe extends JFrame {
    private JButton[][] buttons = new JButton[3][3];
    private char[][] board = new char[3][3];
    private char currentPlayer = 'X';
    private String playerName = "Player";
    private int playerWins = 0;
    private int computerWins = 0;
    private int draws = 0;
    
    private JLabel statsLabel;
    private JButton resetButton;
    
    public TicTacToe() {
        setTitle("Tic Tac Toe - Player Profile");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        // Get player name
        playerName = JOptionPane.showInputDialog(this, "Enter your name:");
        if (playerName == null || playerName.trim().isEmpty()) {
            playerName = "Player";
        }
        setTitle("Tic Tac Toe - " + playerName);
        
        initializeBoard();
        
        JPanel boardPanel = new JPanel(new GridLayout(3, 3));
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j] = new JButton("");
                buttons[i][j].setFont(new Font("Arial", Font.BOLD, 60));
                buttons[i][j].setFocusPainted(false);
                final int row = i;
                final int col = j;
                buttons[i][j].addActionListener(e -> buttonClicked(row, col));
                boardPanel.add(buttons[i][j]);
            }
        }
        
        JPanel controlPanel = new JPanel(new FlowLayout());
        resetButton = new JButton("Reset Game");
        resetButton.addActionListener(e -> resetGame());
        controlPanel.add(resetButton);
        
        statsLabel = new JLabel(playerName + ": 0 | Computer: 0 | Draws: 0");
        controlPanel.add(statsLabel);
        
        add(boardPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);
        
        setVisible(true);
    }
    
    private void initializeBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = ' ';
            }
        }
    }
    
    private void buttonClicked(int row, int col) {
        if (board[row][col] == ' ' && currentPlayer == 'X') {
            board[row][col] = 'X';
            buttons[row][col].setText("X");
            buttons[row][col].setEnabled(false);
            
            if (checkWin('X')) {
                playerWins++;
                updateStats();
                JOptionPane.showMessageDialog(this, "Player wins!");
                disableAllButtons();
                return;
            } else if (isBoardFull()) {
                draws++;
                updateStats();
                JOptionPane.showMessageDialog(this, "It's a draw!");
                return;
            }
            
            currentPlayer = 'O';
            computerMove();
        }
    }
    
    private void computerMove() {
        // Simple AI: first try to win, then block, then random move
        int[] move = findWinningMove('O'); // Try to win
        if (move == null) {
            move = findWinningMove('X'); // Block player
        }
        if (move == null) {
            move = getRandomMove(); // Random move
        }
        
        if (move != null) {
            int row = move[0];
            int col = move[1];
            board[row][col] = 'O';
            buttons[row][col].setText("O");
            buttons[row][col].setEnabled(false);
            
            if (checkWin('O')) {
                computerWins++;
                updateStats();
                JOptionPane.showMessageDialog(this, "Computer wins!");
                disableAllButtons();
            } else if (isBoardFull()) {
                draws++;
                updateStats();
                JOptionPane.showMessageDialog(this, "It's a draw!");
            }
            
            currentPlayer = 'X';
        }
    }
    
    private int[] findWinningMove(char player) {
        // Check rows
        for (int i = 0; i < 3; i++) {
            if (board[i][0] == player && board[i][1] == player && board[i][2] == ' ') {
                return new int[]{i, 2};
            }
            if (board[i][0] == player && board[i][2] == player && board[i][1] == ' ') {
                return new int[]{i, 1};
            }
            if (board[i][1] == player && board[i][2] == player && board[i][0] == ' ') {
                return new int[]{i, 0};
            }
        }
        
        // Check columns
        for (int j = 0; j < 3; j++) {
            if (board[0][j] == player && board[1][j] == player && board[2][j] == ' ') {
                return new int[]{2, j};
            }
            if (board[0][j] == player && board[2][j] == player && board[1][j] == ' ') {
                return new int[]{1, j};
            }
            if (board[1][j] == player && board[2][j] == player && board[0][j] == ' ') {
                return new int[]{0, j};
            }
        }
        
        // Check diagonals
        if (board[0][0] == player && board[1][1] == player && board[2][2] == ' ') {
            return new int[]{2, 2};
        }
        if (board[0][0] == player && board[2][2] == player && board[1][1] == ' ') {
            return new int[]{1, 1};
        }
        if (board[1][1] == player && board[2][2] == player && board[0][0] == ' ') {
            return new int[]{0, 0};
        }
        
        if (board[0][2] == player && board[1][1] == player && board[2][0] == ' ') {
            return new int[]{2, 0};
        }
        if (board[0][2] == player && board[2][0] == player && board[1][1] == ' ') {
            return new int[]{1, 1};
        }
        if (board[1][1] == player && board[2][0] == player && board[0][2] == ' ') {
            return new int[]{0, 2};
        }
        
        return null;
    }
    
    private int[] getRandomMove() {
        Random random = new Random();
        int emptyCells = 0;
        
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == ' ') {
                    emptyCells++;
                }
            }
        }
        
        if (emptyCells == 0) return null;
        
        int randomIndex = random.nextInt(emptyCells);
        int count = 0;
        
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == ' ') {
                    if (count == randomIndex) {
                        return new int[]{i, j};
                    }
                    count++;
                }
            }
        }
        
        return null;
    }
    
    private boolean checkWin(char player) {
        // Check rows
        for (int i = 0; i < 3; i++) {
            if (board[i][0] == player && board[i][1] == player && board[i][2] == player) {
                return true;
            }
        }
        
        // Check columns
        for (int j = 0; j < 3; j++) {
            if (board[0][j] == player && board[1][j] == player && board[2][j] == player) {
                return true;
            }
        }
        
        // Check diagonals
        if (board[0][0] == player && board[1][1] == player && board[2][2] == player) {
            return true;
        }
        if (board[0][2] == player && board[1][1] == player && board[2][0] == player) {
            return true;
        }
        
        return false;
    }
    
    private boolean isBoardFull() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == ' ') {
                    return false;
                }
            }
        }
        return true;
    }
    
    private void disableAllButtons() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setEnabled(false);
            }
        }
    }
    
    private void resetGame() {
        initializeBoard();
        currentPlayer = 'X';
        
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText("");
                buttons[i][j].setEnabled(true);
            }
        }
    }
    
    private void updateStats() {
        statsLabel.setText(String.format("%s: %d | Computer: %d | Draws: %d", 
            playerName, playerWins, computerWins, draws));
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TicTacToe());
    }
}
