package SnakeGamejava;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {

    static final int SCREEN_WIDTH = 800;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / (UNIT_SIZE * UNIT_SIZE);
    static final int DELAY = 100;

    final int[] x = new int[GAME_UNITS];
    final int[] y = new int[GAME_UNITS];

    int bodyParts = 6;
    int applesEaten = 0;
    int appleX, appleY;

    char direction = 'R';
    boolean running = false;
    boolean paused = false;

    Timer timer;
    Random random;
    JButton pauseButton, restartButton, exitButton;
    int highScore = 0;

    public GamePanel(JFrame frame) {
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.setLayout(null);
        this.addKeyListener(new MyKeyAdapter());

        random = new Random();

        // Pause Button
        pauseButton = new JButton("⏸ Pause");
        pauseButton.setBounds(20, 20, 120, 30);
        this.add(pauseButton);

        pauseButton.addActionListener(
            _ -> {
            if (paused) {
                timer.start();
                pauseButton.setText("⏸ Pause");
            } else {
                timer.stop();
                pauseButton.setText("▶ Resume");
            }
            paused = !paused;
        });

        // Restart Button
        restartButton = new JButton("🔁 Restart");
        restartButton.setBounds(160, 20, 120, 30);
        restartButton.setVisible(false);
        this.add(restartButton);

        restartButton.addActionListener(_ -> {
            applesEaten = 0;
            bodyParts = 6;
            direction = 'R';
            paused = false;
            for (int i = 0; i < x.length; i++) {
                x[i] = 0;
                y[i] = 0;
            }
            newApple();
            running = true;
            restartButton.setVisible(false);
            pauseButton.setText("⏸ Pause");
            timer.start();
        });

        // Exit Button
        exitButton = new JButton("🔚 Exit");
        exitButton.setBounds(300, 20, 120, 30);
        this.add(exitButton);

        exitButton.addActionListener(_ -> System.exit(0));

        startGame();
    }

    public void startGame() {
        newApple();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        if (running) {
            // Apple
            g.setColor(Color.red);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

            // Snake
            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) {
                    g.setColor(Color.green);
                } else {
                    g.setColor(new Color(45, 180, 0));
                }
                g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
            }

            // Score
            g.setColor(Color.white);
            g.setFont(new Font("Ink Free", Font.BOLD, 30));
            g.drawString("Score: " + applesEaten, 460, 45);
        } else {
            gameOver(g);
        }
    }

    public void newApple() {
        appleX = random.nextInt(SCREEN_WIDTH / UNIT_SIZE) * UNIT_SIZE;
        appleY = random.nextInt(SCREEN_HEIGHT / UNIT_SIZE) * UNIT_SIZE;
    }

    public void move() {
        for (int i = bodyParts; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        switch (direction) {
            case 'U' -> y[0] -= UNIT_SIZE;
            case 'D' -> y[0] += UNIT_SIZE;
            case 'L' -> x[0] -= UNIT_SIZE;
            case 'R' -> x[0] += UNIT_SIZE;
        }
    }

    public void checkApple() {
        if (x[0] == appleX && y[0] == appleY) {
            bodyParts++;
            applesEaten++;
            Toolkit.getDefaultToolkit().beep();
            newApple();
        }
    }

    public void checkCollisions() {
        // Body
        for (int i = bodyParts; i > 0; i--) {
            if (x[0] == x[i] && y[0] == y[i]) {
                running = false;
                break;
            }
        }

        // Walls
        if (x[0] < 0 || x[0] >= SCREEN_WIDTH || y[0] < 0 || y[0] >= SCREEN_HEIGHT) {
            running = false;
        }

        if (!running) timer.stop();
    }

    public void gameOver(Graphics g) {
        setBackground(Color.darkGray);

        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 60));
        g.drawString("Score: " + applesEaten, 60, 100);

        readHighScore();
        if (applesEaten > highScore) {
            highScore = applesEaten;
            saveHighScore();
        }

        g.setColor(Color.cyan);
        g.setFont(new Font("Ink Free", Font.BOLD, 40));
        g.drawString("High Score: " + highScore, 60, 160);

        g.setColor(Color.orange);
        g.setFont(new Font("Ink Free", Font.BOLD, 100));
        g.drawString("Game Over", SCREEN_WIDTH / 2 - 300, SCREEN_HEIGHT / 2);

        restartButton.setVisible(true);
    }

    public void readHighScore() {
        try (BufferedReader br = new BufferedReader(new FileReader("highscore.txt"))) {
            highScore = Integer.parseInt(br.readLine());
        } catch (Exception e) {
            highScore = 0;
        }
    }

    public void saveHighScore() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("highscore.txt"))) {
            bw.write(String.valueOf(highScore));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter {
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT -> {
                    if (direction != 'R') direction = 'L';
                }
                case KeyEvent.VK_RIGHT -> {
                    if (direction != 'L') direction = 'R';
                }
                case KeyEvent.VK_UP -> {
                    if (direction != 'D') direction = 'U';
                }
                case KeyEvent.VK_DOWN -> {
                    if (direction != 'U') direction = 'D';
                }
                case KeyEvent.VK_ESCAPE -> System.exit(0);
            }
        }
    }
}
