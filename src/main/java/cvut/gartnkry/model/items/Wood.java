package cvut.gartnkry.model.items;

import com.google.gson.JsonObject;
import cvut.gartnkry.model.Model;
import cvut.gartnkry.model.map.Tile;
import cvut.gartnkry.view.assets.Animation;
import cvut.gartnkry.view.assets.AssetsManager;
import javafx.scene.shape.Rectangle;

import java.util.LinkedList;

import static cvut.gartnkry.view.View.pixelTileSize;

public class Wood extends Item {
    public Wood(JsonObject json) {
        super(json);
    }

    @Override
    public void parseJson(JsonObject json) {

    }

    @Override
    public boolean use() {
        Rectangle hitboxRec = Model.getInstance().getPlayer().getHitboxRec();
        int idxX = (int) (hitboxRec.getX() / pixelTileSize);
        int idxY = (int) (hitboxRec.getY() / pixelTileSize);
        System.out.println(idxX + " " + idxY);
        int incX, incY;
        if (hitboxRec.getX() - idxX * pixelTileSize > (idxX + 1) * pixelTileSize - hitboxRec.getWidth() - hitboxRec.getX()) {
            incX = 1;
        } else {
            incX = -1;
        }
        if (hitboxRec.getY() - idxY * pixelTileSize > (idxY + 1) * pixelTileSize - hitboxRec.getHeight() - hitboxRec.getY()) {
            incY = 1;
        } else {
            incY = -1;
        }
        if (Model.getInstance().getMap().buildBridge(idxX, idxY, incX, 0)) {
            Model.getInstance().getMap().buildBridge(idxX, idxY, 0, incY);
        }

        return false;
    }
}
