package use_case.LogInSpotify;

import entities.SpotifyAccount;

import javax.security.auth.login.FailedLoginException;
import java.io.IOException;

public class LogInSpotifyInteractor implements LogInSpotifyInputBoundary {
    private LogInSpotifyOutputBoundary presenter;
    private LogInSpotifyDataAccessInterface dataAccessInterface;

    public LogInSpotifyInteractor(LogInSpotifyOutputBoundary presenter, LogInSpotifyDataAccessInterface logInSpotifyDataAccessInterface) {
        this.presenter = presenter;
        this.dataAccessInterface = logInSpotifyDataAccessInterface;
    }


    public void execute(LogInSpotifyInputData inputData){
        String callback_code = inputData.getCode();

        SpotifyAccount spotifyAccount;

        try {
            spotifyAccount = dataAccessInterface.createSpotifyAccountFromCode(callback_code);
        } catch (IOException | FailedLoginException e) {
            //either no failed to complete API request or bad login.
            presenter.prepareFailView();
            return;
        }

        inputData.getActiveAccount().addMusicService(spotifyAccount);

        presenter.prepareSuccessView();

    }
}
