import java.util.ArrayList;

// Classe représentant un garde orange dans le jeu
public class OrangeGuard extends Guard {

    // Constructeur
    public OrangeGuard(int x, int y) {
        super(x, y, new String[] { "/img/orangeGuardLeft.png",
                                   "/img/orangeGuardRight.png",
                                   "/img/orangeGuardUp.png",
                                   "/img/orangeGuardDown.png" });
    }

    // Redéfinition de la méthode move de la superclasse
    @Override
    public void move(ArrayList<GameElement> gameElementList) {
        // Vérifie si le garde est en vie
        if (isAlive()) {
            // Génère un nombre aléatoire entre 0 et 3 inclus
            int random = (int) (Math.random() * 4);

            // Effectue le déplacement en fonction du nombre aléatoire généré
            switch (random) {
                case 0:
                    setRight();
                    if (isMoveOk(gameElementList, getX() + 1, getY())) {
                        moveRight();
                    }
                    break;

                case 1:
                    setLeft();
                    if (isMoveOk(gameElementList, getX() - 1, getY())) {
                        moveLeft();
                    }
                    break;

                case 2:
                    setUp();
                    if (isMoveOk(gameElementList, getX(), getY() - 1)) {
                        moveUp();
                    }
                    break;

                case 3:
                    setDown();
                    if (isMoveOk(gameElementList, getX(), getY() + 1)) {
                        moveDown();
                    }
                    break;
            }
        }
    }
}
