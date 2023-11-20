package entities;

public class MusicService {

    public String token;
    public String type;
    public String userID;
    public String displayName;

    public void setToken(String tokenNew) {
        token = tokenNew;
    }

    public String getToken() {
        return token;
    }

    public String getType() {
        return type;
    }

    public String getUserID() {
        return userID;
    }

    public String getDisplayName() {
        return displayName;
    }
}
