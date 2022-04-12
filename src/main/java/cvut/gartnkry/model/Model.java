package cvut.gartnkry.model;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import cvut.gartnkry.Data;
import cvut.gartnkry.model.entities.Entity;
import cvut.gartnkry.model.entities.Ghost;
import cvut.gartnkry.model.entities.player.Bullet;
import cvut.gartnkry.model.entities.player.Player;

import java.util.ArrayList;
import java.util.LinkedList;

public class Model {
    private Player player;
    private final LinkedList<Entity> enemies;
    private final ArrayList<Prop> props;
    private final Map map;

    public Model(Data data) {
        enemies = new LinkedList<>();
        // iterate and initialize all entities
        // (loaded from json save file)
        for(JsonElement element : data.getEntitiesData()){
            JsonObject entity = element.getAsJsonObject();
            // TODO: more effectively?
            switch (entity.get("name").getAsString()){
                case "player":
                    player = new Player(entity);
                    break;
                case "ghost":
                    enemies.add(new Ghost(entity));
                    break;
            }
        }
        map = new Map(data.getMapFilename());

        JsonArray propsData = data.getPropsData();
        props = new ArrayList<Prop>(propsData.size());
        for (int i = 0; i < propsData.size(); i++) {
            props.add(i, new Prop(propsData.get(i).getAsJsonObject()));
        }
    }

    public void update(){
        player.update();
        for (Entity enemy : enemies){
            enemy.update();
        }
        // detect collision for bullets, enemies, props ...
        LinkedList<Bullet> bullets = player.getBullets();
    }

    public Player getPlayer() {
        return player;
    }

    public LinkedList<Entity> getEnemies(){
        return enemies;
    }

    public Map getMap() {
        return map;
    }

    public ArrayList<Prop> getProps(){
        return props;
    }
}
