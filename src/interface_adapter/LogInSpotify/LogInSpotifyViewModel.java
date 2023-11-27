package interface_adapter.LogInSpotify;

import interface_adapter.ViewModel;

public class LogInSpotifyViewModel extends ViewModel {
    LogInSpotifyState state = new LogInSpotifyState();

    public LogInSpotifyViewModel() {
        super("Log In Spotify ViewModel");
    }

    public LogInSpotifyState getState() {
        return state;
    }

    @Override
    public void firePropertyChanged() {
        //TODO firePropertyChanged for LogInSpotifyViewModel
    }

    @Override
    public void addPropertyChangeListener() {
        //TODO addPropertyChangeListener for LogInSpotifyViewModel
    }
}
