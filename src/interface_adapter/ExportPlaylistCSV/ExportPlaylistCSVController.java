package interface_adapter.ExportPlaylistCSV;

import entities.Account;
import entities.Playlist;
import use_case.ExportPlaylistCSV.ExportPlaylistCSVInputBoundary;
import use_case.ExportPlaylistCSV.ExportPlaylistCSVInputData;

public class ExportPlaylistCSVController {
    final ExportPlaylistCSVInputBoundary exportPlaylistCSVUseCaseInteractor;

    public ExportPlaylistCSVController(ExportPlaylistCSVInputBoundary exportPlaylistCSVUseCaseInteractor) {
        this.exportPlaylistCSVUseCaseInteractor = exportPlaylistCSVUseCaseInteractor;
    }

    public void execute(String userID, String playlist, Account account) {
        ExportPlaylistCSVInputData exportPlaylistCSVInputData = new ExportPlaylistCSVInputData(userID, playlist, account);

        exportPlaylistCSVUseCaseInteractor.execute(exportPlaylistCSVInputData);
    }
}
