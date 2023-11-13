import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class Model {
    public static final int WIDTH = 800;
    public static final int HEIGHT = WIDTH;
    private static final int ROWS = 20;
    private static final int COLUMNS = ROWS;
    private static final int SQUARE_SIZE = WIDTH / ROWS;
    private static final int RIGHT_BORDER = ROWS - 1;
    private static final int BOTTOM_BORDER = COLUMNS - 1;

    private int voidX = -2;
    private int voidY = -2;

    private boolean gameOver;
    private int score = 0; // a changer dans la vue
    public int coinCounter = 5;

    private ArrayList<Coin> coinList = new ArrayList<Coin>();

    private Food food = new Food(voidX, voidY);
    private Coin coin = new Coin(voidX, voidY);
    public static Point snakeHead = new Point(5,5);

    private Image foodImage = new Image(food.getPathToImage());
    private Image coinImage = new Image(coin.getPathToImage());
    private String foodClassDescriptionString = food.getClass().getName();
    private String coinClassDescriptionString = new Coin(voidX, voidY).getClass().getName();

    private ArrayList<GameElement> gameElementList = new ArrayList<GameElement>();

    private Map<String, Image> imageMap = new HashMap<>();

    public Model() {
        gameElementList.add(food);
        imageMap.put(foodClassDescriptionString, foodImage);
        imageMap.put(coinClassDescriptionString, coinImage);

        generateFood();
        for (int i = 0; i < coinCounter; i++) {
            generateCoin();
        }
    }

    public void start(GraphicsContext gc) {
        if (gameOver) {
            gc.setFill(Color.RED);
            gc.setFont(new Font("Digital-7", 70));
            gc.fillText("Game Over", WIDTH / 3.5, HEIGHT / 2);
            return;
        }

        drawBackground(gc);
        drawSnake(gc);
        drawScore(gc);

        drawGameElements(gc);

        eatFood();
        eatCoin();
    }

    // Controls
    public void handle(KeyCode code) {
        if (code == KeyCode.RIGHT || code == KeyCode.D) {
            if (snakeHead.getX() < RIGHT_BORDER) {
                moveRight();
            }
        } else if (code == KeyCode.LEFT || code == KeyCode.A) {
            if (snakeHead.getX() > 0) {
                moveLeft();
            }
        } else if (code == KeyCode.UP || code == KeyCode.W) {
            if (snakeHead.getY() > 0) {
                moveUp();
            }
        } else if (code == KeyCode.DOWN || code == KeyCode.S) {
            if (snakeHead.getY() < BOTTOM_BORDER) {
                moveDown();
            }
        }
        // DEBUG LOCATION System.out.println(snakeHead.getX() + " || " +
        // snakeHead.getY());
    }

    // GENERATE GAME ELEMENTS

    public void generateFood() {
        start: while (true) {
            int randomXPos = (int) (Math.random() * ROWS);
            int randomYPos = (int) (Math.random() * COLUMNS);

            if (snakeHead.getX() == randomXPos && snakeHead.getY() == randomYPos) {
                continue start;
            }

            for (Coin otherCoin : coinList) {
                if (otherCoin.getPosX() == randomXPos
                        && otherCoin.getPosY() == randomYPos) {
                    continue start;
                }
            }

            if (food.getPosX() == randomXPos
                    && food.getPosY() == randomYPos) {
                continue start;
            }

            food.setPosX(randomXPos);
            food.setPosY(randomYPos);
            food.setFoodImage();
            foodImage = new Image(food.getPathToImage());
            imageMap.put(foodClassDescriptionString, foodImage);
            break;
        }
    }

    public void generateCoin() {

        start: while (true) {
            int randomXPos = (int) (Math.random() * ROWS);
            int randomYPos = (int) (Math.random() * COLUMNS);

            if (snakeHead.getX() == randomXPos && snakeHead.getY() == randomYPos) {
                continue start;
            }

            for (Coin otherCoin : coinList) {
                if (otherCoin.getPosX() == randomXPos
                        && otherCoin.getPosY() == randomYPos) {
                    continue start;
                }
            }

            if (food.getPosX() == randomXPos
                    && food.getPosY() == randomYPos) {
                continue start;
            }

            Coin newCoin = new Coin(randomXPos, randomYPos);
            coinList.add(newCoin);
            gameElementList.add(newCoin);
            break;
        }
    }

    // DRAW ELEMENTS

    private void drawBackground(GraphicsContext gc) {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                if ((i + j) % 2 == 0) {
                    gc.setFill(Color.web("ffffff"));
                } else {
                    gc.setFill(Color.web("f1f1f1"));
                }
                gc.fillRect(i * SQUARE_SIZE, j * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
            }
        }
    }

    private void drawScore(GraphicsContext gc) {
        gc.setFill(Color.BLACK);
        gc.setFont(new Font("Digital-7", 35));
        gc.fillText("Score: " + score, 10, 35);
    }

    private void drawSnake(GraphicsContext gc) {
        gc.setFill(Color.web("ff8800"));
        gc.fillRoundRect(snakeHead.getX() * SQUARE_SIZE, snakeHead.getY() * SQUARE_SIZE, SQUARE_SIZE - 1,
                SQUARE_SIZE - 1, 35, 35);
    }

    private void drawGameElements(GraphicsContext gc) {
        for (GameElement gameElem : gameElementList) {
            gc.drawImage(imageMap.get(gameElem.getClass().getName()),
                    gameElem.getPosX() * SQUARE_SIZE,
                    gameElem.getPosY() * SQUARE_SIZE,
                    SQUARE_SIZE,
                    SQUARE_SIZE);
        }
    }

    // EAT ELEMENTS
    private void eatFood() {
        if (snakeHead.getX() == food.getPosX()
                && snakeHead.getY() == food.getPosY()) {
            food.triggerAction(this);
        }
    }

    private void eatCoin() {
        for (Coin coin : coinList) {
            if (snakeHead.getX() == coin.getPosX()
                    && snakeHead.getY() == coin.getPosY()) {
                coin.triggerAction(this);
            }
        }
    }

    // GETS & SETS

    public int getVoidX() {
        return voidX;
    }

    public int getVoidY() {
        return voidY;
    }

    public void setGameOver() {
        gameOver = true;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int newScore) {
        this.score = newScore;
    }

    public int getCoinCounter(){
        return coinCounter;
    }
    
    public void decreaseCoinCounter(){
        coinCounter--;
    }

    private void moveRight() {
        snakeHead.x++;
    }

    private void moveLeft() {
        snakeHead.x--;
    }

    private void moveUp() {
        snakeHead.y--;
    }

    private void moveDown() {
        snakeHead.y++;
    }
}
