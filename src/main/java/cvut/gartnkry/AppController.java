package cvut.gartnkry;

import cvut.gartnkry.model.Model;
import cvut.gartnkry.view.View;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class AppController extends Application {

    private View view;
    private Model model;

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) {

        Data data = new Data("none.json");
        model = new Model(data);
        view = new View(stage, model);
        view.initialization();

        // Event handlers
        Scene scene = view.getStage().getScene();
        scene.setOnKeyPressed(event -> model.getPlayer().onKeyPressed(event.getCode()));
        scene.setOnKeyReleased(event -> model.getPlayer().onKeyReleased(event.getCode()));


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
    }
}