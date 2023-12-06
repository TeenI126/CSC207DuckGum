package data_access;

import Secrets.Secrets;
import entities.Builders.SongFactory;
import entities.Playlist;
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
    // USER DETAILS
    private String displayName;
    private String userID;
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

    /**
     * TEMPORARY MEASURE, after a user logs into their spotify account, they will be redirected to a URI containing
     * a code linked to a user that can be exchanged for a token.
     *
     * @param uri
     */
    public String getCodeFromURI(String uri){
        //remove base uri, leaving extension with code left.
        return uri.substring(47);
    }

    private void refreshAccessToken() {
        //TODO
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

        updateSpotifyInformation(spotifyAccount, accessToken);//adds user id and display name

        return spotifyAccount;

    }

    private void updateSpotifyInformation(SpotifyAccount spotifyAccount, String accessToken) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder().build();

        Request request = new Request.Builder()
                .addHeader("Authorization", "Bearer " + accessToken)
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
        spotifyAccount.setPlaylists(getPlaylists(accessToken,spotifyAccount));
    }

    public java.util.List<Playlist> getPlaylists(String accessToken, SpotifyAccount spotifyAccount) {
        OkHttpClient client = new OkHttpClient().newBuilder().build();

        Request request = new Request.Builder()
                .url("https://api.spotify.com/v1/users/"+spotifyAccount.getUserID()+"/playlists")
                .method("GET",null)
                .addHeader("Authorization", "Bearer " + spotifyAccount.getUserAccessToken())
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
            returnPlaylists.add(getPlaylist(accessToken, p.getString("href")));

        }
        return returnPlaylists;
        //TODO overflowed responses
    }

    public Playlist getPlaylist(String accessToken, String spotifyPlaylistHref) {
        OkHttpClient client = new OkHttpClient().newBuilder().build();

        Request request = new Request.Builder()
                .url(spotifyPlaylistHref)
                .method("GET",null)
                .addHeader("Authorization", "Bearer " + accessToken)
                .build();

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
                p.addSong(SongFactory.songFromSpotifyTrackJSONObject(items.getJSONObject(i).getJSONObject("track")));
            } catch (JSONException ignored){

            }
        }

        return p;

        //TODO loop over next pages for lage playlists that exceec response limit
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

//    /**
//     * This is for an application token NOT linked to a user
//     */
//    void updateToken(){
//
//        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
//        OkHttpClient client = new OkHttpClient().newBuilder().build();
//        RequestBody body = RequestBody.create(mediaType, "grant_type:client_credentials");
//        Request request = new Request.Builder().url(url+"token")
//                .method("POST",body)
//                .addHeader("Authorization", "Basic" + Base64.getEncoder().encodeToString((clientID+":"+clientSecret).getBytes()))
//                .addHeader("Content-Type", "application/x-www-form-urlencoded")
//                .build();
//
//        try {
//            Response response = client.newCall(request).execute();
//            JSONObject responseJSON = new JSONObject(response.body().string());
//
//            String accessToken = responseJSON.getString("access_token");
//
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }

}

