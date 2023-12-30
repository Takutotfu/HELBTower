public class Potion extends GameElement{
    private int duration; // 1000 = 1sec
    private int currentSkinIndex;
    private String color;
    private boolean isTaked;

    public Potion(int x, int y, int currentSkinIndex) {
        super(x, y, new String[]{ "/img/redPotion.png",
                                  "/img/orangePotion.png",
                                  "/img/yellowPotion.png" });
        this.isTaked = false;
        this.currentSkinIndex = currentSkinIndex;

        switch (currentSkinIndex) {
            case 0:
                color = "Red";
                duration = 7000;
                break;
                
            case 1:
                color = "Orange";
                duration = 5000;
                break;

            case 2:
                color = "Yellow";
                duration = 3000;
                break;
        }
    }

    public String getColor() {return color;}

    public int getDuration() {return duration;}

    public boolean isTaked() {return isTaked;}

    public void setTaked() {isTaked = true;}

    public void unsetTaked() {isTaked = false;}

    @Override
    public String getPathToImage() {return getPathToImage(currentSkinIndex);}

    @Override
    public void triggerAction(HelbTowerModel gameBoard) {
        setTaked();
        setPosX(gameBoard.getVoidX());
        setPosY(gameBoard.getVoidY());
        
    }
}
