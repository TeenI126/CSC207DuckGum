package use_case.TransferPlaylist;

public class TransferPlaylistInputData {

    final private Playlist playlist;
    final private Account fromAccount;
    final private Account toAccount;

    public TransferPlaylistInputData(Playlist playlist, Account fromAccount, Account toAccount) {
        this.playlist = playlist;
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
    }

    Playlist getPlaylist() {return playlist; }

    Account getFromAccount() {return fromAccount; }
    Account getToAccount() {return toAccount; }

}
