package use_case.LogInSpotify;

import entities.SpotifyAccount;

import javax.security.auth.login.FailedLoginException;
import java.io.IOException;

public interface LogInSpotifyDataAccessInterface {
    SpotifyAccount createSpotifyAccountFromCode(String callbackCode) throws IOException, FailedLoginException;
}
