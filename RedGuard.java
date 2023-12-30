import java.util.ArrayList;

public class RedGuard extends Guard {

    public RedGuard(int x, int y) {
        super(x, y, new String[]{ "/img/redGuardLeft.png",
                                  "/img/redGuardRight.png",
                                  "/img/redGuardUp.png",
                                  "/img/redGuardDown.png" });
    }

    public void move(ArrayList<GameElement> gameElementList, int x, int y) {
        if (isAlive()) {
            if (x < getX() && isMoveOk(gameElementList, getX() - 1, getY())) {
                moveLeft();
            } else if (x > getX() && isMoveOk(gameElementList, getX() + 1, getY())) {
                moveRight();
            } else if (y > getY() && isMoveOk(gameElementList, getX(), getY() + 1)) {
                moveDown();
            } else if (y < getY() && isMoveOk(gameElementList, getX(), getY() - 1)) {
                moveUp();
            } else if (x < getX() && !(isMoveOk(gameElementList, getX() - 1, getY()))) {
                
            }
        }
    }
    
}
