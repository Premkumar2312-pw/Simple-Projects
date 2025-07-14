package TicTacToeApp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class TicTacToe extends JFrame implements ActionListener {
    private final JButton[][] buttons = new JButton[3][3];
    private JLabel statusLabel; // removed final
    private boolean gameOver = false;
    private final Random random = new Random();

    public TicTacToe() { // corrected constructor name
        setTitle("Tic Tac Toe - Play vs Computer");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(400, 450);
        setLayout(new BorderLayout());

        JPanel grid = new JPanel(new GridLayout(3, 3));
        Font font = new Font("Arial", Font.BOLD, 40);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j] = new JButton("");
                buttons[i][j].setFont(font);
                buttons[i][j].addActionListener(this);
                grid.add(buttons[i][j]);
            }
        }

        statusLabel = new JLabel("Your turn (X)");
        statusLabel.setHorizontalAlignment(JLabel.CENTER);
        statusLabel.setFont(new Font("Verdana", Font.PLAIN, 18));

        JButton resetButton = new JButton("Reset");
        resetButton.addActionListener(_ -> resetGame());

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(statusLabel, BorderLayout.CENTER);
        bottomPanel.add(resetButton, BorderLayout.EAST);

        add(grid, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (gameOver) return;

        JButton btn = (JButton) e.getSource();
        if (!btn.getText().equals("")) return;

        btn.setText("X");
        btn.setForeground(Color.BLUE);

        if (checkWin("X")) {
            statusLabel.setText("You win! 🎉");
            gameOver = true;
            return;
        }

        if (isDraw()) {
            statusLabel.setText("Draw!");
            gameOver = true;
            return;
        }

        statusLabel.setText("Computer's turn (O)");
        Timer t = new Timer(500, _ -> {
            computerMove();
            if (checkWin("O")) {
                statusLabel.setText("Computer wins! 🤖");
                gameOver = true;
            } else if (isDraw()) {
                statusLabel.setText("Draw!");
                gameOver = true;
            } else {
                statusLabel.setText("Your turn (X)");
            }
        });
        t.setRepeats(false);
        t.start();
    }

    private void computerMove() {
        List<JButton> empty = new ArrayList<>();
        for (JButton[] row : buttons)
            for (JButton b : row)
                if (b.getText().isEmpty())
                    empty.add(b);

        if (!empty.isEmpty()) {
            JButton move = empty.get(random.nextInt(empty.size()));
            move.setText("O");
            move.setForeground(Color.RED);
        }
    }

    private boolean checkWin(String symbol) {
        for (int i = 0; i < 3; i++) {
            if (symbol.equals(buttons[i][0].getText()) &&
                symbol.equals(buttons[i][1].getText()) &&
                symbol.equals(buttons[i][2].getText())) return true;

            if (symbol.equals(buttons[0][i].getText()) &&
                symbol.equals(buttons[1][i].getText()) &&
                symbol.equals(buttons[2][i].getText())) return true;
        }

        if (symbol.equals(buttons[0][0].getText()) &&
            symbol.equals(buttons[1][1].getText()) &&
            symbol.equals(buttons[2][2].getText())) return true;

        if (symbol.equals(buttons[0][2].getText()) &&
            symbol.equals(buttons[1][1].getText()) &&
            symbol.equals(buttons[2][0].getText())) return true;

        return false;
    }

    private boolean isDraw() {
        for (JButton[] row : buttons)
            for (JButton b : row)
                if (b.getText().isEmpty())
                    return false;
        return true;
    }

    private void resetGame() {
        for (JButton[] row : buttons)
            for (JButton b : row) {
                b.setText("");
                b.setEnabled(true);
            }
        gameOver = false;
        statusLabel.setText("Your turn (X)");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TicTacToe::new);
    }
}
