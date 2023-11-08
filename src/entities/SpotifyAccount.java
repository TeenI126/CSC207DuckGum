package entities;

import Secrets.Secrets;
import okhttp3.*;
import org.json.*;
import java.io.IOException;
import java.net.URI;
import java.time.LocalDateTime;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalUnit;
import java.util.Base64;
import java.awt.Desktop;
import java.util.Iterator;

public class SpotifyAccount extends MusicService{
    private String clientID = "b273f4e8f44d44168fe8c86492e95f86";
    private String clientSecret = Secrets.spotifyClientSecret;
    private final String url = "https://accounts.spotify.com/api/";
    private final String redirectURI = "https://github.com/TeenI126/CSC207DuckGum";
    private String accessToken = null;
    private String refreshToken = null;
    private LocalDateTime accessTokenExpires = null;

    // USER DETAILS
    private String displayName;
    private String userID;


    SpotifyAccount(){

    }
    void openLoginPage(){
        openLoginPage(false);
    }

    /**
     *
     * @param forceLogin: TRUE - makes user sign in regardless if DuckGum has already been authorized.
     */
    void openLoginPage(boolean forceLogin){
        JSONObject params = new JSONObject();
        params.put("client_id", clientID)
                .put("response_type", "code")
                .put("redirect_uri", redirectURI)
                .put("scope", "user-read-private user-read-email");
        if (forceLogin){
            params.put("show_dialog", "true");
        }


        try {
            Desktop.getDesktop()
                    .browse(URI.create("https://accounts.spotify.com/authorize?" + encodeJSON(params)));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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

    /**
     *
     * @return String with user's access token, which can be used to do various actions through API
     * @throws NoAccessTokenException, happens when the users login is no longer active and needs to log in via url from
     * openLoginPage()
     */
    public String getUserAccessToken() throws NoAccessTokenException {
        if (accessToken == null){
            throw new NoAccessTokenException();
        } else if (accessTokenExpires.isAfter(LocalDateTime.now())) {
            refreshAccessToken();
            return accessToken;
        } else {
            return accessToken;
        }
    }

    private void refreshAccessToken() {
    }

    private boolean isUserAccessTokenValid(){
        //TODO
        return true;
    }

    public void createNewUserAccessToken(String code){

        OkHttpClient client = new OkHttpClient().newBuilder().build();
        RequestBody body = RequestBody.create(
                "grant_type=authorization_code&code="+code+"&redirect_uri="+redirectURI,
                MediaType.parse("application/x-www-form-urlencoded")
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
        try {
            Response response = client.newCall(request).execute();
            responseJSON = new JSONObject(response.body().string());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //TODO handle not status 200 messages like a failed log in.
        accessToken = responseJSON.getString("access_token");
        refreshToken = responseJSON.getString("refresh_token");
        accessTokenExpires = LocalDateTime.now().plusSeconds(responseJSON.getInt("expires_in")-60);


    }

    private void updateSpotifyInformation() throws NoAccessTokenException {
        OkHttpClient client = new OkHttpClient().newBuilder().build();

        Request request = new Request.Builder()
                .addHeader("Authorization", "Bearer " + getUserAccessToken())
                .url("https://api.spotify.com/v1/me")
                .method("GET",null)
                .build();

        JSONObject responseJSON;
        try {
            Response response = client.newCall(request).execute();
            responseJSON = new JSONObject(response.body().string());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        userID = responseJSON.getString("id");
        displayName = responseJSON.getString("display_name");

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
    void updateToken(){

        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        RequestBody body = RequestBody.create(mediaType, "grant_type:client_credentials");
        Request request = new Request.Builder().url(url+"token")
                .method("POST",body)
                .addHeader("Authorization", "Basic" + Base64.getEncoder().encodeToString((clientID+":"+clientSecret).getBytes()))
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();

        try {
            Response response = client.newCall(request).execute();
            JSONObject responseJSON = new JSONObject(response.body().string());

            accessToken = responseJSON.getString("access_token");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    class NoAccessTokenException extends Exception{}
}
