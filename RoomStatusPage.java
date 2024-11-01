import javax.swing.*;
import java.awt.*;
import java.util.List;

public class RoomStatusPage extends JFrame {

    public RoomStatusPage(List<Room> rooms) {
        setTitle("Room Status");
        setSize(400, 300);
        setLayout(new BorderLayout());

        JTextArea roomStatusArea = new JTextArea();
        roomStatusArea.setEditable(false);

        StringBuilder statusBuilder = new StringBuilder();
        for (Room room : rooms) {
            String status = room.isAvailable() ? "Available" : "Occupied";
            statusBuilder.append("Room ").append(room.getRoomNumber()).append(": ").append(status).append("\n");
        }
        roomStatusArea.setText(statusBuilder.toString());

        add(new JScrollPane(roomStatusArea), BorderLayout.CENTER);
    }
}
