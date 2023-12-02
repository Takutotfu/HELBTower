import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HelbTowerModel {
    private int spawnRows;
    private int spawnColumns;
    private int voidX = -2;
    private int voidY = -2;

    private boolean gameOver;

    private Point charHead = new Point(5,2);

    private int score;
    private int coinCounter;
    private Food food = new Food(voidX, voidY);

    private String foodClassDescriptionString = food.getClass().getName();
    private String coinClassDescriptionString = new Coin(voidX,voidY).getClass().getName();
    
    private String pathToFoodImage = food.getPathToImage();
    private String pathToCoinImage = new Coin(voidX,voidY).getPathToImage();

    private ArrayList<Coin> coinList = new ArrayList<Coin>();
    private ArrayList<GameElement> gameElementList = new ArrayList<GameElement>();
    private Map<String, String> pathToImageMap = new HashMap<>();

    public HelbTowerModel(int rows, int columns) {
        gameElementList.add(food);
        pathToImageMap.put(foodClassDescriptionString, pathToFoodImage);
        pathToImageMap.put(coinClassDescriptionString, pathToCoinImage);

        // spawn area
        spawnRows = rows - 2;
        spawnColumns = columns - 2;

        coinCounter = (rows-2) * (columns-2) - 2;
    }

    public void generateFood() {
        start:
        while (true) {
            // 1 -> spawnRows/spawnColumns
            int randomXPos = (int) (Math.random() * spawnRows + 1);
            int randomYPos = (int) (Math.random() * spawnColumns + 1);

            if (charHead.getX() == randomXPos 
                && charHead.getY() == randomYPos) {
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
            pathToFoodImage = food.getPathToImage();
            pathToImageMap.put(foodClassDescriptionString, pathToFoodImage);
            break;
        }
    }
    
    public void generateCoin() {
        start:
        while (true) {
            // 1 -> spawnRows/spawnColumns
            int randomXPos = (int) (Math.random() * spawnRows + 1);
            int randomYPos = (int) (Math.random() * spawnColumns + 1);

            if (charHead.getX() == randomXPos
                    && charHead.getY() == randomYPos) {
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

    public void eatFood() {
        if (charHead.getX() == food.getPosX() 
        && charHead.getY() == food.getPosY()) {
            food.triggerAction(this);
        }
    }
    
    public void eatCoin() {
        for (Coin coin : coinList) {
            if (charHead.getX() == coin.getPosX() 
            && charHead.getY() == coin.getPosY()) {
                coin.triggerAction(this);
            }
        }
    }
    

    // GET & SET
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

    public boolean getGameOver() {
        return gameOver;
    }
    
    public int getVoidX(){
        return voidX;
    }
    
    public int getVoidY(){
        return voidY;
    }

    public Point getCharHead() {
        return charHead;
    }

    public Map<String, String> getPathToImageMap() {
        return pathToImageMap;
    }

    public ArrayList<GameElement> getGameElementList() {
        return gameElementList;
    }

    // DEPLACEMENTS
    public void moveRight() {
        charHead.x++;
    }

    public void moveLeft() {
        charHead.x--;
    }

    public void moveUp() {
        charHead.y--;
    }

    public void moveDown() {
        charHead.y++;
    }

    
}