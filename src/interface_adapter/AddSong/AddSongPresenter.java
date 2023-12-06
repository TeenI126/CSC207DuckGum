package interface_adapter.AddSong;

import use_case.AddSong.AddSongOutputBoundary;
import use_case.AddSong.AddSongOutputData;

public class AddSongPresenter implements AddSongOutputBoundary {

    private final AddSongViewModel addSongViewModel;

    public AddSongPresenter(AddSongViewModel addSongViewModel) {
        this.addSongViewModel = addSongViewModel;
    }

    public void prepareSuccessView(AddSongOutputData response) {
        AddSongState addSongState = addSongViewModel.getState();
        addSongState.setPlaylist(response.getPlaylist());
        addSongState.setSong(response.getSong());
        addSongViewModel.setState(addSongState);
        addSongViewModel.firePropertyChanged();
    }


    public void prepareFailView(String error) {
        AddSongState addSongState = addSongViewModel.getState();
        addSongState.addSongError(error);
        addSongViewModel.firePropertyChanged();
    }
}

