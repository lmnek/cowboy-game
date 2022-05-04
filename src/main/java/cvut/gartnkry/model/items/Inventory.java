package cvut.gartnkry.model.items;

import com.google.gson.JsonArray;
import cvut.gartnkry.control.ResourcesUtils;
import cvut.gartnkry.Settings;
import cvut.gartnkry.model.Prop;
import cvut.gartnkry.view.UI;
import cvut.gartnkry.view.assets.PlayerAnimation;

import java.util.ArrayList;

public class Inventory {
    private final Item[] items;
    private int selectedIndex;

    public Inventory(JsonArray json) {
        items = new Item[Settings.INVENTORY_SIZE];
        selectedIndex = 0;

        for (int i = 0; i < json.size(); i++) {
            items[i] = (Item) ResourcesUtils.loadReflection(json.get(i).getAsJsonObject(), "items");
        }
        PlayerAnimation.setGunSelected(gunSelected());
    }

    public void pickup(PropItem propItem) {
        items[selectedIndex] = propItem.getItem();
    }

    public boolean gunSelected() {
        return items[selectedIndex].getClass() == Gun.class;
    }

    public int size() {
        return items.length;
    }

    public Item[] getItems() {
        return items;
    }

    public Item getSelectedItem(){
        return items[selectedIndex];
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public void selectNextItem() {
        selectItem(selectedIndex + 1);
    }

    public void selectPreviousItem() {
        selectItem(selectedIndex - 1);
    }

    private void selectItem(int index){
        int tmp = selectedIndex;
        selectedIndex = Math.floorMod(index, items.length);
        UI.getInstance().newSelectedItem(tmp, selectedIndex);
    }
}
