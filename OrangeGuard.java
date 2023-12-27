import java.util.ArrayList;
public class OrangeGuard extends Guard {

    public OrangeGuard() {
        super(5, 3, new String[]{ "/img/orangeGuardLeft.png",
                                      "/img/orangeGuardRight.png",
                                      "/img/orangeGuardUp.png",
                                      "/img/orangeGuardDown.png" });
    }

    public void spawnGuard(ArrayList<GameElement> gameElementList) {
        if (this.isAlive()) {
            int random = (int) (Math.random() * 4);
            setRandomDirection(random);
            if (this.isNextCaseIsAvaible(gameElementList)) {
                switch (random) {
                    case 0:
                        moveRight();
                        break;

                    case 1:
                        moveLeft();
                        break;

                    case 2:
                        moveUp();
                        break;

                    case 3:
                        moveDown();
                        break;
                }
            }
        }
    }

    private void setRandomDirection(int random) {
        switch (random) {
            case 0:
                setRight();
                break;

            case 1:
                setLeft();
                break;
            
            case 2:
                setUp();
                break;
            
            case 3:
                setDown();
                break;
        }
    }
    
}
