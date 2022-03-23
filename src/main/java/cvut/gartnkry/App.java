package cvut.gartnkry;

import cvut.gartnkry.view.View;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {

    private View view;

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) {
        view = new View(stage);

        view.init();
    }



}