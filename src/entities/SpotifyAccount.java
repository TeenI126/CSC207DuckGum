package entities;

import entities.Builders.SongFactory;
import okhttp3.*;
import org.json.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Iterator;
import java.util.List;

public class SpotifyAccount extends MusicService{
    private String clientID = "b273f4e8f44d44168fe8c86492e95f86";
    private String clientSecret = Secrets.spotifyClientSecret;
    private final String url = "https://accounts.spotify.com/api/";
    private final String redirectURI = "https://github.com/TeenI126/CSC207DuckGum";

    // DAO
    public SpotifyAccount(String userID, List<Playlist> playlists){
        this.userID = userID;
        this.playlists = playlists;
    }

    public SpotifyAccount(String accessToken, String refreshToken, LocalDateTime accessTokenExpires) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.accessTokenExpires = accessTokenExpires;
    }

    //    /**
//     *
//     * @param forceLogin: TRUE - makes user sign in regardless if DuckGum has already been authorized.
//     */
//    void openLoginPage(boolean forceLogin){
//        JSONObject params = new JSONObject();
//        params.put("client_id", clientID)
//                .put("response_type", "code")
//                .put("redirect_uri", redirectURI)
//                .put("scope", "user-read-private user-read-email playlist-read-private playlist-read-collaborative");
//        if (forceLogin){
//            params.put("show_dialog", "true");
//        }
//
//
//        try {
//            Desktop.getDesktop()
//                    .browse(URI.create("https://accounts.spotify.com/authorize?" + encodeJSON(params)));
//
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }

    /**
     * TEMPORARY MEASURE, after a user logs into their spotify account, they will be redirected to a URI containing
     * a code linked to a user that can be exchanged for a token.
     *
     * @param uri
     */
//    public String getCodeFromURI(String uri){
//        //remove base uri, leaving extension with code left.
//        return uri.substring(47);
//    }

    /**
     *
     * @return String with user's access token, which can be used to do various actions through API
     * @throws NoAccessTokenException, happens when the users login is no longer active and needs to log in via url from
     * openLoginPage()
     */
//    public String getUserAccessToken() throws NoAccessTokenException {
//        if (accessToken == null){
//            throw new NoAccessTokenException();
//        } else if (accessTokenExpires.isAfter(LocalDateTime.now())) {
//            refreshAccessToken();
//            return accessToken;
//        } else {
//            return accessToken;
//        }
//    }
//    public void createNewUserAccessToken(String code){
//
//        OkHttpClient client = new OkHttpClient().newBuilder().build();
//        RequestBody body = RequestBody.create(
//                MediaType.parse("application/x-www-form-urlencoded"),
//                "grant_type=authorization_code&code="+code+"&redirect_uri="+redirectURI
//                );
//
//        Request request = new Request.Builder()
//                .url(url + "token")
//                .addHeader(
//                        "Authorization","Basic " +
//                                Base64.getEncoder().encodeToString((clientID+":"+clientSecret).getBytes())
//                )
//                .addHeader("Content-Type", "application/x-www-form-urlencoded")
//                .method("POST",body)
//                .build();
//
//
//        JSONObject responseJSON;
//        try {
//            Response response = client.newCall(request).execute();
//            responseJSON = new JSONObject(response.body().string());
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        //TODO handle not status 200 messages like a failed log in.
//        accessToken = responseJSON.getString("access_token");
//        refreshToken = responseJSON.getString("refresh_token");
//        accessTokenExpires = LocalDateTime.now().plusSeconds(responseJSON.getInt("expires_in")-60);
//        try {
//            updateSpotifyInformation();
//        } catch (NoAccessTokenException e) {
//            throw new RuntimeException(e);
//        }
//
//
//    }

    private void updateSpotifyInformation() throws NoAccessTokenException {
//        OkHttpClient client = new OkHttpClient().newBuilder().build();
//
//        Request request = new Request.Builder()
//                .addHeader("Authorization", "Bearer " + getUserAccessToken())
//                .url("https://api.spotify.com/v1/me")
//                .method("GET",null)
//                .build();
//
//        JSONObject responseJSON;
//        try {
//            Response response = client.newCall(request).execute();
//            responseJSON = new JSONObject(response.body().string());
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        userID = responseJSON.getString("id");
//        displayName = responseJSON.getString("display_name");

    }

    public List<Playlist> getPlaylists() throws NoAccessTokenException {
        OkHttpClient client = new OkHttpClient().newBuilder().build();

        Request request = new Request.Builder()
                .url("https://api.spotify.com/v1/users/"+userID+"/playlists")
                .method("GET",null)
                .addHeader("Authorization", "Bearer " + getUserAccessToken())
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
            returnPlaylists.add(getPlaylist(p.getString("href")));

        }
        return returnPlaylists;
        //TODO overflowed responses
    }

    public Playlist getPlaylist(String spotifyPlaylistHref) throws NoAccessTokenException {
        OkHttpClient client = new OkHttpClient().newBuilder().build();

        Request request = new Request.Builder()
                .url(spotifyPlaylistHref)
                .method("GET",null)
                .addHeader("Authorization", "Bearer " + getUserAccessToken())
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

    /**
     * This is for an application token NOT linked to a user
     */

}
