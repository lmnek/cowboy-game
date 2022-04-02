package cvut.gartnkry.view.assets;

import java.io.*;
import java.util.HashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class TileManager {

    // many thanks to:
    // https://stackoverflow.com/questions/8811815/is-it-possible-to-assign-numeric-value-to-an-enum-in-java
    // @Michael Sims <333
    private static final HashMap<String, Tile> TileMap;

    static {
        TileMap = new HashMap<>();

        // Load and parse JSON file with tiles info
        try {
            // Load
            BufferedReader br = AssetsUtils.getRecourcesReader("/Tiles/tiles.json");
            // Parse
            JSONArray tileTypes = (JSONArray) new JSONParser().parse(br);
            br.close();

            tileTypes.forEach(_t -> {
                        JSONObject t = (JSONObject) _t;
                        // parse tile
                        String code = (String) t.get("code");
                        String filename = (String) t.get("filename");
                        boolean hasHitbox = (boolean) t.get("hasHitbox");
                        // add tile to hashmap
                        TileMap.put(code, new Tile(filename, hasHitbox));
                    }
            );
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public static Tile getTile(String code) {
        return TileMap.get(code);
    }
}
