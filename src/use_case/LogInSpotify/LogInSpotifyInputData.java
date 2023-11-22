package use_case.LogInSpotify;

import entities.Account;

public class LogInSpotifyInputData {
    private String code;
    private Account activeAccount;

    public LogInSpotifyInputData(String code, Account activeAccount) {
        this.code = code;
        this.activeAccount = activeAccount;
    }

    public String getCode(){
        return code;
    }
    public Account getActiveAccount(){
        return activeAccount;
    }
}
