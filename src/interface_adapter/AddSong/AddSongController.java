package interface_adapter.AddSong;

import entities.Playlist;
import entities.Song;
import use_case.AddSong.AddSongInputBoundary;
import use_case.AddSong.AddSongInputData;

public class AddSongController {

    final AddSongInputBoundary addSongUseCaseInteractor;

    public AddSongController(AddSongInputBoundary addSongUseCaseInteractor) {
        this.addSongUseCaseInteractor = addSongUseCaseInteractor;
    }

    public void execute(Playlist playlist, Song song) {
        AddSongInputData addSongInputData = new AddSongInputData(playlist, song);
        addSongUseCaseInteractor.execute(addSongInputData);
    }

}
