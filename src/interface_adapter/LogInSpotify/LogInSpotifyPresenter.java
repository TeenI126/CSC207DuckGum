package interface_adapter.LogInSpotify;

import interface_adapter.OpenSpotifyLogin.OpenSpotifyLoginViewModel;
import interface_adapter.ViewManagerModel;
import use_case.LogInSpotify.LogInSpotifyOutputBoundary;

public class LogInSpotifyPresenter implements LogInSpotifyOutputBoundary {
    private LogInSpotifyViewModel logInSpotifyViewModel;

    private ViewManagerModel viewManagerModel;
    @Override
    public void prepareSuccessView() {
        LogInSpotifyState state = logInSpotifyViewModel.getState();
        state.setLogInSuccessful(true);
    }

    @Override
    public void prepareFailView() {
        logInSpotifyViewModel.getState().setLogInSuccessful(false);
    }
}
