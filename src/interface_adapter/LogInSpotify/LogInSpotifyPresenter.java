package interface_adapter.LogInSpotify;

import interface_adapter.MainViewModel;
import interface_adapter.MainViewModelState;
import use_case.LogInSpotify.LogInSpotifyOutputBoundary;
import use_case.LogInSpotify.LogInSpotifyOutputData;

public class LogInSpotifyPresenter implements LogInSpotifyOutputBoundary {
    private MainViewModel mainViewModel;

    public LogInSpotifyPresenter(MainViewModel mainViewModel) {
        this.mainViewModel = mainViewModel;
    }

    @Override
    public void loadSpotifyAccount(LogInSpotifyOutputData data) {
        MainViewModelState state = mainViewModel.getState();
        state.setSpotifyAccount(data.getSpotifyAccount());
        mainViewModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView() {

    }
}
