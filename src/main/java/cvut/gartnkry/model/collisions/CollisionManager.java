package cvut.gartnkry.model.collisions;

import cvut.gartnkry.Settings;
import cvut.gartnkry.model.Model;
import cvut.gartnkry.model.Prop;
import cvut.gartnkry.model.entities.Player;
import cvut.gartnkry.model.map.Tile;
import cvut.gartnkry.view.View;
import javafx.scene.shape.Rectangle;

import static cvut.gartnkry.Settings.HITBOX_PADDING;
import static java.lang.Math.max;
import static java.lang.Math.min;

public class CollisionManager {
    private final Player player;
    private final Model model;
    private final Tile[][] tileMap;
    private final HitboxInfo playerHbInfo;

    private final double propsRadius;
    private final int tilesRadius;

    private double velocityX;
    private double velocityY;

    public CollisionManager(Model model) {
        this.model = model;
        this.player = model.getPlayer();
        tileMap = model.getMap().getTileMap();
        propsRadius = 50 * Settings.SCALE;
        tilesRadius = 3;
        playerHbInfo = player.getHitboxInfo();
    }

    public void handlePlayerCollisions() {
        player.computeVelocities();
        velocityX = player.getVelocityX();
        velocityY = player.getVelocityY();

        Rectangle playerRec = player.getHitboxRec();
        Rectangle activeRec = new Rectangle(playerRec.getX() - propsRadius / 2,
                playerRec.getY() - propsRadius / 2, propsRadius, propsRadius);

        //---TILES---
        int startX = max((int) (playerRec.getX() / View.pixelTileSize) - 1, 0);
        int startY = max((int) (playerRec.getY() / View.pixelTileSize) - 1, 0);
        int endX = min(startX + tilesRadius, tileMap[0].length);
        int endY = min(startY + tilesRadius, tileMap.length);
        for (int x = startX; x < endX; x++) {
            for (int y = startY; y < endY; y++) {
                Tile tile = tileMap[y][x];
                if (tile.hasHitbox()) {
                    Rectangle tileRec = new Rectangle(x * View.pixelTileSize, y * View.pixelTileSize, View.pixelTileSize, View.pixelTileSize);
                    handlePlayerCollision(tileRec);
                }
            }
        }

        //---PROPS---
        for (Prop p : model.getProps()) {
            Rectangle pRec = p.getHitboxRec();
            // close to the player?
            if (p.isActive() && activeRec.getBoundsInParent().intersects(pRec.getBoundsInParent())) {
                // handle collision
                handlePlayerCollision(pRec);
            }
        }
        player.setVelocityX(velocityX);
        player.setVelocityY(velocityY);
    }


    public boolean handlePlayerCollision(Rectangle cRec) {
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
                velocityY = checkY(cRec, velocityY, false, playerHbInfo);
                velocityX = checkX(cRec, velocityX, true, playerHbInfo);
            } else {
                velocityX = checkX(cRec, velocityX, false, playerHbInfo);
                velocityY = checkY(cRec, velocityY, true, playerHbInfo);
            }
        } else if (collidedX) {
            velocityX = checkX(cRec, velocityX, false, playerHbInfo);
        } else if (collidedY) {
            velocityY = checkY(cRec, velocityY, false, playerHbInfo);
        } else {
            collided = false;
        }
        return collided;
    }

    public double checkX(Rectangle rec, double velocity, boolean compute, HitboxInfo hbInfo) {
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

    public double checkY(Rectangle rec, double velocity, boolean compute, HitboxInfo hbInfo) {
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
