package entities;

import okhttp3.*;
import org.json.*;
import java.io.IOException;
import java.net.URI;
import java.util.Base64;
import java.awt.Desktop;
import java.util.Iterator;

public class SpotifyAccount extends Account{
    private String clientID = "b273f4e8f44d44168fe8c86492e95f86";
    private String clientSecret = "5bfa9aa652c5461d98fcc235842cbc6c";
    private final String url = "https://accounts.spotify.com/api/";
    private final String redirectURI = "https://github.com/TeenI126/CSC207DuckGum";
    private String token = null;
    SpotifyAccount(){

    }

    void authorize(){
        JSONObject params = new JSONObject();
        params.put("client_id", clientID);
                params.put("response_type", "code")
                .put("redirect_uri", redirectURI)
                .put("scope", "user-read-private user-read-email");

        try {
            Desktop.getDesktop()
                    .browse(URI.create("https://accounts.spotify.com/authorize?" + encodeJSON(params)));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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

            token = responseJSON.getString("access_token");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
