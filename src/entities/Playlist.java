package entities;

import java.util.List;

public class Playlist {
    private List<Song> songs;

    public Playlist(List<Song> songs) {
        this.songs = songs;
    }
    public void addSong(Song song){
        //TODO
        songs.add(song);
    }

    public void removeSong(Song song) {
        int n = songs.size();
        for (int i = 0; i < n; i++) {
            if (song == songs.get(i)) {
                songs.remove(i);
            }
        }
    }

    public boolean isPlaylistEmpty() {
        return songs.isEmpty();
    }

    public List<Song> getPlaylist() {
        if (isPlaylistEmpty()) {
            return null;
        }
        return songs;
    }
}
