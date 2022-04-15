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
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.LinkedList;

import static cvut.gartnkry.Settings.HITBOX_PADDING;

public class Model {
    private Player player;
    private final LinkedList<Entity> enemies;
    private final ArrayList<Prop> props;
    private final Map map;

    public Model(Data data) {
        enemies = new LinkedList<>();
        // iterate and initialize all entities
        // (loaded from json save file)
        for (JsonElement element : data.getEntitiesData()) {
            JsonObject entity = element.getAsJsonObject();
            // TODO: more effectively?
            switch (entity.get("name").getAsString()) {
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

    public void update() {
        for (Entity enemy : enemies) {
            //enemy.update();
        }

        // detect collision for bullets, enemies, props ...
        handlePlayerCollision();

        player.update();

//        System.out.println(player.getSprite().getXCenter()  + "   " + player.getSprite().getYCenter());
        LinkedList<Bullet> bullets = player.getBullets();
    }

    private void handlePlayerCollision() {
        player.computeVelocities();
        double velocityX = player.getVelocityX();
        double velocityY = player.getVelocityY();

        HitboxInfo hbInfo = player.getHitboxInfo();
        for (Prop p : props) {
            Rectangle pRec = p.getHitboxRect();
            if (pRec != null) {
                Rectangle xRec = player.getHitboxRect(velocityX, 0);
                Rectangle yRec = player.getHitboxRect(0, velocityY);
                boolean collidedX = xRec.getBoundsInParent().intersects(pRec.getBoundsInParent());
                boolean collidedY = yRec.getBoundsInParent().intersects(pRec.getBoundsInParent());
                if (collidedX && collidedY) {
                    double diffX, diffY;
                    if (velocityX > 0) {
                        diffX = xRec.getX() - pRec.getX();
                    } else {
                        diffX = pRec.getX() + pRec.getWidth() - xRec.getX();
                    }
                    if (velocityY > 0) {
                        diffY = xRec.getY() - pRec.getY();
                    } else {
                        diffY = pRec.getY() + pRec.getHeight() - xRec.getY();
                    }
                    if (diffX > diffY) {
                        velocityY = checkY(pRec, velocityY, false, hbInfo);
                        velocityX = checkX(pRec, velocityX, true, hbInfo);
                    } else {
                        velocityX = checkX(pRec, velocityX, false, hbInfo);
                        velocityY = checkY(pRec, velocityY, true, hbInfo);
                    }
                } else {
                    if (collidedX) {
                        velocityX = checkX(pRec, velocityX, false, hbInfo);
                    }
                    if (collidedY) {
                        velocityY = checkY(pRec, velocityY, false, hbInfo);
                    }
                }
            }
        }
        player.setVelocityX(velocityX);
        player.setVelocityY(velocityY);
    }

    public double checkX(Rectangle rec, double velocity, boolean compute, HitboxInfo hbInfo) {
        if (!compute || player.getHitboxRect(velocity, 0).getBoundsInParent().intersects(rec.getBoundsInParent())) {
            if (velocity > 0) {
                player.getSprite().setX(rec.getX() - hbInfo.getX() - hbInfo.getWidth() - HITBOX_PADDING);
            } else if (velocity < 0) {
                player.getSprite().setX(rec.getX() + rec.getWidth() - hbInfo.getX() + HITBOX_PADDING);
            }
            velocity = 0;
        }
        return velocity;
    }

    public double checkY(Rectangle rec, double velocity, boolean compute, HitboxInfo hbInfo) {
        if (!compute || player.getHitboxRect(0, velocity).getBoundsInParent().intersects(rec.getBoundsInParent())) {
            if (velocity > 0) {
                player.getSprite().setY(rec.getY() - hbInfo.getY() - hbInfo.getHeight() - HITBOX_PADDING);
            } else if (velocity < 0) {
                player.getSprite().setY(rec.getY() + rec.getHeight() - hbInfo.getY() + HITBOX_PADDING);
            }
            velocity = 0;
        }
        return velocity;
    }

    public Player getPlayer() {
        return player;
    }

    public LinkedList<Entity> getEnemies() {
        return enemies;
    }

    public Map getMap() {
        return map;
    }

    public ArrayList<Prop> getProps() {
        return props;
    }
}
