import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class HelbTowerView {
    private int width, height, rows, columns, squareSize;

    private static final String GRASS_MORNING_PATH = "/img/grassMorning.png";
    private static final String GRASS_2_MORNING_PATH = "/img/grass2Morning.png";

    private static final String GRASS_PATH = "/img/grass.png";
    private static final String GRASS_2_PATH = "/img/grass2.png";

    private static final String GRASS_NIGHT_PATH = "/img/grassNight.png";
    private static final String GRASS_2_NIGHT_PATH = "/img/grass2Night.png";

    private Map<String, String> gameElemPathMap = new HashMap<>();
    private Map<String, String> charPathMap = new HashMap<>();
    private Map<String, Image> imageCache = new HashMap<>();

    public HelbTowerView(int width, int height, int rows, int columns, int squareSize) {
        this.width = width;
        this.height = height;
        this.rows = rows;
        this.columns = columns;
        this.squareSize = squareSize;
    }

    public void loadPaths(ArrayList<GameElement> gameElementList, ArrayList<Character> charList,
                           Map<String, String> pathToCharSkin) {
        loadGameElementPaths(gameElementList);
        loadCharacterPaths(charList, pathToCharSkin);
    }

    private void loadGameElementPaths(ArrayList<GameElement> gameElementList) {
        for (GameElement gameElem : gameElementList) {
            String key = gameElem.getClass().getName();

            if (gameElem instanceof Teleporter) { // Path to teleporter IMG
                key += ((Teleporter) gameElem).getColor();

            } else if (gameElem instanceof Potion) { // Path to Potion IMG
                key += ((Potion) gameElem).getColor();

            }
            gameElemPathMap.put(key, gameElem.getPathToImage());
        }
    }

    private void loadCharacterPaths(ArrayList<Character> charList, Map<String, String> pathToCharSkin) {
        for (Character character : charList) {
            String baseKey = character.getClass().getName();
            charPathMap.put(baseKey + "Down", pathToCharSkin.get(baseKey + "Down"));
            charPathMap.put(baseKey + "Up", pathToCharSkin.get(baseKey + "Up"));
            charPathMap.put(baseKey + "Right", pathToCharSkin.get(baseKey + "Right"));
            charPathMap.put(baseKey + "Left", pathToCharSkin.get(baseKey + "Left"));
        }
    }


    public void drawGameOver(GraphicsContext gc) {
        gc.setFill(Color.RED);
        gc.setFont(new Font("Digital-7", 70));
        gc.fillText("Game Over!", width / 3, height / 2);
        return;
    }

    public void drawBackground(int period, GraphicsContext gc) {
        String currentPath;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                currentPath = getBackgroundPath(period, (i + j) % 2 == 0);
                drawImage(gc, currentPath, i, j);
            }
        }
    }

    private String getBackgroundPath(int period, boolean isEven) {
        if (isEven) {
            // Paire = Texture1
            return period == 0 ? GRASS_MORNING_PATH : (period == 1 ? GRASS_PATH : GRASS_NIGHT_PATH);
        } else {
            // Impaire = Texture2
            return period == 0 ? GRASS_2_MORNING_PATH : (period == 1 ? GRASS_2_PATH : GRASS_2_NIGHT_PATH);
        }
    }

    public void drawChar(ArrayList<Character> charList, GraphicsContext gc) {
        String currentSkin;

        for (Character character : charList) {
            currentSkin = getCurrentSkinPath(character);

            drawImage(gc, currentSkin, character.getX(), character.getY());
        }
    }

    private String getCurrentSkinPath(Character character) {
        String baseKey = character.getClass().getName();
        switch (character.getCurrentDirection()) {
            case Character.DOWN:
                return charPathMap.get(baseKey + "Down");
            case Character.UP:
                return charPathMap.get(baseKey + "Up");
            case Character.RIGHT:
                return charPathMap.get(baseKey + "Right");
            case Character.LEFT:
                return charPathMap.get(baseKey + "Left");
            default:
                throw new IllegalArgumentException("Invalid direction");
        }
    }

    public void drawScore(int score, int coinCounter, int bestScore, GraphicsContext gc) {
        gc.setFill(Color.YELLOW);
        gc.setFont(new Font("IMPACT", 20));
        gc.fillText("Score: " + score, 10, 20);
        gc.fillText("Best score: " + bestScore, 200, 20);
        gc.fillText("Coin: " + coinCounter, width - 100, 20);
    }

    public void drawGameElements(ArrayList<GameElement> gameElementList, GraphicsContext gc) {
        for (GameElement gameElem : gameElementList) {
            if (gameElem instanceof Teleporter) {
                Teleporter teleporter = (Teleporter) gameElem;
                String color = teleporter.getColor();
                drawImage(gc, gameElemPathMap.get(teleporter.getClass().getName() + color),
                        teleporter.getPosX(),
                        teleporter.getPosY());
                drawImage(gc, gameElemPathMap.get(teleporter.getClass().getName() + color),
                        teleporter.getPortal2X(),
                        teleporter.getPortal2Y());
                
            } else if (gameElem instanceof Potion) {
                Potion potion = (Potion) gameElem;
                String color = potion.getColor();
                drawImage(gc, gameElemPathMap.get(potion.getClass().getName() + color),
                        potion.getPosX(),
                        potion.getPosY());
            } else {
                drawImage(gc, gameElem.getPathToImage(),
                        gameElem.getPosX(),
                        gameElem.getPosY());
            }
        }
    }

    private void drawImage(GraphicsContext gc, String pathToImg, int x, int y) {
        if (!imageCache.containsKey(pathToImg)) {
            // Chargez l'image uniquement si elle n'est pas déjà en cache
            Image image = new Image(pathToImg);
            imageCache.put(pathToImg, image);
        }
    
        Image image = imageCache.get(pathToImg);
        gc.drawImage(image, x * squareSize, y * squareSize, squareSize, squareSize);
        
    }
    
}
