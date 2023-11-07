package entities;


import org.junit.Test;
import org.junit.Before;

public class TestSpotifyAccount {
    SpotifyAccount spotifyAccount;
    @Before
    public void init(){
        spotifyAccount = new SpotifyAccount();
    }
    @Test
    public void TestAuthorize(){
        spotifyAccount.openLoginPage(true);

    }
}
