package entities;


import org.junit.Test;
import org.junit.Before;

import javax.swing.*;
import java.util.List;

public class TestSpotifyAccount {
    SpotifyAccount spotifyAccount;
    @Before
    public void init(){
        spotifyAccount = new SpotifyAccount();
        spotifyAccount.openLoginPage(true);
        spotifyAccount.createNewUserAccessToken(spotifyAccount.getCodeFromURI(JOptionPane.showInputDialog("enter redirected url")));
    }
    @Test
    public void TestGetPlaylists() {
        try {
            List<Playlist> playlists = spotifyAccount.getPlaylists();
            for (Playlist p : playlists){
                System.out.println(p.getName());
            }
            assert(!playlists.isEmpty());
        } catch (SpotifyAccount.NoAccessTokenException e) {
            throw new RuntimeException(e);
        }

    }
}
