package interface_adapter.ExportPlaylistCSV;

import interface_adapter.ViewModel;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class ExportPlaylistCSVViewModel extends ViewModel {

    public static final String TITLE_LABEL = "Export to CSV";
    public static final String PLAYLIST_LABEL = "Choose playlist";
    public static final String EXPORT_BUTTON_LABEL = "Export";

    private ExportPlaylistCSVState state = new ExportPlaylistCSVState();

    public ExportPlaylistCSVViewModel() {
        super("export to csv");
    }

    public void setState(ExportPlaylistCSVState state) {
        this.state = state;
    }

    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    public void firePropertyChanged() {
        support.firePropertyChange("state", null, this.state);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public ExportPlaylistCSVState getState() {
        return state;
    }

}
