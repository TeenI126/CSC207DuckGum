package entities;

public class Song {
    //TODO
    public String id;
    public String artist;
    public String trackName;

    public Song(String id, String artist, String trackName) {
        this.id = id;
        this.artist = artist;
        this.trackName = trackName;
    }

    public String getId() {
        return id;
    }

    public String getArtist() {
        return artist;
    }

    public String getTrackName() {
        return trackName;
    }

}
