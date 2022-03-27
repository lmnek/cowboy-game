package cvut.gartnkry.model;

import cvut.gartnkry.Data;
import cvut.gartnkry.model.entities.Player;
import cvut.gartnkry.view.Images;
import javafx.scene.image.Image;

public class Model {
    private final Player player;

    public Model(Data data) {
        this.player = new Player(data, new Sprite(Images.PLAYER, data.getPlayerCoords()));
    }

    public void update(){
        player.update();
    }

    public Player getPlayer() {
        return player;
    }
}
