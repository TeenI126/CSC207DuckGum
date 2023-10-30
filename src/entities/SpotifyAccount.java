package entities;

import okhttp3.*;
import org.json.*;
import org.jsoup.Connection;

import java.io.IOException;
import java.util.Base64;

public class SpotifyAccount extends Account{
    private String clientID = "b273f44d44168fe8c8649e95f86";
    private String clientSecret = "5bfa9aa652c5461d98fcc235842cbc6c";
    private final String url = "https://accounts.spotify.com/api/";
    private String token = null;
    SpotifyAccount(){
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        Request request = new Request.Builder().url("").addHeader("")
    }

    private void authorize(){
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        Request request = new Request.Builder().url(url + "authorize")
    }

    private void updateToken(){
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        RequestBody body = RequestBody.create(mediaType, "grant_type:client_credentials")
        Request request = new Request.Builder().url(url+"token")
                .method("POST",body)
                .addHeader("Authorization", "Basic" + Base64.getEncoder().encodeToString((clientID+":"+clientSecret).getBytes()))
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();

        try {
            Response response = client.newCall(request).execute();
            JSONObject responseJSON = new JSONObject(response.body().string());

            token = responseJSON.getString("access_token");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
