import javax.swing.*;
import java.awt.*;

public class DigitalClock extends JFrame {

    public DigitalClock() {
        setTitle("🔥 Smart Clock Suite");
        setSize(800, 400); // half screen approx
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null); // center on screen

        JTabbedPane tabs = new JTabbedPane();

        tabs.add("🕒 Clock", new ClockPanel());
        tabs.add("🔔 Alarm", new AlarmManager());
        tabs.add("⏱ Stopwatch", new StopwatchPanel());
        tabs.add("⏳ Countdown", new CountdownTimerPanel());
        tabs.add("⚡ Battery", new BatteryStatus());

        add(tabs);

        setVisible(true);
    }

    public static void main(String[] args) {
        new DigitalClock();
    }
}
