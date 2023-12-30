public class Coin extends GameElement {
    
    public Coin(int posX, int posY){
        super(posX, posY, new String[]{"/img/coin.png"});
    }
    
    @Override
    public void triggerAction(HelbTowerModel gameBoard) {
        gameBoard.decreaseCoinCounter();
        
        if (gameBoard.getCoinCounter() == 0){
            gameBoard.setLevelFinished();
        }

        setPosX(gameBoard.getVoidX()); 
        setPosY(gameBoard.getVoidY());
        gameBoard.setScore(gameBoard.getScore()+1);
    }
}
