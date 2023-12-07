package interface_adapter.ExportPlaylistCSV;

import entities.Playlist;
import use_case.ExportPlaylistCSV.ExportPlaylistCSVInputBoundary;
import use_case.ExportPlaylistCSV.ExportPlaylistCSVInputData;

public class ExportPlaylistCSVController {
    final ExportPlaylistCSVInputBoundary exportPlaylistCSVUseCaseInteractor;

    public ExportPlaylistCSVController(ExportPlaylistCSVInputBoundary exportPlaylistCSVUseCaseInteractor) {
        this.exportPlaylistCSVUseCaseInteractor = exportPlaylistCSVUseCaseInteractor;
    }

    public void execute(Playlist playlist) {
        ExportPlaylistCSVInputData exportPlaylistCSVInputData = new ExportPlaylistCSVInputData(playlist);

        exportPlaylistCSVUseCaseInteractor.execute(exportPlaylistCSVInputData);
    }
}
