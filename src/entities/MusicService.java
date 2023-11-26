package entities;

import java.time.LocalDateTime;
import java.util.List;

public abstract class MusicService {
    // API Referencing
    private String accessToken = null;
    private String refreshToken = null;
    private LocalDateTime accessTokenExpires = null;

    // USER DETAILS
    private String displayName;
    private String userID;
    private List<Playlist> playlists;

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
