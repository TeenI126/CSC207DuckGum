package interface_adapter.ExportPlaylistCSV;

import entities.Account;
import entities.Playlist;

public class ExportPlaylistCSVState {
    private String userID = "";
    private Playlist playlist = null;
    private String playlistError = null;
    private Account account = null;

    public ExportPlaylistCSVState(ExportPlaylistCSVState copy) {
        this.userID = copy.userID;
        this.playlist = copy.playlist;
        this.playlistError = copy.playlistError;
        this.account = copy.account;
    }

    public ExportPlaylistCSVState() {

    }

    public String getUserID() {
        return userID;
    }

    public Playlist getPlaylist() {
        return playlist;
    }

    public Account getAccount() {
        return account;
    }

    public String getPlaylistError() {
        return playlistError;
    }

    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public void setPlaylistError(String playlistError) {
        this.playlistError = playlistError;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
