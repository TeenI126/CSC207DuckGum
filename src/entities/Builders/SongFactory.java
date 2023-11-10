package entities.Builders;

import entities.Song;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SongFactory {
    public static Song songFromSpotifyTrackJSONObject(JSONObject trackJSONObject){
        String id;
        List<String> artists = new ArrayList<String>();
        String name;
        String album;

        id = trackJSONObject.getJSONObject("external_ids").getString("isrc");

        JSONArray artistsRaw = trackJSONObject.getJSONArray("artists");
        for (int i = 0; i < artistsRaw.length(); i++){
            artists.add(artistsRaw.getJSONObject(i).getString("name"));
        }

        name = trackJSONObject.getString("name");
        album = trackJSONObject.getJSONObject("album").getString("name");

        return new Song(id,artists,name,album);
    }
}
