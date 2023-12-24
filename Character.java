import java.util.ArrayList;

public abstract class Character {
    private final String[] IMAGE_PATHS;
    public static final int RIGHT = 0;
    public static final int LEFT = 1;
    public static final int DOWN = 2;
    public static final int UP = 3;

    private int posX, posY;
    private int direction;

    public Character(int posX, int posY, String[] imagePaths) {
        this.posX = posX;
        this.posY = posY;
        this.IMAGE_PATHS = imagePaths;
        direction = RIGHT;
    }

    public boolean isNextCaseIsAvaible(ArrayList<GameElement> gameElementsList) {
        for (GameElement gameElem : gameElementsList) {
            if (gameElem instanceof Wall) {
                if ((posX - 1 == gameElem.getPosX() && posY == gameElem.getPosY() && direction == LEFT) || // left direction
                        (posX + 1 == gameElem.getPosX() && posY == gameElem.getPosY() && direction == RIGHT) || // right direction
                        (posY - 1 == gameElem.getPosY() && posX == gameElem.getPosX() && direction == UP) || // up direction
                        (posY + 1 == gameElem.getPosY() && posX == gameElem.getPosX() && direction == DOWN)) { // down direction
                    return false;
                }
            }
        }
        return true;
    }

    public String getImagePath() {return IMAGE_PATHS[0];}

    public String getImagePath(int index) {return IMAGE_PATHS[index];}

    public int getX() {return posX;}

    public int getY() {return posY;}

    public void setLocation(int x, int y) {posX = x; posY = y;} 
        
    public void setLocation(double x, double y) {posX = (int) x; posY = (int) y;} 

    public void moveRight() {posX++;}

    public void moveLeft() {posX--;}

    public void moveUp() {posY--;}

    public void moveDown() {posY++;}

    public void setLeft() {direction = LEFT;}
    
    public void setRight() {direction = RIGHT;}

    public void setUp() {direction = UP;}

    public void setDown() {direction = DOWN;}

    public int getCurrentDirection() {return direction;}
}
