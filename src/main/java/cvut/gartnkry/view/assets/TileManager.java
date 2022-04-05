package cvut.gartnkry.view.assets;

import java.io.*;
import java.util.HashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

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
            BufferedReader br = AssetsUtils.getResourcesReader("/Maps/Tiles/tiles.json");
            // Parse
            JSONArray tileTypes = (JSONArray) new JSONParser().parse(br);
            br.close();

            // Loop each tile
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
