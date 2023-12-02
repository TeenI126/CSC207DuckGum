package use_case.RemoveSong;

import entities.Playlist;
import entities.Song;

public class RemoveSongInteractor implements RemoveSongInputBoundary{

    final RemoveSongOutputBoundary removeSongOutputBoundary;

    public RemoveSongInteractor(RemoveSongOutputBoundary removeSongOutputBoundary) {
        this.removeSongOutputBoundary = removeSongOutputBoundary;
    }

    public void execute(RemoveSongInputData removeSongInputData) {
        Playlist playlist = removeSongInputData.getPlaylist();
        Song song = removeSongInputData.getSong();
        playlist.removeSong(song);
        RemoveSongOutputData removeSongOutputData = new RemoveSongOutputData(song);
        removeSongOutputBoundary.prepareSuccessView(removeSongOutputData);
    }
}
