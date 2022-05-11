package cvut.gartnkry.model.items;

import com.google.gson.JsonObject;
import cvut.gartnkry.model.Model;
import cvut.gartnkry.model.map.Map;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

import static cvut.gartnkry.view.View.pixelTileSize;

/**
 * Wood item. It required for building a bridge.
 */
public class Wood extends Item {

    /**
     * Class constructor
     * @param json JsonObject with item data
     */
    public Wood(JsonObject json) {
        super(json);
    }

    @Override
    public void parseJson(JsonObject json) {
    }

    /**
     * Try building a bridge across two tile long gap.
     *
     * @return whether the bridge was built ~ the wood and a plank was used
     */
    @Override
    public boolean use() {
        boolean built = false;
        ArrayList<Item> inventoryItems = Model.getInstance().getPlayer().getInventory().getItems();
        Item plankItem = inventoryItems.stream().filter(i -> i != null && i.getClass().equals(Plank.class)).findAny().orElse(null);
        // Plank item needed
        if (plankItem != null) {
            Rectangle hitboxRec = Model.getInstance().getPlayer().getHitboxRec();
            // Compute the closest tile for placing the bridge
            int idxX = (int) (hitboxRec.getX() / pixelTileSize);
            int idxY = (int) (hitboxRec.getY() / pixelTileSize);
            int incX = hitboxRec.getX() - idxX * pixelTileSize > (idxX + 1) * pixelTileSize - hitboxRec.getWidth() - hitboxRec.getX() ? 1 : -1;
            int incY = hitboxRec.getY() - idxY * pixelTileSize > (idxY + 1) * pixelTileSize - hitboxRec.getHeight() - hitboxRec.getY() ? 1 : -1;
            // Try building a bridge
            Map map = Model.getInstance().getMap();
            built = map.buildBridge(idxX, idxY, incX, 0) || map.buildBridge(idxX, idxY, 0, incY);
            if (built) {
                inventoryItems.set(inventoryItems.indexOf(plankItem), null);
            }
        }
        return built;
    }
}
