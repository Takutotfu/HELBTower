// Classe représentant un mur dans le jeu, héritant de la classe GameElement
public class Wall extends GameElement {

    // Constructeur
    public Wall(int posX, int posY) {
        super(posX, posY, new String[]{"img/wall.jpg"});
    }

    // Méthode déclenchée lorsqu'une action est effectuée sur le mur (ne fait rien dans ce cas)
    @Override
    public void triggerAction(Model gameBoard) {}

}
