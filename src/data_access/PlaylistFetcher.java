package data_access;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class PlaylistFetcher {

    private static final String BASE_URL = "https://api.music.amazon.com/v1/me/playlists";
    private static final String accessToken = "YOUR_ACCESS_TOKEN"; // Replace with actual access token

    public static String fetchPlaylists() {
        try {
            URL url = new URL(BASE_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Bearer " + accessToken);

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

    public static void main(String[] args) {
        // Fetch and print playlists
        String playlistsJson = fetchPlaylists();
        System.out.println(playlistsJson);
    }
}
