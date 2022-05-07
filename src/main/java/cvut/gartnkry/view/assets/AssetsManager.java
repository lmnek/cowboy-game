package cvut.gartnkry.view.assets;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import cvut.gartnkry.control.ResourcesUtils;
import cvut.gartnkry.Settings;
import cvut.gartnkry.control.collisions.HitboxInfo;
import cvut.gartnkry.model.map.Tile;
import javafx.scene.image.Image;

import java.util.HashMap;

/**
 * Class for loading and managing Tiles.
 * Firstly statically loads "assets_data.json" from which it gets filename of Tiles assets, code
 * and hitbox value. The images are then loaded and hashmap of all Tiles is constructed.
 * This hashmap has code of the tile as a key and Tile as a value.
 */
public class AssetsManager {

    private static HashMap<String, Tile> tileMap;
    private static JsonArray propsData;
    private static JsonArray entitiesData;

    private static HashMap<String, Image> images;

    /**
     * Load all Tiles and store them in the hashmap
     */
    static {
        JsonObject assetsData = ResourcesUtils.readJsonFile("assets_data.json").getAsJsonObject();
        loadTileMap(assetsData.get("tiles").getAsJsonArray());
        entitiesData = assetsData.get("entities").getAsJsonArray();
        propsData = assetsData.get("props").getAsJsonArray();

        loadImages("Props", propsData);
    }

    private static void loadImages(String folder, JsonArray data) {
        images = new HashMap<>();
        for (JsonElement el : data) {
            String name = el.getAsJsonObject().get("name").getAsString();
            images.put(name, ResourcesUtils.loadAsset(folder + "/" + name));
        }
    }

    private static void loadTileMap(JsonArray tileTypes) {
        AssetsManager.tileMap = new HashMap<>();
        // Loop each tile
        tileTypes.forEach(_t -> {
                    JsonObject t = _t.getAsJsonObject();
                    // parse tile
                    String code = t.get("code").getAsString();
                    String filename = t.get("filename").getAsString();
                    boolean hasHitbox = t.get("hasHitbox").getAsBoolean();
                    // add tile to hashmap
                    AssetsManager.tileMap.put(code, new Tile(filename, hasHitbox));
                }
        );
    }

    public static Image getImage(String name) {
        return images.get(name);
    }

    public static HitboxInfo getHitboxInfo(String name) {
        HitboxInfo hbInfo = getHitboxInfoFromJson(propsData, name, "hitbox");
        return hbInfo == null ? getHitboxInfoFromJson(entitiesData, name, "hitbox") : hbInfo;
    }

    public static HitboxInfo getEntityHitboxInfo(String name) {
        return getHitboxInfoFromJson(entitiesData, name, "entityHitbox");
    }

    private static HitboxInfo getHitboxInfoFromJson(JsonArray data, String name, String hitboxName) {
        for (JsonElement en : data) {
            if (en.getAsJsonObject().get("name").getAsString().equals(name)) {
                JsonElement hitboxElement = en.getAsJsonObject().get(hitboxName);
                if (hitboxElement != null) {
                    JsonObject hitboxData = hitboxElement.getAsJsonObject();
                    return new HitboxInfo(hitboxData.get("x").getAsInt() * Settings.SCALE,
                            hitboxData.get("y").getAsInt() * Settings.SCALE,
                            hitboxData.get("width").getAsInt() * Settings.SCALE,
                            hitboxData.get("height").getAsInt() * Settings.SCALE);
                }
            }
        }
        return null;
    }

    public static int getDamage(String name) {
        for (JsonElement en : entitiesData) {
            if (en.getAsJsonObject().get("name").getAsString().equals(name)) {
                return en.getAsJsonObject().get("damage").getAsInt();
            }
        }
        return 0;
    }

    /**
     * This method is usually used to get Tile
     * from tile code used in a CSV map file.
     * This Tile is then used to construct in-game Map.
     *
     * @param code code of the Tile
     * @return Tile corresponding to the code
     */
    public static Tile getTile(String code) {
        return tileMap.get(code);
    }

    public static Tile getTileFromName(String name) {
        for (Tile t : tileMap.values()) {
            if (t.getName().equals(name)){
                return t;
            }
        }
        return null;
    }
}
