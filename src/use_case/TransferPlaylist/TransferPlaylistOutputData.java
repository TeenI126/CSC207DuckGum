package use_case.TransferPlaylist;

public class TransferPlaylistOutputData {
    private boolean success;
    private String message;

    public TransferPlaylistOutputData(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    // Getters
    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }

    // Setters if needed
}