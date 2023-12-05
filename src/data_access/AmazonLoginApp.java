package data_access;

// Importing required classes for GUI, event handling, and URI processing
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URI;

/**
 * The AmazonLoginApp class provides a graphical user interface for users to log in using their Amazon credentials.
 * It demonstrates OAuth authentication flow by redirecting to the Amazon login page and handling the callback.
 */
//public class AmazonLoginApp {
//    // ***Test 1***

//    private static final String CLIENT_ID = "amzn1.application-oa2-client.951516002f594c19922fd8aa22fa93fc";
//    private static final String REDIRECT_URI = "http://localhost:8080/callback";
//    private static JTextArea userInfoArea;
//
//    public static void main(String[] args) {
//        JFrame frame = new JFrame("Amazon Login Demo");
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setSize(400, 500);
//        frame.setLayout(new BorderLayout());
//
//        JButton loginButton = new JButton("Login with Amazon");
//        frame.add(loginButton, BorderLayout.NORTH);
//
//        userInfoArea = new JTextArea();
//        userInfoArea.setEditable(false);
//        JScrollPane scrollPane = new JScrollPane(userInfoArea);
//        frame.add(scrollPane, BorderLayout.CENTER);
//
//        loginButton.addActionListener(e -> {
//            try {
//                String amazonLoginUrl = String.format("https://www.amazon.com/ap/oa?client_id=%s&scope=profile&response_type=code&redirect_uri=%s", CLIENT_ID, REDIRECT_URI);
//                Desktop.getDesktop().browse(new URI(amazonLoginUrl));
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            }
//        });
//
//        frame.setLocationRelativeTo(null);
//        frame.setVisible(true);
//    }
//
//    public static void updateUserInfo(String userProfile, String playlists) {
//        String displayText = "Login Successful!\n\nUser Profile:\n" + userProfile + "\n\nUser Playlists:\n" + playlists;
//        SwingUtilities.invokeLater(() -> userInfoArea.setText(displayText));
//    }
//
//    // ***Test 2***
//
//    // Client ID for OAuth authentication with Amazon; replace with your actual client ID.
//    private static final String CLIENT_ID = "amzn1.application-oa2-client.951516002f594c19922fd8aa22fa93fc";
//    // Redirect URI for OAuth callback; should be registered with your Amazon developer account.
//    private static final String REDIRECT_URI = "http://localhost:8080/callback";
//    // Text area for displaying user information post successful login.
//    private static JTextArea userInfoArea;
//
//    /**
//     * The main method sets up the GUI and adds behavior to the login button.
//     * @param args Not used in this application.
//     */
//    public static void main(String[] args) {
//        // Setting up the main frame of the application
//        JFrame frame = new JFrame("Amazon Login Demo");
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setSize(400, 500);
//        frame.setLayout(new BorderLayout());
//
//        // Creating and configuring a button used for initiating the Amazon login process
//        JButton loginButton = new JButton("Login with Amazon");
//        frame.add(loginButton, BorderLayout.NORTH);
//
//        // Text area within a scroll pane to display user information or login status
//        userInfoArea = new JTextArea();
//        userInfoArea.setEditable(false); // Making the text area non-editable
//        JScrollPane scrollPane = new JScrollPane(userInfoArea); // Adding the text area to a scroll pane for scrollability
//        frame.add(scrollPane, BorderLayout.CENTER);
//
//        // Adding an action listener to the login button to handle user click action
//        loginButton.addActionListener(e -> {
//            try {
//                // Constructing the Amazon login URL with necessary parameters
//                String amazonLoginUrl = String.format("https://www.amazon.com/ap/oa?client_id=%s&scope=profile&response_type=code&redirect_uri=%s", CLIENT_ID, REDIRECT_URI);
//                // Directing the user's default browser to the Amazon login page
//                Desktop.getDesktop().browse(new URI(amazonLoginUrl));
//            } catch (Exception ex) {
//                // Print the stack trace to the console in case of an error
//                ex.printStackTrace();
//            }
//        });
//
//        // Positioning the frame in the center of the screen and making it visible
//        frame.setLocationRelativeTo(null);
//        frame.setVisible(true);
//    }
//
//    /**
//     * This method updates the GUI with user information received from the login process.
//     * @param info The user information string to be displayed in the text area.
//     */
//    public static void updateUserInfo(String info) {
//        // Logging the received information to the console for debugging purposes
//        System.out.println("Received info for GUI update: " + info);
//        // Updating the text area content in the Event Dispatch Thread to ensure thread safety
//        SwingUtilities.invokeLater(() -> userInfoArea.setText(info));
//    }
//
//}
