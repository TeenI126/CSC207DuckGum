package use_case.AddSong;

public interface AddSongOutputBoundary {

    void prepareSuccessView(AddSongOutputData addSongOutputData);

    void prepareFailView(String error);


}
