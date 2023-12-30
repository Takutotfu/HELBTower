import java.awt.Point;
import java.util.ArrayList;

public class RedGuard extends Guard {
    MainCharacter mainCharacter;
    Point currentDirection;

    public RedGuard(int x, int y, MainCharacter mainCharacter) {
        super(x, y, new String[]{ "/img/redGuardLeft.png",
                                  "/img/redGuardRight.png",
                                  "/img/redGuardUp.png",
                                  "/img/redGuardDown.png" });

        this.mainCharacter = mainCharacter;
    }

    @Override
    public void move(ArrayList<GameElement> gameElementList) {
        verifCurrentDirectionPosition(mainCharacter);
        if (isAlive()) {
            if (currentDirection.getX() < getX() && isMoveOk(gameElementList, getX() - 1, getY())) {
                moveLeft();
            } else if (currentDirection.getX() > getX() && isMoveOk(gameElementList, getX() + 1, getY())) {
                moveRight();
            } else if (currentDirection.getY() > getY() && isMoveOk(gameElementList, getX(), getY() + 1)) {
                moveDown();
            } else if (currentDirection.getY() < getY() && isMoveOk(gameElementList, getX(), getY() - 1)) {
                moveUp();
            }
        }
    }

    private void verifCurrentDirectionPosition(MainCharacter mainCharacter) {
        currentDirection = new Point(mainCharacter.getX(), mainCharacter.getY());
    }
    
}
