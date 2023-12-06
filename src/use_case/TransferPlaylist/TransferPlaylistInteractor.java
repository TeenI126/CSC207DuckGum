package use_case.TransferPlaylist;

import data_access.AmazonMusicDataAccessObject;
import data_access.SpotifyDataAccessObject;
import entities.*;

import java.util.ArrayList;
import java.util.List;

public class TransferPlaylistInteractor implements TransferPlaylistInputBoundary {

    private final MusicServiceAdapter spotifyAdapter;
    private final MusicServiceAdapter amazonAdapter;
    private final TransferPlaylistOutputBoundary outputBoundary;

    public TransferPlaylistInteractor(MusicServiceAdapter spotifyAdapter,
                                      MusicServiceAdapter amazonAdapter,
                                      TransferPlaylistOutputBoundary outputBoundary) {
        this.spotifyAdapter = spotifyAdapter;
        this.amazonAdapter = amazonAdapter;
        this.outputBoundary = outputBoundary;
    }

    public void transferPlaylist(TransferPlaylistInputData inputData) {
        try {
            if (inputData.getSourceService().equalsIgnoreCase("Spotify")) {
                transferServiceToService(spotifyAdapter, amazonAdapter, inputData);
            } else if (inputData.getSourceService().equalsIgnoreCase("Amazon")) {
                transferServiceToService(amazonAdapter, spotifyAdapter, inputData);
            } else {
                throw new IllegalArgumentException("Unsupported music service for transfer.");
            }
        } catch (Exception e) {
            outputBoundary.presentTransferResult(new TransferPlaylistOutputData(false, "Failed to transfer playlist: " + e.getMessage()));
        }
    }

    private void transferServiceToService(MusicServiceAdapter fromAdapter, MusicServiceAdapter toAdapter, TransferPlaylistInputData inputData) throws Exception {
        Playlist localPlaylist = fromAdapter.fetchPlaylist(inputData.getSourceAccessToken(), inputData.getPlaylistId());
        toAdapter.createPlaylist(localPlaylist, inputData.getTargetAccessToken());
        outputBoundary.presentTransferResult(new TransferPlaylistOutputData(true, "Playlist transferred successfully."));
    }
}

// Adapter interface
interface MusicServiceAdapter {
    Playlist fetchPlaylist(String accessToken, String playlistId) throws Exception;
    void createPlaylist(Playlist playlist, String accessToken) throws Exception;
}

// Implementation of the Spotify Adapter
class SpotifyAdapter implements MusicServiceAdapter {
    private final SpotifyDataAccessObject spotifyDAO;

    public SpotifyAdapter(SpotifyDataAccessObject spotifyDAO) {
        this.spotifyDAO = spotifyDAO;
    }

    public Playlist fetchPlaylist(SpotifyAccount spotifyAccount, String Href) throws Exception {
        return spotifyDAO.getPlaylist(spotifyAccount, Href);
    }

    public void createPlaylist(SpotifyAccount spotifyAccount, Playlist playlist) throws Exception {
        spotifyDAO.createPlaylist(spotifyAccount, playlist);
    }

    @Override
    public Playlist fetchPlaylist(String accessToken, String playlistId) throws Exception {
        return null;
    }

    @Override
    public void createPlaylist(Playlist playlist, String accessToken) throws Exception {

    }
}

// Implementation of the Amazon Adapter
class AmazonAdapter implements MusicServiceAdapter {
    private final AmazonMusicDataAccessObject amazonDAO;

    public AmazonAdapter(AmazonMusicDataAccessObject amazonDAO) {
        this.amazonDAO = amazonDAO;
    }

    public String fetchPlaylist(String accessToken) throws Exception {
        return AmazonMusicDataAccessObject.fetchUserPlaylists(accessToken);
    }

    @Override
    public Playlist fetchPlaylist(String accessToken, String playlistId) throws Exception {
        return null;
    }

    public void createPlaylist(Playlist playlist, String accessToken) throws Exception {
        List<String> trackIds = new ArrayList<>();
        for (Song song : playlist.getSongs()) {
            trackIds.add(song.getId());
        }
        String response = amazonDAO.createPlaylist(playlist.getName(), trackIds, accessToken);
    }
}