package interface_adapter.RemoveSong;

import use_case.RemoveSong.RemoveSongOutputBoundary;
import use_case.RemoveSong.RemoveSongOutputData;

public class RemoveSongPresenter implements RemoveSongOutputBoundary {

    private final RemoveSongViewModel removeSongViewModel;

    public RemoveSongPresenter(RemoveSongViewModel removeSongViewModel) {
        this.removeSongViewModel = removeSongViewModel;
    }

    public void prepareSuccessView(RemoveSongOutputData response) {
        RemoveSongState removeSongState = removeSongViewModel.getState();
        removeSongState.setPlaylist(response.getPlaylist());
        removeSongState.setSong(response.getSong());
        removeSongViewModel.setState(removeSongState);
        removeSongViewModel.firePropertyChanged();
    }
}
