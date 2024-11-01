import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WelcomePage extends JFrame {

    public WelcomePage() {
        setTitle("Welcome to Our Hotel");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Hotel Image
        JLabel imageLabel = new JLabel();
        ImageIcon hotelImage = new ImageIcon("hotel.jpg"); // Make sure to place hotel.jpg in your project directory
        imageLabel.setIcon(hotelImage);
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        add(imageLabel, BorderLayout.CENTER);

        // Enter Button
        JButton enterButton = new JButton("Enter");
        enterButton.setFont(new Font("Arial", Font.BOLD, 18));
        enterButton.setPreferredSize(new Dimension(200, 50));
        add(enterButton, BorderLayout.SOUTH);

        // Action Listener for Enter Button
        enterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();  // Close the welcome page
                new HotelManagementSystem().setVisible(true);  // Open the main system
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new WelcomePage().setVisible(true);
            }
        });
    }
}
