import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AlarmManager extends JPanel {
    private String alarmTime = "";
    private final SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");
    private boolean alarmPlaying = false;
    private Clip clip;
    private String triggeredTime = "";

    public AlarmManager() {
        setLayout(new BorderLayout());
        JButton setAlarm = new JButton("Set Alarm");
        add(setAlarm, BorderLayout.NORTH);

        setAlarm.addActionListener(_ -> {
            alarmTime = JOptionPane.showInputDialog("Set alarm (hh:mm AM/PM):");
            if (alarmTime != null && !alarmTime.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Alarm set for " + alarmTime);
            }
        });

        Timer t = new Timer(1000, _ -> {
            String now = timeFormat.format(Calendar.getInstance().getTime());

            if (alarmTime != null && now.equalsIgnoreCase(alarmTime) && !alarmPlaying) {
                triggeredTime = now;
                playAlarmSound();
            }

            // Stop after 1 minute
            if (alarmPlaying && !now.equals(triggeredTime)) {
                stopAlarmSound();
            }
        });
        t.start();
    }

    private void playAlarmSound() {
        try {
            File soundFile = new File("alarm.wav");
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
            clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.loop(Clip.LOOP_CONTINUOUSLY); // Loop the sound
            clip.start();
            alarmPlaying = true;
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            JOptionPane.showMessageDialog(this, "❌ Could not play alarm sound!");
            e.printStackTrace();
        }
    }

    private void stopAlarmSound() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
            clip.close();
        }
        alarmPlaying = false;
        alarmTime = ""; // Reset alarm
        JOptionPane.showMessageDialog(this, "🔕 Alarm stopped!");
    }
}
