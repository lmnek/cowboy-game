package cvut.gartnkry.control;

import cvut.gartnkry.AppController;
import cvut.gartnkry.model.Model;
import cvut.gartnkry.model.items.Inventory;
import cvut.gartnkry.model.items.Item;
import cvut.gartnkry.model.items.PropItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.HashMap;

/**
 * Class for handling keyboard inputs.
 * W, A, S, D and Left, right, up, down keys are stored in hashmap with state whether they are pressed.
 * Other keys are only handled as single time actions - key typing.
 */
public class KeysEventHandler {
    private static final HashMap<KeyCode, Boolean> pressedKeys = new HashMap<>();

    /**
     * Set state as pressed in hashmap for this key.
     * @param event KeyEvent
     */
    public static void onKeyPressed(KeyEvent event) {
        pressedKeys.put(event.getCode(), true);
    }

    /**
     * Set state as not pressed in hashmap for this key.
     * @param event KeyEvent
     */
    public static void onKeyReleased(KeyEvent event) {
        pressedKeys.put(event.getCode(), false);
    }

    /**
     * Get values of keycodes (1 if pressed, 0 if not pressed)
     * and subtract them.
     * @param keyCode1 positive keycode
     * @param keyCode2 negative keycode
     * @return direction as int (1/-1/0)
     */
    public static int getDirection(KeyCode keyCode1, KeyCode keyCode2) {
        return getDirection(keyCode1) - getDirection(keyCode2);
    }

    private static int getDirection(KeyCode keyCode) {
        return (pressedKeys.containsKey(keyCode) && pressedKeys.get(keyCode) ? 1 : 0);
    }

    /**
     * Handle key typed by according action.
     * @param event KeyEvent
     */
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
                PropItem propItem = AppController.getCollisionManager().getCollidedItem();
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
        }
    }

}
