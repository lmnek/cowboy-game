package cvut.gartnkry.model.entities;

import com.google.gson.JsonObject;
import cvut.gartnkry.control.Settings;
import cvut.gartnkry.model.Model;
import cvut.gartnkry.model.Sprite;
import cvut.gartnkry.view.assets.Animation;
import cvut.gartnkry.view.assets.AssetsManager;

import java.util.Random;

public class Ghost extends Entity {
    private final Random random;

    public Ghost(JsonObject entityData) {
        super(entityData, Animation.GHOST.getFrame(0));
        damage = AssetsManager.getDamage(name);
        random = new Random();
    }

    public void update() {
        Sprite playerSprite = Model.getInstance().getPlayer().getSprite();
        double vecX = playerSprite.getX() - sprite.getX();
        double vecY = playerSprite.getY() - sprite.getY();
        double norm = Math.sqrt(vecX * vecX + vecY * vecY);
        sprite.addXY(Settings.MAX_GHOST_VELOCITY * vecX / norm, Settings.MAX_GHOST_VELOCITY * vecY / norm);
    }
}
