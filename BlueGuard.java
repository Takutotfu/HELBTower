import java.awt.Point;
import java.util.ArrayList;

public class BlueGuard extends Guard {
    private ArrayList<Point> memoryPosition = new ArrayList<>();
    private Point currentDirection;
    private boolean currentDirectionHasReached = true;

    public BlueGuard() {
        super(16, 3, new String[]{ "/img/blueGuardLeft.png",
                                      "/img/blueGuardRight.png",
                                      "/img/blueGuardUp.png",
                                      "/img/blueGuardDown.png" });
    }

    public void move(ArrayList<GameElement> gameElementList, int rows, int columns) {
        if (isAlive()) {
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

    // Si la position x;y n'est pas un mur ou un teleporter
    private boolean isMoveOk(ArrayList<GameElement> gameElementList, int x, int y) {
        for (GameElement gameElement : gameElementList) {
            if (gameElement instanceof Wall || gameElement instanceof Teleporter) {
                if (gameElement.getPosX() == x
                        && gameElement.getPosY() == y) {
                    return false;
                }
            }
        }
        return true;
    }
    
}
