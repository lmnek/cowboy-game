package cvut.gartnkry.model;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import cvut.gartnkry.Data;
import cvut.gartnkry.model.entities.Entity;
import cvut.gartnkry.model.entities.Ghost;
import cvut.gartnkry.model.shooting.Bullet;
import cvut.gartnkry.model.entities.Player;
import cvut.gartnkry.view.assets.AssetsManager;
import javafx.geometry.Point2D;

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
        props = new ArrayList<>(propsData.size());
        for (int i = 0; i < propsData.size(); i++) {
            JsonObject prop = propsData.get(i).getAsJsonObject();
            props.add(i, new Prop(prop, AssetsManager.getImage(prop.get("name").getAsString())));
        }
    }

    public void update(){
        for (Entity enemy : enemies){
            //enemy.update();
        }

        // detect collision for bullets, enemies, props ...
        handlePlayerCollision();

        player.update();

//        System.out.println(player.getSprite().getXCenter()  + "   " + player.getSprite().getYCenter());
        LinkedList<Bullet> bullets = player.getBullets();
    }

    private void handlePlayerCollision() {
        Point2D velocity = player.getVelocity();
        for (Prop p: props) {
            //if(p.)
        }
        player.setVelocity(velocity);
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
