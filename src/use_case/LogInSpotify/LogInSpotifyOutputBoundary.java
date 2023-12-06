package use_case.LogInSpotify;

import entities.SpotifyAccount;

public interface LogInSpotifyOutputBoundary {
    void loadSpotifyAccount(LogInSpotifyOutputData data);
    void prepareFailView();
}
