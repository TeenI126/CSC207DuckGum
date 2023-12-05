package use_case.LogInSpotify;

import entities.SpotifyAccount;

public class LogInSpotifyOutputData {
    private SpotifyAccount spotifyAccount;

    public SpotifyAccount getSpotifyAccount() {
        return spotifyAccount;
    }

    public LogInSpotifyOutputData setSpotifyAccount(SpotifyAccount spotifyAccount) {
        this.spotifyAccount = spotifyAccount;
        return this;
    }
}
