package use_case.ExportPlaylistCSV;

import entities.Playlist;

public interface ExportPlaylistCSVDataAccessInterface {
    boolean existsByName(String identifier);

    Playlist get(String Playlist);

    void writeCSV(String name, Playlist playlist);
}
