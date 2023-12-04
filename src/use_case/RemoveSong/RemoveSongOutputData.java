package use_case.RemoveSong;

import entities.Song;

public class RemoveSongOutputData {

    private Song song;

    public RemoveSongOutputData(Song song) {
        this.song = song;
    }

    public Song getSong() {
        return song;
    }
}
