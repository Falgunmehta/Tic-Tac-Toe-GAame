import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Tictactoesplashgame {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SplashScreen());
    }
}

/* ------------ Splash Screen Class ------------*/
class SplashScreen extends JFrame {
    public SplashScreen() {
        setTitle("Tic Tac Toe");
        setSize(850, 530);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setUndecorated(true);

        ImageIcon bgIcon = new ImageIcon("page_1.png");
        JLabel background = new JLabel(bgIcon);
        background.setLayout(new BorderLayout());

        
        JPanel overlayPanel = new JPanel();
        overlayPanel.setOpaque(false);
        overlayPanel.setLayout(new BoxLayout(overlayPanel, BoxLayout.Y_AXIS));
        overlayPanel.setBorder(BorderFactory.createEmptyBorder(150, 0, 150, 0));

        JButton startButton = new JButton("Start Game");
        startButton.setFont(new Font("Arial", Font.BOLD, 24));
        startButton.setBackground(new Color(255, 105, 180));
        startButton.setForeground(Color.WHITE);
        startButton.setFocusPainted(false);
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        startButton.setPreferredSize(new Dimension(220, 60));
        startButton.setMaximumSize(new Dimension(220, 60));
        startButton.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2, true));
        startButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        startButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                startButton.setBackground(new Color(255, 70, 160));
            }

            public void mouseExited(MouseEvent e) {
                startButton.setBackground(new Color(255, 105, 180));
            }
        });

        startButton.addActionListener(e -> {
            dispose();
            new EnhancedTicTacToeGame();
        });
        overlayPanel.add(Box.createVerticalStrut(200)); 
        overlayPanel.add(startButton);
        background.add(overlayPanel, BorderLayout.CENTER);
        setContentPane(background);
        setVisible(true);
    }
}


/* ------------ Main Game Class ------------*/
class EnhancedTicTacToeGame extends JFrame {
    private JButton[][] buttons = new JButton[3][3];
    private boolean xTurn = true;
    private int movesCount = 0;
    private int xScore = 0, oScore = 0, drawCount = 0;
    private JLabel statusLabel, scoreLabel;
    private String playerX = "Player X", playerO = "Player O";

    public EnhancedTicTacToeGame() {
        getPlayers();
        setupUI();
    }

    private void getPlayers() {
        playerX = JOptionPane.showInputDialog(this, "Enter name for X:");
        if (playerX == null || playerX.trim().isEmpty()) playerX = "Player X";

        playerO = JOptionPane.showInputDialog(this, "Enter name for O:");
        if (playerO == null || playerO.trim().isEmpty()) playerO = "Player O";
    }

    private void setupUI() {
        setTitle("Tic Tac Toe");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500, 600);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(0x2c3e50));

        statusLabel = new JLabel(playerX + "'s Turn (X)", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 20));
        statusLabel.setForeground(Color.WHITE);
        statusLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        mainPanel.add(statusLabel, BorderLayout.NORTH);

        JPanel boardPanel = new JPanel(new GridLayout(3, 3, 10, 10));
        boardPanel.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));
        boardPanel.setBackground(new Color(0x2c3e50));

        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++) {
                JButton btn = new JButton();
                btn.setFont(new Font("Arial", Font.BOLD, 60));
                btn.setBackground(new Color(0xecf0f1));
                btn.setFocusPainted(false);
                int row = i, col = j;
                btn.addActionListener(e -> handleMove(btn, row, col));
                buttons[i][j] = btn;
                boardPanel.add(btn);
            }

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(new Color(0x34495e));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        scoreLabel = new JLabel(getScoreText(), SwingConstants.CENTER);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 16));
        scoreLabel.setForeground(Color.WHITE);

        JButton resetBtn = new JButton("New Match");
        resetBtn.setFont(new Font("Arial", Font.BOLD, 16));
        resetBtn.setBackground(new Color(0x16a085));
        resetBtn.setForeground(Color.WHITE);
        resetBtn.setFocusPainted(false);
        resetBtn.addActionListener(e -> resetGame());

        bottomPanel.add(scoreLabel, BorderLayout.CENTER);
        bottomPanel.add(resetBtn, BorderLayout.EAST);

        mainPanel.add(boardPanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);
    }

    private void handleMove(JButton btn, int row, int col) {
        if (!btn.getText().isEmpty() || gameOver()) return;

        btn.setText(xTurn ? "X" : "O");
        btn.setForeground(xTurn ? new Color(0x2980b9) : new Color(0xc0392b));
        movesCount++;

        if (checkForWin()) {
            String winner = xTurn ? playerX : playerO;
            if (xTurn) xScore++; else oScore++;
            statusLabel.setText(winner + " Wins!");
            JOptionPane.showMessageDialog(this, winner + " wins this round!");
        } else if (movesCount == 9) {
            drawCount++;
            statusLabel.setText("It's a Draw!");
            JOptionPane.showMessageDialog(this, "It's a draw!");
        } else {
            xTurn = !xTurn;
            statusLabel.setText((xTurn ? playerX : playerO) + "'s Turn (" + (xTurn ? "X" : "O") + ")");
        }

        scoreLabel.setText(getScoreText());
    }

    private boolean gameOver() {
        return statusLabel.getText().contains("Wins") || statusLabel.getText().contains("Draw");
    }

    private boolean checkForWin() {
        String[][] board = new String[3][3];
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                board[i][j] = buttons[i][j].getText();

        for (int i = 0; i < 3; i++) {
            if (equal(board[i][0], board[i][1], board[i][2])) {
                highlight(i, 0, i, 1, i, 2);
                return true;
            }
            if (equal(board[0][i], board[1][i], board[2][i])) {
                highlight(0, i, 1, i, 2, i);
                return true;
            }
        }

        if (equal(board[0][0], board[1][1], board[2][2])) {
            highlight(0, 0, 1, 1, 2, 2);
            return true;
        }

        if (equal(board[0][2], board[1][1], board[2][0])) {
            highlight(0, 2, 1, 1, 2, 0);
            return true;
        }

        return false;
    }

    private boolean equal(String a, String b, String c) {
        return !a.equals("") && a.equals(b) && b.equals(c);
    }

    private void highlight(int r1, int c1, int r2, int c2, int r3, int c3) {
        Color winColor = new Color(0x2ecc71);
        buttons[r1][c1].setBackground(winColor);
        buttons[r2][c2].setBackground(winColor);
        buttons[r3][c3].setBackground(winColor);
    }

    private void resetGame() {
        for (JButton[] row : buttons)
            for (JButton btn : row) {
                btn.setText("");
                btn.setBackground(new Color(0xecf0f1));
            }
        movesCount = 0;
        xTurn = true;
        statusLabel.setText(playerX + "'s Turn (X)");
    }

    private String getScoreText() {
        return playerX + " (X): " + xScore + " | " + playerO + " (O): " + oScore + " | Draws: " + drawCount;
    }
}
