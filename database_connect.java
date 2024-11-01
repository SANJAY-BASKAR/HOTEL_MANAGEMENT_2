import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

public class database_connect {

    // Database URL, username, and password
    private static final String URL = "jdbc:postgresql://localhost:5432/hotel_db"; // Change database name as needed
    private static final String USER = "postgres"; // Replace with your database username
    private static final String PASSWORD = "sanjay2005"; // Replace with your database password

    public static void main(String[] args) {
        checkDatabaseConnection();
    }

    public static void checkDatabaseConnection() {
        Connection connection = null;

        try {
            // Attempt to establish a connection
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            if (connection != null) {
                System.out.println("Connection to the database was successful!");
                fetchAndUpdateGuestData(connection); // Fetch and update data from guest.txt
            }
        } catch (SQLException e) {
            System.out.println("Failed to connect to the database!");
            e.printStackTrace(); // Print the exception details
        } finally {
            // Close the connection
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
        String filePath = "guests.txt"; // Path to your guest.txt file
        String sql = "INSERT INTO guests (name, room_number, guest_id, check_in_time) VALUES (?, ?, ?, ?);";

        try (BufferedReader br = new BufferedReader(new FileReader(filePath));
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            String line;
            while ((line = br.readLine()) != null) {
                String[] guestData = line.split(",");

                // Ensure we have the correct number of fields
                if (guestData.length == 4) {
                    String name = guestData[0].trim();
                    int roomNumber = Integer.parseInt(guestData[1].trim());
                    int guestId = Integer.parseInt(guestData[2].trim());
                    String checkInTime = guestData[3].trim();

                    // Convert the check-in time string to a Timestamp
                    Timestamp timestamp = Timestamp.valueOf(checkInTime);

                    // Set parameters in the prepared statement
                    pstmt.setString(1, name);
                    pstmt.setInt(2, roomNumber);
                    pstmt.setInt(3, guestId);
                    pstmt.setTimestamp(4, timestamp); // Set the Timestamp parameter

                    // Execute the update
                    pstmt.executeUpdate();
                    System.out.println("Inserted guest: " + name);
                } else {
                    System.out.println("Invalid guest data format: " + line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading the guest file: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Database update error: " + e.getMessage());
        }
    }
}