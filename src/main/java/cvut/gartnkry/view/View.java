package cvut.gartnkry.view;

import cvut.gartnkry.Settings;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class View {

    private final Stage stage;
    private Canvas canvas;

    public View(Stage stage) {
        this.stage = stage;
    }

    public void init(){
        canvas = new Canvas(); // container for all contents
        Pane pane = new Pane(canvas); // for layout with absolute positions
        Scene scene = new Scene(pane);

        // stage = window
        stage.setScene(scene);
        stage.setTitle(Settings.TITLE);
        stage.centerOnScreen();
        stage.setResizable(false);
        stage.show();
    }

    public void render(){
        GraphicsContext gc = canvas.getGraphicsContext2D();
    }
}
