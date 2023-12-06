package use_case.AddSong;

import entities.Playlist;
import entities.Song;

public class AddSongInteractor implements AddSongInputBoundary{

    final AddSongOutputBoundary addSongOutputBoundary;


    public AddSongInteractor(AddSongOutputBoundary addSongOutputBoundary) {
        this.addSongOutputBoundary = addSongOutputBoundary;
    }

    public void execute(AddSongInputData addSongInputData) {
        Playlist playlist = addSongInputData.getPlaylist();
        Song song = addSongInputData.getSong();
        if (!playlist.contains(song)) {
            playlist.addSong(song);
            AddSongOutputData addSongOutputData = new AddSongOutputData(playlist, song);
            addSongOutputBoundary.prepareSuccessView(addSongOutputData);
        } else {
            addSongOutputBoundary.prepareFailView("Song already exists in playlist.");
        }

    }
}
