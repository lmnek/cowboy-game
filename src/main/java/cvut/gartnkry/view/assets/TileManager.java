package cvut.gartnkry.view.assets;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.util.HashMap;

/**
 *  Class for loading and managing Tiles.
 *  Firstly statically loads "tiles.json" from which it gets filename of Tiles assets, code
 *  and hitbox value. The images are then loaded and hashmap of all Tiles is constructed.
 *  This hashmap has code of the tile as a key and Tile as a value.
 */
public class TileManager {

    private static final HashMap<String, Tile> TileMap;

    /**
     * Load all Tiles and store them in the hashmap
     */
    static {
        TileMap = new HashMap<>();

        // Load and parse JSON file with tiles info
        try {
            // Load
            BufferedReader br = AssetsUtils.getResourcesReader("Maps/Tiles/tiles.json");
            // Parse
            JsonParser parser = new JsonParser();
            JsonArray tileTypes = parser.parse(br).getAsJsonArray();
            br.close();

            // Loop each tile
            tileTypes.forEach(_t -> {
                        JsonObject t = _t.getAsJsonObject();
                        // parse tile
                        String code = t.get("code").getAsString();
                        String filename = t.get("filename").getAsString();
                        boolean hasHitbox = t.get("hasHitbox").getAsBoolean();
                        // add tile to hashmap
                        TileMap.put(code, new Tile(filename, hasHitbox));
                    }
            );
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is usually used to get Tile
     * from tile code used in a CSV map file.
     * This Tile is then used to construct in-game Map.
     * @param code code of the Tile
     * @return Tile corresponding to the code
     */
    public static Tile getTile(String code) {
        return TileMap.get(code);
    }
}
