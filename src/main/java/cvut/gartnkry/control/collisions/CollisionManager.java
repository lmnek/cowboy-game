package cvut.gartnkry.control.collisions;

import cvut.gartnkry.control.AppLogger;
import cvut.gartnkry.control.Settings;
import cvut.gartnkry.model.Model;
import cvut.gartnkry.model.Prop;
import cvut.gartnkry.model.entities.Bullet;
import cvut.gartnkry.model.entities.Entity;
import cvut.gartnkry.model.entities.Player;
import cvut.gartnkry.model.items.PropItem;
import cvut.gartnkry.model.map.Tile;
import cvut.gartnkry.view.View;
import cvut.gartnkry.view.assets.Sound;
import javafx.geometry.Bounds;
import javafx.scene.shape.Rectangle;

import java.util.LinkedList;
import java.util.List;

import static cvut.gartnkry.control.Settings.CACTUS_DAMAGE;
import static cvut.gartnkry.control.Settings.HITBOX_PADDING;
import static java.lang.Math.max;
import static java.lang.Math.min;

/**
 * Class with methods used for handling collision between player, props and entities.
 */
public class CollisionManager {
    private static final double PROPS_RADIUS = 50 * Settings.SCALE;
    private static final int TILES_RADIUS = 3;
    private static double velocityX;
    private static double velocityY;

    private static Tile[][] tileMap;
    private static Player player;
    private static HitboxInfo hbInfo;

    /**
     * Class constructor.
     * Load references from model.
     */
    public CollisionManager(){
        tileMap = Model.getInstance().getMap().getTileMap();
        player = Model.getInstance().getPlayer();
        hbInfo = player.getHitboxInfo();
    }

    /**
     * Handle collisions between: player/tiles, player/props, player/entities, player/nonactive voids.
     */
    public void handleCollisions() {
        // Load current player velocities
        player.computeVelocities();
        velocityX = player.getVelocityX();
        velocityY = player.getVelocityY();

        // Players position in next tick
        Rectangle playerRec = player.getHitboxRec();
        // Check collisions around player only in this rectangle
        Rectangle activeRec = new Rectangle(playerRec.getX() - PROPS_RADIUS / 2,
                playerRec.getY() - PROPS_RADIUS / 2, PROPS_RADIUS, PROPS_RADIUS);

        // Handle all needed collisions
        handleTilesCollision(playerRec);
        handlePropsCollision(activeRec);
        handleEntitiesCollision(playerRec);
        handleNonActiveVoidsCollision(playerRec);

        // Set new velocities after colliding
        player.setVelocityX(velocityX);
        player.setVelocityY(velocityY);
    }

    /**
     * Check collisions between player and tiles around him.
     *
     * @param playerRec Player walking hitbox rectangle
     */
    private void handleTilesCollision(Rectangle playerRec) {
        // Compute indexes of 3x3 tiles around player to check
        final int startX = max((int) (playerRec.getX() / View.pixelTileSize) - 1, 0);
        final int startY = max((int) (playerRec.getY() / View.pixelTileSize) - 1, 0);
        final int endX = min(startX + TILES_RADIUS, tileMap[0].length);
        final int endY = min(startY + TILES_RADIUS, tileMap.length);
        // Loop tiles
        for (int x = startX; x < endX; x++) {
            for (int y = startY; y < endY; y++) {
                Tile tile = tileMap[y][x];
                if (tile.hasHitbox()) {
                    // Collision between player and tile with hitbox
                    if (checkPlayerCollision(new Rectangle(x * View.pixelTileSize, y * View.pixelTileSize, View.pixelTileSize, View.pixelTileSize))) {
                        AppLogger.fine(() -> "Player collided with tile: " + tile.getName());
                    }
                }
            }
        }
    }

    /**
     * Check collisions between player/props and bullets/props.
     *
     * @param activeRec Active rectangle around player to check
     */
    private void handlePropsCollision(Rectangle activeRec) {
        LinkedList<Bullet> bullets = Model.getInstance().getBullets();
        // Loop all props
        for (Prop p : Model.getInstance().getProps()) {
            Rectangle pRec = p.getHitboxRec();
            // On screen and not an item?
            if (p.isActive() && p.getClass() != PropItem.class) {
                // Check player collision
                if (activeRec.getBoundsInParent().intersects(pRec.getBoundsInParent()) && checkPlayerCollision(pRec)) {
                    AppLogger.fine(() -> "Player collided with prop: " + p.getName());
                    if (p.getName().equals("Cactus")) {
                        player.damage(CACTUS_DAMAGE);
                    }
                }
                // Remove bullets if collided with a prop
                bullets.removeIf(b -> b.getRectangle().getBoundsInParent().intersects(pRec.getBoundsInParent()));
            }
        }
    }

    /**
     * Check collisions between player/entities and bullets/entities.
     *
     * @param playerRec Player walking hitbox rectangle
     */
    private void handleEntitiesCollision(Rectangle playerRec) {
        LinkedList<Bullet> bullets = Model.getInstance().getBullets();
        Bounds playerBounds = player.getEntityHitboxRec().getBoundsInParent();
        List<Entity> entities = Model.getInstance().getEntities();
        // Loop all entities
        entities.forEach(e -> {
            if (e.isActive()) {
                Bounds entityBounds = e.getEntityHitboxRec().getBoundsInParent();
                // Damage player if collided
                if (entityBounds.intersects(e.getName().equals("Void") ? playerRec.getBoundsInParent() : playerBounds)) {
                    AppLogger.fine(() -> "Player collided with entity: " + e.getName());
                    player.damage(e.getDamage());
                }

                // Remove bullet if collided with entity
                bullets.removeIf(b -> {
                    if (b.getRectangle().getBoundsInParent().intersects(entityBounds)) {
                        e.damage(b.getGun().getDamage()); // damage entity
                        return true;
                    }
                    return false;
                });
            }
        });
    }

    /**
     * Activate nonactive voids if player is nearby.
     *
     * @param playerRec Player walking hitbox rectangle
     */
    private void handleNonActiveVoidsCollision(Rectangle playerRec) {
        List<Entity> entities = Model.getInstance().getEntities();
        Model.getInstance().getVoids().removeIf(v -> {
            // On screen and player nearby?
            if (v.isActive() && v.getActivateBounds().intersects(playerRec.getBoundsInParent())) {
                // Activate void
                v.activate();
                Sound.VOID_OPEN.play();
                entities.add(v);
                return true;
            }
            return false;
        });
    }


    /**
     * Check player collision with given rectangle and compute new velocities if collision was detected.
     *
     * @param cRec Rectangle for player to collide with
     * @return boolean whether player collided
     */
    public boolean checkPlayerCollision(Rectangle cRec) {
        boolean collided = true;
        Rectangle xRec = player.getHitboxRec(velocityX, 0);
        Rectangle yRec = player.getHitboxRec(0, velocityY);
        boolean collidedX = xRec.getBoundsInParent().intersects(cRec.getBoundsInParent());
        boolean collidedY = yRec.getBoundsInParent().intersects(cRec.getBoundsInParent());
        // Collided from both directions?
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

    /**
     * Change players position from collision in X axis.
     */
    private double checkX(Rectangle rec, double velocity, boolean compute) {
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

    /**
     * Change players position from collision in Y axis.
     */
    private double checkY(Rectangle rec, double velocity, boolean compute) {
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

    /**
     * Loop all items on the ground and find any that collides with player.
     * @return PropItem
     */
    public PropItem getCollidedItem() {
        Bounds playerBounds = player.getEntityHitboxRec().getBoundsInParent();
        return (PropItem) Model.getInstance().getProps().stream().filter(p ->
                (p.isActive() && p.getClass() == PropItem.class && playerBounds.intersects(p.getHitboxRec().getBoundsInParent()))
        ).findFirst().orElse(null);
    }
}
