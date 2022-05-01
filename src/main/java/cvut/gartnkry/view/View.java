package cvut.gartnkry.view;

import cvut.gartnkry.Settings;
import cvut.gartnkry.model.Model;
import cvut.gartnkry.model.Prop;
import cvut.gartnkry.model.Sprite;
import cvut.gartnkry.model.entities.Entity;
import cvut.gartnkry.model.items.Item;
import cvut.gartnkry.model.map.Tile;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.LinkedList;
import java.util.List;

import static cvut.gartnkry.Settings.*;

/**
 * Render game in current model state:
 * 1. draw background
 * 2. draw tiles
 * 3. draw entities (items, player, enemies)
 */
public class View {
    private final Stage stage;
    private Canvas canvas;
    private final Model model;
    private InventoryUI inventoryUI;

    public static final int pixelTileSize = 16 * Settings.SCALE;

    private double playerScreenX;
    private double playerScreenY;
    private Point2D camera;

    public View(Stage stage, Model model) {
        this.stage = stage;
        this.model = model;
    }

    public void initialization() {
        // Compute screen size
        int screenWidth = pixelTileSize * Settings.TILES_COUNT_WIDTH;
        int screenHeight = pixelTileSize * Settings.TILES_COUNT_HEIGHT;

        canvas = new Canvas(screenWidth, screenHeight); // container for all drawing components
        Pane pane = new Pane(canvas); // for layout with absolute positions
        Scene scene = new Scene(pane);

        // player in the middle of the screen
        Image playerImage = model.getPlayer().getSprite().getImage();
        playerScreenX = screenWidth / 2 - playerImage.getWidth() / 2;
        playerScreenY = screenHeight / 2 - playerImage.getHeight() / 2;

        drawBackground(canvas.getGraphicsContext2D());

        // stage = window
        stage.setScene(scene);
        stage.setTitle(Settings.TITLE);
        stage.centerOnScreen();
        stage.setResizable(false);
        stage.show();

        inventoryUI = new InventoryUI(stage, model);
        updateActiveProps();
    }

    public void updateActiveProps() {
        // top left corner
        camera = new Point2D(model.getPlayer().getSprite().getXCenter() - stage.getWidth() / 2,
                model.getPlayer().getSprite().getYCenter() - stage.getHeight() / 2);

        Bounds activeBounds = new Rectangle(camera.getX(), camera.getY(),
                stage.getWidth(), stage.getHeight()).getBoundsInParent();
        setActiveOnProps(model.getProps(), activeBounds);
        //setActiveOnProps(model.get, activeBounds);
    }

    private void setActiveOnProps(List<Prop> props, Bounds activeBounds) {
        for (Prop p : props) {
            p.setActive(activeBounds.intersects(p.getSprite().getImageRect().getBoundsInParent()));
        }
    }

    public void render() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        drawBackground(gc);

        drawTiles(gc);

        for (Item item : model.getItems()) {
            drawSprite(gc, item.getProp().getSprite());
        }

        LinkedList<Prop> frontProps = new LinkedList<>();
        Rectangle screenRec = new Rectangle(camera.getX(), camera.getY(), stage.getWidth(), stage.getHeight());
        for (Prop prop : model.getProps()) {
            // on screen?
            if (prop.isActive()) {
                // check for props in front of player
                if (prop.getHitboxRec().getY() > model.getPlayer().getHitboxRec().getY() + HITBOX_PADDING) {
                    frontProps.add(prop);
                } else {
                    drawSprite(gc, prop.getSprite());
                }
            }
        }

        // Draw player
        gc.drawImage(model.getPlayer().getSprite().getImage(), playerScreenX, playerScreenY);

        // Draw remaining props
        for (Prop prop : frontProps) {
            drawSprite(gc, prop.getSprite());
        }

        drawEntities(gc);

        if (DRAW_HITBOXES) {
            drawHitboxes(gc);
        }

        inventoryUI.draw(gc);
    }

    // Draw tiles depending on player position - camera
    private void drawTiles(GraphicsContext gc) {
        Tile[][] tileMap = model.getMap().getTileMap();

        // inspiration for implementing camera:
        // https://www.javacodegeeks.com/2013/01/writing-a-tile-engine-in-javafx.html

        // index of the first tile to show
        int startX = (int) (camera.getX() / pixelTileSize);
        int startY = (int) (camera.getY() / pixelTileSize);

        // offset of a tile in pixels
        int offsetX = (int) (camera.getX() % pixelTileSize);
        int offsetY = (int) (camera.getY() % pixelTileSize);

        // draw visible tiles
        for (int i = 0; i < Settings.TILES_COUNT_HEIGHT + 1; i++) {
            int iIndex = i + startY;
            if (iIndex >= 0 && iIndex < tileMap.length) {
                for (int j = 0; j < Settings.TILES_COUNT_WIDTH + 1; j++) {
                    int jIndex = j + startX;
                    if (jIndex >= 0 && jIndex < tileMap[0].length) {
                        gc.drawImage(tileMap[iIndex][jIndex].getImage(), j * pixelTileSize - offsetX, i * pixelTileSize - offsetY);
                    }
                }
            }
        }
    }

    private void drawEntities(GraphicsContext gc) {
        for (Entity entity : model.getEntities()) {
            drawSprite(gc, entity.getSprite());
        }
    }


    private void drawSprite(GraphicsContext gc, Sprite sprite) {
        gc.drawImage(sprite.getImage(), sprite.getX() - camera.getX(), sprite.getY() - camera.getY());
    }

    private void drawBackground(GraphicsContext gc) {
        gc.setFill(Color.rgb(29, 22, 7));
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    private void drawHitboxes(GraphicsContext gc) {
        Color color = new Color(1, 0.3, 0.3, 0.7);
        gc.setFill(color);
        Rectangle rec = model.getPlayer().getHitboxRec();
        gc.fillRect(rec.getX() - camera.getX(), rec.getY() - camera.getY(), rec.getWidth(), rec.getHeight());

        for (Prop prop : model.getProps()) {
            if (prop.isActive() && ((rec = prop.getHitboxRec()) != null)) {
                gc.fillRect(rec.getX() - camera.getX(), rec.getY() - camera.getY(), rec.getWidth(), rec.getHeight());
            }
        }
    }

}
