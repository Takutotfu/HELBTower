import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class Guard extends Character {
    private boolean isAlive = false;
    private HashMap<String, String> charSkinMap = new HashMap<>();

    public Guard(int x, int y, String[] imagePaths) {
        super(x, y, imagePaths);

        charSkinMap.put(this.getClass().getName()+"Left", getImagePath(0));
        charSkinMap.put(this.getClass().getName()+"Right", getImagePath(1));
        charSkinMap.put(this.getClass().getName()+"Up", getImagePath(2));
        charSkinMap.put(this.getClass().getName()+"Down", getImagePath(3));

        setDown();
    }
    
    public void setAlive() {
        isAlive = true;
        this.getCharPoint().setLocation(this.getCharPoint().getX()+1, this.getCharPoint().getY());
    }

    public abstract void spawnGuard(ArrayList<Point> wallArrayList);

    public boolean isAlive() {return isAlive;}

    public Map<String, String> getCharSkinMap() {return charSkinMap;}
}
