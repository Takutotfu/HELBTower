import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;

public class Wall {
    private int rows;
    private int columns;
    private int crossOpening = 5;
    private String pathToImg = "img/wall.jpg";
    private ArrayList<Point> wallArrayList = new ArrayList<>();
    private HashMap<String, Point> teleportersHashMap;

    public Wall(int rows, int columns, HashMap<String, Point> teleportersHashMap) {
        this.rows = rows;
        this.columns = columns;
        this.teleportersHashMap = teleportersHashMap;
        generateBorder();
    }

    public void generateBorder() {
        for (int i = 0; i < rows - 1; i++) {
            if (teleportersHashMap.get("portalTop").getX() != i) {
                wallArrayList.add(new Point(i, 0)); // top border
            }
            if (teleportersHashMap.get("portalDown").getX() != i) {
                wallArrayList.add(new Point(i, columns - 1)); // bottom border
            }
        }

        for (int i = 1; i < columns - 1; i++) {
            if (teleportersHashMap.get("portalLeft").getY() != i) {
                wallArrayList.add(new Point(0, i)); // left border
            }
            if (teleportersHashMap.get("portalRight").getY() != i) {
                wallArrayList.add(new Point(rows - 1, i)); // right border
            }
        }
        
        wallArrayList.add(new Point(rows-1,columns-1));
        wallArrayList.add(new Point(rows-1, 0));
    }

    public void generateWall() {
        int portalRow = (int) teleportersHashMap.get("portalRight").getY();
        int portalColumn = (int) teleportersHashMap.get("portalTop").getX();

        for (int i = crossOpening; i < columns-crossOpening; i++) {
            wallArrayList.add(new Point(portalColumn,i));
        }
        
        for (int i = crossOpening; i < rows-crossOpening; i++) {
            wallArrayList.add(new Point(i,portalRow));
        }
    }

    public void generateTower() {
        // top left tower
        for (int i = (rows/4)-1; i <= rows/4; i++) {
            for (int j = (columns/4)-1; j <= (columns/4); j++) {
                wallArrayList.add(new Point(i,j));
            }
        }

        // top right tower
        for (int i = rows-(rows/4)-1; i <= rows-(rows/4); i++) {
            for (int j = (columns/4)-1; j <= (columns/4); j++) {
                wallArrayList.add(new Point(i,j));
            }
        }

        // Bottom left tower
        for (int i = (rows/4)-1; i <= rows/4; i++) {
            for (int j = columns-(columns/4)-1; j <= columns-(columns/4); j++) {
                wallArrayList.add(new Point(i,j));
            }
        }

        // Bottom right tower
        for (int i = rows-(rows/4)-1; i <= rows-(rows/4); i++) {
            for (int j = columns-(columns/4)-1; j <= columns-(columns/4); j++) {
                wallArrayList.add(new Point(i,j));
            }
        }
    }

    public String getPathToImg() {
        return pathToImg;
    }

    public ArrayList<Point> getWallArrayList() {
        return wallArrayList;
    }

}
