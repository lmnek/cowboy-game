package cvut.gartnkry.model.entities;

import cvut.gartnkry.Data;
import cvut.gartnkry.Settings;
import cvut.gartnkry.model.Sprite;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;

import java.security.Key;
import java.util.HashMap;
import java.util.Map;

import static javafx.scene.input.KeyCode.*;

public class Player extends Entity {

    //private double right, left, up, down;
    private Map<KeyCode, Double> directions;

    public Player(Data data, Sprite sprite) {
        super(sprite);
        //right = left = up = down = 0;

        directions = new HashMap<>();
        directions.put(W, 0.0);
        directions.put(A, 0.0);
        directions.put(S, 0.0);
        directions.put(D, 0.0);

        //this.data = data;
    }

    public void onKeyPressed(KeyCode code) {
       // setMovement(code, 1);
        directions.replace(code, 1.0);
    }

    public void onKeyReleased(KeyCode code) {
       // setMovement(code, 0);
        directions.replace(code, 0.0);
    }

    public void update() {
//        double velocityX = (right - left) * speed;
//        double velocityY = (down - up) * speed;
        double velocityX = (directions.get(D) - directions.get(A));
        double velocityY = (directions.get(S) - directions.get(W));

        // fixes faster movement sideways -> pythagoras theorem
        /*if(velocityX != 0 && velocityY != 0){
            velocityX = Math.signum(velocityX) * Math.sqrt(2);
            velocityY = Math.signum(velocityY) * Math.sqrt(2);
        }*/

        sprite.addXY(velocityX  * Settings.PLAYER_SPEED, velocityY  * Settings.PLAYER_SPEED);
    }

   /* @Deprecated
    private void setMovement(KeyCode code, double value){
        switch (code) {
            case W:
                up = value;
                break;
            case S:
                down = value;
                break;
            case A:
                left = value;
                break;
            case D:
                right = value;
                break;
        }
    }*/
}
