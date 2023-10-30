package use_case.TransferPlaylist;

public class TransferPlaylistInteractor implements TransferPlaylistInputBoundary{
    final TransferPlaylistDataAccessInterface playlistDataAccessInterface;
    final TransferPlaylistOutputBoundary playlistPresenter;

    public TransferPlaylistInteractor(TransferPlaylistDataAccessInterface playlistDataAccessInterface,
                                      TransferPlaylistOutputBoundary playlistOutputBoundary){
        this.playlistDataAccessObject = playlistDataAccessInterface;
        this.playlistPresenter = playlistOutputBoundary;
    }

    public void execute(TransferPlaylistInputData playlistInputData){

    }
}


