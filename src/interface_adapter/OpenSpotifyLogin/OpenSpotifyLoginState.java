package interface_adapter.OpenSpotifyLogin;

public class OpenSpotifyLoginState {
    String callbackUrl = "";

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }
}
