package cvut.gartnkry;

import cvut.gartnkry.control.AppLogger;
import cvut.gartnkry.control.files.JsonData;
import cvut.gartnkry.control.KeysEventHandler;
import cvut.gartnkry.control.Settings;
import cvut.gartnkry.control.collisions.CollisionManager;
import cvut.gartnkry.model.Model;
import cvut.gartnkry.view.UI;
import cvut.gartnkry.view.View;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class AppController extends Application {

    private View view;
    private JsonData data;

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) {
        AppLogger.init();
        AppLogger.info(() -> "Start loading/initializing the game.");

        data = new JsonData("save2.json");
        Model.getInstance().initialize(data);
        view = new View(stage);
        CollisionManager.initialize();
        setEvents(stage);

        AnimationTimer loopTimer = new AnimationTimer() {
            private long lastUpdate = 0;

            // game loop
            @Override
            public void handle(long now) {
                if (now - lastUpdate >= Settings.LOOP_INTERVAL) {
                    // Render and draw graphics
                    view.render();
                    CollisionManager.handleCollisions();
                    // Set states
                    Model.getInstance().update();
                    updateActiveProps();
                    lastUpdate = now;
                }
            }
        };
        loopTimer.start();
        AppLogger.info(() -> "Game loop started.");
    }

    public void updateActiveProps() {
        Bounds activeBounds = view.getScreenBounds();
        Model model = Model.getInstance();
        model.getProps().forEach(p -> p.setActive(activeBounds.intersects(p.getSprite().getImageRect().getBoundsInParent())));
        model.getEntities().forEach(e -> e.setActive(activeBounds.intersects(e.getSprite().getImageRect().getBoundsInParent())));
        model.getVoids().forEach(v -> v.setActive(activeBounds.intersects(v.getSprite().getImageRect().getBoundsInParent())));

        model.getBullets().removeIf(b -> !activeBounds.intersects(b.getRectangle().getBoundsInParent()));
        AppLogger.finer(() -> "Active bullets count: " + model.getBullets().size());
    }

    public static void reloadGame() {
        Model.getInstance().reinitialize();
        UI.getInstance().redraw();
        CollisionManager.initialize();
    }


    private void setEvents(Stage stage) {
        // player movement - W A S D
        stage.getScene().setOnKeyPressed(KeysEventHandler::onKeyPressed);
        stage.getScene().setOnKeyReleased(KeysEventHandler::onKeyReleased);
        stage.getScene().setOnKeyTyped(KeysEventHandler::onKeyTyped);

        stage.setOnCloseRequest(t -> {
            data.saveJson();
            Platform.exit();
            System.exit(0);
        });
    }
}