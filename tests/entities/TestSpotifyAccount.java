package entities;


import org.junit.Test;
import org.junit.Before;

import javax.swing.*;

public class TestSpotifyAccount {
    SpotifyAccount spotifyAccount;
    @Before
    public void init(){
        spotifyAccount = new SpotifyAccount();
        spotifyAccount.openLoginPage(true);
        spotifyAccount.createNewUserAccessToken(spotifyAccount.getCodeFromURI(JOptionPane.showInputDialog("enter redirected url")));
    }
    @Test
    public void TestGetUsername(){


    }
}
