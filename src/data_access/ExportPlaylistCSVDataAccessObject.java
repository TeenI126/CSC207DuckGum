package data_access;

import entities.Account;
import entities.MusicService;
import entities.Playlist;
import entities.Song;
import use_case.ExportPlaylistCSV.ExportPlaylistCSVDataAccessInterface;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ExportPlaylistCSVDataAccessObject implements ExportPlaylistCSVDataAccessInterface {

    private Playlist playlist;

    public ExportPlaylistCSVDataAccessObject(Playlist playlist) {
        this.playlist = playlist;
    }

    public void writeCSV(String csvPath, Playlist playlist) {

        File csvFile = new File("./" + csvPath + ".csv");
        Map<String, Integer> headers = new LinkedHashMap<>();
        headers.put("Track Name", 0);
        headers.put("Artist", 1);
        headers.put("Album", 2);
        headers.put("Playlist name", 3);
        headers.put("Type", 5);
        headers.put("ISRC", 6);
        /* The International Standard Recording Code (ISRC)
        is the international identification system for
        sound recordings and music video recordings.
        */

        BufferedWriter writer;
        try {
            writer = new BufferedWriter(new FileWriter(csvFile));
            writer.write(String.join(",", headers.keySet()));
            writer.newLine();

            for (Song song : playlist.getSongs()) {
                String line = String.format("%s,%s,%s,%s,%s,%s",
                        song.getTrackName(), song.getArtists(), song.getAlbum(), csvPath, "Playlist", song.getId());
                writer.write(line);
                writer.newLine();
            }

            writer.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    // double check this handles weird characters correctly
}
