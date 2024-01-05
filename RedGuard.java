import java.awt.Point;
import java.util.ArrayList;

// Classe représentant un garde rouge dans le jeu
public class RedGuard extends Guard {
    MainCharacter mainCharacter; // Personnage principal suivi par le garde
    Point currentDirection; // Direction actuelle vers laquelle se déplacer

    // Constructeur
    public RedGuard(int x, int y, MainCharacter mainCharacter) {
        super(x, y, new String[]{ "/img/redGuardLeft.png",
                                  "/img/redGuardRight.png",
                                  "/img/redGuardUp.png",
                                  "/img/redGuardDown.png" });

        this.mainCharacter = mainCharacter;
    }

    // Méthode pour gérer le déplacement du garde
    @Override
    public void move(ArrayList<GameElement> gameElementList) {
        verifCurrentDirectionPosition(mainCharacter); // Vérifier la position actuelle du personnage principal
        if (isAlive()) {
            // Déplacer le garde en fonction de la currentDirection
            if (currentDirection.getX() < getX() && isMoveOk(gameElementList, getX() - 1, getY())) {
                moveLeft();
            } else if (currentDirection.getX() > getX() && isMoveOk(gameElementList, getX() + 1, getY())) {
                moveRight();
            } else if (currentDirection.getY() > getY() && isMoveOk(gameElementList, getX(), getY() + 1)) {
                moveDown();
            } else if (currentDirection.getY() < getY() && isMoveOk(gameElementList, getX(), getY() - 1)) {
                moveUp();
            }
        }
    }

    // Méthode pour vérifier la position actuelle du personnage principal
    private void verifCurrentDirectionPosition(MainCharacter mainCharacter) {
        currentDirection = new Point(mainCharacter.getX(), mainCharacter.getY());
    }
}

