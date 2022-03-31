package cvut.gartnkry.model;

import cvut.gartnkry.view.assets.AssetsUtils;
import cvut.gartnkry.view.assets.Tile;
import cvut.gartnkry.view.assets.TileManager;
import javafx.application.Platform;

import java.io.BufferedReader;


public class Map {
    private Tile[][] tileMap;
    private String filename;
    private static final String DELIMITER = ";";

    public Map(String filename) {
        this.filename = filename;
        loadMap(filename);
    }

    private void loadMap(String filename) {

        // Load map from CSV file
        try {
            // Read csv file
            BufferedReader br = AssetsUtils.getRecourcesReader("/Maps/" + filename);
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

    public Tile[][] getTileMap() {
        return tileMap;
    }
}
