package cvut.gartnkry.model;

import cvut.gartnkry.Data;
import cvut.gartnkry.model.entities.Player;
import cvut.gartnkry.view.assets.Images;

public class Model {
    private final Player player;

    public Model(Data data) {
        this.player = new Player(data, new Sprite(Images.PLAYER_DEFAULT.getImage(), data.getPlayerCoords()));
    }

    public void update(){
        player.update();
    }

    public Player getPlayer() {
        return player;
    }
}
