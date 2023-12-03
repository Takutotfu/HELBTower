import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Character {
    private Point charPoint;
    private Map<String, String> charSkinMap = new HashMap<>();

    public Character(int x, int y) {
        charPoint = new Point(x, y);
        charSkinMap.put("charcterLeft", "img/characterLeft.png");
        charSkinMap.put("charcterRight", "img/characterRight.png");
        charSkinMap.put("charcterUp", "img/characterUp.png");
        charSkinMap.put("charcterDown", "img/characterDown.png");        
    }

    public boolean isNextCaseIsAvaible(ArrayList<Point> wallArrayList, int direction) {
        for (Point wall : wallArrayList) {
            if ((charPoint.getX() - 1 == wall.getX() && charPoint.getY() == wall.getY() && direction == 1) ||  // left direction
                (charPoint.getX() + 1 == wall.getX() && charPoint.getY() == wall.getY() && direction == 0) ||  // right direction
                (charPoint.getY() - 1 == wall.getY() && charPoint.getX() == wall.getX() && direction == 3) ||  // up direction
                (charPoint.getY() + 1 == wall.getY() && charPoint.getX() == wall.getX() && direction == 2) ) { // down direction
                return false;
            } 
        }
        return true;
    }

    public Point getCharPoint() {
        return charPoint;
    }

    public Map<String, String> getCharSkinMap() {
        return charSkinMap;
    }

    public void moveRight() {
        charPoint.x++;
    }

    public void moveLeft() {
        charPoint.x--;
    }

    public void moveUp() {
        charPoint.y--;
    }

    public void moveDown() {
        charPoint.y++;
    }
}
