package entities;

import java.util.List;

public class Account {
    public String id;
    public List<MusicService> musicAccount;
    private String password;
    public String email;
    public List<Playlist> playlists;

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public boolean isMusicServiceEmpty() {
        return musicAccount.isEmpty();
    }

    public boolean isPlaylistEmpty() {
        return playlists.isEmpty();
    }

    public List<MusicService> getMusicAccounts() {
        if (isMusicServiceEmpty()) {
            return null;
        }
        return musicAccount;
    }

    public List<Playlist> getPlaylists() {
        if (isPlaylistEmpty()) {
            return null;
        }
        return playlists;
    }
}
