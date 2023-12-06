package use_case.AddSong;

import entities.Playlist;
import entities.Song;

public class AddSongOutputData {

    private final Playlist playlist;

    private final Song song;

    public AddSongOutputData(Playlist playlist, Song song) {
        this.playlist = playlist;
        this.song = song;
    }

    public Playlist getPlaylist() { return playlist; }
    public Song getSong() {
        return song;
    }
}
