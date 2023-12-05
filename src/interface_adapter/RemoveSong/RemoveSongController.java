package interface_adapter.RemoveSong;

import entities.Playlist;
import entities.Song;
import use_case.RemoveSong.RemoveSongInputBoundary;
import use_case.RemoveSong.RemoveSongInputData;

public class RemoveSongController {

    final RemoveSongInputBoundary removeSongInputBoundary;

    public RemoveSongController(RemoveSongInputBoundary removeSongInputBoundary) {
        this.removeSongInputBoundary = removeSongInputBoundary;
    }

    public void execute(Playlist playlist, Song song) {
        RemoveSongInputData removeSongInputData = new RemoveSongInputData(playlist, song);
        removeSongInputBoundary.execute(removeSongInputData);
    }
}
