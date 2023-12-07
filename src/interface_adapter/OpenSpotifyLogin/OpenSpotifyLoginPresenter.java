package interface_adapter.OpenSpotifyLogin;

import interface_adapter.MainViewModel;
import interface_adapter.MainViewModelState;
import use_case.OpenLoginSpotify.OpenLoginSpotifyOutputBoundary;
import use_case.OpenLoginSpotify.OpenLoginSpotifyOutputData;

public class OpenSpotifyLoginPresenter implements OpenLoginSpotifyOutputBoundary {

    private MainViewModel mainViewModel;

    public OpenSpotifyLoginPresenter(MainViewModel viewModel) {
        this.mainViewModel = viewModel;
    }

    @Override
    public void openLoginCallbackURL(OpenLoginSpotifyOutputData data) {
        MainViewModelState state = mainViewModel.getState();
        state.setCallbackUrl(data.getCallback_url());
        mainViewModel.firePropertyChanged();
    }
}
