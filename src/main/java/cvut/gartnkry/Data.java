package cvut.gartnkry;

import javafx.geometry.Point2D;

/**
 *  Class for loading saves from JSON files
 */
public class Data {
    private Point2D playerCoords;

    public Data(String filename) {
        loadFromJSON(filename);
    }

    private void loadFromJSON(String filename) {
        // TODO: loading from json file
        this.playerCoords = new Point2D(1000, 1000);
    }

    public Point2D getPlayerCoords() {
        return playerCoords;
    }

    public String getMapFilename() {
        return "map2.csv";
    }
}
