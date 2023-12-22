import java.util.HashMap;
import java.util.Map;

public class MainCharacter extends Character {
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

    public Map<String, String> getCharSkinMap() {return charSkinMap;}
}
