import javax.swing.*;
import java.awt.*;

public class CountdownTimerPanel extends JPanel {
    private Timer timer;
    private int seconds;
    private final JLabel display = new JLabel("00:00");

    public CountdownTimerPanel() {
        setLayout(new BorderLayout());
        display.setFont(new Font("Consolas", Font.BOLD, 40));
        display.setHorizontalAlignment(SwingConstants.CENTER);
        add(display, BorderLayout.CENTER);

        JButton set = new JButton("Set Timer"), start = new JButton("Start");
        JPanel buttons = new JPanel();
        buttons.add(set);
        buttons.add(start);
        add(buttons, BorderLayout.SOUTH);

        set.addActionListener(_ -> {
            String input = JOptionPane.showInputDialog("Enter minutes:");
            if (input != null && input.matches("\\d+")) {
                seconds = Integer.parseInt(input) * 60;
                display.setText(String.format("%02d:00", Integer.parseInt(input)));
            }
        });

        start.addActionListener(_ -> {
            timer = new Timer(1000, _ -> {
                if (seconds > 0) {
                    seconds--;
                    display.setText(String.format("%02d:%02d", seconds / 60, seconds % 60));
                } else {
                    Toolkit.getDefaultToolkit().beep();
                    JOptionPane.showMessageDialog(this, "⏳ Time’s up!");
                    timer.stop();
                }
            });
            timer.start();
        });
    }
}
