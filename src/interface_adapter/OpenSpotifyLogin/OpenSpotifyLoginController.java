package interface_adapter.OpenSpotifyLogin;

import use_case.OpenLoginSpotify.OpenLoginSpotifyInputBoundary;

public class OpenSpotifyLoginController {

    OpenLoginSpotifyInputBoundary openLoginSpotifyInputInteractor;

    public OpenSpotifyLoginController(OpenLoginSpotifyInputBoundary interactor){
        openLoginSpotifyInputInteractor = interactor;
    }
    public void execute(){
        openLoginSpotifyInputInteractor.execute();//no input data needed.
    }
}
