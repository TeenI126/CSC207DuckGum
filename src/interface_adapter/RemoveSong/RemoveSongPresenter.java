package interface_adapter.RemoveSong;

import interface_adapter.MainViewModel;
import interface_adapter.MainViewModelState;
import use_case.RemoveSong.RemoveSongOutputBoundary;
import use_case.RemoveSong.RemoveSongOutputData;

public class RemoveSongPresenter implements RemoveSongOutputBoundary {

    private final MainViewModel removeSongViewModel;

    public RemoveSongPresenter(MainViewModel removeSongViewModel) {
        this.removeSongViewModel = removeSongViewModel;
    }

    public void prepareSuccessView(RemoveSongOutputData response) {
        MainViewModelState removeSongState = removeSongViewModel.getState();
        removeSongState.setPlaylist(response.getPlaylist());
        removeSongState.setSong(response.getSong());
        removeSongViewModel.setState(removeSongState);
        removeSongViewModel.firePropertyChanged();
    }
}
