import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class HelbTowerView {
    private int width, height, rows, columns, squareSize;

    private Image grassTexture = new Image("img/grass.png");
    private Image grass2Texture = new Image("img/grass2.png");

    private Image currentSkin;
    private Map<String, Image> gameElemImgMap = new HashMap<>();
    private Map<String, Image> charImgMap = new HashMap<>();

    public HelbTowerView(int width, int height, int rows, int columns, int squareSize) {
        this.width = width;
        this.height = height;
        this.rows = rows;
        this.columns = columns;
        this.squareSize = squareSize;
    }

    public void convertPathToImage(ArrayList<GameElement> gameElementList,
                                   ArrayList<Character> charList,
                                   Map<String, String> pathToCharSkin) {

        for (GameElement gameElem : gameElementList) {
            if (gameElem instanceof Teleporter) {
                if (((Teleporter) gameElem).getColor().equals("Red")) {
                    gameElemImgMap.put(gameElem.getClass().getName() + "Red", new Image(gameElem.getPathToImage()));
                } else {
                    gameElemImgMap.put(gameElem.getClass().getName() + "Blue", new Image(gameElem.getPathToImage()));
                }
            }
            gameElemImgMap.put(gameElem.getClass().getName(), new Image(gameElem.getPathToImage()));
        }

        for (Character character : charList) {
            charImgMap.put(character.getClass().getName() + "Down",
                           new Image(pathToCharSkin.get(character.getClass().getName() + "Down")));
            charImgMap.put(character.getClass().getName() + "Up",
                           new Image(pathToCharSkin.get(character.getClass().getName() + "Up")));
            charImgMap.put(character.getClass().getName() + "Right",
                           new Image(pathToCharSkin.get(character.getClass().getName() + "Right")));
            charImgMap.put(character.getClass().getName() + "Left",
                           new Image(pathToCharSkin.get(character.getClass().getName() + "Left")));
        }

    }

    public void drawPortal(ArrayList<Teleporter> portalList, GraphicsContext gc) {
        for (Teleporter teleporter : portalList) {
            gc.drawImage(gameElemImgMap.get(teleporter.getClass().getName()),
                        teleporter.getPosX() * squareSize,
                        teleporter.getPosY() * squareSize,
                        squareSize,
                        squareSize);
            gc.drawImage(gameElemImgMap.get(teleporter.getClass().getName()),
                        teleporter.getPortal2X() * squareSize,
                        teleporter.getPortal2Y() * squareSize,
                        squareSize,
                        squareSize);
        }
    } 

    public void drawGameOver(GraphicsContext gc) {
        gc.setFill(Color.RED);
        gc.setFont(new Font("Digital-7", 70));
        gc.fillText("Game Over!", width / 3, height / 2);
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

    public void drawChar(ArrayList<Character> charList, GraphicsContext gc) {

        for (Character character : charList) {
            if (character.getCurrentDirection() == Character.DOWN) {
                currentSkin = charImgMap.get(character.getClass().getName()+"Down");
            } else if (character.getCurrentDirection() == Character.UP) {
                currentSkin = charImgMap.get(character.getClass().getName()+"Up");
            } else if (character.getCurrentDirection() == Character.RIGHT) {
                currentSkin = charImgMap.get(character.getClass().getName()+"Right");
            } else {
                currentSkin = charImgMap.get(character.getClass().getName()+"Left");
            }

            gc.drawImage(currentSkin,
                         character.getX() * squareSize,
                         character.getY() * squareSize,
                         squareSize,
                         squareSize);
        }
    }

    public void drawScore(int score, int coinCounter, GraphicsContext gc) {
        gc.setFill(Color.YELLOW);
        gc.setFont(new Font("IMPACT", 20));
        gc.fillText("Score: " + score, 10, 20);
        gc.fillText("Coin: " + coinCounter, width - 100, 20);
    }

    public void drawGameElements(ArrayList<GameElement> gameElementList, GraphicsContext gc) {
        for (GameElement gameElem : gameElementList) {
            if (gameElem instanceof Teleporter) {
                Teleporter teleporter = (Teleporter) gameElem;
                String color = teleporter.getColor().equals("Red") ? "Red" : "Blue";
                gc.drawImage(gameElemImgMap.get(teleporter.getClass().getName() + color),
                        teleporter.getPosX() * squareSize,
                        teleporter.getPosY() * squareSize,
                        squareSize,
                        squareSize);
                gc.drawImage(gameElemImgMap.get(teleporter.getClass().getName() + color),
                        teleporter.getPortal2X() * squareSize,
                        teleporter.getPortal2Y() * squareSize,
                        squareSize,
                        squareSize);
                
            } else {
                gc.drawImage(gameElemImgMap.get(gameElem.getClass().getName()),
                        gameElem.getPosX() * squareSize,
                        gameElem.getPosY() * squareSize,
                        squareSize,
                        squareSize);
            }
        }
    }
}
