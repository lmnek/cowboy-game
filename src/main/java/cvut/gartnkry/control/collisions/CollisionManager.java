package cvut.gartnkry.control.collisions;

import cvut.gartnkry.Settings;
import cvut.gartnkry.model.Model;
import cvut.gartnkry.model.Prop;
import cvut.gartnkry.model.entities.Player;
import cvut.gartnkry.model.items.PropItem;
import cvut.gartnkry.model.map.Tile;
import cvut.gartnkry.view.UI;
import cvut.gartnkry.view.View;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

import static cvut.gartnkry.Settings.CACTUS_DAMAGE;
import static cvut.gartnkry.Settings.HITBOX_PADDING;
import static java.lang.Math.max;
import static java.lang.Math.min;

public class CollisionManager {
    private static final double PROPS_RADIUS = 50 * Settings.SCALE;
    private static final int TILES_RADIUS = 3;
    private static double velocityX;
    private static double velocityY;

    private static Model model;
    private static Tile[][] tileMap;
    private static Player player;
    private static HitboxInfo hbInfo;

    public static void initialize(Model _model) {
        model = _model;
        tileMap = model.getMap().getTileMap();
        player = model.getPlayer();
        hbInfo = model.getPlayer().getHitboxInfo();
    }

    public static void handlePlayerCollisions() {
        player.computeVelocities();
        velocityX = player.getVelocityX();
        velocityY = player.getVelocityY();

        Rectangle playerRec = player.getHitboxRec();
        Rectangle activeRec = new Rectangle(playerRec.getX() - PROPS_RADIUS / 2,
                playerRec.getY() - PROPS_RADIUS / 2, PROPS_RADIUS, PROPS_RADIUS);

        //---TILES---
        int startX = max((int) (playerRec.getX() / View.pixelTileSize) - 1, 0);
        int startY = max((int) (playerRec.getY() / View.pixelTileSize) - 1, 0);
        int endX = min(startX + TILES_RADIUS, tileMap[0].length);
        int endY = min(startY + TILES_RADIUS, tileMap.length);
        for (int x = startX; x < endX; x++) {
            for (int y = startY; y < endY; y++) {
                Tile tile = tileMap[y][x];
                if (tile.hasHitbox()) {
                    Rectangle tileRec = new Rectangle(x * View.pixelTileSize, y * View.pixelTileSize, View.pixelTileSize, View.pixelTileSize);
                    checkPlayerCollision(tileRec);
                }
            }
        }

        //---PROPS---
        ArrayList<Prop> removedProps = new ArrayList<>();
        for (Prop p : model.getProps()) {
            Rectangle pRec = p.getHitboxRec();
            // close to the player?
            if (p.isActive() && p.getClass() != PropItem.class &&
                    activeRec.getBoundsInParent().intersects(pRec.getBoundsInParent()) && checkPlayerCollision(pRec)) {
                // Player collided
                if (p.getName().equals("Cactus")) {
                    player.decreaseHealth(CACTUS_DAMAGE);
                    UI.getInstance().drawHearts(player);
                }
            }
        }
        player.setVelocityX(velocityX);
        player.setVelocityY(velocityY);
    }

    public static PropItem getCollidedItem() {
        for (Prop p : model.getProps()) {
            if (p.isActive() && p.getClass() == PropItem.class &&
                    player.getEntityHitboxRec().getBoundsInParent().intersects(p.getHitboxRec().getBoundsInParent())) {
                return (PropItem) p;
            }
        }
        return null;
    }


    public static boolean checkPlayerCollision(Rectangle cRec) {
        boolean collided = true;
        Rectangle xRec = player.getHitboxRec(velocityX, 0);
        Rectangle yRec = player.getHitboxRec(0, velocityY);
        boolean collidedX = xRec.getBoundsInParent().intersects(cRec.getBoundsInParent());
        boolean collidedY = yRec.getBoundsInParent().intersects(cRec.getBoundsInParent());
        if (collidedX && collidedY) {
            double diffX, diffY;
            if (velocityX > 0) {
                diffX = xRec.getX() - cRec.getX();
            } else {
                diffX = cRec.getX() + cRec.getWidth() - xRec.getX();
            }
            if (velocityY > 0) {
                diffY = yRec.getY() - cRec.getY();
            } else {
                diffY = cRec.getY() + cRec.getHeight() - yRec.getY();
            }
            if (diffX > diffY) {
                velocityY = checkY(cRec, velocityY, false);
                velocityX = checkX(cRec, velocityX, true);
            } else {
                velocityX = checkX(cRec, velocityX, false);
                velocityY = checkY(cRec, velocityY, true);
            }
        } else if (collidedX) {
            velocityX = checkX(cRec, velocityX, false);
        } else if (collidedY) {
            velocityY = checkY(cRec, velocityY, false);
        } else {
            collided = false;
        }
        return collided;
    }

    public static double checkX(Rectangle rec, double velocity, boolean compute) {
        if (!compute || player.getHitboxRec(velocity, 0).getBoundsInParent().intersects(rec.getBoundsInParent())) {
            if (velocity > 0) {
                player.getSprite().setX(rec.getX() - hbInfo.getX() - hbInfo.getWidth() - HITBOX_PADDING);
            } else if (velocity < 0) {
                player.getSprite().setX(rec.getX() + rec.getWidth() - hbInfo.getX() + HITBOX_PADDING);
            }
            velocity = 0;
        }
        return velocity;
    }

    public static double checkY(Rectangle rec, double velocity, boolean compute) {
        if (!compute || player.getHitboxRec(0, velocity).getBoundsInParent().intersects(rec.getBoundsInParent())) {
            if (velocity > 0) {
                player.getSprite().setY(rec.getY() - hbInfo.getY() - hbInfo.getHeight() - HITBOX_PADDING);
            } else if (velocity < 0) {
                player.getSprite().setY(rec.getY() + rec.getHeight() - hbInfo.getY() + HITBOX_PADDING);
            }
            velocity = 0;
        }
        return velocity;
    }


}
