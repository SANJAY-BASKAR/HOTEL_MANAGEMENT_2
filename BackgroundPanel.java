import javax.swing.*;
import java.awt.*;

public class BackgroundPanel extends JPanel {
    private Image backgroundImage;

    public BackgroundPanel(String imagePath) {
        try {
            backgroundImage = new ImageIcon(imagePath).getImage();
        } catch (Exception e) {
            e.printStackTrace();
        }
        setOpaque(false); // Make sure the background is not opaque

        // Set up the JLabel with the image
        JLabel imageLabel = new JLabel();
        ImageIcon homeImage = new ImageIcon("home.jpg"); // Make sure to place home.jpg in your project directory
        imageLabel.setIcon(homeImage);
        imageLabel.setHorizontalAlignment(JLabel.CENTER);

        // Add the label to the panel
        setLayout(new BorderLayout());
        add(imageLabel, BorderLayout.CENTER);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
