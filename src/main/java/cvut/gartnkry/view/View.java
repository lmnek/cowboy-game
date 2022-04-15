package cvut.gartnkry.view;

import cvut.gartnkry.Settings;
import cvut.gartnkry.model.Model;
import cvut.gartnkry.model.Prop;
import cvut.gartnkry.model.Sprite;
import cvut.gartnkry.model.entities.Entity;
import cvut.gartnkry.model.map.Tile;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class View {

    private final Stage stage;
    private Canvas canvas;
    private final Model model;

    public static final int pixelTileSize = 16 * Settings.SCALE;

    private double playerScreenX;
    private double playerScreenY;

    public View(Stage stage, Model model) {
        this.stage = stage;
        this.model = model;
    }

    private double widthHalf;
    private double heightHalf;

    public void initialization() {
        // Compute screen size
        int screenWidth = pixelTileSize * Settings.TILES_COUNT_WIDTH;
        int screenHeight = pixelTileSize * Settings.TILES_COUNT_HEIGHT;

        canvas = new Canvas(screenWidth, screenHeight); // container for all drawing components
        Pane pane = new Pane(canvas); // for layout with absolute positions
        Scene scene = new Scene(pane);

        widthHalf = 0;
        heightHalf =0;
        // player in the middle of the screen
        Image playerImage = model.getPlayer().getSprite().getImage();
        playerScreenX = screenWidth / 2 - playerImage.getWidth() / 2;
        playerScreenY =  screenHeight / 2 - playerImage.getHeight() / 2;

        drawBackground(canvas.getGraphicsContext2D());

        // stage = window
        stage.setScene(scene);
        stage.setTitle(Settings.TITLE);
        stage.centerOnScreen();
        stage.setResizable(false);
        stage.show();
    }

    /**
     * Render game in current model state:
     * 1. draw background
     * 2. draw tiles
     * 3. draw entities (items, player, enemies)
     */
    public void render() {
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // top left corner
        Sprite playerSprite = model.getPlayer().getSprite();
        double cameraX = playerSprite.getXCenter() - stage.getWidth() / 2;
        double cameraY = playerSprite.getYCenter() - stage.getHeight() / 2;

        drawTiles(gc, cameraX, cameraY);
        drawProps(gc, cameraX, cameraY);
        drawEntities(gc, cameraX, cameraY);

        drawHitboxes(gc, cameraX, cameraY);
    }

    // Draw tiles depending on player position - camera
    private void drawTiles(GraphicsContext gc, double cameraX, double cameraY) {
        Tile[][] tileMap = model.getMap().getTileMap();

        // inspiration for implementing camera:
        // https://www.javacodegeeks.com/2013/01/writing-a-tile-engine-in-javafx.html

        // index of the first tile to show
        int startX = (int) (cameraX / pixelTileSize);
        int startY = (int) (cameraY / pixelTileSize);

        // offset of a tile in pixels
        int offsetX = (int) (cameraX % pixelTileSize);
        int offsetY = (int) (cameraY % pixelTileSize);

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

    private void drawProps(GraphicsContext gc, double cameraX, double cameraY) {
        for (Prop prop : model.getProps()) {
            drawSprite(gc, prop.getSprite(), cameraX, cameraY);
        }
    }

    private void drawEntities(GraphicsContext gc, double cameraX, double cameraY) {
        drawPlayer(gc);
        drawEnemies(gc, cameraX, cameraY);
    }

    private void drawPlayer(GraphicsContext gc) {
        // image -> resources
        Sprite sprite = model.getPlayer().getSprite();
        gc.drawImage(sprite.getImage(), playerScreenX, playerScreenY);
    }

    private void drawEnemies(GraphicsContext gc, double cameraX, double cameraY) {
        for (Entity entity : model.getEnemies()) {
            drawSprite(gc, entity.getSprite(), cameraX, cameraY);
        }
    }

    private void drawBackground(GraphicsContext gc) {
        gc.setFill(Color.rgb(29, 22, 7));
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    private void drawSprite(GraphicsContext gc, Sprite sprite, double cameraX, double cameraY) {
        // TODO: load only when on screen
        gc.drawImage(sprite.getImage(), sprite.getX() - cameraX, sprite.getY() - cameraY);
    }


    private void drawHitboxes(GraphicsContext gc, double cameraX, double cameraY) {
        Color color = new Color(1, 0.3, 0.3, 0.7);
        gc.setFill(color);
        drawRectangle(gc, model.getPlayer().getHitboxRec(), cameraX, cameraY);

        for (Prop prop : model.getProps()) {
            drawRectangle(gc, prop.getHitboxRec(), cameraX, cameraY);
        }
    }

    private void drawRectangle(GraphicsContext gc, Rectangle rec, double cameraX, double cameraY) {
        if (rec != null) {
            gc.fillRect(rec.getX() - cameraX, rec.getY() - cameraY, rec.getWidth(), rec.getHeight());
        }
    }
}
