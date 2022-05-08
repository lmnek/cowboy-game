package cvut.gartnkry.control.collisions;

import cvut.gartnkry.Settings;
import cvut.gartnkry.model.Model;
import cvut.gartnkry.model.Prop;
import cvut.gartnkry.model.entities.Bullet;
import cvut.gartnkry.model.entities.Entity;
import cvut.gartnkry.model.entities.Player;
import cvut.gartnkry.model.items.PropItem;
import cvut.gartnkry.model.map.Tile;
import cvut.gartnkry.view.View;
import javafx.geometry.Bounds;
import javafx.scene.shape.Rectangle;

import java.util.LinkedList;
import java.util.List;

import static cvut.gartnkry.Settings.CACTUS_DAMAGE;
import static cvut.gartnkry.Settings.HITBOX_PADDING;
import static java.lang.Math.max;
import static java.lang.Math.min;

public class CollisionManager {
    private static final double PROPS_RADIUS = 50 * Settings.SCALE;
    private static final int TILES_RADIUS = 3;
    private static double velocityX;
    private static double velocityY;

    private static Tile[][] tileMap;
    private static Player player;
    private static HitboxInfo hbInfo;

    public static void initialize() {
        tileMap = Model.getInstance().getMap().getTileMap();
        player = Model.getInstance().getPlayer();
        hbInfo = player.getHitboxInfo();
    }

    public static void handleCollisions() {
        player.computeVelocities();
        velocityX = player.getVelocityX();
        velocityY = player.getVelocityY();

        Rectangle playerRec = player.getHitboxRec();
        Rectangle activeRec = new Rectangle(playerRec.getX() - PROPS_RADIUS / 2,
                playerRec.getY() - PROPS_RADIUS / 2, PROPS_RADIUS, PROPS_RADIUS);

        handleTilesCollision(playerRec);
        handlePropsCollision(activeRec);
        handleEntitiesCollision(playerRec);
        handleNonActiveVoidsCollision(playerRec);

        player.setVelocityX(velocityX);
        player.setVelocityY(velocityY);
    }

    private static void handleTilesCollision(Rectangle playerRec) {
        int startX = max((int) (playerRec.getX() / View.pixelTileSize) - 1, 0);
        int startY = max((int) (playerRec.getY() / View.pixelTileSize) - 1, 0);
        int endX = min(startX + TILES_RADIUS, tileMap[0].length);
        int endY = min(startY + TILES_RADIUS, tileMap.length);
        for (int x = startX; x < endX; x++) {
            for (int y = startY; y < endY; y++) {
                Tile tile = tileMap[y][x];
                if (tile.hasHitbox()) {
                    checkPlayerCollision(new Rectangle(x * View.pixelTileSize, y * View.pixelTileSize, View.pixelTileSize, View.pixelTileSize));
                }
            }
        }
    }

    private static void handlePropsCollision(Rectangle activeRec) {
        LinkedList<Bullet> bullets = Model.getInstance().getBullets();
        for (Prop p : Model.getInstance().getProps()) {
            Rectangle pRec = p.getHitboxRec();
            // close to the player?
            if (p.isActive() && p.getClass() != PropItem.class) {
                if (activeRec.getBoundsInParent().intersects(pRec.getBoundsInParent()) && checkPlayerCollision(pRec)) {
                    // Player collided
                    if (p.getName().equals("Cactus")) {
                        player.damage(CACTUS_DAMAGE);
                    }
                }
                bullets.removeIf(b -> b.getRectangle().getBoundsInParent().intersects(pRec.getBoundsInParent()));
            }
        }
    }

    private static void handleEntitiesCollision(Rectangle playerRec) {
        LinkedList<Bullet> bullets = Model.getInstance().getBullets();
        Bounds playerBounds = player.getEntityHitboxRec().getBoundsInParent();
        List<Entity> entities = Model.getInstance().getEntities();
        entities.forEach(e -> {
            if (e.isActive()) {
                Bounds entityBounds = e.getEntityHitboxRec().getBoundsInParent();
                if (entityBounds.intersects(e.getName().equals("Void") ? playerRec.getBoundsInParent() : playerBounds)) {
                    player.damage(e.getDamage());
                }

                // bullets
                bullets.removeIf(b -> {
                    if (b.getRectangle().getBoundsInParent().intersects(entityBounds)) {
                        e.damage(b.getGun().getDamage());
                        return true;
                    }
                    return false;
                });
            }
        });
    }

    private static void handleNonActiveVoidsCollision(Rectangle playerRec) {
        List<Entity> entities = Model.getInstance().getEntities();
        Model.getInstance().getVoids().removeIf(v -> {
            if (v.isActive() && v.getActivateBounds().intersects(playerRec.getBoundsInParent())) {
                v.activate();
                entities.add(v);
                return true;
            }
            return false;
        });
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

    public static PropItem getCollidedItem() {
        Bounds playerBounds = player.getEntityHitboxRec().getBoundsInParent();
        return (PropItem) Model.getInstance().getProps().stream().filter(p ->
                (p.isActive() && p.getClass() == PropItem.class && playerBounds.intersects(p.getHitboxRec().getBoundsInParent()))
        ).findFirst().orElseGet(null);
    }
}
