import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class HelbTowerView {
    private int width, height, rows, columns, squareSize;
    private int right, left, down, up;

    private Image charSkinDown = new Image("img/characterDown.png");
    private Image charSkinUp = new Image("img/characterUp.png");
    private Image charSkinRight = new Image("img/characterRight.png");
    private Image charSkinLeft = new Image("img/characterLeft.png");

    private Image grassTexture = new Image("img/grass.png");
    private Image grass2Texture = new Image("img/grass2.png");

    public HelbTowerView(int width, int height, int rows, int columns, int squareSize, 
                         int right, int left, int down, int up) {
        this.width = width;
        this.height = height;
        this.rows = rows;
        this.columns = columns;
        this.squareSize = squareSize;
        this.right = right;
        this.left = left;
        this.down = down;
        this.up = up;
    }

    public void drawGameOver(GraphicsContext gc) {
        gc.setFill(Color.BLACK);
        gc.setFont(new Font("Digital-7", 70));
        gc.fillText("GG you win !", width / 4, height / 2);
        return;
    }

    public void drawBackground(GraphicsContext gc) {
        Image currentImage;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if ((i + j) % 2 == 0) {
                    currentImage = grassTexture;
                } else {
                    currentImage = grass2Texture;
                }
                gc.drawImage(currentImage, i * squareSize, j * squareSize, squareSize, squareSize);
            }
        }
    }

    public void drawWall(ArrayList<Point> walls, String pathToImg, GraphicsContext gc) {
        Image wallSkin = new Image(pathToImg);
        for (Point wall : walls) {
            gc.drawImage(wallSkin, 
                         wall.getX() * squareSize, 
                         wall.getY() * squareSize, 
                         squareSize, 
                         squareSize);
        }
    }

    public void drawTelporter(HashMap<String, Point> teleporters, String redSkinPath, String blueSkinPath, GraphicsContext gc) {
        Image redSkin = new Image(redSkinPath);
        Image blueSkin = new Image(blueSkinPath);

        gc.drawImage(blueSkin, 
                     teleporters.get("portalTop").getX() * squareSize, 
                     teleporters.get("portalTop").getY() * squareSize, 
                     squareSize, 
                     squareSize);
        gc.drawImage(blueSkin, 
                     teleporters.get("portalDown").getX() * squareSize, 
                     teleporters.get("portalDown").getY() * squareSize, 
                     squareSize, 
                     squareSize);
        gc.drawImage(redSkin, 
                     teleporters.get("portalLeft").getX() * squareSize, 
                     teleporters.get("portalLeft").getY() * squareSize, 
                     squareSize, 
                     squareSize);
        gc.drawImage(redSkin, 
                     teleporters.get("portalRight").getX() * squareSize, 
                     teleporters.get("portalRight").getY() * squareSize, 
                     squareSize, 
                     squareSize);
    }

    public void drawChar(Point charHead, int currentDirection, GraphicsContext gc) {
        Image currentSkin = charSkinDown;
        
        if (currentDirection == down) {
            currentSkin = charSkinDown;
        } else if (currentDirection == up) {
            currentSkin = charSkinUp;
        } else if (currentDirection == right) {
            currentSkin = charSkinRight;
        } else if (currentDirection == left) {
            currentSkin = charSkinLeft;
        }

        gc.drawImage(currentSkin, 
                     charHead.getX() * squareSize, 
                     charHead.getY() * squareSize, 
                     squareSize, 
                     squareSize);
    }

    public void drawScore(int score, int coinCounter, GraphicsContext gc) {
        gc.setFill(Color.BLACK);
        gc.setFont(new Font("Digital-7", 35));
        gc.fillText("Score: " + score, 10, 35);
        gc.fillText("Coin: " + coinCounter, width - 150, 35);
    }

    public void drawGameElements(ArrayList<GameElement> gameElementList, Map<String, String> pathToImageMap, GraphicsContext gc) {
        for (GameElement gameElem : gameElementList) {
            Image currentImage = new Image(pathToImageMap.get(gameElem.getClass().getName()));
            gc.drawImage(currentImage, 
                         gameElem.getPosX() * squareSize, 
                         gameElem.getPosY() * squareSize, 
                         squareSize, 
                         squareSize);
        }
    }
}
