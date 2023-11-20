package use_case.LogInSpotify;

public class LogInSpotifyInputData {
    String code;

    public LogInSpotifyInputData(String code) {
        this.code = code;
    }

    public String getCode(){
        return code;
    }
}
