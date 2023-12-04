package use_case.AddSong;

import entities.Song;

public class AddSongOutputData {

    private final Song song;

    public AddSongOutputData(Song song) {
        this.song = song;
    }

    public Song getSong() {
        return song;
    }
}
