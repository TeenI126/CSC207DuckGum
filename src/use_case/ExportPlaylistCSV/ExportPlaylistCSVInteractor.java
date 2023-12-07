package use_case.ExportPlaylistCSV;

import entities.MusicService;
import entities.Playlist;

public class ExportPlaylistCSVInteractor implements ExportPlaylistCSVInputBoundary {
    final ExportPlaylistCSVDataAccessInterface exportPlaylistCSVDataAccessObject;
    final ExportPlaylistCSVOutputBoundary exportPlaylistCSVPresenter;

    public ExportPlaylistCSVInteractor(ExportPlaylistCSVDataAccessInterface exportPlaylistCSVDataAccessObject, ExportPlaylistCSVOutputBoundary exportPlaylistCSVPresenter) {
        this.exportPlaylistCSVDataAccessObject = exportPlaylistCSVDataAccessObject;
        this.exportPlaylistCSVPresenter = exportPlaylistCSVPresenter;
    }
    @Override
    public void execute(ExportPlaylistCSVInputData exportPlaylistCSVInputData) {
        Playlist playlist = exportPlaylistCSVInputData.getPlaylist();
        exportPlaylistCSVDataAccessObject.writeCSV(playlist.getName(), playlist);
        ExportPlaylistCSVOutputData exportPlaylistCSVOutputData = new ExportPlaylistCSVOutputData(playlist, false);
        exportPlaylistCSVPresenter.prepareSuccessView(exportPlaylistCSVOutputData);
    }
}
