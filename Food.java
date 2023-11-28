public class Food extends GameElement{

    private int currentImageIndex;
    private int foodScoreValue = 5;
    
    public Food(int posX, int posY) {
        super(posX, posY, new String[]{"/img/ic_orange.png", 
                                       "/img/ic_apple.png"});
    }
    
    public void setFoodImage(){
        currentImageIndex = 
            (int) (Math.random() * getPathToImageLen());
    }
    
    @Override
    public String getPathToImage(){
        return getPathToImage(currentImageIndex);
    }
    
    @Override
    public void triggerAction(HelbTowerModel gameBoard){
        gameBoard.setScore(gameBoard.getScore()+foodScoreValue);
        gameBoard.generateFood();
    }
}
