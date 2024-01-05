import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

// Classe abstraite représentant un gardien dans le jeu
public abstract class Guard extends Character {
    private boolean isAlive = false;  // Indique si le gardien est en vie
    private HashMap<String, String> charSkinMap = new HashMap<>();  // Map des skins du gardien

    // Constructeur
    public Guard(int x, int y, String[] imagePaths) {
        super(x, y, imagePaths);

        // Initialisation de la map des skins du gardien
        charSkinMap.put(getClass().getName()+"Left", getImagePath(0));
        charSkinMap.put(getClass().getName()+"Right", getImagePath(1));
        charSkinMap.put(getClass().getName()+"Up", getImagePath(2));
        charSkinMap.put(getClass().getName()+"Down", getImagePath(3));

        setDown();  // Initialisation de la direction vers le bas
    }

    // Méthode abstraite pour définir le déplacement du gardien
    protected abstract void move(ArrayList<GameElement> gameElementList);
    
    // Vérifie si la position (x, y) est un déplacement valide (pas un mur ou un téléporteur)
    protected boolean isMoveOk(ArrayList<GameElement> gameElementList, int x, int y) {
        for (GameElement gameElement : gameElementList) {
            if (gameElement instanceof Wall || gameElement instanceof Teleporter) {
                if (gameElement.getPosX() == x
                        && gameElement.getPosY() == y) {
                    return false;
                }
            } else if (gameElement instanceof Teleporter) {
                Teleporter tp = (Teleporter) gameElement;

                if (tp.getPortal2X() == x && tp.getPortal2Y() == y) {
                    return false;
                }
            }
        }
        return true;
    }
    
    // Initialise le gardien en tant que vivant et le sort en dehors de la tour
    public void setAlive() {isAlive = true; moveRight();}

    // Désactive le gardien
    public void unsetAlive() {isAlive = false;}

    // Vérifie si le gardien est activé
    public boolean isAlive() {return isAlive;}

    // Récupère la map des skins du gardien
    public Map<String, String> getCharSkinMap() {return charSkinMap;}
}
