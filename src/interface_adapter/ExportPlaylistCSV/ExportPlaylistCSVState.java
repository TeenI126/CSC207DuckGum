package interface_adapter.ExportPlaylistCSV;

import entities.MusicService;
import entities.Playlist;

public class ExportPlaylistCSVState {
    private Playlist playlist = null;
    private String playlistError = null;
    private MusicService musicService = null;

    public ExportPlaylistCSVState(ExportPlaylistCSVState copy) {
        this.playlist = copy.playlist;
        this.playlistError = copy.playlistError;
        this.musicService = copy.musicService;
    }

    public ExportPlaylistCSVState() {

    }

    public Playlist getPlaylist() {
        return playlist;
    }

    public MusicService getMusicService() {
        return musicService;
    }

    public String getPlaylistError() {
        return playlistError;
    }

    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;
    }

    public void setMusicService(MusicService musicService) {
        this.musicService = musicService;
    }

    public void setPlaylistError(String playlistError) {
        this.playlistError = playlistError;
    }
}
