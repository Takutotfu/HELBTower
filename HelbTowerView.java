import java.awt.Point;
import java.util.ArrayList;
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

    public void gameOver(GraphicsContext gc) {
        gc.setFill(Color.RED);
        gc.setFont(new Font("Digital-7", 70));
        gc.fillText("Game Over", width / 3.5, height / 2);
        return;
    }

    public void drawBackground(GraphicsContext gc, int tpX, int tpY) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if ((i == tpX && j == 0) || (i == tpX && j == columns - 1)) { // teleporters
                    gc.setFill(Color.web("2277ea"));
                } else if ((i == 0 && j == tpY) || (i == rows - 1 && j == tpY)) {
                    gc.setFill(Color.web("ea2522"));
                } else if (i == 0 || j == 0 || i == rows - 1 || j == columns - 1) {
                    gc.setFill(Color.web("#935900")); // borders
                } else if ((i + j) % 2 == 0) {
                    gc.setFill(Color.web("ffffff"));
                } else {
                    gc.setFill(Color.web("f1f1f1"));
                }
                gc.fillRect(i * squareSize, j * squareSize, squareSize, squareSize);
            }
        }
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

    public void drawGameElements(ArrayList<GameElement> gameElementList, Map<String, Image> imageMap, GraphicsContext gc) {
        for (GameElement gameElem : gameElementList) {
            gc.drawImage(imageMap.get(gameElem.getClass().getName()), 
                         gameElem.getPosX() * squareSize, 
                         gameElem.getPosY() * squareSize, 
                         squareSize, 
                         squareSize);
        }
    }
}
