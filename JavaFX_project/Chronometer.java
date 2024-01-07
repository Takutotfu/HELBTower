public class Chronometer extends GameElement {

    // Puissance du chronomètre (nombre de cases de téléportation)
    private final static int POWER = 5;

    // Durée de l'effet du chronomètre (en millisecondes)
    private final int DURATION = 10000;

    // Délai ajouté sur les gardes (en millisecondes)
    private final int DELAY = 500;

    // Indique si le chronomètre a été pris par le héro
    private boolean isTaken = false;

    // Constructeur
    public Chronometer(int posX, int posY) {
        super(posX, posY, new String[]{"/img/chronometer.png"});
    }

    // Déclenche l'action associée au chronomètre
    @Override
    public void triggerAction(GameBoard gameBoard) {
        isTaken = true;
        // Déplace le chronomètre vers une position vide sur le plateau
        setPosX(gameBoard.getVoidX());
        setPosY(gameBoard.getVoidY());

        gameBoard.triggerChronometer(this);
    }

    // Vérifie si le chronomètre a été pris
    public boolean isTaked() {return isTaken;}

    // Réinitialise l'état du chronomètre (non pris)
    public void unsetTaked() {isTaken = false;}

    // Obtient la puissance du chronomètre
    public static int getPower() {return POWER;}

    // Obtient la durée de l'effet du chronomètre
    public int getDuration() {return DURATION;}

    // Obtient le délai ajouté sur les gardes
    public int getDelay() {return DELAY;}
}
