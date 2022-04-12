package cvut.gartnkry.model;

import cvut.gartnkry.ResourcesUtils;
import cvut.gartnkry.view.assets.Tile;
import cvut.gartnkry.view.assets.TileManager;

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
     * @param filename name of CSV map file in resources/Maps/
     */
    public Map(String filename) {
        this.filename = filename;
        loadMap(filename);
    }

    private void loadMap(String filename) {
        // Load map from CSV file
        try {
            // Read csv file
            BufferedReader br = ResourcesUtils.getReader("Maps/" + filename);
            String[] lines = br.lines().toArray(String[]::new);
            br.close();

            // Set dimensions
            tileMap = new Tile[lines.length][lines[0].split(DELIMITER).length];

            // Fill map with tiles
            for (int x = 0; x < tileMap.length; x++) {
                String[] line = lines[x].split(DELIMITER);
                for (int y = 0; y < tileMap[0].length; y++) {
                    tileMap[x][y] = TileManager.getTile(line[y]);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @return 2D array of Tiles representing the map
     */
    public Tile[][] getTileMap() {
        return tileMap;
    }
}
