import java.awt.Point;
import java.util.ArrayList;

public abstract class Character {
    private final String[] IMAGE_PATHS;
    public static final int RIGHT = 0;
    public static final int LEFT = 1;
    public static final int DOWN = 2;
    public static final int UP = 3;

    private Point charPoint;
    private int direction;

    public Character(int x, int y, String[] imagePaths) {
        charPoint = new Point(x, y);
        this.IMAGE_PATHS = imagePaths;
        direction = RIGHT;
    }

    public boolean isNextCaseIsAvaible(ArrayList<Point> wallArrayList) {
        for (Point wall : wallArrayList) {
            if ((charPoint.getX() - 1 == wall.getX() && charPoint.getY() == wall.getY() && direction == LEFT) ||  // left direction
                (charPoint.getX() + 1 == wall.getX() && charPoint.getY() == wall.getY() && direction == RIGHT) ||  // right direction
                (charPoint.getY() - 1 == wall.getY() && charPoint.getX() == wall.getX() && direction == UP) ||  // up direction
                (charPoint.getY() + 1 == wall.getY() && charPoint.getX() == wall.getX() && direction == DOWN) ) { // down direction
                return false;
            } 
        }
        return true;
    }

    public String getImagePath() {return IMAGE_PATHS[0];}

    public String getImagePath(int index) {return IMAGE_PATHS[index];}

    public Point getCharPoint() {return charPoint;}

    public void moveRight() {charPoint.x++;}

    public void moveLeft() {charPoint.x--;}

    public void moveUp() {charPoint.y--;}

    public void moveDown() {charPoint.y++;}

    public void setLeft() {direction = LEFT;}
    
    public void setRight() {direction = RIGHT;}

    public void setUp() {direction = UP;}

    public void setDown() {direction = DOWN;}

    public int getCurrentDirection() {return direction;}
}
