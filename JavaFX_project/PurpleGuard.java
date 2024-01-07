import java.awt.Point;
import java.util.ArrayList;

// Classe représentant un garde violet dans le jeu
public class PurpleGuard extends Guard {
    private boolean currentDirectionHasReached = true; // Indique si la direction actuelle a été atteinte
    private ArrayList<Point> towersPosition = new ArrayList<>(); // Positions des tours
    private Point currentDirection; // Direction actuelle vers laquelle se déplacer

    // Constructeur
    public PurpleGuard(int x, int y, int towerX, int towerY, int rows, int columns) {
        super(x, y, new String[]{ "/img/purpleGuardLeft.png",
                                      "/img/purpleGuardRight.png",
                                      "/img/purpleGuardUp.png",
                                      "/img/purpleGuardDown.png" });
        
        // Initialisation des positions des tours
        towersPosition.add(new Point(towerX-2, towerY-2)); // Tour en haut à gauche
        towersPosition.add(new Point(rows-towerX+1, towerY-2)); // Tour en haut à droite
        towersPosition.add(new Point(x-2, columns-towerY)); // Tour en bas à gauche
        towersPosition.add(new Point(x+1, columns-towerY)); // Tour en bas à droite
    }

    // Méthode pour gérer le déplacement du garde
    @Override
    public void move(ArrayList<GameElement> gameElementList) {
        if (isAlive()) {
            if (currentDirectionHasReached) {
                setRandomDirection(); // Définir une nouvelle direction aléatoire
            } else {
                moveTowards(gameElementList); // Se déplacer vers la direction définie
            }
        }
    }

    // Méthode pour déplacer le garde vers la direction définie
    public void moveTowards(ArrayList<GameElement> gameElementList) {
        
        // Déplacer le garde en fonction de la currentDirection
        if (currentDirection.getX() < getX() && isMoveOk(gameElementList, getX()-1, getY())) {
            moveLeft();
        } else if (currentDirection.getX() > getX() && isMoveOk(gameElementList, getX()+1, getY())) {
            moveRight();
        } else if (currentDirection.getY() > getY() && isMoveOk(gameElementList, getX(), getY()+1)) {
            moveDown();
        } else if (currentDirection.getY() < getY() && isMoveOk(gameElementList, getX(), getY()-1)) {
            moveUp();
        } else {
            // Si la currentDirection est inatteignable, on la change
            currentDirectionHasReached = true;
        }
        
        // Vérifier si la currentDirection est atteinte
        if (currentDirection.getX() == getX() && currentDirection.getY() == getY()) {
            currentDirectionHasReached = true;
        }
    }

    // Méthode pour définir une nouvelle direction aléatoire
    private void setRandomDirection() {
        int random = (int) (Math.random() * 4);
        switch (random) {
            case 0:
                currentDirection = towersPosition.get(0);
                break;
            
            case 1:
                currentDirection = towersPosition.get(1);
                break;
            
            case 2:
                currentDirection = towersPosition.get(2);
                break;
            
            case 3:
                currentDirection = towersPosition.get(3);
                break;
        }
        
        currentDirectionHasReached = false;
    }
}
