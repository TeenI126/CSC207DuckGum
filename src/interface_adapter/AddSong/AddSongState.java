package interface_adapter.AddSong;

import entities.Playlist;
import entities.Song;

public class AddSongState {
    private Playlist playlist = null;
    private Song song = null;

    private String add_song_error = null;

    public AddSongState(AddSongState copy) {
        this.playlist = copy.playlist;
        this.song = copy.song;
    }

    public AddSongState() {}

    public Playlist getPlaylist() {
        return playlist;
    }

    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;
    }

    public void addSongError(String error) {
        this.add_song_error = error;
    }
    public Song getSong() {
        return song;
    }

    public void setSong(Song song) {
        this.song = song;
    }
}
