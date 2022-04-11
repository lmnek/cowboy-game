package cvut.gartnkry.model;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import cvut.gartnkry.Data;
import cvut.gartnkry.model.entities.Entity;
import cvut.gartnkry.model.entities.Ghost;
import cvut.gartnkry.model.entities.Player;

import java.util.LinkedList;

public class Model {
    private Player player;
    private final LinkedList<Entity> enemies;
    private final Map map;

    public Model(Data data) {
        enemies = new LinkedList<>();
        // iterate and initialize all entities
        // (loaded from json save file)
        for(JsonElement element : data.getEntitiesData()){
            JsonObject entity = element.getAsJsonObject();
            // TODO: efektivneji?
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
    }

    public void update(){
        player.update();
        for (Entity en : enemies){
            en.update();
        }
    }

    public Player getPlayer() {
        return player;
    }

    public Map getMap() {
        return map;
    }
}
