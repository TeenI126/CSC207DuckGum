package use_case.OpenLoginSpotify;

public class OpenLoginSpotifyInteractor implements OpenLoginSpotifyInputBoundary{
    OpenLoginSpotifyOutputBoundary openPresenter;
    OpenLoginSpotifyDataAccessInterface dataAccessObject;
    public OpenLoginSpotifyInteractor(OpenLoginSpotifyOutputBoundary openLoginSpotifyOutputBoundary){
        openPresenter = openLoginSpotifyOutputBoundary;
    }
    public void execute() {
        OpenLoginSpotifyOutputData outputData = new OpenLoginSpotifyOutputData(dataAccessObject.getLoginPage());
        openPresenter.openLoginCallbackURL(outputData);
    }
}
