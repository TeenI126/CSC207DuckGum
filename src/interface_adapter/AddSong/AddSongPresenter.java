package interface_adapter.AddSong;

import interface_adapter.MainViewModel;
import interface_adapter.MainViewModelState;
import use_case.AddSong.AddSongOutputBoundary;
import use_case.AddSong.AddSongOutputData;

public class AddSongPresenter implements AddSongOutputBoundary {

    private final MainViewModel addSongViewModel;

    public AddSongPresenter(MainViewModel addSongViewModel) {
        this.addSongViewModel = addSongViewModel;
    }

    public void prepareSuccessView(AddSongOutputData response) {
        MainViewModelState addSongState = addSongViewModel.getState();
        addSongState.setPlaylist(response.getPlaylist());
        addSongState.setSong(response.getSong());
        addSongViewModel.setState(addSongState);
        addSongViewModel.firePropertyChanged();
    }


    public void prepareFailView(String error) {
        MainViewModelState addSongState = addSongViewModel.getState();
        addSongState.addSongError(error);
        addSongViewModel.firePropertyChanged();
    }
}

