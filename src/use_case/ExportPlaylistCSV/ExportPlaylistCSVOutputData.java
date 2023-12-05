package use_case.ExportPlaylistCSV;

import entities.Playlist;

public class ExportPlaylistCSVOutputData {

    private Playlist playlist;
    private boolean useCaseFailed;

    public ExportPlaylistCSVOutputData(Playlist playlist, boolean useCaseFailed) {
        this.playlist = playlist;
        this.useCaseFailed = useCaseFailed;
    }

    public Playlist getPlaylist() {
        return playlist;
    }
}
