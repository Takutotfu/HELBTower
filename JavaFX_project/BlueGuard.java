import java.awt.Point;
import java.util.ArrayList;

public class BlueGuard extends Guard {
    private int rows;
    private int columns;

    private boolean currentDirectionHasReached = true;
    private Point currentDirection;
    private ArrayList<Point> memoryPosition = new ArrayList<>();

    // Constructeur
    public BlueGuard(int x, int y, int rows, int columns) {
        // Appel du constructeur de la superclasse (Guard)
        super(x, y, new String[]{ "/img/blueGuardLeft.png",
                                  "/img/blueGuardRight.png",
                                  "/img/blueGuardUp.png",
                                  "/img/blueGuardDown.png" });
        this.rows = rows;
        this.columns = columns;
    }

    // Redéfinition de la méthode move de la superclasse
    @Override
    public void move(ArrayList<GameElement> gameElementList) {
        if (isAlive()) {
            int maxCase = 0;

            // Compter le nombre de cases disponibles (hors murs)
            for (GameElement gameElem : gameElementList) {
                if (!(gameElem instanceof Wall)) {
                    maxCase++;
                }
            }

            // Vider la mémoire si elle est pleine
            if (memoryPosition.size() == maxCase) {
                memoryPosition.clear();
            }

            // Si la direction actuelle a été atteinte, définir une nouvelle direction aléatoire
            if (currentDirectionHasReached) {
                setRandomDirection(gameElementList, rows, columns);
            } else {
                moveTowards(gameElementList);
            }
        }
    }

    // Méthode pour se déplacer vers la direction actuelle
    public void moveTowards(ArrayList<GameElement> gameElementList) {
        // Vérifier si la direction actuelle a été atteinte
        if (currentDirection.getX() == getX() && currentDirection.getY() == getY()) {
            currentDirectionHasReached = true;
            memoryPosition.add(currentDirection);
        }

        // Se déplacer vers la direction actuelle
        if (currentDirection.getX() < getX() && isMoveOk(gameElementList, getX()-1, getY())) {
            moveLeft();
        } else if (currentDirection.getX() > getX() && isMoveOk(gameElementList, getX()+1, getY())) {
            moveRight();
        } else if (currentDirection.getY() > getY() && isMoveOk(gameElementList, getX(), getY()+1)) {
            moveDown();
        } else if (currentDirection.getY() < getY() && isMoveOk(gameElementList, getX(), getY()-1)) {
            moveUp();
        } else {
            // Si la direction est inatteignable, régénérer et sauvegarder la position actuelle
            currentDirectionHasReached = true;
            memoryPosition.add(new Point(getX(), getY()));
        }
    }

    // Méthode pour définir une direction aléatoire
    private void setRandomDirection(ArrayList<GameElement> gameElementList, int rows, int columns) {
        // Boucle pour trouver une position aléatoire valide
        start:
        while (true) {
            int randomXPos = (int) (Math.random() * rows);
            int randomYPos = (int) (Math.random() * columns);

            // Vérifier si la position est valide
            if (!(isMoveOk(gameElementList, randomXPos, randomYPos))) {
                continue start;
            }
            
            // Vérifier si la position n'est pas en mémoire
            for (Point position : memoryPosition) {
                if (position.getX() == randomXPos && position.getY() == randomYPos) {
                    continue start;
                }
            }

            // Définir la nouvelle direction et la marquer comme non atteinte
            currentDirection = new Point(randomXPos, randomYPos);
            currentDirectionHasReached = false;
            break;
        }
    }   
}
