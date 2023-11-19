package interface_adapter.OpenSpotifyLogin;

import use_case.OpenLoginSpotify.OpenLoginSpotifyOutputBoundary;
import use_case.OpenLoginSpotify.OpenLoginSpotifyOutputData;

public class OpenSpotifyLoginPresenter implements OpenLoginSpotifyOutputBoundary {

    OpenSpotifyLoginViewModel viewModel;

    public OpenSpotifyLoginPresenter(OpenSpotifyLoginViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void openLoginCallbackURL(OpenLoginSpotifyOutputData data) {

    }
}
