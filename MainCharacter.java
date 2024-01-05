import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainCharacter extends Character {
    private boolean isAlive = true;
    private ArrayList<Point> memoryPosition = new ArrayList<>();
    private Map<String, String> charSkinMap = new HashMap<>();

    public MainCharacter(int x, int y) {
        super(x, y, new String[] { "/img/characterLeft.png",
                                   "/img/characterRight.png",
                                   "/img/characterUp.png",
                                   "/img/characterDown.png" });

        charSkinMap.put("MainCharacterLeft", getImagePath(0));
        charSkinMap.put("MainCharacterRight", getImagePath(1));
        charSkinMap.put("MainCharacterUp", getImagePath(2));
        charSkinMap.put("MainCharacterDown", getImagePath(3)); 
    }

    public void isKillByGuards(ArrayList<Character> charArray) {
        for (Character character : charArray) {
            if (getX() == character.getX() && 
                getY() == character.getY() &&
                character instanceof Guard) {
                    
                isAlive = false;
            }
        }
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void addThisPositionInMemory() {memoryPosition.add(new Point(getX(),getY()));}

    public Map<String, String> getCharSkinMap() {return charSkinMap;}

    public ArrayList<Point> getMemoryPostion() {return memoryPosition;}

    public void setAlive() {isAlive=true;}
}
