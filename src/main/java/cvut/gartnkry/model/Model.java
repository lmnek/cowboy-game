package cvut.gartnkry.model;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import cvut.gartnkry.Data;
import cvut.gartnkry.ResourcesUtils;
import cvut.gartnkry.model.collisions.CollisionManager;
import cvut.gartnkry.model.entities.Entity;
import cvut.gartnkry.model.items.Item;
import cvut.gartnkry.model.map.Map;
import cvut.gartnkry.model.entities.Bullet;
import cvut.gartnkry.model.entities.Player;

import java.util.ArrayList;
import java.util.LinkedList;

public class Model {
    private Player player;
    private final ArrayList<Entity> entities;
    private final ArrayList<Prop> props;
    private final ArrayList<Item> items;
    private final Map map;
    private final CollisionManager collisionManager;

    public Model(Data data) {
        map = new Map(data.getMapFilename());

        entities = new ArrayList<>();
        // iterate and initialize all entities
        // (loaded from json save file)
        for (JsonElement element : data.getArrayData("entities")) {
            Entity entity = (Entity) ResourcesUtils.loadReflection(element.getAsJsonObject(), "entities");
            if (entity.getClass() == Player.class) {
                player = (Player) entity;
            } else {
                entities.add(entity);
            }
        }

        JsonArray propsData = data.getArrayData("props");
        props = new ArrayList<>(propsData.size());
        for (int i = 0; i < propsData.size(); i++) {
            JsonObject prop = propsData.get(i).getAsJsonObject();
            props.add(i, new Prop(prop));
        }

        JsonArray itemsData = data.getArrayData("items");
        items = new ArrayList<>(itemsData.size());
        for (int i = 0; i < itemsData.size(); i++) {
            items.add(i, (Item) ResourcesUtils.loadReflection(itemsData.get(i).getAsJsonObject(), "items"));
        }

        collisionManager = new CollisionManager(this);
    }

    public void update() {
        for (Entity enemy : entities) {
            //enemy.update();
        }
        collisionManager.handlePlayerCollisions();
        player.update();

        LinkedList<Bullet> bullets = player.getBullets();
    }

    public Player getPlayer() {
        return player;
    }

    public ArrayList<Entity> getEntities() {
        return entities;
    }

    public Map getMap() {
        return map;
    }

    public ArrayList<Prop> getProps() {
        return props;
    }

    public ArrayList<Item> getItems() {
        return items;
    }
}
