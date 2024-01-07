
// Classe représentant un élément potion dans le jeu
public class Potion extends GameElement {
    private int duration; // Durée de l'effet (en millisecondes)
    private int skinIndex; // Indice de l'apparence de la potion
    private String color; // Couleur de la potion
    private boolean isTaked; // Indique si la potion a été ramassée

    // Constructeur
    public Potion(int x, int y, int currentSkinIndex) {
        super(x, y, new String[]{ "/img/redPotion.png",
                                  "/img/orangePotion.png",
                                  "/img/yellowPotion.png" });

        // Initialisation des propriétés en fonction de la couleur
        this.isTaked = false;
        this.skinIndex = currentSkinIndex;

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
    
    // Méthode déclenchée lorsqu'un personnage interagit avec la potion
    @Override
    public void triggerAction(GameBoard gameBoard) {
        // Marque la potion comme ramassée
        setTaked();

        // Déplace la pièce vers une position vide sur le plateau
        setPosX(gameBoard.getVoidX());
        setPosY(gameBoard.getVoidY());

        gameBoard.triggerPotion(this);;
    }

    // Méthode renvoyant la couleur de la potion
    public String getColor() {return color;}

    // Méthode renvoyant la durée de l'effet de la potion
    public int getDuration() {return duration;}

    // Méthode indiquant si la potion a été ramassée
    public boolean isTaked() {return isTaked;}

    // Méthode pour marquer la potion comme ramassée
    public void setTaked() {isTaked = true;}

    // Méthode pour marquer la potion comme non ramassée
    public void unsetTaked() {isTaked = false;}

    // Méthode renvoyant le chemin de l'image de la potion
    @Override
    public String getPathToImage() {return getPathToImage(skinIndex);}
}
