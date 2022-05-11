package cvut.gartnkry.view.assets;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import cvut.gartnkry.control.files.ResourcesUtils;
import cvut.gartnkry.control.Settings;
import cvut.gartnkry.control.collisions.HitboxInfo;
import cvut.gartnkry.model.map.Tile;
import javafx.scene.image.Image;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * Class for loading and managing assets/asset information.
 * Firstly statically loads "assets_data.json" from which it gets information of all tile types
 * and props/items/entities hitboxes.
 * Also loads needed images which can be retrieved by get methods by their name.
 */
public class AssetsManager {
    private static LinkedList<Tile> tileTypes;
    private final static JsonArray propsData;
    private final static JsonArray entitiesData;

    private static HashMap<String, Image> images;

    // Load all asset's info, tile types and images.
    static {
        JsonObject assetsData = ResourcesUtils.readJsonFile("assets_data.json").getAsJsonObject();
        loadTileTypes(assetsData.get("tiles").getAsJsonArray());
        entitiesData = assetsData.get("entities").getAsJsonArray();
        propsData = assetsData.get("props").getAsJsonArray();

        loadImages("Props", propsData);
    }

    /**
     * Get Image according to the name.
     * @param name name of the image
     * @return Image object
     */
    public static Image getImage(String name) {
        return images.get(name);
    }

    /**
     * Iterate props and entities data and return hitbox info.
     * @param name name of the prop/entity
     * @return HitboxInfo object corresponding to the name
     */
    public static HitboxInfo getHitboxInfo(String name) {
        HitboxInfo hbInfo = getHitboxInfoFromJson(propsData, name, "hitbox");
        return hbInfo == null ? getHitboxInfoFromJson(entitiesData, name, "hitbox") : hbInfo;
    }

    /**
     * Iterate props and entities data and return entity hitbox info.
     * @param name name of the prop/entity
     * @return HitboxInfo object corresponding to the name
     */
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

    /**
     * Get entity damage.
     * @param name Name of the entity
     * @return damage as an int corresponding to the name
     */
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
     * The Tile is then used to construct in-game Map.
     *
     * @param code code of the Tile
     * @return Tile corresponding to the code
     */
    public static Tile getTile(String code) {
        return tileTypes.stream().filter(t -> t.getCode().equals(code)).findFirst().orElse(null);
    }

    /**
     * Get Tile from all tile types from the given name
     * @param name name of the Tile
     * @return Tile corresponding to the name
     */
    public static Tile getTileFromName(String name) {
        return tileTypes.stream().filter(t -> t.getName().equals(name)).findFirst().orElse(null);
    }


    private static void loadImages(String folder, JsonArray data) {
        images = new HashMap<>();
        for (JsonElement el : data) {
            String name = el.getAsJsonObject().get("name").getAsString();
            images.put(name, ResourcesUtils.loadAsset(folder + "/" + name));
        }
    }

    private static void loadTileTypes(JsonArray json) {
        tileTypes = new LinkedList<>();
        // Loop each tile
        json.forEach(_t -> {
                    // parse tile
                    JsonObject t = _t.getAsJsonObject();
                    String code = t.get("code").getAsString();
                    String filename = t.get("filename").getAsString();
                    boolean hasHitbox = t.get("hasHitbox").getAsBoolean();
                    tileTypes.add(new Tile(filename, code, hasHitbox));
                }
        );
    }
}
