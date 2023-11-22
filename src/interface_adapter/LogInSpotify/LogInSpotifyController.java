package interface_adapter.LogInSpotify;

import entities.Account;
import use_case.LogInSpotify.LogInSpotifyInputBoundary;
import use_case.LogInSpotify.LogInSpotifyInputData;
import use_case.LogInSpotify.LogInSpotifyInteractor;

public class LogInSpotifyController {
    LogInSpotifyInputBoundary interactor;

    public LogInSpotifyController(LogInSpotifyInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void execute(String code, Account activeAccount){
        interactor.execute(new LogInSpotifyInputData(code, activeAccount));
    }
}
