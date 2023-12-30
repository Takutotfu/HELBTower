public class Wall extends GameElement {

    public Wall(int posX, int posY) {
        super(posX, posY, new String[]{"img/wall.jpg"});
    }

    @Override
    public void triggerAction(HelbTowerModel gameBoard) {}

}
