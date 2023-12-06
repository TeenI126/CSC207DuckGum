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
                amazonDAO.createPlaylist(playlistToTransfer, inputData.getAmazonAccessToken());
                outputBoundary.presentTransferResult(new TransferPlaylistOutputData(true, "Playlist transferred from Spotify to Amazon Music successfully."));
            } else if (inputData.getSourceService().equalsIgnoreCase("Amazon")) {
                playlistToTransfer = amazonDAO.getPlaylist(inputData.getAmazonAccessToken(), inputData.getPlaylistId());
                // Convert Amazon playlist to Spotify format and create it
                String userId = spotifyDAO.getCurrentUserId(inputData.getSpotifyAccessToken());
                spotifyDAO.createPlaylist(userId, playlistToTransfer, inputData.getSpotifyAccessToken());
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
        amazonDAO.createPlaylist(amazonPlaylist, amazonAccessToken);
    }

    // Transfer from Amazon to Spotify
    private void transferFromAmazonToSpotify(String amazonAccessToken, String playlistId, String spotifyAccessToken) throws IOException {
        Playlist amazonPlaylist = amazonDAO.getPlaylist(amazonAccessToken, playlistId);
        List<Song> mappedSongs = mapSongsForSpotify(amazonPlaylist.getSongs());
        Playlist spotifyPlaylist = createSpotifyPlaylist(mappedSongs, amazonPlaylist.getName(), spotifyAccessToken);
        String userId = spotifyDAO.getCurrentUserId(inputData.getSpotifyAccessToken());
        spotifyDAO.createPlaylist(userId, playlistToTransfer, inputData.getSpotifyAccessToken());
    }

    // Map songs from a Spotify playlist for Amazon Music
    private List<Song> mapSongsForAmazon(List<Song> spotifySongs) {
        List<Song> amazonSongs = new ArrayList<>();
        for (Song spotifySong : spotifySongs) {
            // Implement song mapping logic here
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
}