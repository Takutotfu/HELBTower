
public class Teleporter extends GameElement {
    private int portal2PosX;
    private int portal2PosY;
    private int skinIndex;

    public Teleporter(int posX, int posY, int posX2, int posY2, int skinIndex) {
        super(posX, posY, new String[]{ "img/portalRed.png",
                                        "img/portalBlue.png" });
        portal2PosX = posX2;
        portal2PosY = posY2;

        this.skinIndex = skinIndex;
    }
    
    public int getPortalX(int portal) {
        if (portal == 2) {
            return portal2PosX;
        } else {
            return getPosX();
        }
    }

    public int getPortalY(int portal) {
        if (portal == 2) {
            return portal2PosY;
        } else {
            return getPosY();
        }
    }

    @Override
    public String getPathToImage(){
        return getPathToImage(skinIndex);
    }

    @Override
    public void triggerAction(HelbTowerModel gameBoard) {
        
    }
}
