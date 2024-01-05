public class Chronometer extends GameElement{

    private final int POWER = 5; // How much case teleportation
    private final int DURATION = 10000; // Duration of effect
    private final int DELAY = 500; // On guard effect

    private boolean isTaken = false;
    
    public Chronometer(int posX, int posY) {
        super(posX, posY, new String[]{"/img/chronometer.png"});
    }
    
    @Override
    public void triggerAction(HelbTowerModel gameBoard) {
        isTaken = true;
        setPosX(gameBoard.getVoidX());
        setPosY(gameBoard.getVoidY());
    }

    public boolean isTaked() {return isTaken;}

    public void unsetTaked() {isTaken = false;}

    public int getPower() {return POWER;}

    public int getDuration() {return DURATION;}

    public int getDelay() {return DELAY;}
}
