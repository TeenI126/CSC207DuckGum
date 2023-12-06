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

    public String getUserAccessToken() {
        return accessToken;
    }

    public LocalDateTime getAccessTokenExpires(){
        return accessTokenExpires;
    }

    public String getRefreshToken(){
        return refreshToken;
    }

    public void setAccessToken(String accessToken){
        this.accessToken = accessToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void setAccessTokenExpires(LocalDateTime accessTokenExpires) {
        this.accessTokenExpires = accessTokenExpires;
    }

    public void setPlaylists(List<Playlist> playlists) {
        this.playlists = playlists;
    }

    public List<Playlist> getPlaylists() {
        return playlists;
    }

    public String getUserID() {
        return userID;
    }

    public String getDisplayName() {
        return displayName;
    }
}
