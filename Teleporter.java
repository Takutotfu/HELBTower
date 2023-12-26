
public class Teleporter extends GameElement {
    private int portal2PosX;
    private int portal2PosY;
    private int skinIndex;
    private String color;

    public Teleporter(int posX, int posY, int posX2, int posY2, int skinIndex) {
        super(posX, posY, new String[]{ "img/portalRed.png",
                                        "img/portalBlue.png" });
        portal2PosX = posX2;
        portal2PosY = posY2;

        this.skinIndex = skinIndex;
    }
    
    public int getPortal2X() {return portal2PosX;}

    public int getPortal2Y() {return portal2PosY;}

    public String getColor() {return color = skinIndex == 0 ? "Red" : "Blue";}

    @Override
    public String getPathToImage(){return getPathToImage(skinIndex);}

    public void triggerAction(HelbTowerModel gameBoard) {}
}
