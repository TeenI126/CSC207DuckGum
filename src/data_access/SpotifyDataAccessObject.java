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


    public void updateSpotifyInformation(SpotifyAccount spotifyAccount) throws IOException {

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

    /**
     *
     * @param spotifyAccount the account the playlist is being added to
     * @param playlist playlist to copy from, Note this playlist object will not be added to playlist of account, but
     *                 a copy of it with the new accounts playlist id
     */
    public void createPlaylist(SpotifyAccount spotifyAccount, Playlist playlist) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        RequestBody body = RequestBody.create(
                MediaType.parse("application/json"),
                "{\n" +
                        "\"name\": \""+playlist.getName()+"\"\n" +
                        "\"description\": \"\"\n" +
                        "\n}"
        );

        Request request = new Request.Builder()
                .url("https://api.spotify.com/v1/users/"+spotifyAccount.getUserID()+"/playlists")
                .addHeader("Content-Type", "application/json")
                .addHeader(
                        "Authorization","Bearer " +
                                getAccessTokenFromAccount(spotifyAccount)
                )
                .method("POST",body)
                .build();


        JSONObject responseJSON;

        Response response = client.newCall(request).execute();
        responseJSON = new JSONObject(response.body().string());

        String playlistName = responseJSON.getString("name");
        String playlistID = responseJSON.getString("id");

        Playlist newPlaylist = new Playlist(playlistName);
        newPlaylist.setId(playlistID);

        for (Song song : playlist.getSongs()){
            addSongToPlaylist(spotifyAccount,playlist,song);
        }
        updateSpotifyInformation(spotifyAccount);
    }

    /**
     *
     * @param spotifyAccount
     * @param playlist must be in spotifyAccount.getPlaylists()
     * @param song song to be added
     */
    public void addSongToPlaylist(SpotifyAccount spotifyAccount, Playlist playlist, Song song) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder().build();

        String songURI = getSongURIfromSongObject(spotifyAccount, song).replaceAll(":","%3A");

        Request request = new Request.Builder()
                .url("https://api.spotify.com/v1/playlists/"+playlist.getId()+"/tracks?uris="+songURI)
                .addHeader("Content-Type", "application/json")
                .addHeader(
                        "Authorization","Bearer " +
                                getAccessTokenFromAccount(spotifyAccount)
                )
                .method("POST",null)
                .build();


        JSONObject responseJSON;

        Response response = client.newCall(request).execute();
        responseJSON = new JSONObject(response.body().string());
    }

    public String getSongURIfromSongObject(SpotifyAccount spotifyAccount, Song song) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder().build();


        Request request = new Request.Builder()
                .url("https://api.spotify.com/v1/search?isrc%3A"+song.getId()+"&type=track&limit=1")
                .addHeader("Content-Type", "application/json")
                .addHeader(
                        "Authorization","Bearer " +
                                getAccessTokenFromAccount(spotifyAccount)
                )
                .method("GET",null)
                .build();


        JSONObject responseJSON;

        Response response = client.newCall(request).execute();
        responseJSON = new JSONObject(response.body().string());

        return responseJSON.getJSONObject("tracks").getJSONArray("items").getJSONObject(0).
                getString("uri");
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

    public String getCurrentUserId(String accessToken) throws IOException {
        String url = BASE_URL + "/me";

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer " + accessToken)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response + " with body " + response.body().string());
            }

            JSONObject responseJson = new JSONObject(response.body().string());
            return responseJson.getString("id");
        }
    }

    // Method to create a playlist on Spotify for a given user
    public String createPlaylist(String userId, Playlist playlist, String accessToken) throws IOException {
        // Define the endpoint URL
        String url = BASE_URL + "/users/" + userId + "/playlists";

        // Create the request body with the playlist details
        JSONObject playlistDetails = new JSONObject();
        playlistDetails.put("name", playlist.getName());
        playlistDetails.put("description", "Created via SpotifyAPI");
        playlistDetails.put("public", false);  // Set to 'true' if you want the playlist to be public

        // Build the POST request to create the playlist
        RequestBody body = RequestBody.create(playlistDetails.toString(), MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer " + accessToken)
                .post(body)
                .build();

        // Execute the request
        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response + " with body " + response.body().string());
            }

            // Parse the response to get the new playlist ID
            JSONObject responseJson = new JSONObject(response.body().string());
            return responseJson.getString("id");
        }
    }


}
