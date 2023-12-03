package use_case.TransferPlaylist;

public interface TransferPlaylistDataAccessInterface {
    void savePlaylistTransferDetails(String sourcePlaylistId, String targetPlaylistId);
    String getTargetPlaylistId(String sourcePlaylistId);
}