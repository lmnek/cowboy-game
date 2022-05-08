package cvut.gartnkry.control;

import cvut.gartnkry.AppController;
import cvut.gartnkry.control.collisions.CollisionManager;
import cvut.gartnkry.model.Model;
import cvut.gartnkry.model.items.Inventory;
import cvut.gartnkry.model.items.Item;
import cvut.gartnkry.model.items.PropItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.HashMap;
import java.util.logging.Logger;


public class KeysEventHandler{
    private static final HashMap<KeyCode, Boolean> pressedKeys = new HashMap<>();
    private static final Logger LOG = Logger.getLogger(AppController.class.getName());

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
                LOG.info("E - select previous item");
                inventory.selectNextItem();
                break;
            case "Q":
                LOG.info("Q - select next item");
                inventory.selectPreviousItem();
                break;
            case "F":
                LOG.info("F - pick up item");
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
                LOG.info("C - use selected item");
                inventory.useSelectedItem();
                break;
            case "R":
                LOG.info("R - reload game");
                AppController.reloadGame();
                break;
        }
    }

}
