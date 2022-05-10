package cvut.gartnkry.control;

import cvut.gartnkry.AppController;
import cvut.gartnkry.control.collisions.CollisionManager;
import cvut.gartnkry.model.Model;
import cvut.gartnkry.model.Sprite;
import cvut.gartnkry.model.items.Inventory;
import cvut.gartnkry.model.items.Item;
import cvut.gartnkry.model.items.PropItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.HashMap;

import static cvut.gartnkry.control.Settings.SCALE;


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
        Inventory inventory = Model.getInstance().getPlayer().getInventory();
        switch (event.getCharacter().toUpperCase()) {
            case "E":
                AppLogger.info(() -> "Pressed E - select previous item");
                inventory.selectNextItem();
                break;
            case "Q":
                AppLogger.info(() -> "Pressed Q - select next item");
                inventory.selectPreviousItem();
                break;
            case "F":
                AppLogger.info(() -> "Pressed F - pick up item");
                PropItem propItem = CollisionManager.getCollidedItem();
                if (propItem != null) {
                    Item tmp = inventory.getSelectedItem();
                    Model.getInstance().getPlayer().pickupItem(propItem);
                    if (tmp != null) {
                        propItem.setNewItem(tmp);
                    } else {
                        Model.getInstance().getProps().remove(propItem);
                    }
                }
                break;
            case "C":
                AppLogger.info(() -> "Pressed C - use selected item");
                inventory.useSelectedItem();
                break;
            case "R":
                AppLogger.info(() -> "Pressed R - reload game");
                AppController.reloadGame();
                break;
            case "X":
                Sprite playerSprite = Model.getInstance().getPlayer().getSprite();
                AppLogger.severe(() -> "Player position: X " + playerSprite.getX() / SCALE + ", Y " + playerSprite.getY() / SCALE);
                break;
        }
    }

}
