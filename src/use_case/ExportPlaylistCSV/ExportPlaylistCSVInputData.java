package use_case.ExportPlaylistCSV;

import entities.Account;

public class ExportPlaylistCSVInputData {
    final private String userID;
    final private String playlist;
    final private Account account;

    public ExportPlaylistCSVInputData(String userID, String playlist, Account account) {
        this.playlist = playlist;
        this.userID = userID;
        this.account = account;
    }

    String getUserID() {
        return userID;
    }
    String getPlaylistName() {
        return playlist;
    }

    Account getAccount() {
        return account;
    }
}
