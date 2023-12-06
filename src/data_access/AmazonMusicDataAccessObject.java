package data_access;

import com.sun.net.httpserver.HttpServer;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URL;
import org.json.JSONObject;

public class AmazonMusicDataAccessObject {

    // Client credentials and redirect URI
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
                AmazonMusicDataAccessObject.updateUserInfo(userProfile, userPlaylistsJson);

                // Prepare response text for the web
                String responseTextx = "Login Successful!\n\nUser Profile:\n" + userProfile + "\n\nPlaylists:\n" + userPlaylistsJson;
                exchange.sendResponseHeaders(200, responseTextx.length());
                OutputStream os = exchange.getResponseBody();
                os.write(responseTextx.getBytes());
                os.close();
            }

            exchange.sendResponseHeaders(200, responseText.length());
            OutputStream os = exchange.getResponseBody();
            os.write(responseText.getBytes());
            System.out.println(responseText);
            os.close();
        });

        server.start();
        System.out.println("Server started on port 8080. Navigate to http://localhost:8080/callback to see it in action.");
    }

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

    private static String readAll(BufferedReader reader) throws IOException {
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        return sb.toString();
    }

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
}



// Test Code
//import com.sun.net.httpserver.HttpServer;
//        import java.io.*;
//        import java.net.HttpURLConnection;
//        import java.net.InetSocketAddress;
//        import java.net.URL;
//        import org.json.JSONObject;
//
//public class RedirectUriServer {
//
//    private static final String CLIENT_ID = "Null";
//    private static final String CLIENT_SECRET = "Null";
//    private static final String REDIRECT_URI = "Null";
//
//    // Variables to store token and user information
//    private static String accessToken = "";
//    private static String refreshToken = "";
//    private static String userId = "";
//    private static String userName = "";
//    private static String userEmail = "";
//
//    public static void main(String[] args) throws IOException {
//        startServer();
//    }
//
//    private static void startServer() throws IOException {
//        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
//        server.createContext("/callback", RedirectUriServer::handleCallbackRequest);
//        server.start();
//        System.out.println("Server running on port 8080.");
//    }
//
//    private static void handleCallbackRequest(HttpExchange exchange) throws IOException {
//        String query = exchange.getRequestURI().getQuery();
//        String responseText = processCallbackQuery(query);
//
//        exchange.sendResponseHeaders(200, responseText.getBytes().length);
//        try (OutputStream os = exchange.getResponseBody()) {
//            os.write(responseText.getBytes());
//        }
//    }
//
//    private static String processCallbackQuery(String query) {
//        if (query != null && query.contains("code=")) {
//            String code = extractCodeFromQuery(query);
//            JSONObject tokenJson = exchangeAuthCodeForToken(code);
//            updateTokenInfo(tokenJson);
//
//            String userProfileInfo = fetchUserProfileInfo(accessToken);
//            String userPlaylistInfo = fetchUserPlaylistInfo(accessToken);
//
//            String fullResponse = "User Profile: " + userProfileInfo + "\n\nPlaylists: " + userPlaylistInfo;
//            AmazonLoginApp.updateUserInfo(fullResponse);
//
//            return fullResponse;
//        }
//        return "Authorization code not found.";
//    }
//
//    private static String extractCodeFromQuery(String query) {
//        return query.substring(query.indexOf("code=") + 5);
//    }
//
//    private static JSONObject exchangeAuthCodeForToken(String authorizationCode) {
//        String tokenResponse = exchangeAuthorizationCode(authorizationCode);
//        return new JSONObject(tokenResponse);
//    }
//
//    private static void updateTokenInfo(JSONObject tokenJson) {
//        accessToken = tokenJson.getString("access_token");
//        refreshToken = tokenJson.optString("refresh_token");
//    }
//
//    // Remaining methods (fetchUserProfile, fetchUserPlaylists, readAll, exchangeAuthorizationCode) remain the same
//}
