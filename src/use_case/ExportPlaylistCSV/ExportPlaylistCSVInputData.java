package use_case.ExportPlaylistCSV;

import entities.Playlist;

public class ExportPlaylistCSVInputData {
    final private Playlist playlist;

    public ExportPlaylistCSVInputData(Playlist playlist) {
        this.playlist = playlist;
    }

    String getPlaylistName() {
        return playlist.getName();
    }

    Playlist getPlaylist() {
        return playlist;
    }
}
