package entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public abstract class MusicService {
    // API Referencing
    String accessToken = null;
    String refreshToken = null;
    LocalDateTime accessTokenExpires = LocalDateTime.now();//defaults to now so any check evalaute false without null issues

    // USER DETAILS
    String displayName;
    String userID;
    List<Playlist> playlists = new ArrayList<Playlist>();

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getUserID() {
        return userID;
    }

    public String getUserAccessToken() {
        System.err.println("CALLING getUserAccessToken ABSTRACT METHOD IN MUSIC SERVICE");
        return null;
    }

    public void setPlaylists(List<Playlist> playlists) {
        this.playlists = playlists;
    }
}
