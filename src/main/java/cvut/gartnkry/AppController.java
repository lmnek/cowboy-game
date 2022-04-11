package cvut.gartnkry;

import cvut.gartnkry.model.Model;
import cvut.gartnkry.view.View;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * JavaFX App
 */
public class AppController extends Application {

    private View view;
    private Model model;

    private static final Logger LOGGER = Logger.getLogger(AppController.class.getName());

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) {
        //loadLoggerProperties();
        LOGGER.info("Start loading/initializing the game.");

        Data data = new Data("save1.json");
        model = new Model(data);
        view = new View(stage, model);
        view.initialization();

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
                    lastUpdate = now;
                }
            }
        };

        loopTimer.start();
        LOGGER.info("Game loop started.");
    }


    private void setEvents(Stage stage) {
        Scene scene = stage.getScene();

        // player movement - W A S D
        scene.setOnKeyPressed(event -> model.getPlayer().onKeyPressed(event.getCode()));
        scene.setOnKeyReleased(event -> model.getPlayer().onKeyReleased(event.getCode()));

        stage.setOnCloseRequest(t -> {
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