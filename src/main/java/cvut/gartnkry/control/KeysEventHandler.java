package cvut.gartnkry.control;

import cvut.gartnkry.control.collisions.CollisionManager;
import cvut.gartnkry.model.Model;
import cvut.gartnkry.model.items.Item;
import cvut.gartnkry.model.items.PropItem;
import cvut.gartnkry.view.UI;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import java.util.HashMap;

public class KeysEventHandler {
    private static final HashMap<KeyCode, Boolean> pressedKeys = new HashMap<>();

    public static void onKeyPressed(KeyEvent event) {
        pressedKeys.put(event.getCode(), true);
    }

    public static void onKeyReleased(KeyEvent event) {
        pressedKeys.put(event.getCode(), false);
    }

    public static void onKeyTyped(KeyEvent event, Model model) {
        switch (event.getCharacter().toUpperCase()) {
            case "E":
                model.getPlayer().getInventory().selectNextItem();
                break;
            case "Q":
                model.getPlayer().getInventory().selectPreviousItem();
                break;
            case "F":
                PropItem propItem = CollisionManager.getCollidedItem();
                if (propItem != null) {
                    Item tmp = model.getPlayer().getInventory().getSelectedItem();
                    model.getPlayer().pickupItem(propItem);
                    if (tmp != null) {
                        propItem.setNewItem(tmp);
                    } else {
                        model.getProps().remove(propItem);
                    }
                    UI.getInstance().drawInventoryItems(model.getPlayer().getInventory());
                }
        }
    }

    public static boolean isKeyPressed(KeyCode keyCode) {
        return pressedKeys.containsKey(keyCode) && pressedKeys.get(keyCode);
    }

    public static int getDirection(KeyCode keyCode) {
        return (pressedKeys.containsKey(keyCode) && pressedKeys.get(keyCode) ? 1 : 0);
    }
}
