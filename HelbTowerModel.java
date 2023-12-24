import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HelbTowerModel {
    private int rows;
    private int columns;
    private int spawnRows;
    private int spawnColumns;
    private int voidX = -2;
    private int voidY = -2;

    private boolean gameOver;

    private Character mainChar;
    private Teleporter portalBlue, portalRed;

    private int delay = 1000; // 1sec
    private long tmpTime = System.currentTimeMillis();

    private int score;
    private int coinCounter = 0;
    private Food food = new Food(voidX, voidY);

    private String foodClassDescriptionString = food.getClass().getName();
    private String coinClassDescriptionString = new Coin(voidX,voidY).getClass().getName();
    private String wallClassDescriptionString = new Wall(voidX,voidY).getClass().getName();
    
    private String pathToFoodImage = food.getPathToImage();
    private String pathToCoinImage = new Coin(voidX,voidY).getPathToImage();
    private String pathToWallImage = new Wall(voidX,voidY).getPathToImage();

    private ArrayList<Coin> coinList = new ArrayList<Coin>();
    private ArrayList<GameElement> gameElementList = new ArrayList<GameElement>();
    private Map<String, String> pathToImageMap = new HashMap<>();

    public HelbTowerModel(int rows, int columns, Character mainChar) {
        this.rows = rows;
        this.columns = columns;
        this.mainChar = mainChar;

        portalBlue = new Teleporter((rows-1)/2, 0, (rows-1)/2, columns-1, 1);
        portalRed = new Teleporter(0, (columns-1)/2, rows-1, (columns-1)/2, 0);
        gameElementList.add(portalBlue);
        gameElementList.add(portalRed);

        gameElementList.add(food);

        pathToImageMap.put(foodClassDescriptionString, pathToFoodImage);
        pathToImageMap.put(coinClassDescriptionString, pathToCoinImage);
        pathToImageMap.put(wallClassDescriptionString, pathToWallImage);
        pathToImageMap.put(portalBlue.getClass().getName(), portalBlue.getPathToImage());
        pathToImageMap.put(portalRed.getClass().getName(), portalRed.getPathToImage());

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

            for (GameElement gameElement : gameElementList) {
                if (gameElement.getPosX() == randomXPos 
                        && gameElement.getPosY() == randomYPos) {
                    continue start;
                }
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

            for (GameElement gameElement : gameElementList) {
                if (gameElement.getPosX() == randomXPos 
                        && gameElement.getPosY() == randomYPos) {
                    continue start;
                }
            }
            
            Coin newCoin = new Coin(randomXPos, randomYPos);
            coinList.add(newCoin);
            gameElementList.add(newCoin);
            coinCounter++;
            break;
        }
    }

    public void generateBorder() {
        for (int i = 0; i < rows - 1; i++) {
            if (portalBlue.getPosX() != i) {
                gameElementList.add(new Wall(i, 0)); // top border
                gameElementList.add(new Wall(i, columns-1)); // down border
            }
        }

        for (int i = 0; i < columns; i++) {
            if (portalRed.getPosY() != i) {
                gameElementList.add(new Wall(0, i)); // left border
                gameElementList.add(new Wall(rows - 1, i)); // right border                
            }
        }
    }

    public void generateWall(int crossOpening) {
        int midRow = portalRed.getPosY();
        int midColumn = portalBlue.getPosX();

        for (int i = crossOpening; i < columns-crossOpening; i++) {
            gameElementList.add(new Wall(midColumn,i));
        }
        
        for (int i = crossOpening; i < rows-crossOpening; i++) {
            gameElementList.add(new Wall(i,midRow));
        }
    }

    public void generateTower() {
        // top left tower
        for (int i = (rows/4)-1; i <= rows/4; i++) {
            for (int j = (columns/4)-1; j <= (columns/4); j++) {
                gameElementList.add(new Wall(i,j));
            }
        }

        // top right tower
        for (int i = rows-(rows/4)-1; i <= rows-(rows/4); i++) {
            for (int j = (columns/4)-1; j <= (columns/4); j++) {
                gameElementList.add(new Wall(i,j));
            }
        }

        // Bottom left tower
        for (int i = (rows/4)-1; i <= rows/4; i++) {
            for (int j = columns-(columns/4)-1; j <= columns-(columns/4); j++) {
                gameElementList.add(new Wall(i,j));
            }
        }

        // Bottom right tower
        for (int i = rows-(rows/4)-1; i <= rows-(rows/4); i++) {
            for (int j = columns-(columns/4)-1; j <= columns-(columns/4); j++) {
                gameElementList.add(new Wall(i,j));
            }
        }
    }

    public void eatFood() {
        if (mainChar.getX() == food.getPosX() && mainChar.getY() == food.getPosY()) {
            eatGameElement(food);
            delay -= 100;
            System.out.println(delay);
        }

    }
    
    public void eatCoin() {
        for (Coin coin : coinList) {
            if (mainChar.getX() == coin.getPosX() && mainChar.getY() == coin.getPosY()) {
                eatGameElement(coin);
            }
        }
    }

    public void eatGameElement(GameElement gameElem) {
            int cpt = 0;
            
            while (!(cpt == gameElementList.size() - 1 || gameElementList.get(cpt).getPosX() == gameElem.getPosX() &&
                    gameElementList.get(cpt).getPosY() == gameElem.getPosY())) {
                cpt++;
            }

            if (gameElementList.get(cpt).getPosX() == gameElem.getPosX() &&
                    gameElementList.get(cpt).getPosY() == gameElem.getPosY()) {
                gameElementList.remove(cpt);
            }

            gameElem.triggerAction(this);
        }

    public void triggerPortal(Character character) {
        if (character.getX() == portalBlue.getPosX() && character.getY() == portalBlue.getPosY()) {
            character.setLocation(portalBlue.getPortalX(2), portalBlue.getPortalY(2) - 1);
        } else if (character.getX() == portalBlue.getPortalX(2) && character.getY() == portalBlue.getPortalY(2)) {
            character.setLocation(portalBlue.getPosX(), portalBlue.getPosY() + 1);
        }

        if (character.getX() == portalRed.getPosX() && character.getY() == portalRed.getPosY()) {
            character.setLocation(portalRed.getPortalX(2) - 1, portalRed.getPortalY(2));
        } else if (character.getX() == portalRed.getPortalX(2) && character.getY() == portalRed.getPortalY(2)) {
            character.setLocation(portalRed.getPosX() + 1, portalRed.getPosY());
        }
    }

    public boolean isANewCycle() {
        if (System.currentTimeMillis() >= (tmpTime + delay)) {
            tmpTime = System.currentTimeMillis();
            return true;
        }
        return false;
    }
    

    // GET & SET
    public void setDelay(int newDelay) {
        delay = newDelay;
    }

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