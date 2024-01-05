import java.awt.Point;
import java.util.ArrayList;

public class BlueGuard extends Guard {
    private int rows;
    private int columns;

    private boolean currentDirectionHasReached = true;
    
    private Point currentDirection;
    private ArrayList<Point> memoryPosition = new ArrayList<>();

    public BlueGuard(int x, int y, int rows, int columns) {
        super(x, y, new String[]{ "/img/blueGuardLeft.png",
                                      "/img/blueGuardRight.png",
                                      "/img/blueGuardUp.png",
                                      "/img/blueGuardDown.png" });
        this.rows = rows;
        this.columns = columns;
    }

    @Override
    public void move(ArrayList<GameElement> gameElementList) {
        if (isAlive()) {
            int maxCase = 0;

            for (GameElement gameElem : gameElementList) {
                if (!(gameElem instanceof Wall)) {
                    maxCase++;
                }
            }

            if (memoryPosition.size() == maxCase) {
                memoryPosition.clear();
            }

            if (currentDirectionHasReached) {
                setRandomDirection(gameElementList, rows, columns);
            } else {
                moveTowards(gameElementList);
            }
        }
    }

    public void moveTowards(ArrayList<GameElement> gameElementList) {
        // On verifie si la currentDirection est atteinte
        if (currentDirection.getX() == getX() && currentDirection.getY() == getY()) {
            currentDirectionHasReached = true;
            memoryPosition.add(currentDirection);
        }

        if (currentDirection.getX() < getX() && isMoveOk(gameElementList, getX()-1, getY())) {
            moveLeft();
        } else if (currentDirection.getX() > getX() && isMoveOk(gameElementList, getX()+1, getY())) {
            moveRight();
        } else if (currentDirection.getY() > getY() && isMoveOk(gameElementList, getX(), getY()+1)) {
            moveDown();
        } else if (currentDirection.getY() < getY() && isMoveOk(gameElementList, getX(), getY()-1)) {
            moveUp();
        } else {
            // Direction inatteignable on regénère et on sauvegarde la position actuelle
            currentDirectionHasReached = true;
            memoryPosition.add(new Point(getX(), getY()));
        }
        
    }

    private void setRandomDirection(ArrayList<GameElement> gameElementList, int rows, int columns) {
        start:
        while (true) {
            int randomXPos = (int) (Math.random() * rows);
            int randomYPos = (int) (Math.random() * columns);

            if (!(isMoveOk(gameElementList, randomXPos, randomYPos))) {
                continue start;
            }
            
            for (Point position : memoryPosition) {
                if (position.getX() == randomXPos 
                        && position.getY() == randomYPos) {
                    
                    continue start;
                }
            }

            currentDirection = new Point(randomXPos, randomYPos);
            currentDirectionHasReached = false;
            break;
        }
    }
    
}
