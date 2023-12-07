package interface_adapter;

import entities.Playlist;
import entities.Song;
import entities.SpotifyAccount;

public class MainViewModelState {
    private String callbackUrl = "";

    private SpotifyAccount spotifyAccount = null;

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    public void setSpotifyAccount(SpotifyAccount spotifyAccount) {
        this.spotifyAccount = spotifyAccount;
    }

    public SpotifyAccount getSpotifyAccount(){
        return spotifyAccount;
    }
    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setPlaylist(Playlist playlist) {
    }

    public void setSong(Song song) {
    }

    public void addSongError(String error) {
    }

    public void setPlaylistError(String error) {
    }
}
