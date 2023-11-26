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

    private SpotifyApi spotifyApi;
    private AmazonMusicApi amazonMusicApi;

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
        
    }

    private Playlist createPlaylistOnAmazon(String playlistName) {
        // Implementation to create a new playlist on Amazon Music
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


