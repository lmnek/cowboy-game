package cvut.gartnkry.view;

import cvut.gartnkry.ResourcesUtils;
import cvut.gartnkry.Settings;
import cvut.gartnkry.model.Model;
import cvut.gartnkry.model.entities.Player;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Set;

import static cvut.gartnkry.Settings.SCALE;

public class InventoryUI {
    private final Player player;
    private final Image inventory;
    private final double inventoryX;
    private final double inventoryY;

    private final Image fullHeart;
    private final Image emptyHeart;

    private final double HEARTS_GAP = 4 * SCALE;
    private final double HEARTS_BETWEEN_GAP = 1 * SCALE;
    private final double inventoryGap = 10 * SCALE;

    public InventoryUI(Stage stage, Model model) {
        this.player = model.getPlayer();
        inventory = ResourcesUtils.loadAsset("Inventory/inventory",  0.8);
        inventoryX = stage.getWidth() / 2 - inventory.getWidth() / 2;
        inventoryY = stage.getHeight() - inventory.getHeight() - inventoryGap;

        fullHeart = ResourcesUtils.loadAsset("Inventory/heart");
        emptyHeart = ResourcesUtils.loadAsset("Inventory/heart2");
    }

    public void draw(GraphicsContext gc) {
        drawHearts(gc);

        gc.drawImage(inventory,inventoryX, inventoryY);
    }

    private void drawHearts(GraphicsContext gc){
        double x = HEARTS_GAP;
        for (int i = 0; i < player.getMaxHealth(); i++, x += fullHeart.getWidth() + HEARTS_BETWEEN_GAP) {
            if(i >= player.getHealth()){
                gc.drawImage(emptyHeart, x, HEARTS_GAP);
            }else{
                gc.drawImage(fullHeart, x, HEARTS_GAP);
            }
        }
    }
}
