package entities;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class Playlist {

    private String name;
    private List<Song> songs;

    private String id;

    public Playlist(String name) {
        this.name = name;
        songs = new ArrayList<Song>();
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

    public String getName() {
        return name;
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
