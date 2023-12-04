package use_case.TransferPlaylist;

import data_access.AmazonMusicDataAccessObject;
import data_access.SpotifyDataAccessObject;
import entities.Playlist;
import data_access.*;
import entities.*;
import java.io.IOException;

public class TransferPlaylistInteractor implements TransferPlaylistInputBoundary {

    private SpotifyDataAccessObject spotifyDAO;
    private AmazonMusicDataAccessObject amazonDAO;
    private TransferPlaylistOutputBoundary outputBoundary;

    public TransferPlaylistInteractor(SpotifyDataAccessObject spotifyDAO,
                                      AmazonMusicDataAccessObject amazonDAO,
                                      TransferPlaylistOutputBoundary outputBoundary) {
        this.spotifyDAO = spotifyDAO;
        this.amazonDAO = amazonDAO;
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void transferPlaylist(TransferPlaylistInputData inputData) {
        try {
            Playlist playlistToTransfer;
            if (inputData.getSourceService().equalsIgnoreCase("Spotify")) {
                playlistToTransfer = spotifyDAO.getPlaylist(inputData.getSpotifyAccessToken(), inputData.getPlaylistId());
                // Convert Spotify playlist to Amazon format and create it
                // Implementation will depend on the AmazonMusicDataAccessObject's method signatures
                // Create a createPlaylist method similar to the SpotifyDataAccessObject
                amazonDAO.createPlaylist(playlistToTransfer, inputData.getAmazonAccessToken());
                outputBoundary.presentTransferResult(new TransferPlaylistOutputData(true, "Playlist transferred from Spotify to Amazon Music successfully."));
            } else if (inputData.getSourceService().equalsIgnoreCase("Amazon")) {
                playlistToTransfer = amazonDAO.getPlaylist(inputData.getAmazonAccessToken(), inputData.getPlaylistId());
                // Convert Amazon playlist to Spotify format and create it
                // Implementation will depend on the SpotifyDataAccessObject's method signatures
                spotifyDAO.createPlaylist(playlistToTransfer, inputData.getSpotifyAccessToken());
                outputBoundary.presentTransferResult(new TransferPlaylistOutputData(true, "Playlist transferred from Amazon Music to Spotify successfully."));
            } else {
                throw new IllegalArgumentException("Unsupported music service for transfer.");
            }
        } catch (IOException | IllegalArgumentException e) {
            outputBoundary.presentTransferResult(new TransferPlaylistOutputData(false, "Failed to transfer playlist: " + e.getMessage()));
        }
    }

    // Transfer from Spotify to Amazon
    private void transferFromSpotifyToAmazon(String spotifyAccessToken, String playlistId, String amazonAccessToken) throws IOException {
        Playlist spotifyPlaylist = spotifyDAO.getPlaylist(spotifyAccessToken, playlistId);
        List<Song> mappedSongs = mapSongsForAmazon(spotifyPlaylist.getSongs());
        Playlist amazonPlaylist = createAmazonPlaylist(mappedSongs, spotifyPlaylist.getName(), amazonAccessToken);
        amazonDAO.createPlaylist(amazonPlaylist, amazonAccessToken); // Implement this method in your DAO
    }

    // Transfer from Amazon to Spotify
    private void transferFromAmazonToSpotify(String amazonAccessToken, String playlistId, String spotifyAccessToken) throws IOException {
        Playlist amazonPlaylist = amazonDAO.getPlaylist(amazonAccessToken, playlistId);
        List<Song> mappedSongs = mapSongsForSpotify(amazonPlaylist.getSongs());
        Playlist spotifyPlaylist = createSpotifyPlaylist(mappedSongs, amazonPlaylist.getName(), spotifyAccessToken);
        spotifyDAO.createPlaylist(spotifyPlaylist, spotifyAccessToken); // Implement this method in your DAO
    }

    // Map songs from a Spotify playlist for Amazon Music
    private List<Song> mapSongsForAmazon(List<Song> spotifySongs) {
        List<Song> amazonSongs = new ArrayList<>();
        for (Song spotifySong : spotifySongs) {
            // Implement song mapping logic here
            // You would typically use song metadata like track name and artist to find the corresponding song on Amazon Music
            Song amazonSong = findSongOnAmazon(spotifySong); // This is a placeholder for the actual mapping logic
            if (amazonSong != null) {
                amazonSongs.add(amazonSong);
            }
        }
        return amazonSongs;
    }

    // Map songs from an Amazon playlist for Spotify
    private List<Song> mapSongsForSpotify(List<Song> amazonSongs) {
        List<Song> spotifySongs = new ArrayList<>();
        for (Song amazonSong : amazonSongs) {
            // Implement song mapping logic here
            Song spotifySong = findSongOnSpotify(amazonSong); // This is a placeholder for the actual mapping logic
            if (spotifySong != null) {
                spotifySongs.add(spotifySong);
            }
        }
        return spotifySongs;
    }

    // Create a new Amazon playlist with mapped songs
    private Playlist createAmazonPlaylist(List<Song> songs, String playlistName, String amazonAccessToken) {
        // Implement playlist creation logic here for Amazon Music
        // The AmazonMusicDataAccessObject should have a method to create a playlist given a list of songs
        return new Playlist(playlistName, songs); // This constructor assumes Playlist can take a name and list of songs
    }

    // Create a new Spotify playlist with mapped songs
    private Playlist createSpotifyPlaylist(List<Song> songs, String playlistName, String spotifyAccessToken) {
        // Implement playlist creation logic here for Spotify
        // The SpotifyDataAccessObject should have a method to create a playlist given a list of songs
        return new Playlist(playlistName, songs); // This constructor assumes Playlist can take a name and list of songs
    }

    // Find a song on Amazon Music that matches the Spotify song
    private Song findSongOnAmazon(Song spotifySong) {
        // Implement logic to find a matching song on Amazon Music
        // This would likely involve searching the Amazon Music catalog by track name and artist
        return null; // Placeholder for the actual song object
    }

    // Find a song on Spotify that matches the Amazon song
    private Song findSongOnSpotify(Song amazonSong) {
        // Implement logic to find a matching song on Spotify
        // This would likely involve searching the Spotify catalog by track name and artist
        return null; // Placeholder for the actual song object
    }
}