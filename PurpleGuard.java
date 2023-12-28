import java.awt.Point;
import java.util.ArrayList;

public class PurpleGuard extends Guard {
    private boolean currentDirectionHasReached = true;
    private ArrayList<Point> towersPosition = new ArrayList<>();
    private Point currentDirection;

    public PurpleGuard() {
        super(16, 12, new String[]{ "/img/purpleGuardLeft.png",
                                      "/img/purpleGuardRight.png",
                                      "/img/purpleGuardUp.png",
                                      "/img/purpleGuardDown.png" });
        
        towersPosition.add(new Point(17, 1)); // top right tower 
        towersPosition.add(new Point(3, 13)); // bottom left tower
        towersPosition.add(new Point(17, 13)); // bottom right tower
        towersPosition.add(new Point(3, 1)); // top left tower

    }

    public void move(ArrayList<GameElement> gameElementList) {
        if (isAlive()) {
            if (currentDirectionHasReached) {
                setRandomDirection();
            } else {
                moveTowards(gameElementList);
            }
        }
    }

    public void moveTowards(ArrayList<GameElement> gameElementList) {
        // On verifie si la currentDirection est atteinte
        if (currentDirection.getX() == getX() && currentDirection.getY() == getY()) {
            currentDirectionHasReached = true;
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
            // si la currentDirection est inatteignable on la change
            currentDirectionHasReached = true;
        }
    }

    private void setRandomDirection() {
        int random = (int) (Math.random() * 4);
        switch (random) {
            case 0:
                currentDirection = towersPosition.get(0);
                break;
            
            case 1:
                currentDirection = towersPosition.get(1);
                break;
            
            case 2:
                currentDirection = towersPosition.get(2);
                break;
            
            case 3:
                currentDirection = towersPosition.get(3);
                break;
        }
        
        currentDirectionHasReached = false;
    }
}
