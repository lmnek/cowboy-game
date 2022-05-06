package cvut.gartnkry.view;

import cvut.gartnkry.Settings;
import cvut.gartnkry.model.Model;
import cvut.gartnkry.model.Prop;
import cvut.gartnkry.model.Sprite;
import cvut.gartnkry.model.entities.Bullet;
import cvut.gartnkry.model.entities.Entity;
import cvut.gartnkry.model.entities.Player;
import cvut.gartnkry.model.map.Tile;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.LinkedList;

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

    public static final int pixelTileSize = 16 * Settings.SCALE;

    private double playerScreenX;
    private double playerScreenY;
    private Point2D camera;

    private final ColorAdjust colorAdjust;

    public View(Stage stage, Model model) {
        this.stage = stage;
        this.model = model;

        colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(0.3);
    }

    public void initialization() {
        // Compute screen size
        int screenWidth = pixelTileSize * Settings.TILES_COUNT_WIDTH;
        int screenHeight = pixelTileSize * Settings.TILES_COUNT_HEIGHT;

        canvas = new Canvas(screenWidth, screenHeight); // container for all drawing components
        StackPane pane = new StackPane(canvas); // for layout with absolute positions
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
        stage.sizeToScene();
        stage.show();

        setCamera();
        UI.getInstance().initialize(pane, model.getPlayer().getInventory().size());
        UI.getInstance().drawHearts(model.getPlayer());
        UI.getInstance().drawInventoryItems(model.getPlayer().getInventory());
        UI.getInstance().newSelectedItem(0, 0);
    }

    public void render() {
        setCamera();

        GraphicsContext gc = canvas.getGraphicsContext2D();
        drawBackground(gc);

        drawTiles(gc);

        LinkedList<Prop> frontProps = new LinkedList<>();
        model.getProps().forEach(prop -> {
            // on screen?
            if (prop.isActive()) {
                // check for props in front of player
                if (prop.getHitboxRec().getY() > model.getPlayer().getHitboxRec().getY() + HITBOX_PADDING) {
                    frontProps.add(prop);
                } else {
                    drawSprite(gc, prop.getSprite());
                }
            }
        });

        drawPlayer(gc);

        // Draw remaining props
        frontProps.forEach(p -> drawSprite(gc, p.getSprite()));

        drawEntities(gc);

        drawBullets(gc, model.getPlayer().getBullets());

        if (DRAW_HITBOXES) {
            drawHitboxes(gc);
        }
    }


    private void drawPlayer(GraphicsContext gc) {
        Player player = model.getPlayer();
        if (player.isInvincible()) {
            gc.setEffect(colorAdjust);
        }
        gc.drawImage(player.getSprite().getImage(), playerScreenX, playerScreenY);


        if (player.hasHat()) {
            Sprite hatSprite = player.getHatSprite();
            gc.drawImage(hatSprite.getImage(), hatSprite.getX() + playerScreenX, hatSprite.getY() + playerScreenY);
        }
        gc.setEffect(null);
    }

    private void setCamera() {
        // top left corner
        camera = new Point2D(model.getPlayer().getSprite().getXCenter() - stage.getWidth() / 2,
                model.getPlayer().getSprite().getYCenter() - stage.getHeight() / 2);
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

    private void drawBullets(GraphicsContext gc, LinkedList<Bullet> bullets) {
        gc.setFill(new Color(0, 0, 0, 1));
        bullets.forEach(b -> gc.fillRect(b.getX() - camera.getX(), b.getY() - camera.getY(), SCALE, SCALE));
    }

    private void drawBackground(GraphicsContext gc) {
        gc.setFill(Color.rgb(29, 22, 7));
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    private void drawHitboxes(GraphicsContext gc) {
        gc.setFill(new Color(1, 0.3, 0.3, 0.7));
        Rectangle rec = model.getPlayer().getHitboxRec();
        drawRectangle(gc, rec);
        for (Prop prop : model.getProps()) {
            if (prop.isActive() && ((rec = prop.getHitboxRec()) != null)) {
                drawRectangle(gc, rec);
            }
        }

        gc.setFill(new Color(0.1, 0.2, 1, 0.6));
        drawRectangle(gc, model.getPlayer().getEntityHitboxRec());
        for (Entity entity : model.getEntities()) {
            if (entity.isActive() && ((rec = entity.getEntityHitboxRec()) != null)) {
                drawRectangle(gc, rec);
            }
        }
    }

    private void drawRectangle(GraphicsContext gc, Rectangle rec) {
        gc.fillRect(rec.getX() - camera.getX(), rec.getY() - camera.getY(), rec.getWidth(), rec.getHeight());
    }

    public Point2D getCamera() {
        return camera;
    }
}
