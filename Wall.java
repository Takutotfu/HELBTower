import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;

public class Wall {
    private int rows;
    private int columns;
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

    public void generateRandomWall() {
        int posX = (int) (Math.random() * (rows-2) + 1);
        int posY = (int) (Math.random() * (columns-2) + 1);
        wallArrayList.add(new Point(posX, posY));
        wallArrayList.add(new Point(posX-1, posY));
        wallArrayList.add(new Point(posX+1, posY));
    }

    public boolean isNextCaseIsAWall(Point charHead, int direction) {
        for (Point wall : wallArrayList) {
            if ((charHead.getX() - 1 == wall.getX() && charHead.getY() == wall.getY() && direction == 1) ||  // left direction
                (charHead.getX() + 1 == wall.getX() && charHead.getY() == wall.getY() && direction == 0) ||  // right direction
                (charHead.getY() - 1 == wall.getY() && charHead.getX() == wall.getX() && direction == 3) ||  // up direction
                (charHead.getY() + 1 == wall.getY() && charHead.getX() == wall.getX() && direction == 2) ) { // down direction
                return false;
            } 
        }
        return true;
    }

    public String getPathToImg() {
        return pathToImg;
    }

    public ArrayList<Point> getWallArrayList() {
        return wallArrayList;
    }

}
