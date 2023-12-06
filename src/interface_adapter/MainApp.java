package interface_adapter;
import data_access.AmazonMusicDataAccessObject;
import org.json.JSONArray;
import org.json.JSONObject;

import data_access.SpotifyDataAccessObject;
import entities.Account;
import entities.Playlist;
import interface_adapter.LogInSpotify.LogInSpotifyController;
import interface_adapter.LogInSpotify.LogInSpotifyPresenter;
import interface_adapter.LogInSpotify.LogInSpotifyViewModel;
import interface_adapter.OpenSpotifyLogin.OpenSpotifyLoginController;
import interface_adapter.OpenSpotifyLogin.OpenSpotifyLoginPresenter;
import interface_adapter.OpenSpotifyLogin.OpenSpotifyLoginState;
import interface_adapter.OpenSpotifyLogin.OpenSpotifyLoginViewModel;
import use_case.LogInSpotify.LogInSpotifyInteractor;
import use_case.OpenLoginSpotify.OpenLoginSpotifyInteractor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.net.URI;
import java.util.Objects;

public class MainApp extends JFrame {

    private DefaultListModel<String> spotifyListModel;
    private java.util.List<Playlist> spotifyListPlaylists;
    private DefaultListModel<String> amazonListModel;
    private DefaultListModel<String> spotifySongListModel;
    private DefaultListModel<String> amazonSongListModel;
    private JList<String> spotifyList;
    private JList<String> amazonList;
    private JList<String> spotifySongList;
    private JList<String> amazonSongList;
    private JButton spotifyLoginButton;
    private JButton amazonLoginButton;
    private JButton exportCSVButton;

    private static final String CLIENT_ID = "amzn1.application-oa2-client.951516002f594c19922fd8aa22fa93fc";
    private static final String REDIRECT_URI = "http://localhost:8080/callback";

    SpotifyDataAccessObject spotifyDataAccessObject;

    Account account = new Account();


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
        exportCSVButton = new JButton("Export playlists to CSV");
        transferPanel.add(exportCSVButton);

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
        exportCSVButton.addActionListener(e -> exportPlaylistCSV());
        addSongButton.addActionListener(e -> addSong());
        removeSongButton.addActionListener(e -> removeSong());

        //Data Access Objects
        spotifyDataAccessObject = new SpotifyDataAccessObject();

        //OpenLoginSpotify
        OpenSpotifyLoginViewModel openSpotifyLoginViewModel = new OpenSpotifyLoginViewModel();
        OpenSpotifyLoginController openSpotifyLoginController = new OpenSpotifyLoginController(
                new OpenLoginSpotifyInteractor(
                        new OpenSpotifyLoginPresenter(openSpotifyLoginViewModel),spotifyDataAccessObject));

        spotifyLoginButton.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if(e.getSource().equals(spotifyLoginButton)){
                            OpenSpotifyLoginState currentState = openSpotifyLoginViewModel.getState();

                            openSpotifyLoginController.execute();
                        }

                    }
                }
        );
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

    public void propertyChange(PropertyChangeEvent evt){
       MainViewModelState state = (MainViewModelState) evt.getNewValue();
        if (!Objects.equals(state.getCallbackUrl(), "")){
            try {
                Desktop.getDesktop().browse(URI.create(state.getCallbackUrl()));
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Failed to open browser for signin");
            }

            LogInSpotify();
        } else if (state.getSpotifyAccount() != null){
            updateSpotifyPlaylists(state);

        }
    }

    private void updateSpotifyPlaylists(MainViewModelState state) {
        spotifyListModel.clear();
        java.util.List<Playlist> playlists = state.getSpotifyAccount().getPlaylists();
        int counter = 0;
        for (Playlist playlist : playlists){
            spotifyListModel.add(counter, playlist.getName());
            spotifyListPlaylists.add(counter, playlist);
            counter++;
        }
    }

    private Playlist getSelectedSpotifyPlaylist(){
        return  spotifyListPlaylists.get(spotifyList.getSelectedIndex());
    }

    private void LogInSpotify(){

        String uri = JOptionPane.showInputDialog("Please enter url of page after spotify sign in");
        String code = uri.substring(47);

        LogInSpotifyViewModel logInSpotifyViewModel = new LogInSpotifyViewModel();
        LogInSpotifyPresenter logInSpotifyPresenter = new LogInSpotifyPresenter();
        LogInSpotifyController logInSpotifyController = new LogInSpotifyController(new LogInSpotifyInteractor(logInSpotifyPresenter, spotifyDataAccessObject));

        logInSpotifyController.execute(code, account);

    }

    private void exportPlaylistCSV() {
        // Dummy sync playlists logic
        // ...
    }


    private void addSong() {
        JFrame frame = new JFrame("Select A Music Service:");
        JLabel select = new JLabel("Select A Music Service:");

        // Creating a new buttons
        JButton spotifyButton = new JButton("Spotify");
        JButton amazonButton = new JButton("Amazon");

        // Creating a panel to add buttons
        JPanel panel = new JPanel();

        // Adding buttons and textfield to panel
        // using add() method
        panel.add(select);
        panel.add(spotifyButton);
        panel.add(amazonButton);

        // Adding panel to frame
        frame.add(panel);

        // Setting the size of frame
        frame.setSize(300, 300);

        frame.show();

        spotifyButton.addActionListener(e -> frame.dispose());
        spotifyButton.addActionListener(e -> selectPlaylistSpotifyToAdd());
        amazonButton.addActionListener(e -> frame.dispose());
        amazonButton.addActionListener(e -> selectPlaylistAmazonToAdd());

        // Dummy add song logic
        // ...
    }

    private void selectPlaylistSpotifyToAdd() {
        JFrame frame = new JFrame();
        JLabel select = new JLabel("Select A Spotify Playlist:");

        // Creating a new buttons
        JButton playlist1 = new JButton("Chill Vibes");
        JButton playlist2 = new JButton("Workout Hits");
        JButton playlist3 = new JButton("Good Vibes");
        JButton playlist4 = new JButton("Drake Stuff");

        // Creating a panel to add buttons
        JPanel panel = new JPanel();

        // Adding buttons and textfield to panel
        // using add() method
        panel.add(select);
        panel.add(playlist1);
        panel.add(playlist2);
        panel.add(playlist3);
        panel.add(playlist4);

        // Adding panel to frame
        frame.add(panel);

        // Setting the size of frame
        frame.setSize(300, 300);

        frame.show();
        playlist1.addActionListener(e -> selectSongToAdd("Chill Vibes"));
        playlist1.addActionListener(e -> frame.dispose());
        playlist2.addActionListener(e -> selectSongToAdd("Workout Hits"));
        playlist2.addActionListener(e -> frame.dispose());
        playlist3.addActionListener(e -> selectSongToAdd("Good Vibes"));
        playlist3.addActionListener(e -> frame.dispose());
        playlist4.addActionListener(e -> selectSongToAdd("Drake Stuff"));
        playlist4.addActionListener(e -> frame.dispose());
    }

    private void selectPlaylistAmazonToAdd() {
        JFrame frame = new JFrame();
        JLabel select = new JLabel("Select An Amazon Playlist:");

        // Creating a new buttons
        JButton playlist1 = new JButton("Pop Chartbusters");
        JButton playlist2 = new JButton("Rock Legends");
        JButton playlist3 = new JButton("Lil Yachty Legends");
        JButton playlist4 = new JButton("Summer Pop");

        // Creating a panel to add buttons
        JPanel panel = new JPanel();

        // Adding buttons and textfield to panel
        // using add() method
        panel.add(select);
        panel.add(playlist1);
        panel.add(playlist2);
        panel.add(playlist3);
        panel.add(playlist4);

        // Adding panel to frame
        frame.add(panel);

        // Setting the size of frame
        frame.setSize(300, 300);

        frame.show();
        playlist1.addActionListener(e -> selectSongToAdd("Pop Chartbusters"));
        playlist1.addActionListener(e -> frame.dispose());
        playlist2.addActionListener(e -> selectSongToAdd("Rock Legends"));
        playlist2.addActionListener(e -> frame.dispose());
        playlist3.addActionListener(e -> selectSongToAdd("Lil Yachty Legends"));
        playlist3.addActionListener(e -> frame.dispose());
        playlist4.addActionListener(e -> selectSongToAdd("Summer Pop"));
        playlist4.addActionListener(e -> frame.dispose());
    }

    private void selectSongToAdd(String playlist) {
        JFrame frame = new JFrame();
        JLabel select = new JLabel("Select a song to add to " + playlist + ":");

        // Creating a new buttons
        JButton song1 = new JButton("Song 1");
        JButton song2 = new JButton("Song 2");

        // Creating a panel to add buttons
        JPanel panel = new JPanel();

        // Adding buttons and textfield to panel
        // using add() method
        panel.add(select);
        panel.add(song1);
        panel.add(song2);

        // Adding panel to frame
        frame.add(panel);

        // Setting the size of frame
        frame.setSize(300, 300);

        frame.show();
        song1.addActionListener(e -> songAddedSuccess("Song 1"));
        song1.addActionListener(e -> frame.dispose());
        song2.addActionListener(e -> songAddedSuccess("Song 2"));
        song2.addActionListener(e -> frame.dispose());
    }

    private void songAddedSuccess(String song) {
        JOptionPane pane = new JOptionPane(song + " added successfully!", JOptionPane.INFORMATION_MESSAGE);
        JDialog dialog = pane.createDialog(this, "Status");
        Timer timer = new Timer(2000, e -> dialog.dispose());
        timer.setRepeats(false);
        timer.start();
        dialog.setVisible(true);
    }

    private void removeSong() {
        JFrame frame = new JFrame("Select A Music Service:");
        JLabel select = new JLabel("Select A Music Service:");

        // Creating a new buttons
        JButton spotifyButton = new JButton("Spotify");
        JButton amazonButton = new JButton("Amazon");

        // Creating a panel to add buttons
        JPanel panel = new JPanel();

        // Adding buttons and textfield to panel
        // using add() method
        panel.add(select);
        panel.add(spotifyButton);
        panel.add(amazonButton);

        // Adding panel to frame
        frame.add(panel);

        // Setting the size of frame
        frame.setSize(300, 300);

        frame.show();

        spotifyButton.addActionListener(e -> frame.dispose());
        spotifyButton.addActionListener(e -> selectPlaylistSpotifyToRemove());
        amazonButton.addActionListener(e -> frame.dispose());
        amazonButton.addActionListener(e -> selectPlaylistAmazonToRemove());
        // Dummy remove song logic
        // ...
    }

    private void selectPlaylistSpotifyToRemove() {
        JFrame frame = new JFrame();
        JLabel select = new JLabel("Select A Spotify Playlist:");

        // Creating a new buttons
        JButton playlist1 = new JButton("Chill Vibes");
        JButton playlist2 = new JButton("Workout Hits");
        JButton playlist3 = new JButton("Good Vibes");
        JButton playlist4 = new JButton("Drake Stuff");

        // Creating a panel to add buttons
        JPanel panel = new JPanel();

        // Adding buttons and textfield to panel
        // using add() method
        panel.add(select);
        panel.add(playlist1);
        panel.add(playlist2);
        panel.add(playlist3);
        panel.add(playlist4);

        // Adding panel to frame
        frame.add(panel);

        // Setting the size of frame
        frame.setSize(300, 300);

        frame.show();
        playlist1.addActionListener(e -> selectSongToRemove("Chill Vibes"));
        playlist1.addActionListener(e -> frame.dispose());
        playlist2.addActionListener(e -> selectSongToRemove("Workout Hits"));
        playlist2.addActionListener(e -> frame.dispose());
        playlist3.addActionListener(e -> selectSongToRemove("Good Vibes"));
        playlist3.addActionListener(e -> frame.dispose());
        playlist4.addActionListener(e -> selectSongToRemove("Drake Stuff"));
        playlist4.addActionListener(e -> frame.dispose());
    }

    private void selectPlaylistAmazonToRemove() {
        JFrame frame = new JFrame();
        JLabel select = new JLabel("Select An Amazon Playlist:");

        // Creating a new buttons
        JButton playlist1 = new JButton("Pop Chartbusters");
        JButton playlist2 = new JButton("Rock Legends");
        JButton playlist3 = new JButton("Lil Yachty Legends");
        JButton playlist4 = new JButton("Summer Pop");

        // Creating a panel to add buttons
        JPanel panel = new JPanel();

        // Adding buttons and textfield to panel
        // using add() method
        panel.add(select);
        panel.add(playlist1);
        panel.add(playlist2);
        panel.add(playlist3);
        panel.add(playlist4);

        // Adding panel to frame
        frame.add(panel);

        // Setting the size of frame
        frame.setSize(300, 300);

        frame.show();
        playlist1.addActionListener(e -> selectSongToRemove("Pop Chartbusters"));
        playlist1.addActionListener(e -> frame.dispose());
        playlist2.addActionListener(e -> selectSongToRemove("Rock Legends"));
        playlist2.addActionListener(e -> frame.dispose());
        playlist3.addActionListener(e -> selectSongToRemove("Lil Yachty Legends"));
        playlist3.addActionListener(e -> frame.dispose());
        playlist4.addActionListener(e -> selectSongToRemove("Summer Pop"));
        playlist4.addActionListener(e -> frame.dispose());
    }

    private void selectSongToRemove(String playlist) {
        JFrame frame = new JFrame();
        JLabel select = new JLabel("Select a song to remove from " + playlist + ":");

        // Creating a new buttons
        JButton song3 = new JButton("Song 3");
        JButton song4 = new JButton("Song 4");

        // Creating a panel to add buttons
        JPanel panel = new JPanel();

        // Adding buttons and textfield to panel
        // using add() method
        panel.add(select);
        panel.add(song3);
        panel.add(song4);

        // Adding panel to frame
        frame.add(panel);

        // Setting the size of frame
        frame.setSize(300, 300);

        frame.show();
        song3.addActionListener(e -> songRemovedSuccess("Song 3"));
        song3.addActionListener(e -> frame.dispose());
        song4.addActionListener(e -> songRemovedSuccess("Song 4"));
        song4.addActionListener(e -> frame.dispose());
    }

    private void songRemovedSuccess(String song) {
        JOptionPane pane = new JOptionPane(song + " removed successfully!", JOptionPane.INFORMATION_MESSAGE);
        JDialog dialog = pane.createDialog(this, "Status");
        Timer timer = new Timer(2000, e -> dialog.dispose());
        timer.setRepeats(false);
        timer.start();
        dialog.setVisible(true);
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainApp().setVisible(true));
    }
}

