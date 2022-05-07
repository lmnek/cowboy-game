package cvut.gartnkry.model.items;

import com.google.gson.JsonArray;
import cvut.gartnkry.control.ResourcesUtils;
import cvut.gartnkry.Settings;
import cvut.gartnkry.view.UI;
import cvut.gartnkry.view.assets.PlayerAnimation;

public class Inventory {
    private final Item[] items;
    private int selectedIndex;
    private int hatCount;

    public Inventory(JsonArray json) {
        items = new Item[Settings.INVENTORY_SIZE];
        selectedIndex = 0;
        for (int i = 0; i < json.size(); i++) {
            addItem((Item) ResourcesUtils.loadReflection(json.get(i).getAsJsonObject(), "items"), i);
        }
        PlayerAnimation.setGunSelected(items[selectedIndex].is(Gun.class));
    }


    public void pickup(PropItem propItem) {
        dropItem(items[selectedIndex]);
        addItem(propItem.getItem(), selectedIndex);
        PlayerAnimation.setGunSelected(items[selectedIndex].is(Gun.class));
        UI.getInstance().drawInventoryItems(this);
    }


    private void addItem(Item item, int idx) {
        items[idx] = item;
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
        return items[selectedIndex] != null && items[selectedIndex].is(Gun.class);
    }

    public int size() {
        return items.length;
    }

    public Item[] getItems() {
        return items;
    }

    public Item getSelectedItem() {
        return items[selectedIndex];
    }

    public void selectNextItem() {
        selectItem(selectedIndex + 1);
    }

    public void selectPreviousItem() {
        selectItem(selectedIndex - 1);
    }

    private void selectItem(int index) {
        int tmp = selectedIndex;
        selectedIndex = Math.floorMod(index, items.length);
        UI.getInstance().newSelectedItem(tmp, selectedIndex);
        PlayerAnimation.setGunSelected(false);
        PlayerAnimation.setGunSelected(items[selectedIndex] != null && items[selectedIndex].is(Gun.class));
    }

    public boolean hasHat() {
        return hatCount != 0;
    }

    public void useSelectedItem() {
        if (items[selectedIndex] != null) {
            if(items[selectedIndex].use()){
                items[selectedIndex] = null;
                UI.getInstance().drawInventoryItems(this);
            }
        }
    }
}
