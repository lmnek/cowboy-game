package cvut.gartnkry.control;

import cvut.gartnkry.AppController;
import cvut.gartnkry.AppLogger;
import cvut.gartnkry.control.collisions.CollisionManager;
import cvut.gartnkry.model.Model;
import cvut.gartnkry.model.items.Item;
import cvut.gartnkry.model.items.PropItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.HashMap;
import java.util.logging.Logger;


public class KeysEventHandler {
    private static final HashMap<KeyCode, Boolean> pressedKeys = new HashMap<>();

    public static void onKeyPressed(KeyEvent event) {
        pressedKeys.put(event.getCode(), true);
    }

    public static void onKeyReleased(KeyEvent event) {
        pressedKeys.put(event.getCode(), false);
    }

    public static int getDirection(KeyCode keyCode) {
        return (pressedKeys.containsKey(keyCode) && pressedKeys.get(keyCode) ? 1 : 0);
    }

    public static int getDirection(KeyCode keyCode1, KeyCode keyCode2) {
        return getDirection(keyCode1) - getDirection(keyCode2);
    }

    public static void onKeyTyped(KeyEvent event) {
        switch (event.getCharacter().toUpperCase()) {
            case "E":
                AppLogger.info(() -> "E - select previous item");
                Model.getInstance().getPlayer().getInventory().selectNextItem();
                break;
            case "Q":
                AppLogger.info(() -> "Q - select next item");
                Model.getInstance().getPlayer().getInventory().selectPreviousItem();
                break;
            case "F":
                AppLogger.info(() -> "F - pick up item");
                PropItem propItem = CollisionManager.getCollidedItem();
                if (propItem != null) {
                    Item tmp = Model.getInstance().getPlayer().getInventory().getSelectedItem();
                    Model.getInstance().getPlayer().pickupItem(propItem);
                    if (tmp != null) {
                        propItem.setNewItem(tmp);
                    } else {
                        Model.getInstance().getProps().remove(propItem);
                    }
                }
                break;
            case "C":
                AppLogger.info(() -> "C - use selected item");
                Model.getInstance().getPlayer().getInventory().useSelectedItem();
                break;
            case "R":
                AppLogger.info(() -> "R - reload game");
                AppController.reloadGame();
                break;
        }
    }

}
