public class Cloak extends GameElement {
    private boolean isTaked;

    public Cloak(int posX, int posY) {
        super(posX, posY, new String[]{"/img/cloak.png"});
        isTaked = false;
    }

    @Override
    public void triggerAction(HelbTowerModel gameBoard) {
        isTaked = true;
        setPosX(gameBoard.getVoidX());
        setPosY(gameBoard.getVoidY());
    }

    public boolean isTaked() {return isTaked;}

    public void unsetTaked() {isTaked = false;}
}
