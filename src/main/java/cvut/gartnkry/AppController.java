package cvut.gartnkry;

import cvut.gartnkry.view.View;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class AppController extends Application {

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