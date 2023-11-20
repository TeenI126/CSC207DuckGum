package entities;


import java.util.List;

public class Song {
    //TODO
    private String id;
    private List<String> artists;
    private String trackName;

    private String album;

    public Song(String id, List<String> artists, String trackName, String album) {
        this.id = id;
        this.artists = artists;
        this.trackName = trackName;
        this.album = album;
    }

    public String getId() {
        return id;
    }

    public List<String> getArtists() {
        return artists;
    }

    public String getTrackName() {
        return trackName;
    }

}
