import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

// Classe représentant le Hero
public class MainCharacter extends Character {
    private boolean isAlive = true;
    private ArrayList<Point> memoryPosition = new ArrayList<>();
    private Map<String, String> charSkinMap = new HashMap<>();

    // Constructeur
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

    // Tente de déplacer le personnage principal dans la direction spécifiée
    public void tryMove(int direction, ArrayList<GameElement> gameElementList, boolean wearingCloak) {
        int nextX = getX();
        int nextY = getY();
        
        switch (direction) {
            case RIGHT:
                setRight();
                nextX++;
                break;
            case LEFT:
                setLeft();
                nextX--;
                break;
            case UP:
                setUp();
                nextY--;
                break;
            case DOWN:
                setDown();
                nextY++;
                break;
        }
    
        // Vérifie si la prochaine case n'est pas un mur ou si le personnage porte une cape
        if (isNextCaseNotAWall(gameElementList) || (wearingCloak && isValidPosition(direction, nextX, nextY))) {
            // Effectue le déplacement
            switch (direction) {
                case RIGHT:
                    moveRight();
                    break;
                case LEFT:
                    moveLeft();
                    break;
                case UP:
                    moveUp();
                    break;
                case DOWN:
                    moveDown();
                    break;
            }
            setThisPositionInMemory();
        }
    }

    // Vérifie si la position ne dépasse pas les limite de la map
    private boolean isValidPosition(int direction, int x, int y) {
        switch (direction) {
            case RIGHT:
                return x + 1 < Controller.ROWS - 1;
            case LEFT:
                return x - 1 > 0;
            case UP:
                return y - 1 > 0;
            case DOWN:
                return y + 1 < Controller.COLUMNS - 1;
            default:
                throw new IllegalArgumentException("Direction invalide");
        }
    }

    // Vérifie si le personnage principal est tué par des gardes
    public void killByGuard(ArrayList<Character> charArray) {
        for (Character character : charArray) {
            if (getX() == character.getX() && 
                getY() == character.getY() &&
                character instanceof Guard) {
                    
                isAlive = false;
            }
        }
    }

    // Vérifie si le personnage principal est en vie
    public boolean isAlive() {return isAlive;}

    // Sauvegarde la position actuelle du héro dans sa mémoire
    public void setThisPositionInMemory() {memoryPosition.add(new Point(getX(), getY()));}

    // Récupère la map des chemins des skins du personnage principal
    public Map<String, String> getCharSkinMap() {return charSkinMap;}

    // Efface la mémoire du personnage principal
    public void clearMemoryPosition() {memoryPosition.clear();}

    // Récupère la position mémorisée du personnage principal
    public Point getMemoryPostion() {return memoryPosition.get(memoryPosition.size()-Chronometer.getPower());}

    // Redéfinition de la méthode setAlive pour réinitialiser l'état du personnage principal
    public void setAlive() {isAlive = true;}
}
