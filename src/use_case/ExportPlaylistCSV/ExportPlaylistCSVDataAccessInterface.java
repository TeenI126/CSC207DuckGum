package use_case.ExportPlaylistCSV;

import entities.Playlist;

public interface ExportPlaylistCSVDataAccessInterface {

    void writeCSV(String name, Playlist playlist);
}
