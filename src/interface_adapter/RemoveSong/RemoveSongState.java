package interface_adapter.RemoveSong;

import entities.Playlist;
import entities.Song;

public class RemoveSongState {

    private Playlist playlist = null;
    private Song song = null;


    public RemoveSongState(RemoveSongState copy) {
        this.playlist = copy.playlist;
        this.song = copy.song;
    }

    public RemoveSongState() {}

    public Playlist getPlaylist() {
        return playlist;
    }

    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;
    }


    public Song getSong() {
        return song;
    }

    public void setSong(Song song) {
        this.song = song;
    }

}
