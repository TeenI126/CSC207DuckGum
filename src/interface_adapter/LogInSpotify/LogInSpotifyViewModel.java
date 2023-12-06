package interface_adapter.LogInSpotify;

import interface_adapter.ViewModel;

import java.beans.PropertyChangeListener;

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
    public void addPropertyChangeListener(PropertyChangeListener listener) {

    }
}
