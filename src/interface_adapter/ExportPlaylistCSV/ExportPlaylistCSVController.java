package interface_adapter.ExportPlaylistCSV;

import entities.MusicService;
import use_case.ExportPlaylistCSV.ExportPlaylistCSVInputBoundary;
import use_case.ExportPlaylistCSV.ExportPlaylistCSVInputData;

public class ExportPlaylistCSVController {
    final ExportPlaylistCSVInputBoundary exportPlaylistCSVUseCaseInteractor;

    public ExportPlaylistCSVController(ExportPlaylistCSVInputBoundary exportPlaylistCSVUseCaseInteractor) {
        this.exportPlaylistCSVUseCaseInteractor = exportPlaylistCSVUseCaseInteractor;
    }

    public void execute(String playlist, MusicService musicService) {
        ExportPlaylistCSVInputData exportPlaylistCSVInputData = new ExportPlaylistCSVInputData(playlist, musicService);

        exportPlaylistCSVUseCaseInteractor.execute(exportPlaylistCSVInputData);
    }
}
