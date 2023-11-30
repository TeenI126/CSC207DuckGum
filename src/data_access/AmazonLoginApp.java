package data_access;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URI;

public class AmazonLoginApp {

    private static final String CLIENT_ID = "amzn1.application-oa2-client.951516002f594c19922fd8aa22fa93fc";
    private static final String REDIRECT_URI = "http://localhost:8080/callback";
    private static JTextArea userInfoArea;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Amazon Login Demo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 500);
        frame.setLayout(new BorderLayout());

        JButton loginButton = new JButton("Login with Amazon");
        frame.add(loginButton, BorderLayout.NORTH);

        userInfoArea = new JTextArea();
        userInfoArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(userInfoArea);
        frame.add(scrollPane, BorderLayout.CENTER);

        loginButton.addActionListener(e -> {
            try {
                String amazonLoginUrl = String.format("https://www.amazon.com/ap/oa?client_id=%s&scope=profile&response_type=code&redirect_uri=%s", CLIENT_ID, REDIRECT_URI);
                Desktop.getDesktop().browse(new URI(amazonLoginUrl));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void updateUserInfo(String info) {
        System.out.println("Received info for GUI update: " + info);
        SwingUtilities.invokeLater(() -> userInfoArea.setText(info));
    }

}

