package use_case.ExportPlaylistCSV;

import entities.MusicService;

public class ExportPlaylistCSVInputData {
    final private String playlist;
    final private MusicService musicService;

    public ExportPlaylistCSVInputData(String playlist, MusicService musicService) {
        this.playlist = playlist;
        this.musicService = musicService;
    }

    String getPlaylistName() {
        return playlist;
    }

    MusicService getMusicService() {
        return musicService;
    }
}
