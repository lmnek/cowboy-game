package cvut.gartnkry.model.items;

import com.google.gson.JsonObject;
import cvut.gartnkry.control.files.ResourcesUtils;
import cvut.gartnkry.model.Prop;
import cvut.gartnkry.view.assets.AssetsManager;

import java.util.Random;

public class PropItem extends Prop {
    private static Random random = new Random();
    private Item item;
    private int counter;
    private int add = 1;
    private int max;

    public PropItem(JsonObject data) {
        super(data);
        item = (Item) ResourcesUtils.loadReflection(data, "items");
        max = random.nextInt(10) + 40;
    }

    public void update() {
        if (active) {
            counter = ++counter % max;
            if (counter == 0) {
                sprite.addXYScaled(0, add);
                add *= -1;
            }
        }
    }

    public Item getItem() {
        return item;
    }

    public void setNewItem(Item newItem) {
        item = newItem;
        name = newItem.getName();
        sprite.setImage(item.getImage());
        hitboxInfo = AssetsManager.getHitboxInfo(item.getName());
    }
}
