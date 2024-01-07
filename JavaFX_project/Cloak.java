public class Cloak extends GameElement {
    
    // Indique si la cape a été prise par un personnage
    private boolean isTaked = false;

    // Constructeur
    public Cloak(int posX, int posY) {
        super(posX, posY, new String[]{"/img/cloak.png"});
    }

    // Déclenche l'action associée à la cape
    @Override
    public void triggerAction(GameBoard gameBoard) {
        // Marque la cape comme prise
        isTaked = true;
        // Déplace la cape vers une position vide sur le plateau
        setPosX(gameBoard.getVoidX());
        setPosY(gameBoard.getVoidY());

        gameBoard.triggerCloak(this);
    }

    // Vérifie si la cape a été prise
    public boolean isTaked() {return isTaked;}

    // Réinitialise l'état de la cape (non prise)
    public void unsetTaked() {isTaked = false;}
}
