package cvut.gartnkry;

import cvut.gartnkry.control.KeysEventHandler;
import cvut.gartnkry.control.collisions.CollisionManager;
import cvut.gartnkry.model.Model;
import cvut.gartnkry.model.Prop;
import cvut.gartnkry.view.View;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * JavaFX App
 */
public class AppController extends Application {

    private Stage stage;
    private View view;
    private Model model;

    private static final Logger LOG = Logger.getLogger(AppController.class.getName());

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) {
        this.stage = stage;
        //loadLoggerProperties();
        LOG.info("Start loading/initializing the game.");

        Data data = new Data("save1.json");
        model = new Model(data);
        view = new View(stage, model);
        view.initialization();

        CollisionManager.initialize(model);
        updateActiveProps();
        setEvents(stage);

        AnimationTimer loopTimer = new AnimationTimer() {
            private long lastUpdate = 0;

            // game loop
            @Override
            public void handle(long now) {
                if (now - lastUpdate >= Settings.LOOP_INTERVAL) {
                    // Render and draw graphics
                    view.render();
                    // Set states
                    model.update();
                    updateActiveProps();
                    lastUpdate = now;
                }
            }
        };
        loopTimer.start();
        LOG.info("Game loop started.");
    }

    public void updateActiveProps() {
        Bounds activeBounds = new Rectangle(view.getCamera().getX(), view.getCamera().getY(),
                stage.getWidth(), stage.getHeight()).getBoundsInParent();
        setActiveOnProps(model.getProps(), activeBounds);
        //setActiveOnProps(model.getEntities(), activeBounds);
    }

    private void setActiveOnProps(List<Prop> props, Bounds activeBounds) {
        for (Prop p : props) {
            p.setActive(activeBounds.intersects(p.getSprite().getImageRect().getBoundsInParent()));
        }
    }

    private void setEvents(Stage stage) {
        // player movement - W A S D
        stage.getScene().setOnKeyPressed(event -> KeysEventHandler.onKeyPressed(event));
        stage.getScene().setOnKeyReleased(event -> KeysEventHandler.onKeyReleased(event));
        stage.getScene().setOnKeyTyped(event -> KeysEventHandler.onKeyTyped(event, model));

        stage.setOnCloseRequest(t -> {
            Data.saveSave(model);
            Platform.exit();
            System.exit(0);
        });
    }

    private void loadLoggerProperties() {
        try (InputStream is = AppController.class.getClassLoader().
                getResourceAsStream("logging.properties")) {
            LogManager.getLogManager().readConfiguration(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}