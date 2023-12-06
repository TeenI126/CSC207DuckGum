package interface_adapter.RemoveSong;

import interface_adapter.AddSong.AddSongState;
import interface_adapter.ViewModel;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class RemoveSongViewModel extends ViewModel {

    private RemoveSongState state = new RemoveSongState();

    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    public RemoveSongViewModel() {
        super("remove song");
    }

    public void setState(RemoveSongState state) {
        this.state = state;
    }


    public void firePropertyChanged() {

        support.firePropertyChange("state", null, this.state);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public RemoveSongState getState() {
        return state;
    }

}
