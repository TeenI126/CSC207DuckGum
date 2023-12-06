package use_cases;

import data_access.SpotifyDataAccessObject;
import entities.Account;
import interface_adapter.LogInSpotify.LogInSpotifyController;
import interface_adapter.OpenSpotifyLogin.OpenSpotifyLoginController;
import use_case.LogInSpotify.LogInSpotifyInputBoundary;
import use_case.LogInSpotify.LogInSpotifyInteractor;
import use_case.LogInSpotify.LogInSpotifyOutputBoundary;
import use_case.OpenLoginSpotify.OpenLoginSpotifyInputBoundary;
import use_case.OpenLoginSpotify.OpenLoginSpotifyInteractor;
import use_case.OpenLoginSpotify.OpenLoginSpotifyOutputBoundary;
import use_case.OpenLoginSpotify.OpenLoginSpotifyOutputData;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URI;

import org.junit.Test;
import static org.junit.Assert.*;


public class OpenLoginSpotifyAndLogInSpotifyTest { //testing use cases together as they will always be used together.
    Account testAccount = new Account();
    SpotifyDataAccessObject dataAccessObject = new SpotifyDataAccessObject();
    OpenLoginSpotifyOutputBoundary openLoginSpotifyPresenterSpoof = new OpenLoginSpotifyPresenterSpoof();
    OpenLoginSpotifyInputBoundary openLoginSpotifyInteractor = new OpenLoginSpotifyInteractor(openLoginSpotifyPresenterSpoof,dataAccessObject);
    OpenSpotifyLoginController openLoginSpotifyController = new OpenSpotifyLoginController(openLoginSpotifyInteractor);

    LogInSpotifyOutputBoundary logInSpotifyPresenter = new LogInSpotifyPresenterSpoof();
    LogInSpotifyInputBoundary logInSpotifyInteractor = new LogInSpotifyInteractor(logInSpotifyPresenter,dataAccessObject);
    LogInSpotifyControllerSpoof logInSpotifyController = new LogInSpotifyControllerSpoof(logInSpotifyInteractor);
    @Test
    public void testOpenLoginSpotifyAndLogInSpotify(){
        openLoginSpotifyController.execute();//no input needed

        logInSpotifyController.execute();//is spoofed

        assertTrue(!testAccount.isMusicServiceEmpty());
    }

    private class OpenLoginSpotifyPresenterSpoof implements OpenLoginSpotifyOutputBoundary{

        @Override
        public void openLoginCallbackURL(OpenLoginSpotifyOutputData data) {
            try {
                Desktop.getDesktop().browse(URI.create(data.getCallback_url()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    private class LogInSpotifyPresenterSpoof implements LogInSpotifyOutputBoundary{

        @Override
        public void prepareSuccessView() {

        }

        @Override
        public void prepareFailView() {
            System.err.println("failed");
        }
    }
    private class LogInSpotifyControllerSpoof extends LogInSpotifyController{

        public LogInSpotifyControllerSpoof(LogInSpotifyInputBoundary interactor) {
            super(interactor);
        }

        public void execute() {
            String code = JOptionPane.showInputDialog("enter redirect url please (with ctrl+v)");
            code = code.substring(47);
            super.execute(code, testAccount);
        }
    }
}

