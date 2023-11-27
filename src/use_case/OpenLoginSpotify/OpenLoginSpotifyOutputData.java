package use_case.OpenLoginSpotify;

public class OpenLoginSpotifyOutputData {
    String callback_url;
    OpenLoginSpotifyOutputData(String callback_url){
        this.callback_url = callback_url;
    }

    public String getCallback_url() {
        return callback_url;
    }
}
