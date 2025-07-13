package SnakeGamejava;

import javax.swing.*;

public class SnakeGame {
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        GamePanel panel = new GamePanel(frame);

        frame.setTitle("🐍 Snake Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.add(panel);
        frame.pack(); // set size based on panel preferred size
        frame.setLocationRelativeTo(null); // center screen
        frame.setVisible(true);
    }
}
