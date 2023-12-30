import java.util.ArrayList;

public class OrangeGuard extends Guard {

    public OrangeGuard(int x, int y) {
        super(x, y, new String[] { "/img/orangeGuardLeft.png",
                                       "/img/orangeGuardRight.png",
                                       "/img/orangeGuardUp.png",
                                       "/img/orangeGuardDown.png" });
    }

    public void move(ArrayList<GameElement> gameElementList) {
        if (isAlive()) {
            int random = (int) (Math.random() * 4);
            switch (random) {
                case 0:
                    setRight();
                    if (isMoveOk(gameElementList, getX() + 1, getY())) {
                        moveRight();
                    }
                    break;

                case 1:
                    setLeft();
                    if (isMoveOk(gameElementList, getX() - 1, getY())) {
                        moveLeft();
                    }
                    break;

                case 2:
                    setUp();
                    if (isMoveOk(gameElementList, getX(), getY() - 1)) {
                        moveUp();
                    }
                    break;

                case 3:
                    setDown();
                    if (isMoveOk(gameElementList, getX(), getY() + 1)) {
                        moveDown();
                    }
                    break;
            }
        }
    }
}
