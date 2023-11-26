package interface_adapter.OpenSpotifyLogin;

import interface_adapter.ViewModel;

public class OpenSpotifyLoginViewModel extends ViewModel {
    OpenSpotifyLoginState state = new OpenSpotifyLoginState();

    public OpenSpotifyLoginViewModel() {
        super("Open Log in Spotify");
    }

    public OpenSpotifyLoginState getState() {
        return state;
    }

    public void firePropertyChanged() {
        //TODO Something to do with a class called Property changed support.
    }

    @Override
    public void addPropertyChangeListener() {
        //TODO
    }
}
