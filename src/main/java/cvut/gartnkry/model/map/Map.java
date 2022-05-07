package cvut.gartnkry.model.map;

import cvut.gartnkry.control.ResourcesUtils;
import cvut.gartnkry.model.Model;
import cvut.gartnkry.view.assets.AssetsManager;

import java.io.BufferedReader;

/**
 * Map class is used for keeping 2D array of tiles,
 * on which game is taking place on.
 * </p>
 * Map layout is loaded from a CSV file.
 * CSV file is structured as codes of tiles in given positions.
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
            e.printStackTrace();
        }
    }

    public boolean buildBridge(int startX, int startY, int incX, int incY) {
        String tile1 = tileMap[startY + incY][startX + incX].getName();
        String tile2 = tileMap[startY + 2 * incY][startX + 2 * incX].getName();
        if (tile1.equals("sand_bottom") && tile2.equals("sand_top")
                || tile1.equals("sand_top") && tile2.equals("sand_bottom")
                || tile1.equals("sand_left") && tile2.equals("sand_right")
                || tile1.equals("sand_right") && tile2.equals("sand_left")) {
            tileMap[startY + incY][startX + incX] = AssetsManager.getTileFromName(tile1.substring(0, 4) + "_bridge" + tile1.substring(4));
            tileMap[startY + 2 * incY][startX + 2 * incX] = AssetsManager.getTileFromName(tile2.substring(0, 4) + "_bridge" + tile2.substring(4));
            return true;
        }
        return false;
    }

    /**
     * @return 2D array of Tiles representing the map
     */
    public Tile[][] getTileMap() {
        return tileMap;
    }


}
