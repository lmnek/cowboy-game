package cvut.gartnkry.control.files;

import com.google.gson.*;
import cvut.gartnkry.control.AppLogger;
import cvut.gartnkry.model.Model;
import cvut.gartnkry.model.Prop;
import cvut.gartnkry.model.entities.Entity;
import cvut.gartnkry.model.entities.Void;
import cvut.gartnkry.model.items.Bottle;
import cvut.gartnkry.model.items.Gun;
import cvut.gartnkry.model.items.Item;
import cvut.gartnkry.model.items.PropItem;

import java.io.*;

import static cvut.gartnkry.control.Settings.SCALE;


/**
 * Class for loading and saving saves as JSON file
 */
public class JsonData {
    private JsonObject json;
    private final String filename;

    /**
     * Class constructor.
     *
     * @param filename name of the save in Json format
     */
    public JsonData(String filename) {
        this.filename = filename;
        loadSave(filename);
    }


    /**
     * Get Json array from Json save file.
     * e.g. props, entities, ...
     *
     * @param dataName name of array in Json
     * @return JsonArray object
     */
    public JsonArray getArrayData(String dataName) {
        return json.get(dataName).getAsJsonArray();
    }

    public String getMapFilename() {
        return json.get("map").getAsString();
    }

    private void loadSave(String filename) {
        AppLogger.fine(() -> "Loading JSON save file: " + filename);
        JsonParser parser = new JsonParser();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            json = parser.parse(br).getAsJsonObject();
        } catch (IOException e) {
            AppLogger.exception(() -> "Loading JSON save file failed: " + filename, e);
        }
    }

    private void addItemToJson(JsonObject item, Item i) {
        if (i.getClass().equals(Gun.class)) {
            Gun gun = (Gun) i;
            item.addProperty("bulletVelocity", gun.getBulletVelocity() / SCALE);
            item.addProperty("fireRate", gun.getFireRate());
            item.addProperty("damage", gun.getDamage());
        } else if (i.getClass().equals(Bottle.class)) {
            item.addProperty("heal", ((Bottle) i).getHeal());
        }
    }

    private JsonObject entityToJson(Entity e) {
        JsonObject obj = propToJson(e);
        obj.addProperty("health", e.getHealth());
        return obj;
    }

    private JsonObject propToJson(Prop p) {
        JsonObject obj = new JsonObject();
        obj.addProperty("name", p.getName());
        obj.addProperty("x", p.getSprite().getX() / SCALE);
        obj.addProperty("y", p.getSprite().getY() / SCALE);
        return obj;
    }

    /**
     * Save current game Model to the same Json file as it was loaded int.
     */
    public void saveJson() {
        Model model = Model.getInstance();
        JsonObject obj = new JsonObject();
        obj.addProperty("map", model.getMap().getFilename());

        // Add player data
        JsonObject playerObj = entityToJson(model.getPlayer());
        JsonArray inventory = new JsonArray();
        model.getPlayer().getInventory().getItems().forEach(i -> {
            if (i != null) {
                JsonObject item = new JsonObject();
                item.addProperty("name", i.getName());
                addItemToJson(item, i);
                inventory.add(item);
            }
        });
        playerObj.add("inventory", inventory);
        playerObj.addProperty("maxHealth", model.getPlayer().getMaxHealth());

        // Add entities data
        JsonArray entitiesArr = new JsonArray();
        entitiesArr.add(playerObj);
        model.getEntities().forEach(e -> {
            JsonObject _obj = entityToJson(e);
            if (e.getClass().equals(Void.class)) {
                _obj.addProperty("activated", ((Void) e).isActivated());
            }
            entitiesArr.add(_obj);
        });
        model.getVoids().forEach(v -> {
            JsonObject _obj = entityToJson(v);
            _obj.addProperty("activated", false);
            entitiesArr.add(_obj);
        });
        obj.add("entities", entitiesArr);

        // Add props data
        JsonArray propsArr = new JsonArray();
        JsonArray itemsArr = new JsonArray();
        model.getProps().forEach(p -> {
            JsonObject _obj = propToJson(p);
            if (p.getClass().equals(PropItem.class)) {
                addItemToJson(_obj, ((PropItem) p).getItem());
                itemsArr.add(_obj);
            } else {
                propsArr.add(_obj);
            }
        });
        obj.add("props", propsArr);
        obj.add("items", itemsArr);

        // Save the file
        try (FileWriter writer = new FileWriter(filename)) {
            writer.write(new GsonBuilder().setPrettyPrinting().create().toJson(obj));
        } catch (IOException e) {
            AppLogger.exception(() -> "Failed to save a game to file: " + filename, e);
        }
    }

}
