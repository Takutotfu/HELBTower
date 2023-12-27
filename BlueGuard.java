import java.awt.Point;
import java.util.ArrayList;

public class BlueGuard extends Guard {
    ArrayList<Point> memoryPosition = new ArrayList<>();
    Point currentDirection;
    boolean currentDirectionHasReached = true;

    public BlueGuard() {
        super(16, 3, new String[]{ "/img/blueGuardLeft.png",
                                      "/img/blueGuardRight.png",
                                      "/img/blueGuardUp.png",
                                      "/img/blueGuardDown.png" });
    }

    public void spawnGuard(ArrayList<GameElement> gameElementList, int rows, int columns) {
        if (this.isAlive()) {

            if (currentDirectionHasReached) {
                setRandomDirection(gameElementList, rows, columns);
            } else if (isNextCaseIsAvaible(gameElementList)) {
                if (currentDirection.getX() < getX()) {
                    moveLeft();
                } else if (currentDirection.getX() > getX()) {
                    moveRight();
                } else if (currentDirection.getY() > getY()) {
                    moveDown();
                } else if (currentDirection.getY() < getY()) {
                    moveUp();
                }
            }

            if (currentDirection.getX() == getX() && currentDirection.getY() == getY()) {
                currentDirectionHasReached = true;
            }
            
            System.out.println("x:"+getX()+";y:"+getY());
        }
    }

    

    private void setRandomDirection(ArrayList<GameElement> gameElementList, int rows, int columns) {
        start:
        while (true) {
            int randomXPos = (int) (Math.random() * rows);
            int randomYPos = (int) (Math.random() * columns);

            for (GameElement gameElement : gameElementList) {
                if (gameElement instanceof Wall) {
                    if (gameElement.getPosX() == randomXPos 
                            && gameElement.getPosY() == randomYPos) {
                        continue start;
                    }
                }
            }
            
            for (Point position : memoryPosition) {
                if (position.getX() == randomXPos 
                        && position.getY() == randomYPos) {
                    continue start;
                }
            }

            System.out.println("Blue guard direction: x:"+randomXPos+";y:"+randomYPos);

            Point newDirection = new Point(randomXPos, randomYPos);
            currentDirection = newDirection;
            memoryPosition.add(newDirection);
            currentDirectionHasReached = false;
            break;
        }
    }
    
}
