package data_access;

import Secrets.Secrets;
import entities.MusicService;
import entities.Playlist;
import entities.Song;
import entities.SpotifyAccount;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import use_case.LogInSpotify.LogInSpotifyDataAccessInterface;
import use_case.OpenLoginSpotify.OpenLoginSpotifyDataAccessInterface;

import javax.security.auth.login.FailedLoginException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Iterator;
import java.util.List;

public class SpotifyDataAccessObject implements OpenLoginSpotifyDataAccessInterface, LogInSpotifyDataAccessInterface {
    private String clientSecret = Secrets.spotifyClientSecret;

    private String clientID = "b273f4e8f44d44168fe8c86492e95f86";
    private final String url = "https://accounts.spotify.com/api/";
    private final String redirectURI = "https://github.com/TeenI126/CSC207DuckGum";
    public String getLoginPage(){
        return getLoginPage(false);
    }

    /**
     *
     * @param forceLogin: TRUE - makes user sign in regardless if DuckGum has already been authorized.
     */
    String getLoginPage(boolean forceLogin){
        JSONObject params = new JSONObject();
        params.put("client_id", clientID)
                .put("response_type", "code")
                .put("redirect_uri", redirectURI)
                .put("scope", "user-read-private user-read-email playlist-read-private playlist-read-collaborative");
        if (forceLogin){
            params.put("show_dialog", "true");
        }

        return "https://accounts.spotify.com/authorize?" + encodeJSON(params);

    }

    private String getAccessTokenFromAccount(MusicService spotifyAccount) throws IOException {
        if (spotifyAccount.getAccessTokenExpires().isBefore(LocalDateTime.now())){
            refreshAccessToken(spotifyAccount);
        }
        return spotifyAccount.getUserAccessToken();
    }


    private void refreshAccessToken(MusicService spotifyAccount) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        RequestBody body = RequestBody.create(
                MediaType.parse("application/x-www-form-urlencoded"),
                "grant_type=refresh_token&refresh_token="+spotifyAccount.getRefreshToken()
        );

        Request request = new Request.Builder()
                .url(url + "token")
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader(
                        "Authorization","Basic " +
                                Base64.getEncoder().encodeToString((clientID+":"+clientSecret).getBytes())
                )
                .method("POST",body)
                .build();


        JSONObject responseJSON;

        Response response = client.newCall(request).execute();
        responseJSON = new JSONObject(response.body().string());

        if (responseJSON.has("refresh_token")){ //refreshed refresh token only appears if refresh not still valid
            String newRefreshToken = responseJSON.getString("refresh_token");
            spotifyAccount.setRefreshToken(newRefreshToken);
        }

        String newAccessToken = responseJSON.getString("access_token");
        LocalDateTime newExpires = LocalDateTime.now().plusSeconds(responseJSON.getInt("expires_in")-60);

        spotifyAccount.setAccessToken(newAccessToken);

        spotifyAccount.setAccessTokenExpires(newExpires);

    }

    public SpotifyAccount createSpotifyAccountFromCode(String code) throws IOException, FailedLoginException {

        OkHttpClient client = new OkHttpClient().newBuilder().build();
        RequestBody body = RequestBody.create(
                MediaType.parse("application/x-www-form-urlencoded"),
                "grant_type=authorization_code&code="+code+"&redirect_uri="+redirectURI
        );

        Request request = new Request.Builder()
                .url(url + "token")
                .addHeader(
                        "Authorization","Basic " +
                                Base64.getEncoder().encodeToString((clientID+":"+clientSecret).getBytes())
                )
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .method("POST",body)
                .build();


        JSONObject responseJSON;

        Response response = client.newCall(request).execute();
        responseJSON = new JSONObject(response.body().string());

        if(!response.isSuccessful()){
            throw new FailedLoginException();
        }

        String accessToken = responseJSON.getString("access_token");
        String refreshToken = responseJSON.getString("refresh_token");
        LocalDateTime accessTokenExpires = LocalDateTime.now().plusSeconds(responseJSON.getInt("expires_in")-60);


        SpotifyAccount spotifyAccount =  new SpotifyAccount(accessToken,refreshToken,accessTokenExpires);

        updateSpotifyInformation(spotifyAccount);//adds user id and display name

        return spotifyAccount;

    }

    private void updateSpotifyInformation(SpotifyAccount spotifyAccount) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder().build();

        Request request = new Request.Builder()
                .addHeader("Authorization", "Bearer " + getAccessTokenFromAccount(spotifyAccount))
                .url("https://api.spotify.com/v1/me")
                .method("GET",null)
                .build();

        JSONObject responseJSON;

        Response response = client.newCall(request).execute();
        responseJSON = new JSONObject(response.body().string());


        String userID = responseJSON.getString("id");
        String displayName = responseJSON.getString("display_name");

        spotifyAccount.setUserID(userID);
        spotifyAccount.setDisplayName(displayName);
        spotifyAccount.setPlaylists(getPlaylists(spotifyAccount));
    }

    public java.util.List<Playlist> getPlaylists(SpotifyAccount spotifyAccount) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder().build();

        Request request = new Request.Builder()
                .url("https://api.spotify.com/v1/users/"+spotifyAccount.getUserID()+"/playlists")
                .method("GET",null)
                .addHeader("Authorization", "Bearer " + getAccessTokenFromAccount(spotifyAccount))
                .build();

        JSONObject responseJSON;
        try {
            Response response = client.newCall(request).execute();
            responseJSON = new JSONObject(response.body().string());

            if (!response.isSuccessful()){
                throw new RuntimeException();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        List<Playlist> returnPlaylists = new ArrayList<Playlist>();
        JSONArray rawPlaylists = responseJSON.getJSONArray("items");

        for (int i = 0; i < rawPlaylists.length(); i++){
            JSONObject p = rawPlaylists.getJSONObject(i);
            returnPlaylists.add(getPlaylist(spotifyAccount, p.getString("href")));

        }
        return returnPlaylists;
    }

    public Playlist getPlaylist(SpotifyAccount spotifyAccount, String spotifyPlaylistHref) {
        OkHttpClient client = new OkHttpClient().newBuilder().build();

        Request request;
        try {
            request = new Request.Builder()
                    .url(spotifyPlaylistHref)
                    .method("GET",null)
                    .addHeader("Authorization", "Bearer " + getAccessTokenFromAccount(spotifyAccount))
                    .build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        JSONObject responseJSON;
        try {
            Response response = client.newCall(request).execute();
            responseJSON = new JSONObject(response.body().string());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        JSONObject tracks = responseJSON.getJSONObject("tracks");
        JSONArray items = tracks.getJSONArray("items");
        // Making song object to add to playlist

        Playlist p = new Playlist(responseJSON.getString("name"));
        for (int i = 0; i < items.length();i++){
            try{
                p.addSong(songFromSpotifyTrackJSONObject(items.getJSONObject(i).getJSONObject("track")));
            } catch (JSONException ignored){
                try {
                    Request requestRec = new Request.Builder()
                            .url(spotifyPlaylistHref)
                            .method("GET",null)
                            .addHeader("Authorization", "Bearer " + getAccessTokenFromAccount(spotifyAccount))
                            .build();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                try {
                    Response response = client.newCall(request).execute();
                    responseJSON = new JSONObject(response.body().string());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        return p;


    }

    private Song songFromSpotifyTrackJSONObject(JSONObject trackJSONObject){
        String id;
        List<String> artists = new ArrayList<String>();
        String name;
        String album;

        id = trackJSONObject.getJSONObject("external_ids").getString("isrc");

        JSONArray artistsRaw = trackJSONObject.getJSONArray("artists");
        for (int i = 0; i < artistsRaw.length(); i++){
            artists.add(artistsRaw.getJSONObject(i).getString("name"));
        }

        name = trackJSONObject.getString("name");
        album = trackJSONObject.getJSONObject("album").getString("name");

        return new Song(id,artists,name,album);
    }
    private String encodeJSON(JSONObject jsonObject){
        StringBuilder retString = new StringBuilder();

        Iterator<String> keys = jsonObject.keys();
        while(keys.hasNext()){
            String key = keys.next();
            retString.append(key).append("=").append(jsonObject.getString(key));
            retString.append("&");
        }
        retString.deleteCharAt(retString.length()-1);
        return retString.toString().replaceAll(" ","+");
    }


}
