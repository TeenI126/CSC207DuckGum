package interface_adapter.LogInSpotify;

import interface_adapter.MainViewModel;
import interface_adapter.MainViewModelState;
import interface_adapter.ViewManagerModel;
import use_case.LogInSpotify.LogInSpotifyInputData;
import use_case.LogInSpotify.LogInSpotifyOutputBoundary;
import use_case.LogInSpotify.LogInSpotifyOutputData;

public class LogInSpotifyPresenter implements LogInSpotifyOutputBoundary {
    private MainViewModel mainViewModel;
    private ViewManagerModel viewManagerModel;
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
