package cvut.gartnkry.view;

import cvut.gartnkry.Settings;
import cvut.gartnkry.model.Model;
import cvut.gartnkry.model.Sprite;
import cvut.gartnkry.view.assets.Tile;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class View {

    private final Stage stage;
    private Canvas canvas;
    private final Model model;

    private static final int pixelTileSize = 16 * Settings.SCALE;

    public View(Stage stage, Model model) {
        this.stage = stage;
        this.model = model;
    }

    public void initialization(){
        // Compute screen size
        int screenWidth = pixelTileSize * Settings.TILES_COUNT_WIDTH;
        int screenHeight = pixelTileSize * Settings.TILES_COUNT_HEIGHT;

        canvas = new Canvas(screenWidth,screenHeight); // container for all drawing components
        Pane pane = new Pane(canvas); // for layout with absolute positions
        Scene scene = new Scene(pane);

        // stage = window
        stage.setScene(scene);
        stage.setTitle(Settings.TITLE);
        stage.centerOnScreen();
        stage.setResizable(false);
        stage.show();
    }

    /**
     * Render game in current model state:
     *  1. draw background
     *  2. draw tiles
     *  3. draw entities (items, player, enemies)
     */
    public void render(){
        GraphicsContext gc = canvas.getGraphicsContext2D();
        drawTiles(gc);
        drawEntities(gc);
    }

    private void drawTiles(GraphicsContext gc) {

        Tile[][] tileMap = model.getMap().getTileMap();
        int rows = tileMap.length;
        int cols = tileMap[0].length;
        for (int x = 0; x < rows; x++) {
            for (int y = 0; y < cols; y++) {
                gc.drawImage(tileMap[x][y].getImage(), y * pixelTileSize, x * pixelTileSize);
            }
        }
    }

    private void drawEntities(GraphicsContext gc) {
        drawPlayer(gc);
        drawEnemies(gc);
    }

    private void drawPlayer(GraphicsContext gc) {
        // image -> resources
        Sprite sprite = model.getPlayer().getSprite();
        gc.drawImage(sprite.getImage(), sprite.getX(), sprite.getY());
    }

    private void drawEnemies(GraphicsContext gc) {
    }

    public Stage getStage() {
        return stage;
    }
}
