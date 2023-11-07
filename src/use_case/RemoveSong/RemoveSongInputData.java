package use_case.RemoveSong;

import entities.Playlist;

public class RemoveSongInputData {

    final private Playlist playlist;
    final private Song song;

    public RemoveSongInputData(Playlist playlist, Song song) {
        this.playlist = playlist;
        this.song = song;
    }

    Playlist getPlaylist() {return playlist; }

    Song getSong() {return song; }

}
