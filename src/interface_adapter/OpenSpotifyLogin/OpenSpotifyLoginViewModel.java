package interface_adapter.OpenSpotifyLogin;

import interface_adapter.ViewModel;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class OpenSpotifyLoginViewModel extends ViewModel {
    OpenSpotifyLoginState state = new OpenSpotifyLoginState();

    private final PropertyChangeSupport support = new PropertyChangeSupport(this);
    public OpenSpotifyLoginViewModel() {
        super("Open Log in Spotify");
    }

    public OpenSpotifyLoginState getState() {
        return state;
    }

    public void firePropertyChanged() {
        support.firePropertyChange("state", null, this.state);
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        support.addPropertyChangeListener(propertyChangeListener);
    }
}
