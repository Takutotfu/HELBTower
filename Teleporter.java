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
        if (charHead.getX() < 1) {
            charHead.setLocation(rightBorder, posY);
        } else if (charHead.getX() > rightBorder) {
            charHead.setLocation(1, posY);
        } else if (charHead.getY() < 1) {
            charHead.setLocation(posX, bottomBorder);
        } else if (charHead.getY() > bottomBorder) {
            charHead.setLocation(posX, 1);
        }
    }

    public int getX() {
        return posX;
    }

    public int getY() {
        return posY;
    }
}
