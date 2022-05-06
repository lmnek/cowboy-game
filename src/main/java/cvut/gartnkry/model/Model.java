package cvut.gartnkry.model;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import cvut.gartnkry.Data;
import cvut.gartnkry.control.ResourcesUtils;
import cvut.gartnkry.control.collisions.CollisionManager;
import cvut.gartnkry.model.entities.Bullet;
import cvut.gartnkry.model.entities.Entity;
import cvut.gartnkry.model.items.PropItem;
import cvut.gartnkry.model.map.Map;
import cvut.gartnkry.model.entities.Player;
import java.util.ArrayList;

public class Model {
    private Player player;
    private final ArrayList<Entity> entities;
    private final ArrayList<Prop> props;
    private final Map map;

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
        JsonArray itemsData = data.getArrayData("items");
        props = new ArrayList<>();
        for (int i = 0; i < propsData.size(); i++) {
            props.add(new Prop(propsData.get(i).getAsJsonObject()));
        }
        for (int i = 0; i < itemsData.size(); i++) {
            props.add(new PropItem(itemsData.get(i).getAsJsonObject()));
        }
    }

    public void update() {
        for (Entity enemy : entities) {
            //enemy.update();
        }
        CollisionManager.handlePlayerCollisions();
        player.update();

        player.getBullets().forEach(Bullet::update);
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
}
