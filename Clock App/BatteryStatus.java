import javax.swing.*;
import java.awt.*;
import java.io.*;

public class BatteryStatus extends JPanel {
    private final JLabel batteryLabel = new JLabel("Battery: checking...");

    public BatteryStatus() {
        setLayout(new FlowLayout());
        batteryLabel.setFont(new Font("Arial", Font.BOLD, 16));
        batteryLabel.setForeground(Color.BLUE);
        add(batteryLabel);
        checkBattery();  // Call once on load
    }

    public void checkBattery() {
        try {
            ProcessBuilder builder = new ProcessBuilder(
                "cmd.exe", "/c", "wmic path Win32_Battery get EstimatedChargeRemaining /value"
            );
            builder.redirectErrorStream(true);
            Process process = builder.start();

            BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream())
            );

            String line;
            String charge = null;

            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("EstimatedChargeRemaining")) {
                    String[] parts = line.split("=");
                    if (parts.length == 2) {
                        charge = parts[1].trim();
                        break;
                    }
                }
            }

            if (charge != null && !charge.isEmpty()) {
                batteryLabel.setText("🔋 Battery Level: " + charge + " %");
            } else {
                batteryLabel.setText("Battery info unavailable ❌");
            }

        } catch (Exception e) {
            batteryLabel.setText("Battery info unavailable ❌");
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Battery Checker");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(350, 120);
        frame.setResizable(false);
        frame.add(new BatteryStatus());
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
