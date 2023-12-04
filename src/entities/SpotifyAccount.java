package entities;

import Exceptions.ExpiredAccessTokenException;
import Secrets.Secrets;
import entities.Builders.SongFactory;
import okhttp3.*;
import org.json.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Iterator;
import java.util.List;

public class SpotifyAccount extends MusicService {

    public SpotifyAccount(String userID, List<Playlist> playlists) {
        this.userID = userID;
        this.playlists = playlists;
    }

    public SpotifyAccount(String accessToken, String refreshToken, LocalDateTime accessTokenExpires) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.accessTokenExpires = accessTokenExpires;
    }

    public String getUserAccessToken() {
        return accessToken;
    }

    public void extendPlaylists(List<Playlist> playlists){
        this.playlists.addAll(playlists);
    }
}
