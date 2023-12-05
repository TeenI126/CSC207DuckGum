package entities;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class AmazonAccount {

    private OkHttpClient client;
    private String apiKey;
    private String authToken;
    private String baseUrl;

    public AmazonAccount(String apiKey, String authToken, String baseUrl) {
        this.client = new OkHttpClient();
        this.apiKey = apiKey;
        this.authToken = authToken;
        this.baseUrl = baseUrl;
    }

    public String getCurrentUser() throws IOException {
        Request request = new Request.Builder()
                .url(baseUrl + "/v1/me")
                .get()
                .addHeader("x-api-key", apiKey)
                .addHeader("Authorization", "Bearer " + authToken)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    public String getUserPlaylists(String playlistId) throws IOException {
        Request request = new Request.Builder()
                .url(baseUrl + "/v1/playlists/" + playlistId)
                .get()
                .addHeader("x-api-key", apiKey)
                .addHeader("Authorization", "Bearer " + authToken)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    // Add a main method if you want to run this class as a standalone Java application
    public static void main(String[] args) {
        // Example usage
        AmazonAccount account = new AmazonAccount("<your client ID>", "<your auth token>", "https://api.music.amazon.dev");
        try {
            String currentUser = account.getCurrentUser();
            System.out.println("Current User: " + currentUser);

            String playlists = account.getUserPlaylists("4a5afe86-7316");
            System.out.println("User Playlists: " + playlists);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

