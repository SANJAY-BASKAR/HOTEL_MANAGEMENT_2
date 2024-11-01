import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class HotelManagementSystem extends JFrame {
    private static final int INITIAL_ROOMS = 10; // Initial number of rooms
    private List<Room> rooms = new ArrayList<>();
    private List<Guest> guests = new ArrayList<>();
    private List<Reservation> reservations = new ArrayList<>();

    private JTextField guestNameField;
    private JTextField guestIdField;
    private JTextField roomNumberField;
    private JTextArea outputArea;
    private JLabel roomCountLabel;
    private SideMenuPanel sideMenuPanel;
    private JButton toggleMenuButton;

    private static final String LOG_FILE = "hotel_management_log.txt"; // Log file name
    private static final String GUEST_FILE = "guests.txt"; // Guest data file name

    public HotelManagementSystem() {
        setTitle("Hotel Lodge Management");
        setSize(700, 400); // Increased width to accommodate side menu
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Initialize rooms
        for (int i = 1; i <= INITIAL_ROOMS; i++) {
            rooms.add(new Room(i));
        }
        // Load previously occupied guests
        loadGuestData();

        // Create the side menu panel
        sideMenuPanel = new SideMenuPanel(this);
        add(sideMenuPanel, BorderLayout.WEST); // Add side menu to the west side of the frame

        // Home page content
        showHomePage();
    }

    private void loadGuestData() {
        try (BufferedReader reader = new BufferedReader(new FileReader(GUEST_FILE))) {
            String line;
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    String name = parts[0];
                    String id = parts[1];
                    int roomNumber = Integer.parseInt(parts[2]);
                    Date checkInDate = dateFormat.parse(parts[3]);

                    Room room = rooms.get(roomNumber - 1);
                    Guest guest = new Guest(name, id);
                    guests.add(guest);
                    room.setAvailable(false);

                    Reservation reservation = new Reservation(guest, room, checkInDate, null);
                    reservations.add(reservation);
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading guest data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveGuestData() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(GUEST_FILE))) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            for (Reservation reservation : reservations) {
                if (reservation.getCheckOutDate() == null) {
                    String line = String.join(",",
                            reservation.getGuest().getName(),
                            reservation.getGuest().getId(),
                            String.valueOf(reservation.getRoom().getRoomNumber()),
                            dateFormat.format(reservation.getCheckInDate())
                    );
                    writer.write(line);
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving guest data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void showRoomStatusPage() {
        getContentPane().removeAll();
        sideMenuPanel.setVisible(true);

        JPanel roomStatusPanel = new JPanel(new BorderLayout());
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        roomStatusPanel.add(new JScrollPane(outputArea), BorderLayout.CENTER);

        // Directly view room status
        viewRoomStatus();

        add(sideMenuPanel, BorderLayout.WEST);
        add(roomStatusPanel, BorderLayout.CENTER);

        revalidate();
        repaint();
    }

    public void showLogPage() {
        LogPage logPage = new LogPage();
        logPage.setVisible(true); // Show the log page
    }


    private void viewRoomStatus() {
        outputArea.setText("Room Status:\n");
        for (Room room : rooms) {
            String status = room.isAvailable() ? "Available" : "Occupied";
            outputArea.append("Room " + room.getRoomNumber() + ": " + status + "\n");
        }
        logActivity("Viewed room status");
    }

    public void showHomePage() {
        getContentPane().removeAll();
        sideMenuPanel.setVisible(false); // Hide the side menu by default

        // Create the background panel with the image
        BackgroundPanel backgroundPanel = new BackgroundPanel("src/home.jpg"); // Update path if necessary

        // Create a placeholder for the main content area
        JPanel mainContentPanel = new JPanel();
        mainContentPanel.setLayout(new BorderLayout());

        // Use JEditorPane for HTML support
        JEditorPane welcomeArea = new JEditorPane("text/html", "");
        welcomeArea.setText("<html>" +
                "<h2>Welcome to the Hotel THE HONOURED ONE</h2>" +
                "<pre>     Use the button below to show/hide the navigation menu.</pre>" +
                "<h3>Luxury Hotel</h3>" +
                "<p style='font-weight: bold;'>Indulge in opulence at <strong>THE HONOURED ONE</strong>, where timeless elegance meets modern luxury.</p>" +
                "<p>Immerse yourself in world-class amenities, impeccable service, and breathtaking views.</p>" +
                "<p>Create unforgettable memories in our luxurious accommodations.</p>" +
                "<img src='file:home.jpg' alt='hotel image' width='300' height='200'>" +  // Adjust image path and size
                "</html>");

        welcomeArea.setBackground(new Color(0,0,0,1)); // Set background color to a light gray
        welcomeArea.setEditable(false);
        welcomeArea.setOpaque(true); // Make the color visible

        welcomeArea.setEditable(false);
        welcomeArea.setOpaque(false); // To make sure the background shows through
        mainContentPanel.add(new JScrollPane(welcomeArea), BorderLayout.CENTER);

        // Toggle Menu Button
        toggleMenuButton = new JButton("Show Menu");
        toggleMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (sideMenuPanel.isVisible()) {
                    sideMenuPanel.setVisible(false);
                    toggleMenuButton.setText("Show Menu");
                } else {
                    sideMenuPanel.setVisible(true);
                    toggleMenuButton.setText("Hide Menu");
                }
                revalidate();
                repaint();
            }
        });
        mainContentPanel.add(toggleMenuButton, BorderLayout.SOUTH);

        backgroundPanel.setLayout(new BorderLayout());
        backgroundPanel.add(mainContentPanel, BorderLayout.CENTER);
        welcomeArea.setBackground(new Color(240, 240, 240));

        add(sideMenuPanel, BorderLayout.WEST);
        add(backgroundPanel, BorderLayout.CENTER);

        revalidate();
        repaint();
    }

    public void showCheckInPage() {
        getContentPane().removeAll();
        sideMenuPanel.setVisible(true);

        JPanel checkInPanel = new JPanel(new GridLayout(5, 2));
        checkInPanel.add(new JLabel("Guest Name:"));
        guestNameField = new JTextField();
        checkInPanel.add(guestNameField);

        checkInPanel.add(new JLabel("Guest ID:"));
        guestIdField = new JTextField();
        checkInPanel.add(guestIdField);

        checkInPanel.add(new JLabel("Room Number:"));
        roomNumberField = new JTextField();
        checkInPanel.add(roomNumberField);

        JButton checkInButton = new JButton("Check In");
        checkInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkInGuest();
            }
        });
        checkInPanel.add(checkInButton);

        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close the application
            }
        });
        checkInPanel.add(logoutButton);

        add(sideMenuPanel, BorderLayout.WEST);
        add(checkInPanel, BorderLayout.CENTER);

        revalidate();
        repaint();
    }

    public void showCheckOutPage() {
        getContentPane().removeAll();
        sideMenuPanel.setVisible(true);

        JPanel checkOutPanel = new JPanel(new GridLayout(2, 2));
        checkOutPanel.add(new JLabel("Room Number:"));
        roomNumberField = new JTextField();
        checkOutPanel.add(roomNumberField);

        JButton checkOutButton = new JButton("Check Out");
        checkOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkOutGuest();
            }
        });
        checkOutPanel.add(checkOutButton);

        add(sideMenuPanel, BorderLayout.WEST);
        add(checkOutPanel, BorderLayout.CENTER);

        revalidate();
        repaint();
    }

    public void showViewGuestsPage() {
        getContentPane().removeAll();
        sideMenuPanel.setVisible(true);

        JPanel viewGuestsPanel = new JPanel(new BorderLayout());
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        viewGuestsPanel.add(new JScrollPane(outputArea), BorderLayout.CENTER);

        // Directly view guests
        viewGuests();

        add(sideMenuPanel, BorderLayout.WEST);
        add(viewGuestsPanel, BorderLayout.CENTER);

        revalidate();
        repaint();
    }

    public void increaseRooms() {
        int newRoomNumber = rooms.size() + 1;
        rooms.add(new Room(newRoomNumber));
        roomCountLabel.setText("Total Rooms: " + rooms.size());
        outputArea.append("Room " + newRoomNumber + " added.\n");
        logActivity("Room " + newRoomNumber + " added");
    }

    public void decreaseRooms() {
        if (rooms.size() > 0) {
            Room removedRoom = rooms.remove(rooms.size() - 1);
            roomCountLabel.setText("Total Rooms: " + rooms.size());
            outputArea.append("Room " + removedRoom.getRoomNumber() + " removed.\n");
            logActivity("Room " + removedRoom.getRoomNumber() + " removed");
        } else {
            outputArea.append("No more rooms to remove.\n");
        }
    }

    public void clearScreen() {
        if (outputArea != null) {
            outputArea.setText("");
        }
    }

    private void checkInGuest() {
        String name = guestNameField.getText();
        String id = guestIdField.getText();
        int roomNumber = Integer.parseInt(roomNumberField.getText());

        Room room = rooms.get(roomNumber - 1);
        if (room.isAvailable()) {
            Guest guest = new Guest(name, id);
            guests.add(guest);
            room.setAvailable(false);

            Date checkInDate = new Date();
            Date checkOutDate = null;
            Reservation reservation = new Reservation(guest, room, checkInDate, checkOutDate);
            reservations.add(reservation);

            outputArea.append("Guest " + name + " checked into room " + roomNumber + ".\n");
            logActivity("Guest " + name + " checked into room " + roomNumber);
            saveGuestData(); // Save the guest data

            // Clear input fields after check-in
            guestNameField.setText("");
            guestIdField.setText("");
            roomNumberField.setText("");
        } else {
            outputArea.append("Room " + roomNumber + " is not available.\n");
        }
    }
    private void checkOutGuest() {
        int roomNumber = Integer.parseInt(roomNumberField.getText());
        Room room = rooms.get(roomNumber - 1);
        if (!room.isAvailable()) {
            room.setAvailable(true);
            outputArea.append("Room " + roomNumber + " is now available.\n");

            for (Reservation reservation : reservations) {
                if (reservation.getRoom().getRoomNumber() == roomNumber && reservation.getCheckOutDate() == null) {
                    reservation.setCheckOutDate(new Date()); // Set the checkout date
                    outputArea.append("Guest " + reservation.getGuest().getName() + " checked out from room " + roomNumber + ".\n");
                    logActivity("Guest " + reservation.getGuest().getName() + " checked out from room " + roomNumber);
                    saveGuestData(); // Save the guest data
                    break;
                }
            }
        } else {
            outputArea.append("Room " + roomNumber + " is already available.\n");
        }
    }

    private void viewGuests() {
        outputArea.setText("Current Guests:\n");
        for (Guest guest : guests) {
            outputArea.append("Name: " + guest.getName() + ", ID: " + guest.getId() + "\n");
        }
        outputArea.append("\nReservations:\n");
        for (Reservation reservation : reservations) {
            outputArea.append(reservation.toString() + "\n");
        }
        logActivity("Viewed guests");
    }

    public void logout() {
        // Optionally save data or perform any clean-up tasks here
        saveGuestData(); // Save guest data to file

        // Close the application
        System.exit(0);
    }

    public class DatabaseConnect {
        // Database URL, username, and password
        private static final String URL = "jdbc:postgresql://localhost:5432/hotel_db";
        private static final String USER = "postgres";
        private static final String PASSWORD = "sanjay2005";

        public static void checkDatabaseConnection() {
            Connection connection = null;

            try {
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                if (connection != null) {
                    System.out.println("Connection to the database was successful!");
                    fetchAndUpdateGuestData(connection); // Corrected method call
                }
            } catch (SQLException e) {
                System.out.println("Failed to connect to the database!");
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    try {
                        connection.close();
                    } catch (SQLException e) {
                        System.out.println("Failed to close the connection.");
                        e.printStackTrace();
                    }
                }
            }
        }

        public static void fetchAndUpdateGuestData(Connection connection) {
            // Implement your logic to read from the guest file
            // and update the database here, similar to what you had before.

            // Example logic to read from the guest file and insert/update in the database
            try (BufferedReader reader = new BufferedReader(new FileReader(GUEST_FILE))) {
                String line;
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length == 4) {
                        String name = parts[0];
                        String id = parts[1];
                        int roomNumber = Integer.parseInt(parts[2]);
                        Date checkInDate = dateFormat.parse(parts[3]);

                        // Prepare SQL statement
                        String sql = "INSERT INTO guests (name, id, room_number, check_in_time) VALUES (?, ?, ?, ?) "
                                + "ON CONFLICT (id) DO UPDATE SET room_number = ?, check_in_time = ?"; // Update as needed

                        try (var preparedStatement = connection.prepareStatement(sql)) {
                            preparedStatement.setString(1, name);
                            preparedStatement.setString(2, id);
                            preparedStatement.setInt(3, roomNumber);
                            preparedStatement.setTimestamp(4, new java.sql.Timestamp(checkInDate.getTime()));
                            preparedStatement.setInt(5, roomNumber);
                            preparedStatement.setTimestamp(6, new java.sql.Timestamp(checkInDate.getTime()));
                            preparedStatement.executeUpdate();
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            } catch (IOException | ParseException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error updating guest data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }



    private void logActivity(String message) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE, true))) {
            writer.write(new Date() + ": " + message);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void fetchDatabaseData() {
        DatabaseConnect.checkDatabaseConnection(); // Now you can call this in the HotelManagementSystem
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                HotelManagementSystem hotelManagementSystem = new HotelManagementSystem();
                hotelManagementSystem.setVisible(true);
                // Call to fetch and update guest data after the UI is set up
                hotelManagementSystem.fetchDatabaseData();
            }
        });
    }

}
