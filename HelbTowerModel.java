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

    private Point mainChar;

    private int score;
    private int coinCounter = 0;
    private Food food = new Food(voidX, voidY);

    private String foodClassDescriptionString = food.getClass().getName();
    private String coinClassDescriptionString = new Coin(voidX,voidY).getClass().getName();
    
    private String pathToFoodImage = food.getPathToImage();
    private String pathToCoinImage = new Coin(voidX,voidY).getPathToImage();

    private ArrayList<Coin> coinList = new ArrayList<Coin>();
    private ArrayList<GameElement> gameElementList = new ArrayList<GameElement>();
    private ArrayList<Point> gameElementsPoints;
    private Map<String, String> pathToImageMap = new HashMap<>();

    public HelbTowerModel(int rows, int columns, Point mainChar, ArrayList<Point> gameElementsPoints) {
        this.mainChar = mainChar;
        this.gameElementsPoints = gameElementsPoints;

        gameElementList.add(food);

        pathToImageMap.put(foodClassDescriptionString, pathToFoodImage);
        pathToImageMap.put(coinClassDescriptionString, pathToCoinImage);

        // spawn area
        spawnRows = rows - 2;
        spawnColumns = columns - 2;
    }

    public void generateFood() {
        start:
        while (true) {
            // 1 -> spawnRows/spawnColumns
            int randomXPos = (int) (Math.random() * spawnRows + 1);
            int randomYPos = (int) (Math.random() * spawnColumns + 1);

            for (Point gameElementPoint : gameElementsPoints) {
                if (gameElementPoint.getX() == randomXPos 
                        && gameElementPoint.getY() == randomYPos) {
                    continue start;
                }
            }
            
            food.setPosX(randomXPos);
            food.setPosY(randomYPos);
            food.setFoodImage();
            pathToFoodImage = food.getPathToImage();
            pathToImageMap.put(foodClassDescriptionString, pathToFoodImage);
            gameElementsPoints.add(new Point(randomXPos, randomYPos));
            break;
        }
    }
    
    public void generateCoin() {
        start:
        while (true) {
            // 1 -> spawnRows/spawnColumns
            int randomXPos = (int) (Math.random() * spawnRows + 1);
            int randomYPos = (int) (Math.random() * spawnColumns + 1);

            for (Point gameElementPoint : gameElementsPoints) {
                if (gameElementPoint.getX() == randomXPos 
                        && gameElementPoint.getY() == randomYPos) {
                    continue start;
                }
            }
            
            Coin newCoin = new Coin(randomXPos, randomYPos);
            coinList.add(newCoin);
            gameElementList.add(newCoin);
            gameElementsPoints.add(new Point(randomXPos, randomYPos));
            coinCounter++;
            break;
        }
    }

    public void eatFood() {
        eatGameElement(food);
    }
    
    public void eatCoin() {
        for (Coin coin : coinList) {
            eatGameElement(coin);
        }
    }

    public void eatGameElement(GameElement gameElem) {
        if (mainChar.getX() == gameElem.getPosX() && mainChar.getY() == gameElem.getPosY()) {
            int cpt = 0;

            while (!(cpt == gameElementsPoints.size() - 1 || gameElementsPoints.get(cpt).getX() == gameElem.getPosX() &&
                    gameElementsPoints.get(cpt).getY() == gameElem.getPosY())) {
                cpt++;
            }

            if (gameElementsPoints.get(cpt).getX() == gameElem.getPosX() &&
                    gameElementsPoints.get(cpt).getY() == gameElem.getPosY()) {
                gameElementsPoints.remove(cpt);
            }

            gameElem.triggerAction(this);
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

    public Map<String, String> getPathToImageMap() {
        return pathToImageMap;
    }

    public ArrayList<GameElement> getGameElementList() {
        return gameElementList;
    }
    
}