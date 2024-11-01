import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SideMenuPanel extends JPanel {
    private HotelManagementSystem mainFrame;

    public SideMenuPanel(HotelManagementSystem mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new GridLayout(0, 1)); // Vertical layout for buttons

        // Create buttons
        JButton homeButton = new JButton("Home");
        JButton checkInButton = new JButton("Check In");
        JButton checkOutButton = new JButton("Check Out");
        JButton viewGuestsButton = new JButton("View Guests");
        JButton roomStatusButton = new JButton("Room Status");
        JButton increaseRoomsButton = new JButton("Increase Rooms");
        JButton decreaseRoomsButton = new JButton("Decrease Rooms");
        JButton clearScreenButton = new JButton("Clear Screen");
        JButton logoutButton = new JButton("Logout");

        // New Thank You Button
        JButton thankYouButton = new JButton("Log_History");

        // Add action listeners
        homeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.showHomePage();
            }
        });

        checkInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.showCheckInPage();
            }
        });

        checkOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.showCheckOutPage();
            }
        });

        viewGuestsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.showViewGuestsPage();
            }
        });

        roomStatusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.showRoomStatusPage();
            }
        });

        increaseRoomsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.increaseRooms();
            }
        });

        decreaseRoomsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.decreaseRooms();
            }
        });

        clearScreenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.clearScreen();
            }
        });

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.logout();
            }
        });

        // Action listener for Thank You Button
        thankYouButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.showLogPage();
            }
        });

        // Add buttons to panel
        add(homeButton);
        add(checkInButton);
        add(checkOutButton);
        add(viewGuestsButton);
        add(roomStatusButton);
        add(increaseRoomsButton);
        add(decreaseRoomsButton);
        add(clearScreenButton);
        add(thankYouButton); // Add Thank You button to side menu
        add(logoutButton); // Add logout button to side menu
    }
}
