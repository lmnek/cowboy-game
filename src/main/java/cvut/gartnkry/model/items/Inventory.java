package cvut.gartnkry.model.items;

import com.google.gson.JsonArray;
import cvut.gartnkry.control.files.ResourcesUtils;
import cvut.gartnkry.control.Settings;
import cvut.gartnkry.view.UI;
import cvut.gartnkry.view.assets.PlayerAnimation;

import java.util.ArrayList;

/**
 * Class representing array of items as a player's inventory.
 * Has methods for operating with items in the inventory.
 */
public class Inventory {
    private final ArrayList<Item> items;
    private int selectedIndex;
    private int hatCount;

    /**
     * Class constructor.
     * Load all item's and save them in this inventory.
     *
     * @param json JsonArray with items in player's inventory
     */
    public Inventory(JsonArray json) {
        // Create array list
        items = new ArrayList<>(Settings.INVENTORY_SIZE);
        for (int i = 0; i < Settings.INVENTORY_SIZE; i++) {
            items.add(i, null);
        }
        selectedIndex = 0;
        // Fill array list with items
        for (int i = 0; i < json.size(); i++) {
            Item item = (Item) ResourcesUtils.loadReflection(json.get(i).getAsJsonObject(), "items");
            addItem(item, i);
        }
        PlayerAnimation.setGunSelected(items.get(selectedIndex) != null && items.get(selectedIndex).is(Gun.class));
    }


    /**
     * Add item to the inventory to the current selected position. If another item was there, drop it.
     *
     * @param propItem PropItem for picking up
     */
    public void pickup(PropItem propItem) {
        dropItem(items.get(selectedIndex));
        addItem(propItem.getItem(), selectedIndex);
        PlayerAnimation.setGunSelected(items.get(selectedIndex).is(Gun.class));
        UI.getInstance().drawInventoryItems(this);
    }

    /**
     * Call use() on the selected item.
     * Remove it if it supposed to be after its usage.
     */
    public void useSelectedItem() {
        Item selItem = items.get(selectedIndex);
        if (selItem != null) {
            if (items.get(selectedIndex).use()) {
                items.set(selectedIndex, null);
                UI.getInstance().drawInventoryItems(this);
            }
        }
    }

    private void addItem(Item item, int idx) {
        items.set(idx, item);
        if (item.is(Hat.class)) {
            ++hatCount;
        }
    }

    private void dropItem(Item item) {
        if (item != null) {
            if (item.is(Hat.class)) {
                --hatCount;
            }
        }
    }

    private void selectItem(int index) {
        selectedIndex = Math.floorMod(index, items.size());
        UI.getInstance().newSelectedItem(selectedIndex);
        PlayerAnimation.setGunSelected(false);
        PlayerAnimation.setGunSelected(items.get(selectedIndex) != null && items.get(selectedIndex).is(Gun.class));
    }

    public void selectNextItem() {
        selectItem(selectedIndex + 1);
    }

    public void selectPreviousItem() {
        selectItem(selectedIndex - 1);
    }

    public boolean gunSelected() {
        return items.get(selectedIndex) != null && items.get(selectedIndex).is(Gun.class);
    }

    public int size() {
        return items.size();
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public Item getSelectedItem() {
        return items.get(selectedIndex);
    }

    public boolean hasHat() {
        return hatCount != 0;
    }

}
