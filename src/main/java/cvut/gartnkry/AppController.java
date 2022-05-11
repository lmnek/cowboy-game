package cvut.gartnkry;

import cvut.gartnkry.control.AppLogger;
import cvut.gartnkry.control.files.JsonData;
import cvut.gartnkry.control.KeysEventHandler;
import cvut.gartnkry.control.Settings;
import cvut.gartnkry.control.collisions.CollisionManager;
import cvut.gartnkry.model.Model;
import cvut.gartnkry.view.UI;
import cvut.gartnkry.view.View;
import cvut.gartnkry.view.assets.StepSoundsPlayer;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.stage.Stage;

/**
 * JavaFX App.
 * Main class that is responsible for communication between all parts of the game.
 * It sets up the main game loop and other threads.
 */
public class AppController extends Application {
    private View view;
    private JsonData data;

    /**
     * Main class that launches javafx app.
     */
    public static void main(String[] args) {
        launch();
    }

    /**
     * Javafx start method that sets up the game and its objects.
     */
    @Override
    public void start(Stage stage) {
        AppLogger.init();
        AppLogger.info(() -> "Start loading/initializing the game.");

        data = new JsonData(Settings.SAVE_FILENAME);
        Model.getInstance().initialize(data);
        view = new View(stage);
        CollisionManager.initialize();
        setEvents(stage);

        Thread soundThread = new Thread(StepSoundsPlayer.getInstance());
        soundThread.start();

        // Set game loop
        AnimationTimer loopTimer = new AnimationTimer() {
            private long lastUpdate = 0;

            @Override
            public void handle(long now) {
                if (now - lastUpdate >= Settings.LOOP_INTERVAL) {
                    view.render();
                    CollisionManager.handleCollisions();
                    Model.getInstance().update();
                    updateActiveProps();
                    lastUpdate = now;
                }
            }
        };
        loopTimer.start();
        AppLogger.info(() -> "Game loop started.");
    }

    /**
     * Update active state on all game objects
     * - depends on whether they are on screen.
     * Nonactive objects don't need to be drawn and be checked for collisions.
     */
    public void updateActiveProps() {
        Bounds activeBounds = view.getScreenBounds();
        Model model = Model.getInstance();
        // update active objects
        model.getProps().forEach(p -> p.setActive(activeBounds.intersects(p.getSprite().getImageRect().getBoundsInParent())));
        model.getEntities().forEach(e -> e.setActive(activeBounds.intersects(e.getSprite().getImageRect().getBoundsInParent())));
        model.getVoids().forEach(v -> v.setActive(activeBounds.intersects(v.getSprite().getImageRect().getBoundsInParent())));

        // remove bullets that are not on screen
        model.getBullets().removeIf(b -> !activeBounds.intersects(b.getRectangle().getBoundsInParent()));
        AppLogger.finer(() -> "Active bullets count: " + model.getBullets().size());
    }

    /**
     * Reinitialize objects when restarting the game.
     */
    public static void reloadGame() {
        Model.getInstance().reinitialize();
        UI.getInstance().redraw();
        CollisionManager.initialize();
    }

    /**
     * Set all required events.
     */
    private void setEvents(Stage stage) {
        // player movement - W A S D
        stage.getScene().setOnKeyPressed(KeysEventHandler::onKeyPressed);
        stage.getScene().setOnKeyReleased(KeysEventHandler::onKeyReleased);
        stage.getScene().setOnKeyTyped(KeysEventHandler::onKeyTyped);

        stage.setOnCloseRequest(t -> {
            data.saveJson();
            Model.getInstance().getMap().saveMap();
            StepSoundsPlayer.shutdown();
            AppLogger.info(() -> "The game was closed.");
            Platform.exit();
            System.exit(0);
        });
    }

}
