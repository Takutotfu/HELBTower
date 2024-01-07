import java.util.ArrayList;

public abstract class Character {
    // Chemins vers les images du personnage
    private final String[] IMAGE_PATHS;

    // Constantes représentant les directions
    // En public et static afin d'être atteignable par toutes les autres classes
    // Sans avoir besoin d'une instance de la classe.
    public static final int RIGHT = 0;
    public static final int LEFT = 1;
    public static final int DOWN = 2;
    public static final int UP = 3;

    // Position en X et Y du personnage
    private int posX, posY;

    // Direction actuelle du personnage
    private int direction;

    // Constructeur
    public Character(int posX, int posY, String[] imagePaths) {
        this.posX = posX;
        this.posY = posY;
        this.IMAGE_PATHS = imagePaths;
        direction = RIGHT;
    }

    // Vérifie si la case suivante est disponible pour le déplacement
    public boolean isNextCaseNotAWall(ArrayList<GameElement> gameElementsList) {
        for (GameElement gameElem : gameElementsList) {
            if (gameElem instanceof Wall) {
                if ((posX - 1 == gameElem.getPosX() && posY == gameElem.getPosY() && direction == LEFT) || // direction gauche
                        (posX + 1 == gameElem.getPosX() && posY == gameElem.getPosY() && direction == RIGHT) || // direction droite
                        (posY - 1 == gameElem.getPosY() && posX == gameElem.getPosX() && direction == UP) || // direction haut
                        (posY + 1 == gameElem.getPosY() && posX == gameElem.getPosX() && direction == DOWN)) { // direction bas
                    return false;
                }
            }
        }
        return true;
    }

    // Obtient le chemin d'une image spécifique du personnage
    public String getImagePath(int index) {return IMAGE_PATHS[index];}

    // Obtient la position en X du personnage
    public int getX() {return posX;}

    // Obtient la position en Y du personnage
    public int getY() {return posY;}

    // Définit la position du personnage (version avec des doubles)
    public void setLocation(double x, double y) {posX = (int) x; posY = (int) y;}

    // Déplace le personnage vers la droite
    public void moveRight() {posX++; direction = RIGHT;}

    // Déplace le personnage vers la gauche
    public void moveLeft() {posX--; direction = LEFT;}

    // Déplace le personnage vers le haut
    public void moveUp() {posY--; direction = UP;}

    // Déplace le personnage vers le bas
    public void moveDown() {posY++; direction = DOWN;}

    // Définit la direction du personnage vers la gauche
    public void setLeft() {direction = LEFT;}

    // Définit la direction du personnage vers la droite
    public void setRight() {direction = RIGHT;}

    // Définit la direction du personnage vers le haut
    public void setUp() {direction = UP;}

    // Définit la direction du personnage vers le bas
    public void setDown() {direction = DOWN;}

    // Obtient la direction actuelle du personnage
    public int getCurrentDirection() {return direction;}
}
