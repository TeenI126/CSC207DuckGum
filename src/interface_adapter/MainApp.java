package interface_adapter;

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
import java.util.ArrayList;
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
    private JButton syncPlaylistsButton;

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
        spotifyLoginButton.addActionListener(e -> loginToService("Spotify"));
        amazonLoginButton.addActionListener(e -> loginToService("Amazon"));
        transferToAmazonButton.addActionListener(e -> transferPlaylists("Spotify to Amazon Music"));
        transferToSpotifyButton.addActionListener(e -> transferPlaylists("Amazon Music to Spotify"));
        syncPlaylistsButton.addActionListener(e -> syncPlaylists());
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
    private void loginToService(String service) {
        if (service.equals("Spotify")) {
            spotifyListModel.clear();
            spotifySongListModel.clear();

            // Adding playlists
            spotifyListModel.addElement("Chill Vibes");
            spotifyListModel.addElement("Workout Hits");
            spotifyListModel.addElement("Good Vibes");
            spotifyListModel.addElement("Drake Stuff");

            // Adding songs to the song list model for Spotify playlists
            spotifySongListModel.addElement("Chill Song 1");

            spotifyLoginButton.setText("Successfully logged in");
            spotifyLoginButton.setBackground(Color.GREEN);
        } else if (service.equals("Amazon")) {
            amazonListModel.clear();
            amazonSongListModel.clear();

            // Adding playlists
            amazonListModel.addElement("Pop Chartbusters");
            amazonListModel.addElement("Rock Legends");
            amazonListModel.addElement("Lil Yachty Legends");
            amazonListModel.addElement("Summer Pop");

            // Adding songs to the song list model for Amazon playlists
            amazonSongListModel.addElement("Pop Song 1");

            amazonLoginButton.setText("Successfully logged in");
            amazonLoginButton.setBackground(Color.GREEN);
        }
        spotifySongList.repaint();
        spotifySongList.revalidate();

        amazonSongList.repaint();
        amazonSongList.revalidate();
    }



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

