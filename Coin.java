public class Coin extends GameElement {
    
    // Constructeur
    public Coin(int posX, int posY){
        super(posX, posY, new String[]{"/img/coin.png"});
    }
    
    // Déclenche l'action associée à la pièce
    @Override
    public void triggerAction(Model gameBoard) {
        // Diminue le compteur de pièces du plateau
        gameBoard.decreaseCoinCounter();
        
        // Vérifie si toutes les pièces ont été ramassées, si oui déclare le niveau comme terminé
        if (gameBoard.getCoinCounter() == 0){
            gameBoard.setLevelFinished();
        }

        // Déplace la pièce vers une position vide sur le plateau
        setPosX(gameBoard.getVoidX()); 
        setPosY(gameBoard.getVoidY());

        // Incrémente le score du joueur
        gameBoard.setScore(gameBoard.getScore()+1);
    }
}
