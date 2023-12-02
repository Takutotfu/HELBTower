import java.awt.Point;
import java.util.HashMap;

public class Teleporter {
    private Point portalTop, portalDown, portalLeft, portalRight;
    private HashMap<String, Point> portalHashMap = new HashMap<>();
    private String[] pathToImgs = {"img/portalBlue.png", "img/portalRed.png"};

    public Teleporter(int rows, int columns) {
        portalTop = new Point((rows - 1) / 2, 0);
        portalDown = new Point((rows - 1) / 2, columns - 1);
        portalLeft = new Point(0, (columns - 1) / 2);
        portalRight = new Point(rows - 1, (columns - 1) / 2);
        portalHashMap.put("portalTop", portalTop);
        portalHashMap.put("portalDown", portalDown);
        portalHashMap.put("portalLeft", portalLeft);
        portalHashMap.put("portalRight", portalRight);
    }

    public void triggerPortal(Point charHead) {
        if (charHead.getX() == portalDown.getX() && charHead.getY() == portalDown.getY()) {
            charHead.setLocation(portalTop.getX(), portalTop.getY() + 1);
        } else if (charHead.getX() == portalTop.getX() && charHead.getY() == portalTop.getY()) {
            charHead.setLocation(portalDown.getX(), portalDown.getY() - 1);
        } else if (charHead.getX() == portalLeft.getX() && charHead.getY() == portalLeft.getY()) {
            charHead.setLocation(portalRight.getX() - 1, portalRight.getY());
        } else if (charHead.getX() == portalRight.getX() && charHead.getY() == portalRight.getY()) {
            charHead.setLocation(portalLeft.getX() + 1, portalLeft.getY());
        }
    }

    public HashMap<String, Point> getPortalHashMap() {
        return portalHashMap;
    }

    public String getPathToImages(String color) {
        switch (color) {
            case "red":
                return pathToImgs[1];
        
            case "blue":
                return pathToImgs[0];
            
            default:
                return "";
        }
    }
}
