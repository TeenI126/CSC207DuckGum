package interface_adapter.ExportPlaylistCSV;

import entities.Playlist;

public class ExportPlaylistCSVState {
    private Playlist playlist = null;
    private String playlistError = null;

    public ExportPlaylistCSVState(ExportPlaylistCSVState copy) {
        this.playlist = copy.playlist;
        this.playlistError = copy.playlistError;
    }

    public ExportPlaylistCSVState() {

    }

    public Playlist getPlaylist() {
        return playlist;
    }

    public String getPlaylistError() {
        return playlistError;
    }

    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;
    }

    public void setPlaylistError(String playlistError) {
        this.playlistError = playlistError;
    }
}
