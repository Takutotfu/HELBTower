import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SnakeFXGame extends Application {

    private static final int WIDTH = 800;
    private static final int HEIGHT = WIDTH;
    private static final int ROWS = 20;
    private static final int COLUMNS = ROWS;
    private static final int SQUARE_SIZE = WIDTH / ROWS;
    private static final int RIGHT_BORDER = ROWS - 1;
    private static final int BOTTOM_BORDER = COLUMNS - 1;

    private GraphicsContext gc;
    private Point snakeHead;
    private boolean gameOver;
    private int score = 0;
    
    private ArrayList<GameElement> gameElementList = new ArrayList<GameElement>();
    
    private int voidX = -2;
    private int voidY = -2;
    private Food food = new Food(voidX, voidY);
    
    private int coinCounter = 5;
    private ArrayList<Coin> coinList = new ArrayList<Coin>();
    
    private String foodClassDescriptionString = food.getClass().getName();
    private String coinClassDescriptionString = new Coin(voidX,voidY).getClass().getName();
    private Image foodImage = new Image(food.getPathToImage());
    private Image coinImage = new Image(new Coin(voidX,voidY).getPathToImage());
        
    private Map<String, Image> imageMap = new HashMap<>();

    public int getScore(){
        return score;
    }
    
    public void setScore(int newScore){
        this.score = newScore;
    }
    
    public int getCoinCounter(){
        return coinCounter;
    }
    
    public void decreaseCoinCounter(){
        coinCounter--;
    }
    
    public void setGameOver(){
        gameOver = true;
    }
    
    public int getVoidX(){
        return voidX;
    }
    
    public int getVoidY(){
        return voidY;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("HELBTower");
        Group root = new Group();
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        root.getChildren().add(canvas);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
        gc = canvas.getGraphicsContext2D();

        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                KeyCode code = event.getCode();

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
                // DEBUG POSITION System.out.println(snakeHead.getX() + " || " + snakeHead.getY());
            }
        });
        
        gameElementList.add(food);
        imageMap.put(foodClassDescriptionString, foodImage);
        imageMap.put(coinClassDescriptionString, coinImage);
        
        snakeHead = new Point(5, ROWS/2);
        generateFood();
        
        for (int i = 0; i < coinCounter; i++) {
            generateCoin();
        }

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(130), e -> run(gc)));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private void run(GraphicsContext gc) {
        if (gameOver) {
            gc.setFill(Color.RED);
            gc.setFont(new Font("Digital-7", 70));
            gc.fillText("Game Over", WIDTH / 3.5, HEIGHT / 2);
            return;
        }
        drawBackground(gc);
        drawSnake(gc);
        drawScore();
        
        drawGameElements(gc);

        eatFood();
        eatCoin();
    }

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
    
    public void generateFood() {
        start:
        while (true) {
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
        start:
        while (true) {
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

    private void drawGameElements(GraphicsContext gc) {
        for (GameElement gameElem : gameElementList) {
            gc.drawImage(imageMap.get(gameElem.getClass().getName()), 
                         gameElem.getPosX() * SQUARE_SIZE, 
                         gameElem.getPosY() * SQUARE_SIZE, 
                         SQUARE_SIZE, 
                         SQUARE_SIZE);
        }
    }

    private void drawSnake(GraphicsContext gc) {
        gc.setFill(Color.web("ff8800"));
        gc.fillRoundRect(snakeHead.getX() * SQUARE_SIZE, snakeHead.getY() * SQUARE_SIZE, SQUARE_SIZE - 1, SQUARE_SIZE - 1, 35, 35);
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

    private void drawScore() {
        gc.setFill(Color.BLACK);
        gc.setFont(new Font("Digital-7", 35));
        gc.fillText("Score: " + score, 10, 35);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
