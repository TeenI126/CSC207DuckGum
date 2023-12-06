package use_case.TransferPlaylist;

import okhttp3.*;
import entities.Account;
import entities.Playlist;
import java.io.IOException;


public class TransferPlaylistInputData {

    final private Playlist playlist;
    final private Account fromAccount;
    final private Account toAccount;

    public TransferPlaylistInputData(Playlist playlist, Account fromAccount, Account toAccount) {
        this.playlist = playlist;
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
    }

    Playlist getPlaylist() { return playlist; }

    Account getFromAccount() { return fromAccount; }

    // Updates Amazon Music Playlist
    Account getToAccount() {
        OkHttpClient client = new OkHttpClient();
        String url = "https://api.music.amazon.dev/v1/playlists/4a5afe86-7316";
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType,
                "title=One of these days&description=A day of Pink Floyd&visibility=PUBLIC");
        Request request = new Request.Builder()
                .url(url)
                .put(body)
                .addHeader("x-api-key", "<your client ID>")
                .addHeader("Authorization", "Bearer <your auth token>")
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();

        try (Response response = client.newCall(request).execute()) {
            // Handle the response as needed
            // For example, you could log the response, check for success status, etc.
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception as needed
        }

        return toAccount;
    }

    public String getSourceService() {
        return null;
    }

    public String getSourceAccessToken() {
        String o = null;
        return o;
    }

    public String getTargetAccessToken() {
        String o = null;
        return o;
    }

    public String getPlaylistId() {
        String o = null;
        return null;
    }
}
