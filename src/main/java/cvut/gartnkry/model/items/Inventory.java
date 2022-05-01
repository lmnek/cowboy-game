package cvut.gartnkry.model.items;

import com.google.gson.JsonArray;
import cvut.gartnkry.ResourcesUtils;
import cvut.gartnkry.view.assets.PlayerAnimation;

public class Inventory {
    private final Item[] items;
    private int selectedIndex;


    public Inventory(JsonArray json) {
        items = new Item[5];
        selectedIndex = 0;

        for (int i = 0; i < json.size(); i++) {
            items[i] = (Item) ResourcesUtils.loadReflection(json.get(i).getAsJsonObject(), "items");
        }
        PlayerAnimation.setGunSelected(gunSelected());
    }

    public boolean gunSelected() {
        return items[selectedIndex].getClass() == Gun.class;
    }
}
