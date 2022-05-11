package cvut.gartnkry.model;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import cvut.gartnkry.control.files.JsonData;
import cvut.gartnkry.control.files.ResourcesUtils;
import cvut.gartnkry.model.entities.Bullet;
import cvut.gartnkry.model.entities.Entity;
import cvut.gartnkry.model.entities.Void;
import cvut.gartnkry.model.items.PropItem;
import cvut.gartnkry.model.map.Map;
import cvut.gartnkry.model.entities.Player;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * Model of the game is a Singleton class that contains information about all ingame objects:
 * <ul>
 * <li> Player
 * <li> Props
 * <li> Entities
 * <li> Map
 * </ul>
 * It is used for access to those objects and to call update() method on them.
 */
public class Model {
    private final static Model instance = new Model();
    private JsonData data;
    private Player player;
    private ArrayList<Entity> entities;
    private ArrayList<Prop> props;
    private LinkedList<Void> nonActiveVoids;
    private Map map;

    private Model() {
    }

    public static Model getInstance() {
        return instance;
    }

    /**
     * Initializes all objects in model from given save.
     * Should be called before using Model.
     * @param data JsonData class loaded from a save file
     */
    public void initialize(JsonData data) {
        this.data = data;
        map = new Map(data.getMapFilename());
        entities = new ArrayList<>();
        nonActiveVoids = new LinkedList<>();
        // iterate and initialize all entities
        // (loaded from json save file)
        for (JsonElement element : data.getArrayData("entities")) {
            Entity entity = (Entity) ResourcesUtils.loadReflection(element.getAsJsonObject(), "entities");
            if (Objects.requireNonNull(entity).getClass().equals(Player.class)) {
                player = (Player) entity;
            } else if (entity.getClass().equals(Void.class) && !element.getAsJsonObject().get("activated").getAsBoolean()) {
                nonActiveVoids.add((Void) entity);
            } else {
                entities.add(entity);
            }
        }

        // Add props and items on the ground to the Prop list
        JsonArray propsData = data.getArrayData("props");
        JsonArray itemsData = data.getArrayData("items");
        props = new ArrayList<>();
        for (int i = 0; i < propsData.size(); i++) {
            props.add(new Prop(propsData.get(i).getAsJsonObject()));
        }
        for (int i = 0; i < itemsData.size(); i++) {
            props.add(new PropItem(itemsData.get(i).getAsJsonObject()));
        }
    }

    /**
     * Load the Model again from the same data.
     * Used when restarting the game.
     */
    public void reinitialize() {
        initialize(data);
    }


    /**
     * Calls update() on objects stored in Model.
     * Removes dead entities.
     */
    public void update() {
        player.update();
        getBullets().forEach(Bullet::update);
        entities.removeIf(Entity::isDead);
        entities.forEach(e -> {
            if (e.isActive()) {
                e.update();
            }
        });
        props.forEach(Prop::update);
    }

    public Player getPlayer() {
        return player;
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public Map getMap() {
        return map;
    }

    public ArrayList<Prop> getProps() {
        return props;
    }

    public LinkedList<Bullet> getBullets() {
        return player.getBullets();
    }

    public LinkedList<Void> getVoids() {
        return nonActiveVoids;
    }
}
