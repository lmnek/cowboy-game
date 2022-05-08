package cvut.gartnkry.model.items;

import com.google.gson.JsonObject;
import cvut.gartnkry.model.Model;
import cvut.gartnkry.model.map.Map;
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
        Map map = Model.getInstance().getMap();
        int idxX = (int) (hitboxRec.getX() / pixelTileSize);
        int idxY = (int) (hitboxRec.getY() / pixelTileSize);
        int incX = hitboxRec.getX() - idxX * pixelTileSize > (idxX + 1) * pixelTileSize - hitboxRec.getWidth() - hitboxRec.getX() ? 1 : -1;
        int incY = hitboxRec.getY() - idxY * pixelTileSize > (idxY + 1) * pixelTileSize - hitboxRec.getHeight() - hitboxRec.getY() ? 1 : -1;
        boolean built = map.buildBridge(idxX, idxY, incX, 0);
        if (!built) {
            built = map.buildBridge(idxX, idxY, 0, incY);
        }
        return built;
    }
}
