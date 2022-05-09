package cvut.gartnkry.model.items;

import com.google.gson.JsonArray;
import cvut.gartnkry.control.files.ResourcesUtils;
import cvut.gartnkry.control.Settings;
import cvut.gartnkry.view.UI;
import cvut.gartnkry.view.assets.PlayerAnimation;

import java.util.ArrayList;

public class Inventory {
    private final ArrayList<Item> items;
    private int selectedIndex;
    private int hatCount;

    public Inventory(JsonArray json) {
        items = new ArrayList<>(Settings.INVENTORY_SIZE);
        for (int i = 0; i < Settings.INVENTORY_SIZE; i++) {
            items.add(i, null);
        }
        selectedIndex = 0;
        for (int i = 0; i < json.size(); i++) {
            Item item = (Item) ResourcesUtils.loadReflection(json.get(i).getAsJsonObject(), "items");
            addItem(item, i);
        }
        PlayerAnimation.setGunSelected(items.get(selectedIndex) != null && items.get(selectedIndex).is(Gun.class));
    }


    public void pickup(PropItem propItem) {
        dropItem(items.get(selectedIndex));
        addItem(propItem.getItem(), selectedIndex);
        PlayerAnimation.setGunSelected(items.get(selectedIndex).is(Gun.class));
        UI.getInstance().drawInventoryItems(this);
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

    public void selectNextItem() {
        selectItem(selectedIndex + 1);
    }

    public void selectPreviousItem() {
        selectItem(selectedIndex - 1);
    }

    private void selectItem(int index) {
        int tmp = selectedIndex;
        selectedIndex = Math.floorMod(index, items.size());
        UI.getInstance().newSelectedItem(selectedIndex);
        PlayerAnimation.setGunSelected(false);
        PlayerAnimation.setGunSelected(items.get(selectedIndex) != null && items.get(selectedIndex).is(Gun.class));
    }

    public boolean hasHat() {
        return hatCount != 0;
    }

    public void useSelectedItem() {
        if (items.get(selectedIndex) != null) {
            if (items.get(selectedIndex).use()) {
                items.set(selectedIndex, null);
                UI.getInstance().drawInventoryItems(this);
            }
        }
    }
}
