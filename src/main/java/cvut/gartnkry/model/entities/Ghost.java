package cvut.gartnkry.model.entities;

import com.google.gson.JsonObject;
import cvut.gartnkry.view.assets.PlayerAnimation;

public class Ghost extends Entity{

    public Ghost(JsonObject entityData) {
        super(entityData, PlayerAnimation.PLAYER_DOWN.getDefaultImage());
        animation = PlayerAnimation.PLAYER_DOWN;
    }

    public void update() {

    }
}
