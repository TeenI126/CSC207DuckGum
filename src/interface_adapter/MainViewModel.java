package interface_adapter;

import interface_adapter.OpenSpotifyLogin.OpenSpotifyLoginState;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class MainViewModel extends ViewModel{
    MainViewModelState state = new MainViewModelState();
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);
    public MainViewModel() {
        super("Main screen");
    }

    public MainViewModelState getState() {
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
