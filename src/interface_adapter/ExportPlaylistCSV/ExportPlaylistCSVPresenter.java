package interface_adapter.ExportPlaylistCSV;

import interface_adapter.MainViewModel;
import interface_adapter.MainViewModelState;
import use_case.ExportPlaylistCSV.ExportPlaylistCSVOutputBoundary;
import use_case.ExportPlaylistCSV.ExportPlaylistCSVOutputData;

public class ExportPlaylistCSVPresenter implements ExportPlaylistCSVOutputBoundary {

    private MainViewModel exportPlaylistCSVViewModel;

    public ExportPlaylistCSVPresenter(MainViewModel exportPlaylistCSVViewModel) {
        this.exportPlaylistCSVViewModel = exportPlaylistCSVViewModel;
    }
    @Override
    public void prepareSuccessView(ExportPlaylistCSVOutputData response) {
        MainViewModelState exportPlaylistCSVState = exportPlaylistCSVViewModel.getState();
        exportPlaylistCSVState.setPlaylist(response.getPlaylist());
        exportPlaylistCSVViewModel.setState(exportPlaylistCSVState);
        exportPlaylistCSVViewModel.firePropertyChanged();
    }
    // Switch views?

    @Override
    public void prepareFailView(String error) {
        MainViewModelState exportPlaylistCSVState = exportPlaylistCSVViewModel.getState();
        exportPlaylistCSVState.setPlaylistError(error);
        exportPlaylistCSVViewModel.firePropertyChanged();
    }
}
