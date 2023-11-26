package use_case.TransferPlaylist;

import com.spotify.api.SpotifyApi; // Replace with the actual import from the Spotify SDK
import com.amazon.music.AmazonMusicApi; // Replace with the actual import from the Amazon Music SDK
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONObject;import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONObject;

public class TransferPlaylistInteractor {

    private AmazonAccount amazonAccount;
    private SpotifyAccount spotifyAccount;

    public PlaylistInteractor(SpotifyApi spotifyApi, AmazonMusicApi amazonMusicApi) {
        this.spotifyApi = spotifyApi;
        this.amazonMusicApi = amazonMusicApi;
    }

    public void transferPlaylistFromSpotifyToAmazon(String spotifyPlaylistId) {
        Playlist spotifyPlaylist = fetchPlaylistFromSpotify(spotifyPlaylistId);
        Playlist amazonPlaylist = createPlaylistOnAmazon(spotifyPlaylist.getName());
        addSongsToAmazonPlaylist(spotifyPlaylist, amazonPlaylist);
    }

    public void transferPlaylistFromAmazonToSpotify(String spotifyPlaylistId) {
        Playlist amazonPlaylist = fetchPlaylistFromAmazon(spotifyPlaylistId);
        Playlist spotifyPlaylist = createPlaylistOnSpotify(spotifyPlaylist.getName());
        addSongsToSpotifyPlaylist(amazonPlaylist, spotifyPlaylist);
    }
    
    public Playlist fetchPlaylistFromSpotify(String playlistId) throws NoAccessTokenException {
        OkHttpClient client = new OkHttpClient().newBuilder().build();

        String url = "https://api.spotify.com/v1/playlists/" + playlistId;
        Request request = new Request.Builder()
                .url(url)
                .method("GET", null)
                .addHeader("Authorization", "Bearer " + getUserAccessToken())
                .build();

        JSONObject responseJSON;
        try {
            Response response = client.newCall(request).execute();
            responseJSON = new JSONObject(response.body().string());

            if (!response.isSuccessful()) {
                throw new RuntimeException("Failed to fetch playlist: " + response.message());
            }
        } catch (IOException e) {
            throw new RuntimeException("IO Exception occurred", e);
        }

        JSONObject tracks = responseJSON.getJSONObject("tracks");
        JSONArray items = tracks.getJSONArray("items");

        Playlist p = new Playlist(responseJSON.getString("name"));
        for (int i = 0; i < items.length(); i++) {
            try {
                p.addSong(SongFactory.songFromSpotifyTrackJSONObject(items.getJSONObject(i).getJSONObject("track")));
            } catch (JSONException ignored) {
                // Handle or log the exception as needed
            }
        }

        return p;
    }

    private Playlist fetchPlaylistFromAmazon(String playlistId) throws IOException {
        String playlistJsonString = getUserPlaylists(playlistId);

        JSONObject playlistJson = new JSONObject(playlistJsonString);
        // Assuming the JSON structure contains a 'name' field and an array of 'tracks'
        String playlistName = playlistJson.getString("name");
        JSONArray tracksJson = playlistJson.getJSONArray("tracks");

        Playlist playlist = new Playlist(playlistName);
        for (int i = 0; i < tracksJson.length(); i++) {
            JSONObject trackJson = tracksJson.getJSONObject(i);
            try {
                Song song = SongFactory.songFromAmazonTrackJSONObject(trackJson);
                playlist.addSong(song);
            } catch (JSONException e) {
                // Handle or log the exception as needed
            }
        }

        return playlist;
    }

    public String createPlaylistOnAmazon(String playlistName) throws IOException {
        // Construct the JSON body
        JSONObject body = new JSONObject();
        body.put("name", playlistName);
        // Add any other required fields

        Request request = new Request.Builder()
                .url(amazonAccount.getBaseUrl() + "/path/to/create/playlist") // Replace with correct endpoint
                .post(RequestBody.create(body.toString(), MediaType.parse("application/json")))
                .addHeader("x-api-key", amazonAccount.getApiKey())
                .addHeader("Authorization", "Bearer " + amazonAccount.getAuthToken())
                .build();

        try (Response response = amazonAccount.getClient().newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Failed to create playlist: " + response.message());
            }
            // Parse response to get the playlist ID or details
            JSONObject responseJson = new JSONObject(response.body().string());
            return responseJson.getString("id"); // Adjust based on the actual response structure
        }
    }

    private Playlist createPlaylistOnSpotify(String playlistName) {
        // Implementation to create a new playlist on Spotify
    }

    private void addSongsToAmazonPlaylist(Playlist spotifyPlaylist, Playlist amazonPlaylist) {
        // Map and add songs from Spotify playlist to Amazon playlist
        // Song mapping is a complex process due to different song IDs on platforms
    }

    private void addSongsToSpotifyPlaylist(Playlist amazonPlaylist, Playlist spotifyPlaylist) {
        // Map and add songs from Amazon playlist to Spotify playlist
        // Song mapping is a complex process due to different song IDs on platforms
    }
}


