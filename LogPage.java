import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LogPage extends JFrame {
    private JTextArea logArea;

    public LogPage() {
        setTitle("Log Page");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Allows closing the log page without closing the main frame
        setLayout(new BorderLayout());

        logArea = new JTextArea();
        logArea.setEditable(false);

        // Load log data when the frame is initialized
        loadLogData();

        // Wrap the JTextArea in a JScrollPane for scrolling capability
        JScrollPane scrollPane = new JScrollPane(logArea);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void loadLogData() {
        StringBuilder logContent = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader("hotel_management_log.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                logContent.append(line).append("\n"); // Append each line to the StringBuilder
            }
        } catch (IOException e) {
            logContent.append("Error reading log file: ").append(e.getMessage());
        }
        logArea.setText(logContent.toString());
    }
}
