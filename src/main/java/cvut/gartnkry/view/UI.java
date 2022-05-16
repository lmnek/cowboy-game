package cvut.gartnkry.view;

import cvut.gartnkry.control.files.ResourcesUtils;
import cvut.gartnkry.model.Model;
import cvut.gartnkry.model.entities.Player;
import cvut.gartnkry.model.items.Inventory;
import cvut.gartnkry.model.items.Item;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import java.util.ArrayList;

import static cvut.gartnkry.control.Settings.SCALE;

/**
 * Singleton class for displaying UI on screen.
 * This includes drawing inventory, player hearts and text.
 */
public class UI {
    private final static UI instance = new UI();
    private Canvas canvas;
    private final VBox textsBox;
    private Rectangle[] inventoryRecs;
    private final Image fullHeart;
    private final Image emptyHeart;

    private double inventoryX;
    private final double HEARTS_GAP = 4 * SCALE;
    private final double HEARTS_BETWEEN_GAP = SCALE;
    private final double INVENTORY_GAP = 3 * SCALE;
    private final double INVENTORY_BOX_SIZE = View.pixelTileSize + 2 * SCALE;

    private final Color selectedCol = Color.color(0.5, 0.5, 0.5, 0.9);
    private final Color notSelectedCol = Color.color(0.3, 0.3, 0.3, 0.9);

    /**
     * Class constructor.
     * Load images and setup texts.
     */
    public UI() {
        fullHeart = ResourcesUtils.loadAsset("Inventory/heart");
        emptyHeart = ResourcesUtils.loadAsset("Inventory/heart2");

        // Death texts
        textsBox = new VBox();
        textsBox.getChildren().add(getText("YOU DIED", 18));
        textsBox.getChildren().add(getText("press 'R' to restart", 10));
        textsBox.setAlignment(Pos.CENTER);
        textsBox.setVisible(false);
    }

    public static UI getInstance() {
        return instance;
    }

    /**
     * Initialize UI, add own pane and inventory rectangles.
     * @param pane StackPane from main stage
     * @param inventorySize count of the slots in inventory
     */
    public void initialize(StackPane pane, int inventorySize) {
        final double inventoryEndX = pane.getWidth() - INVENTORY_GAP;
        inventoryRecs = new Rectangle[inventorySize];
        // Inventory rectangles
        for (int i = 0; i < inventorySize; i++) {
            Rectangle rec = getInventoryRec();
            pane.getChildren().add(rec);
            pane.setAlignment(rec, Pos.TOP_LEFT);
            rec.setTranslateX(inventoryEndX - (inventorySize - i) * INVENTORY_BOX_SIZE);
            rec.setTranslateY(INVENTORY_GAP);
            inventoryRecs[i] = rec;
        }
        inventoryX = inventoryEndX - inventorySize * INVENTORY_BOX_SIZE;

        // Another canvas
        canvas = new Canvas(pane.getWidth(), pane.getHeight());
        pane.getChildren().add(canvas);
        pane.getChildren().add(textsBox);
        instance.redraw();
    }

    /**
     * Redraw UI.
     */
    public void redraw() {
        instance.drawInventoryItems(Model.getInstance().getPlayer().getInventory());
        instance.drawHearts(Model.getInstance().getPlayer());
        instance.newSelectedItem(0);
    }

    /**
     * Change selected color in inventory slots
     * @param idx index of new selected slot
     */
    public void newSelectedItem(int idx) {
        for (Rectangle inventoryRec : inventoryRecs) {
            inventoryRec.setFill(notSelectedCol);
        }
        inventoryRecs[idx].setFill(selectedCol);
    }

    /**
     * Draw items in their supposed inventory slots.
     * @param inventory Players inventory
     */
    public void drawInventoryItems(Inventory inventory) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        ArrayList<Item> items = inventory.getItems();
        gc.clearRect(inventoryX, INVENTORY_GAP, items.size() * INVENTORY_BOX_SIZE, INVENTORY_BOX_SIZE);
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i) != null) {
                Image image = items.get(i).getImage();
                gc.drawImage(image, inventoryX + (i * INVENTORY_BOX_SIZE) + INVENTORY_BOX_SIZE / 2 - image.getWidth() / 2,
                        INVENTORY_GAP + INVENTORY_BOX_SIZE / 2 - image.getHeight() / 2);
            }
        }
    }

    /**
     * Draw hearts displaying players health
     * @param player Player object
     */
    public void drawHearts(Player player) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, HEARTS_GAP * player.getMaxHealth() + fullHeart.getWidth() * player.getMaxHealth(),
                HEARTS_GAP + fullHeart.getHeight());
        double x = HEARTS_GAP;
        for (int i = 0; i < player.getMaxHealth(); i++, x += fullHeart.getWidth() + HEARTS_BETWEEN_GAP) {
            gc.drawImage(i >= player.getHealth() ? emptyHeart : fullHeart, x, HEARTS_GAP);
        }
    }

    private Rectangle getInventoryRec() {
        Rectangle rec = new Rectangle(INVENTORY_BOX_SIZE, INVENTORY_BOX_SIZE);
        rec.setArcHeight(SCALE);
        rec.setArcWidth(SCALE);
        rec.setFill(notSelectedCol);
        rec.setStroke(Color.color(0.1, 0.15, 0.1));
        rec.setStrokeWidth(SCALE);
        return rec;
    }

    private Text getText(String text, int size) {
        Text tmpText = new Text(text);
        tmpText.setFont(ResourcesUtils.loadFont("upheavtt.ttf", size * SCALE));
        tmpText.setFill(Color.WHITE);
        tmpText.setTextAlignment(TextAlignment.CENTER);
        return tmpText;
    }

    public void showDeathMessage() {
        textsBox.setVisible(true);
    }

    public void hideDeathMessage() {
        textsBox.setVisible(false);
    }
}
