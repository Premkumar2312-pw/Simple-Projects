import javax.swing.*;
import java.awt.*;
import java.util.Calendar;

public class ClockPanel extends JPanel {
    private final Timer timer;

    public ClockPanel() {
        setBackground(Color.black);
        timer = new Timer(1000, _ -> repaint());
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.white);

        int width = getWidth();
        int height = getHeight();
        int radius = Math.min(width, height) / 2 - 50;
        int centerX = width / 2;
        int centerY = height / 2;

        // Draw clock border
        g2.setStroke(new BasicStroke(4));
        g2.drawOval(centerX - radius, centerY - radius, radius * 2, radius * 2);

        // Draw numbers
        g2.setFont(new Font("Arial", Font.BOLD, 20));
        for (int i = 1; i <= 12; i++) {
            double angle = Math.toRadians((i * 30) - 90);
            int x = (int) (centerX + (radius - 30) * Math.cos(angle));
            int y = (int) (centerY + (radius - 30) * Math.sin(angle));
            String num = String.valueOf(i);
            int strWidth = g2.getFontMetrics().stringWidth(num);
            int strHeight = g2.getFontMetrics().getAscent();
            g2.drawString(num, x - strWidth / 2, y + strHeight / 3);
        }

        // Get current time
        Calendar cal = Calendar.getInstance();
        int sec = cal.get(Calendar.SECOND);
        int min = cal.get(Calendar.MINUTE);
        int hour = cal.get(Calendar.HOUR);

        // Calculate angles
        double secAngle = Math.toRadians((sec * 6) - 90);
        double minAngle = Math.toRadians((min * 6 + sec * 0.1) - 90);
        double hourAngle = Math.toRadians((hour * 30 + min * 0.5) - 90);

        // Draw hands
        drawHand(g2, centerX, centerY, secAngle, radius - 20, Color.red, 1);
        drawHand(g2, centerX, centerY, minAngle, radius - 40, Color.green, 3);
        drawHand(g2, centerX, centerY, hourAngle, radius - 70, Color.cyan, 5);

        // Center dot
        g2.setColor(Color.white);
        g2.fillOval(centerX - 5, centerY - 5, 10, 10);
    }

    private void drawHand(Graphics2D g2, int x, int y, double angle, int length, Color color, int strokeWidth) {
        int x2 = (int) (x + length * Math.cos(angle));
        int y2 = (int) (y + length * Math.sin(angle));
        g2.setColor(color);
        g2.setStroke(new BasicStroke(strokeWidth));
        g2.drawLine(x, y, x2, y2);
    }
}
