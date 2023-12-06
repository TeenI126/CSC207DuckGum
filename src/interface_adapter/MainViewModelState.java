package interface_adapter;

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
}
