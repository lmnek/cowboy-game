package cvut.gartnkry.model.map;

import cvut.gartnkry.control.AppLogger;
import cvut.gartnkry.control.files.ResourcesUtils;
import cvut.gartnkry.view.assets.AssetsManager;
import cvut.gartnkry.view.assets.Sound;

import java.io.*;
import java.util.Objects;

/**
 * Map class is used for keeping 2D array of tiles,
 * on which game is taking place on.
 * </p>
 * Map layout is loaded from a CSV file.
 * CSV file is structured as codes of tiles in given positions.
 * Map is also being saved when the game is closed.
 */
public class Map {
    private Tile[][] tileMap;
    private final String filename;
    private static final String DELIMITER = ";";

    /**
     * Class constructor.
     * Loads CSV file and constructs 2D array of Tiles from tile codes.
     *
     * @param filename name of CSV map file in resources/Maps/
     */
    public Map(String filename) {
        this.filename = filename;
        loadMap(filename);
    }

    private void loadMap(String filename) {
        AppLogger.fine(() -> "Loading map from: " + filename);
        // Load map from CSV file
        try (BufferedReader br = ResourcesUtils.getReader("Maps/" + filename)) {
            // Read csv file
            String[] lines = br.lines().toArray(String[]::new);

            // Set dimensions
            tileMap = new Tile[lines.length][lines[0].split(DELIMITER).length];

            // Fill map with tiles
            for (int x = 0; x < tileMap.length; x++) {
                String[] line = lines[x].split(DELIMITER);
                for (int y = 0; y < tileMap[0].length; y++) {
                    tileMap[x][y] = AssetsManager.getTile(line[y]);
                }
            }
        } catch (Exception e) {
            AppLogger.exception(() -> "Failed to load map from: " + filename, e);
        }
    }

    /**
     * Save the map from this instance to a CSV file.
     */
    public void saveMap() {
        AppLogger.info(() -> "Saving the map: " + filename);
        try (BufferedOutputStream bos = ResourcesUtils.getOutputStream("Maps/" + filename)) {
            StringBuilder string = new StringBuilder();
            for (Tile[] tiles : tileMap) {
                for (int y = 0; y < tileMap[0].length; y++) {
                    string.append(tiles[y].getCode());
                    if (y != tileMap[0].length - 1) {
                        string.append(";");
                    }
                }
                string.append("\n");
            }
            Objects.requireNonNull(bos).write(string.toString().getBytes());
        } catch (Exception e) {
            AppLogger.exception(() -> "Failed to save map to: " + filename, e);
        }
    }

    /**
     * Place a bridge according to given indexes if it is possible
     * ~ modifies the map.
     *
     * @param startX starting X index of the bridge
     * @param startY starting Y index of the bridge
     * @param incX   X direction to increment/decrement index
     * @param incY   Y direction to increment/decrement index
     * @return whether the bridge was built
     */
    public boolean buildBridge(int startX, int startY, int incX, int incY) {
        String tile1 = tileMap[startY + incY][startX + incX].getName();
        String tile2 = tileMap[startY + 2 * incY][startX + 2 * incX].getName();
        // Possible to build a bridge?
        if (tileNameEquals(tile1, tile2, "sand_bottom", "sand_top")
                || tileNameEquals(tile1, tile2, "sand_left", "sand_right")) {
            // Place the bridge
            tileMap[startY + incY][startX + incX] = AssetsManager.getTileFromName(tile1.substring(0, 4) + "_bridge" + tile1.substring(4));
            tileMap[startY + 2 * incY][startX + 2 * incX] = AssetsManager.getTileFromName(tile2.substring(0, 4) + "_bridge" + tile2.substring(4));
            Sound.BRIDGE.play();
            return true;
        }
        return false;
    }

    private boolean tileNameEquals(String tile1, String tile2, String name1, String name2) {
        return (tile1.equals(name1) && tile2.equals(name2)
                || tile1.equals(name2) && tile2.equals(name1));
    }

    public Tile[][] getTileMap() {
        return tileMap;
    }

    public String getFilename() {
        return filename;
    }
}
