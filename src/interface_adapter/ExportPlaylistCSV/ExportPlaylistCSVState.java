package interface_adapter.ExportPlaylistCSV;

import entities.Playlist;

public class ExportPlaylistCSVState {
    private String userID;
    public ExportPlaylistCSVState(ExportPlaylistCSVState copy) {
        this.userID = copy.userID;
    }
}
