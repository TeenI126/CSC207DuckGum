package use_case.AddSong;

import entities.Playlist;

public class AddSongInputData {

    final private Playlist playlist;
    final private Song song;

    public AddSongInputData(Playlist playlist, Song song) {
        this.playlist = playlist;
        this.song = song;
    }

    Playlist getPlaylist() {return this.playlist; }

    Song getSong() {return this.song; }
}
