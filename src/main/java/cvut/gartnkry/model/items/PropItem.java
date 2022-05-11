package cvut.gartnkry.model.items;

import com.google.gson.JsonObject;
import cvut.gartnkry.control.files.ResourcesUtils;
import cvut.gartnkry.model.Prop;
import cvut.gartnkry.view.assets.AssetsManager;

import java.util.Random;

/**
 * Class for having an item as a prop on the ground.
 * Inherits Prop and has Item object in composition.
 */
public class PropItem extends Prop {
    private Item item;
    private int counter;
    private int add = 1;
    private final int max;

    /**
     * Class constructor
     * @param data JsonObject with Prop and item data
     */
    public PropItem(JsonObject data) {
        super(data);
        item = (Item) ResourcesUtils.loadReflection(data, "items");
        max = new Random().nextInt(10) + 40;
    }

    /**
     * Animate item's movement on the ground
     * - motion up and down.
     */
    public void update() {
        if (active) {
            counter = ++counter % max;
            if (counter == 0) {
                // Move up / down
                sprite.addXYScaled(0, add);
                add *= -1;
            }
        }
    }

    /**
     * Set new to be in this position instead of the old one.
     * Used when picking up / dropping items.
     * @param newItem item to be set on the ground
     */
    public void setNewItem(Item newItem) {
        item = newItem;
        name = newItem.getName();
        sprite.setImage(item.getImage());
        hitboxInfo = AssetsManager.getHitboxInfo(item.getName());
    }
    public Item getItem() {
        return item;
    }
}
