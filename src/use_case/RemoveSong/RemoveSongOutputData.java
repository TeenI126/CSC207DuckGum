package use_case.RemoveSong;

import entities.Playlist;
import entities.Song;

public class RemoveSongOutputData {

    private Song song;

    private Playlist playlist;

    public RemoveSongOutputData(Playlist playlist, Song song) {
        this.playlist = playlist;
        this.song = song;
    }

    public Playlist getPlaylist() { return playlist; }
    public Song getSong() {
        return song;
    }
}
