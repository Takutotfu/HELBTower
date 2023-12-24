import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class Guard extends Character {
    private boolean isAlive = false;
    private HashMap<String, String> charSkinMap = new HashMap<>();

    public Guard(int x, int y, String[] imagePaths) {
        super(x, y, imagePaths);

        charSkinMap.put(getClass().getName()+"Left", getImagePath(0));
        charSkinMap.put(getClass().getName()+"Right", getImagePath(1));
        charSkinMap.put(getClass().getName()+"Up", getImagePath(2));
        charSkinMap.put(getClass().getName()+"Down", getImagePath(3));

        setDown();
    }
    
    public void setAlive() {
        isAlive = true;
        setLocation(getX()+1, getY());
    }

    public abstract void spawnGuard(ArrayList<GameElement> wallArrayList);

    public boolean isAlive() {return isAlive;}

    public Map<String, String> getCharSkinMap() {return charSkinMap;}
}
