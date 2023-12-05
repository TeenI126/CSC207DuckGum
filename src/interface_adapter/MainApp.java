package interface_adapter;
import data_access.AmazonMusicDataAccessObject;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.net.URI;
import java.util.Random;

public class MainApp extends JFrame {

    private DefaultListModel<String> spotifyListModel;
    private DefaultListModel<String> amazonListModel;
    private DefaultListModel<String> spotifySongListModel;
    private DefaultListModel<String> amazonSongListModel;
    private JList<String> spotifyList;
    private JList<String> amazonList;
    private JList<String> spotifySongList;
    private JList<String> amazonSongList;
    private JButton spotifyLoginButton;
    private JButton amazonLoginButton;
    private JButton syncPlaylistsButton;

    private static final String CLIENT_ID = "amzn1.application-oa2-client.951516002f594c19922fd8aa22fa93fc";
    private static final String REDIRECT_URI = "http://localhost:8080/callback";

    public MainApp() {
        // Frame settings
        setTitle("Playlist Transfer App");
        setSize(1600, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(new GridLayout(1, 2));

        // Spotify Section
        JPanel spotifyPanel = new JPanel();
        spotifyPanel.setLayout(new BorderLayout());
        spotifyLoginButton = new JButton("Login to Spotify");
        spotifyListModel = new DefaultListModel<>();
        spotifySongListModel = new DefaultListModel<>();
        spotifyList = new JList<>(spotifyListModel);
        spotifySongList = new JList<>(spotifySongListModel);
        spotifyPanel.add(spotifyLoginButton, BorderLayout.NORTH);
        spotifyPanel.add(new JScrollPane(spotifyList), BorderLayout.CENTER);
        spotifyPanel.add(new JScrollPane(spotifySongList), BorderLayout.SOUTH);
        getContentPane().add(spotifyPanel);

        // Amazon Music Section
        JPanel amazonPanel = new JPanel();
        amazonPanel.setLayout(new BorderLayout());
        amazonLoginButton = new JButton("Login to Amazon Music");
        amazonListModel = new DefaultListModel<>();
        amazonSongListModel = new DefaultListModel<>();
        amazonList = new JList<>(amazonListModel);
        amazonSongList = new JList<>(amazonSongListModel);
        amazonPanel.add(amazonLoginButton, BorderLayout.NORTH);
        amazonPanel.add(new JScrollPane(amazonList), BorderLayout.CENTER);
        amazonPanel.add(new JScrollPane(amazonSongList), BorderLayout.SOUTH);
        getContentPane().add(amazonPanel);

        // Transfer Buttons Panel
        JPanel transferPanel = new JPanel(new FlowLayout());
        JButton transferToAmazonButton = new JButton("Transfer to Amazon Music");
        JButton transferToSpotifyButton = new JButton("Transfer to Spotify");
        transferPanel.add(transferToAmazonButton);
        transferPanel.add(transferToSpotifyButton);
        syncPlaylistsButton = new JButton("Sync Playlists");
        transferPanel.add(syncPlaylistsButton);

        // Add song and remove song buttons
        JButton addSongButton = new JButton("Add Song");
        JButton removeSongButton = new JButton("Remove Song");
        transferPanel.add(addSongButton);
        transferPanel.add(removeSongButton);

        // Add transfer panel to both Spotify and Amazon panels at the bottom
        spotifyPanel.add(transferPanel, BorderLayout.SOUTH);
        amazonPanel.add(transferPanel, BorderLayout.SOUTH);

        // Action Listeners
        //spotifyLoginButton.addActionListener(e -> loginToService("Spotify"));
        //amazonLoginButton.addActionListener(e -> loginToService("Amazon"));
        amazonLoginButton.addActionListener(e -> loginToAmazon());
        transferToAmazonButton.addActionListener(e -> transferPlaylists("Spotify to Amazon Music"));
        transferToSpotifyButton.addActionListener(e -> transferPlaylists("Amazon Music to Spotify"));
        syncPlaylistsButton.addActionListener(e -> syncPlaylists());
        addSongButton.addActionListener(e -> addSong());
        removeSongButton.addActionListener(e -> removeSong());


    }

    private void loginToAmazon() {
        try {
            String amazonLoginUrl = String.format("https://www.amazon.com/ap/oa?client_id=%s&scope=profile&response_type=code&redirect_uri=%s", CLIENT_ID, REDIRECT_URI);
            Desktop.getDesktop().browse(new URI(amazonLoginUrl));
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error opening Amazon login page: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void handleAmazonLoginCallback(String authorizationCode) {
        // Exchange authorization code for access token
        String tokenResponse = AmazonMusicDataAccessObject.exchangeAuthorizationCode(authorizationCode);
        JSONObject tokenJson = new JSONObject(tokenResponse);
        String accessToken = tokenJson.getString("access_token");

        // Fetch user profile and playlists
        String userPlaylistsJson = AmazonMusicDataAccessObject.fetchUserPlaylists(accessToken);

        updateAmazonPlaylistDisplay(userPlaylistsJson);
    }

    // Method to update Amazon playlist display
    private void updateAmazonPlaylistDisplay(String playlistsJson) {
        SwingUtilities.invokeLater(() -> {
            amazonListModel.clear();
            amazonSongListModel.clear();

            // Parse playlists JSON and update the list models
            // Assuming playlistsJson is a JSON array of playlist objects
            JSONObject playlistsObject = new JSONObject(playlistsJson);
            JSONArray playlistsArray = playlistsObject.getJSONArray("playlists");
            for (int i = 0; i < playlistsArray.length(); i++) {
                JSONObject playlist = playlistsArray.getJSONObject(i);
                String playlistName = playlist.getString("name");
                amazonListModel.addElement(playlistName);

                // Add songs to amazonSongListModel if needed
            }

            amazonLoginButton.setText("Successfully logged in");
            amazonLoginButton.setBackground(Color.GREEN);
        });
    }
    // Test
//    void loginToService(String service) {
//        if (service.equals("Spotify")) {
//            spotifyListModel.clear();
//            spotifySongListModel.clear();
//
//            // Adding playlists
//            spotifyListModel.addElement("Chill Vibes");
//            spotifyListModel.addElement("Workout Hits");
//            spotifyListModel.addElement("Good Vibes");
//            spotifyListModel.addElement("Drake Stuff");
//
//            // Adding songs to the song list model for Spotify playlists
//            spotifySongListModel.addElement("Chill Song 1");
//
//            spotifyLoginButton.setText("Successfully logged in");
//            spotifyLoginButton.setBackground(Color.GREEN);
//        } else if (service.equals("Amazon")) {
//            amazonListModel.clear();
//            amazonSongListModel.clear();
//
//            // Adding playlists
//            amazonListModel.addElement("Pop Chartbusters");
//            amazonListModel.addElement("Rock Legends");
//            amazonListModel.addElement("Lil Yachty Legends");
//            amazonListModel.addElement("Summer Pop");
//
//            // Adding songs to the song list model for Amazon playlists
//            amazonSongListModel.addElement("Pop Song 1");
//
//            amazonLoginButton.setText("Successfully logged in");
//            amazonLoginButton.setBackground(Color.GREEN);
//        }
//        spotifySongList.repaint();
//        spotifySongList.revalidate();
//
//        amazonSongList.repaint();
//        amazonSongList.revalidate();
//    }



    private void transferPlaylists(String direction) {
        JOptionPane pane = new JOptionPane("Transferring playlists from " + direction, JOptionPane.INFORMATION_MESSAGE);
        JDialog dialog = pane.createDialog(this, "Status");
        Timer timer = new Timer(2000, e -> dialog.dispose());
        timer.setRepeats(false);
        timer.start();
        dialog.setVisible(true);
    }

    private void syncPlaylists() {
        // Dummy sync playlists logic
        // ...
    }

    private void addSong() {
        // Dummy add song logic
        // ...
    }

    private void removeSong() {
        // Dummy remove song logic
        // ...
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainApp().setVisible(true));
    }
}

