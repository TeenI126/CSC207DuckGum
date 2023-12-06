package use_case.TransferPlaylist;

import entities.*;

public class TransferPlaylistInteractor implements TransferPlaylistInputBoundary {

    private MusicServiceAdapter spotifyAdapter;
    private MusicServiceAdapter amazonAdapter;
    private TransferPlaylistOutputBoundary outputBoundary;

    public TransferPlaylistInteractor(MusicServiceAdapter spotifyAdapter,
                                      MusicServiceAdapter amazonAdapter,
                                      TransferPlaylistOutputBoundary outputBoundary) {
        this.spotifyAdapter = spotifyAdapter;
        this.amazonAdapter = amazonAdapter;
        this.outputBoundary = outputBoundary;
    }

    @Override
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
    private SpotifyDataAccessObject spotifyDAO;

    public SpotifyAdapter(SpotifyDataAccessObject spotifyDAO) {
        this.spotifyDAO = spotifyDAO;
    }

    @Override
    public Playlist fetchPlaylist(String accessToken, String playlistId) throws Exception {
        return spotifyDAO.getPlaylist(accessToken, playlistId);
    }

    @Override
    public void createPlaylist(Playlist playlist, String accessToken) throws Exception {
        String userId = spotifyDAO.getCurrentUserId(accessToken);
        spotifyDAO.createPlaylist(userId, playlist, accessToken);
    }
}

// Implementation of the Amazon Adapter
class AmazonAdapter implements MusicServiceAdapter {
    private AmazonMusicDataAccessObject amazonDAO;

    public AmazonAdapter(AmazonMusicDataAccessObject amazonDAO) {
        this.amazonDAO = amazonDAO;
    }

    @Override
    public Playlist fetchPlaylist(String accessToken, String playlistId) throws Exception {
        return amazonDAO.getPlaylist(accessToken, playlistId);
    }

    @Override
    public void createPlaylist(Playlist playlist, String accessToken) throws Exception {
        amazonDAO.createPlaylist(playlist, accessToken);
    }
}