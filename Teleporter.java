import java.awt.Point;

public class Teleporter {
    int posX;
    int posY;
    Point charHead;

    public Teleporter(int posX, int posY, Point charHead) {
        this.posX = posX;
        this.posY = posY;
        this.charHead = charHead;
    }

    public void portal(int rightBorder, int bottomBorder) {
        System.out.println(posX);
        if (charHead.getX() < 1) {
            charHead.setLocation(rightBorder, charHead.getY());
        } else if (charHead.getX() > rightBorder) {
            charHead.setLocation(1, charHead.getY());
        } else if (charHead.getY() < 1) {
            charHead.setLocation(bottomBorder, charHead.getX());
        } else if (charHead.getY() > bottomBorder) {
            charHead.setLocation(1, charHead.getX());
        }
    }

    public int getX() {
        return posX;
    }

    public int getY() {
        return posY;
    }
}
