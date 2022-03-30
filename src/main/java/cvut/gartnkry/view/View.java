package cvut.gartnkry.view;

import cvut.gartnkry.Settings;
import cvut.gartnkry.model.Model;
import cvut.gartnkry.model.Sprite;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class View {

    private final Stage stage;
    private Canvas canvas;
    private final Model model;

    public View(Stage stage, Model model) {
        this.stage = stage;
        this.model = model;
    }

    public void initialization(){
        // Compute screen size
        int pixelTileSize = 16 * Settings.SCALE;
        int screenWidth = pixelTileSize * Settings.TILES_COUNT_WIDTH;
        int screenHeight = pixelTileSize * Settings.TILES_COUNT_HEIGHT;

        canvas = new Canvas(screenWidth,screenHeight); // container for all drawing components
        Pane pane = new Pane(canvas); // for layout with absolute positions
        Scene scene = new Scene(pane);

        // Event handlers
        scene.setOnKeyPressed(event -> model.getPlayer().onKeyPressed(event.getCode()));
        scene.setOnKeyReleased(event -> model.getPlayer().onKeyReleased(event.getCode()));

        // stage = window
        stage.setScene(scene);
        stage.setTitle(Settings.TITLE);
        stage.centerOnScreen();
        stage.setResizable(false);
        stage.show();
    }

    public void render(){
        GraphicsContext gc = canvas.getGraphicsContext2D();
        drawTiles(gc);
        drawEntities(gc);
    }

    private void drawTiles(GraphicsContext gc) {
        gc.fillRect(0, 0, 1000, 1000);
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
}
