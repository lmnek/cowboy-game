package cvut.gartnkry.view;

import cvut.gartnkry.control.ResourcesUtils;
import cvut.gartnkry.model.Model;
import cvut.gartnkry.model.entities.Player;
import cvut.gartnkry.model.items.Inventory;
import cvut.gartnkry.model.items.Item;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import static cvut.gartnkry.Settings.SCALE;

public class UI {
    private static UI instance = new UI();
    private Canvas canvas;
    private Rectangle[] inventoryRecs;

    private final Image fullHeart;
    private final Image emptyHeart;

    private double inventoryX;
    private final double HEARTS_GAP = 4 * SCALE;
    private final double HEARTS_BETWEEN_GAP = 1 * SCALE;
    private final double INVENTORY_GAP = 3 * SCALE;
    private final double INVENTORY_BOX_SIZE = View.pixelTileSize + SCALE + SCALE;

    private final Color selectedCol = Color.color(0.5, 0.5, 0.5, 0.9);
    private final Color notSelectedCol = Color.color(0.3,0.3,0.3, 0.9);

    public UI() {
        fullHeart = ResourcesUtils.loadAsset("Inventory/heart");
        emptyHeart = ResourcesUtils.loadAsset("Inventory/heart2");
    }

    public static UI getInstance(){
        return instance;
    }

    public void initialize(StackPane pane, int inventorySize) {
        final double inventoryEndX = pane.getWidth() - INVENTORY_GAP;

        inventoryRecs = new Rectangle[inventorySize];
        for (int i = 0; i < inventorySize; i++) {
            Rectangle rec = getInventoryRec();
            pane.getChildren().add(rec);
            pane.setAlignment(rec, Pos.TOP_LEFT);
            rec.setTranslateX(inventoryEndX - (inventorySize - i) * INVENTORY_BOX_SIZE);
            rec.setTranslateY(INVENTORY_GAP);
            inventoryRecs[i] = rec;
        }
        inventoryX = inventoryEndX - inventorySize * INVENTORY_BOX_SIZE;

        canvas = new Canvas(pane.getWidth(), pane.getHeight());
        pane.getChildren().add(canvas);
        instance.redraw();
    }

    public void redraw(){
        instance.drawInventoryItems(Model.getInstance().getPlayer().getInventory());
        instance.drawHearts(Model.getInstance().getPlayer());
        instance.newSelectedItem(0, 0);
    }

    private Rectangle getInventoryRec() {
        Rectangle rec = new Rectangle(INVENTORY_BOX_SIZE, INVENTORY_BOX_SIZE);
        rec.setArcHeight(SCALE);
        rec.setArcWidth(SCALE);
        rec.setFill(notSelectedCol);
        rec.setStroke(Color.color(0.1,0.15,0.1));
        rec.setStrokeWidth(SCALE);
        return rec;
    }

    public void newSelectedItem(int prevIndex, int newIndex) {
        inventoryRecs[prevIndex].setFill(notSelectedCol);
        inventoryRecs[newIndex].setFill(selectedCol);
    }

    public void drawInventoryItems(Inventory inventory) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        Item[] items = inventory.getItems();
        gc.clearRect(inventoryX, INVENTORY_GAP, items.length * INVENTORY_BOX_SIZE, INVENTORY_BOX_SIZE);
        for (int i = 0; i < items.length; i++) {
            if (items[i] != null) {
                Image image = items[i].getImage();
                gc.drawImage(image, inventoryX + (i * INVENTORY_BOX_SIZE) + INVENTORY_BOX_SIZE / 2 - image.getWidth() / 2,
                        INVENTORY_GAP + INVENTORY_BOX_SIZE / 2 - image.getHeight() / 2);
            }
        }
    }

    public void drawHearts(Player player) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, HEARTS_GAP * player.getMaxHealth() + fullHeart.getWidth() * player.getMaxHealth(),
                HEARTS_GAP + fullHeart.getHeight());
        double x = HEARTS_GAP;
        for (int i = 0; i < player.getMaxHealth(); i++, x += fullHeart.getWidth() + HEARTS_BETWEEN_GAP) {
            gc.drawImage(i >= player.getHealth() ? emptyHeart : fullHeart, x, HEARTS_GAP);
        }
    }
}
