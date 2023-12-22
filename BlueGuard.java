import java.awt.Point;
import java.util.ArrayList;

public class BlueGuard extends Guard {
    ArrayList<Point> memoryPosition = new ArrayList<>();

    public BlueGuard() {
        super(16, 2, new String[]{ "/img/blueGuardLeft.png",
                                      "/img/blueGuardRight.png",
                                      "/img/blueGuardUp.png",
                                      "/img/blueGuardDown.png" });

    }

    @Override
    public void spawnGuard(ArrayList<Point> wallArrayList) {
        if (this.isAlive()) {
            int random = (int) (Math.random() * 4);
            setRandomDirection(random);
            if (this.isNextCaseIsAvaible(wallArrayList) && isNextCaseIsAvaible(memoryPosition)) {
                switch (random) {
                    case 0:
                        moveRight();
                        memoryPosition.add(new Point(this.getCharPoint()));
                        break;

                    case 1:
                        moveLeft();
                        memoryPosition.add(new Point(this.getCharPoint()));
                        break;

                    case 2:
                        moveUp();
                        memoryPosition.add(new Point(this.getCharPoint()));
                        break;

                    case 3:
                        moveDown();
                        memoryPosition.add(new Point(this.getCharPoint()));
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
