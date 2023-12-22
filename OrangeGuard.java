import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OrangeGuard extends Character {
    private boolean isAlive = false;
    private HashMap<String, String> charSkinMap = new HashMap<>();

    public OrangeGuard() {
        super(5, 2, new String[]{ "/img/orangeGuardLeft.png",
                                      "/img/orangeGuardRight.png",
                                      "/img/orangeGuardUp.png",
                                      "/img/orangeGuardDown.png" });

        charSkinMap.put("OrangeGuardLeft", getImagePath(0));
        charSkinMap.put("OrangeGuardRight", getImagePath(1));
        charSkinMap.put("OrangeGuardUp", getImagePath(2));
        charSkinMap.put("OrangeGuardDown", getImagePath(3)); 

        setRight();
    }
    
    public void setAlive() {
        isAlive = true;
        this.getCharPoint().setLocation(6, 2);
    }

    public void spawnGuard(ArrayList<Point> wallArrayList) {
        if (isAlive) {
            int random = (int) (Math.random() * 4);
            setRandomDirection(random);
            if (this.isNextCaseIsAvaible(wallArrayList)) {
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

    public Map<String, String> getCharSkinMap() {return charSkinMap;}
    
}
