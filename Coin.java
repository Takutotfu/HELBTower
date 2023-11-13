public class Coin extends GameElement {
    public int counter = 5;
    
    public Coin(int posX, int posY){
        super(posX, posY, new String[]{"/img/ic_coin.png"});
    }

    @Override
    public void triggerAction(Model gameBoard){

        counter--;
        if (counter == 0){
            gameBoard.setGameOver();
        }
        setPosX(gameBoard.getVoidX()) ; 
        setPosY(gameBoard.getVoidY()) ;
    }
}





