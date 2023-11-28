import java.awt.Point;
import java.util.ArrayList;
import java.util.Map;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class HelbTowerView {
    private int width;
    private int height;
    private int rows;
    private int columns;
    private int squareSize;

    public HelbTowerView(int width, int height, int rows, int columns, int squareSize) {
        this.width = width;
        this.height = height;
        this.rows = rows;
        this.columns = columns;
        this.squareSize = squareSize;
    }

    public void gameOver(GraphicsContext gc) {
        gc.setFill(Color.RED);
        gc.setFont(new Font("Digital-7", 70));
        gc.fillText("Game Over", width / 3.5, height / 2);
        return;
    }

    public void drawBackground(GraphicsContext gc) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if ((i + j) % 2 == 0) {
                    gc.setFill(Color.web("ffffff"));
                } else {
                    gc.setFill(Color.web("f1f1f1"));
                }
                gc.fillRect(i * squareSize, j * squareSize, squareSize, squareSize);
            }
        }
    }

    public void drawChar(Point charHead, GraphicsContext gc) {
        gc.setFill(Color.web("ff8800"));
        gc.fillRoundRect(charHead.getX() * squareSize, charHead.getY() * squareSize, squareSize - 1, squareSize - 1, 35, 35);
    }

    public void drawScore(int score, GraphicsContext gc) {
        gc.setFill(Color.BLACK);
        gc.setFont(new Font("Digital-7", 35));
        gc.fillText("Score: " + score, 10, 35);
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
