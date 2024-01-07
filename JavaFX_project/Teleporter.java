
// Classe représentant un téléporteur dans le jeu, héritant de la classe GameElement
public class Teleporter extends GameElement {
    private int portal2PosX; // Position X du deuxième portail
    private int portal2PosY; // Position Y du deuxième portail
    private int skinIndex; // Index de l'apparence du téléporteur

    // Constructeur
    public Teleporter(int posX, int posY, int posX2, int posY2, int skinIndex) {
        super(posX, posY, new String[]{ "img/portalRed.png",
                                        "img/portalBlue.png" });
        portal2PosX = posX2;
        portal2PosY = posY2;
        this.skinIndex = skinIndex;
    }
    
    // Méthode pour obtenir la position X du deuxième portail
    public int getPortal2X() {return portal2PosX;}

    // Méthode pour obtenir la position Y du deuxième portail
    public int getPortal2Y() {return portal2PosY;}

    // Méthode pour obtenir la couleur du téléporteur
    public String getColor() {return skinIndex == 0 ? "Red" : "Blue";}

    // Méthode pour obtenir le chemin de l'image du téléporteur
    @Override
    public String getPathToImage(){return getPathToImage(skinIndex);}

    // Méthode déclenchée lorsqu'une action est effectuée sur le téléporteur
    public void triggerAction(GameBoard gameBoard) {System.out.println("Tp in other portal");}
}
