package interface_adapter.ExportPlaylistCSV;

import use_case.ExportPlaylistCSV.ExportPlaylistCSVOutputBoundary;
import use_case.ExportPlaylistCSV.ExportPlaylistCSVOutputData;

public class ExportPlaylistCSVPresenter implements ExportPlaylistCSVOutputBoundary {

    private ExportPlaylistCSVViewModel exportPlaylistCSVViewModel;

    public ExportPlaylistCSVPresenter(ExportPlaylistCSVViewModel exportPlaylistCSVViewModel) {
        this.exportPlaylistCSVViewModel = exportPlaylistCSVViewModel;
    }
    @Override
    public void prepareSuccessView(ExportPlaylistCSVOutputData response) {
        ExportPlaylistCSVState exportPlaylistCSVState = exportPlaylistCSVViewModel.getState();
        exportPlaylistCSVState.setPlaylist(response.getPlaylist());
        exportPlaylistCSVViewModel.setState(exportPlaylistCSVState);
        exportPlaylistCSVViewModel.firePropertyChanged();
    }
    // Switch views?

    @Override
    public void prepareFailView(String error) {
        ExportPlaylistCSVState exportPlaylistCSVState = exportPlaylistCSVViewModel.getState();
        exportPlaylistCSVState.setPlaylistError(error);
        exportPlaylistCSVViewModel.firePropertyChanged();
    }
}
