package cvut.gartnkry.model;

import cvut.gartnkry.Data;
import cvut.gartnkry.model.entities.Player;

public class Model {
    private final Player player;
    private final Map map;

    public Model(Data data) {
        player = new Player(data);
        map = new Map(data.getMapFilename());
    }

    public void update(){
        player.update();
    }

    public Player getPlayer() {
        return player;
    }

    public Map getMap() {
        return map;
    }
}
