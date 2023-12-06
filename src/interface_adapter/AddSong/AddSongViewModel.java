package interface_adapter.AddSong;

import interface_adapter.ViewModel;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class AddSongViewModel extends ViewModel {

    private AddSongState state = new AddSongState();

    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    public AddSongViewModel() {
        super("add song");
    }

    public void setState(AddSongState state) {
        this.state = state;
    }


    public void firePropertyChanged() {

        support.firePropertyChange("state", null, this.state);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public AddSongState getState() {
        return state;
    }

}
