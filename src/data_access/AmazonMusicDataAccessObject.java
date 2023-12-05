package data_access;

import com.sun.net.httpserver.HttpServer;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URL;
import org.json.JSONObject;

/**
 * Handles data access operations for Amazon Music.
 * Responsible for authenticating users and fetching user-related data from Amazon Music API.
 */
public class AmazonMusicDataAccessObject {

    // Define the client credentials and redirect URI for Amazon API
    private static final String CLIENT_ID = "amzn1.application-oa2-client.951516002f594c19922fd8aa22fa93fc";
    private static final String CLIENT_SECRET = "amzn1.oa2-cs.v1.2aa4cb84b16e1d32dbc64b4c686e956188c875fb24f9d5f896426d9301fb6684";
    private static final String REDIRECT_URI = "http://localhost:8080/callback";

    // Variables to store token and user information
    private static String accessToken = "";
    private static String refreshToken = "";
    private static String userId = "";
    private static String userName = "";
    private static String userEmail = "";

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        server.createContext("/callback", exchange -> {
            String query = exchange.getRequestURI().getQuery();
            String responseText = "Authorization code not found.";
            if (query != null && query.contains("code=")) {
                String authorizationCode = query.substring(query.indexOf("code=") + 5);
                JSONObject tokenJson = new JSONObject(exchangeAuthorizationCode(authorizationCode));
                accessToken = tokenJson.getString("access_token");
                refreshToken = tokenJson.optString("refresh_token");

                String userProfile = fetchUserProfile(accessToken);
                String userPlaylistsJson = fetchUserPlaylists(accessToken);
                responseText = "User Profile: " + userProfile + "\n\nPlaylists: " + userPlaylistsJson;

                // Update GUI with user profile and playlists
                AmazonLoginApp.updateUserInfo(responseText);
            }

            exchange.sendResponseHeaders(200, responseText.length());
            OutputStream os = exchange.getResponseBody();
            os.write(responseText.getBytes());
            os.close();
        });

        server.start();
        System.out.println("Server started on port 8080. Navigate to http://localhost:8080/callback to see it in action.");
    }

    // Method to fetch the user profile using the access token
    private static String fetchUserProfile(String accessToken) {
        try {
            URL url = new URL("https://api.amazon.com/user/profile");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Bearer " + accessToken);

            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            JSONObject userProfileJson = new JSONObject(readAll(br));
            userId = userProfileJson.getString("user_id");
            userName = userProfileJson.getString("name");
            userEmail = userProfileJson.getString("email");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return accessToken;
    }

    // Method to fetch the user's playlists
    private static String fetchUserPlaylists(String accessToken) {
        try {
            URL url = new URL("https://api.music.amazon.com/v1/me/playlists");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Bearer " + accessToken);

            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            return readAll(br);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Helper method to read all data from a BufferedReader instance
    private static String readAll(BufferedReader reader) throws IOException {
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        return sb.toString();
    }

    // Method to exchange authorization code for an access token
    private static String exchangeAuthorizationCode(String authorizationCode) {
        try {
            URL url = new URL("https://api.amazon.com/auth/o2/token");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setDoOutput(true);

            String params = String.format("grant_type=authorization_code&code=%s&client_id=%s&client_secret=%s&redirect_uri=%s",
                    authorizationCode, CLIENT_ID, CLIENT_SECRET, REDIRECT_URI);
            OutputStream os = connection.getOutputStream();
            os.write(params.getBytes());
            os.close();

            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line);
            }
            br.close();

            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Method to create a playlist in Amazon Music
    public String createPlaylist(Playlist playlist, String accessToken) throws IOException {
        String url = BASE_URL + "/playlists"; // Update with the correct endpoint if necessary

        // Build the JSON payload with the playlist details
        JSONObject playlistDetails = new JSONObject();
        playlistDetails.put("name", playlist.getName());
        playlistDetails.put("description", "Created via Amazon Music API");
        playlistDetails.put("public", false); // Or set to true if the playlist should be public

        // Construct the playlist tracks if the API requires it
        JSONArray tracksArray = new JSONArray();
        for (Song song : playlist.getSongs()) {
            tracksArray.put(new JSONObject().put("id", song.getId()));
        }
        playlistDetails.put("tracks", tracksArray);

        RequestBody body = RequestBody.create(playlistDetails.toString(), MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer " + accessToken)
                .post(body)
                .build();

        // Execute the request and handle the response
        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response + " with body " + response.body().string());
            }
            JSONObject responseJson = new JSONObject(response.body().string());
            return responseJson.getString("id");
        }
    }
}