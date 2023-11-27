package use_case.ExportPlaylistCSV;

import entities.Playlist;

public class ExportPlaylistCSVInputData {
    final private String userID;
    final private Playlist playlist;

    public ExportPlaylistCSVInputData(String userID, Playlist playlist) {
        this.playlist = playlist;
        this.userID = userID;
    }

    String getUserID() {
        return userID;
    }
    Playlist getPlaylist() {
        return playlist;
    }
}
