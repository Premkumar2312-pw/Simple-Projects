import javax.swing.*;
import java.awt.*;

public class StopwatchPanel extends JPanel {
    private long startTime = 0;
    private Timer timer;
    private final JLabel display = new JLabel("00:00:00");

    public StopwatchPanel() {
        setLayout(new BorderLayout());
        display.setFont(new Font("Consolas", Font.BOLD, 40));
        display.setHorizontalAlignment(SwingConstants.CENTER);
        add(display, BorderLayout.CENTER);

        JButton start = new JButton("Start"), stop = new JButton("Stop"), reset = new JButton("Reset");
        JPanel buttons = new JPanel();
        buttons.add(start);
        buttons.add(stop);
        buttons.add(reset);
        add(buttons, BorderLayout.SOUTH);

        timer = new Timer(1000, _ -> {
            long elapsed = (System.currentTimeMillis() - startTime) / 1000;
            long h = elapsed / 3600, m = (elapsed % 3600) / 60, s = elapsed % 60;
            display.setText(String.format("%02d:%02d:%02d", h, m, s));
        });

        start.addActionListener(_ -> {
            startTime = System.currentTimeMillis();
            timer.start();
        });

        stop.addActionListener(_ -> timer.stop());

        reset.addActionListener(_ -> {
            timer.stop();
            display.setText("00:00:00");
        });
    }
}
