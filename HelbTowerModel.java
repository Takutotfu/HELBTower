import java.util.ArrayList;

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

    //private String foodClassDescriptionString = food.getClass().getName();
    //private String pathToFoodImage = food.getPathToImage();

    private ArrayList<Coin> coinList = new ArrayList<Coin>();
    private ArrayList<GameElement> gameElementList = new ArrayList<GameElement>();
    //private Map<String, String> pathToImageMap = new HashMap<>();

    public HelbTowerModel(int rows, int columns, Character mainChar) {
        this.rows = rows;
        this.columns = columns;
        this.mainChar = mainChar;

        portalBlue = new Teleporter((rows-1)/2, 0, (rows-1)/2, columns-1, 1);
        portalRed = new Teleporter(0, (columns-1)/2, rows-1, (columns-1)/2, 0);
        gameElementList.add(portalBlue);
        gameElementList.add(portalRed);

        gameElementList.add(food);

        //pathToImageMap.put(foodClassDescriptionString, pathToFoodImage);
        //System.out.println(portalBlue.getClass().getName() + " " + portalBlue.getPathToImage());
        //pathToImageMap.put(portalBlue.getClass().getName()+"Blue", portalBlue.getPathToImage());
        //System.out.println(portalRed.getClass().getName() + " " + portalRed.getPathToImage());
        //pathToImageMap.put(portalRed.getClass().getName()+"Red", portalRed.getPathToImage());

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
            //pathToFoodImage = food.getPathToImage();
            //pathToImageMap.put(foodClassDescriptionString, pathToFoodImage);
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
            food.triggerAction(this);
            delay -= 100;
            System.out.println(delay);
        }

    }
    
    public void eatCoin() {
        for (Coin coin : coinList) {
            if (mainChar.getX() == coin.getPosX() && mainChar.getY() == coin.getPosY()) {
                coin.triggerAction(this);
            }
        }
    }

    public void triggerPortal(Character character) {
        if (character.getX() == portalBlue.getPosX() && character.getY() == portalBlue.getPosY()) {
            character.setLocation(portalBlue.getPortal2X(), portalBlue.getPortal2Y() - 1);
        } else if (character.getX() == portalBlue.getPortal2X() && character.getY() == portalBlue.getPortal2Y()) {
            character.setLocation(portalBlue.getPosX(), portalBlue.getPosY() + 1);
        }

        if (character.getX() == portalRed.getPosX() && character.getY() == portalRed.getPosY()) {
            character.setLocation(portalRed.getPortal2X() - 1, portalRed.getPortal2Y());
        } else if (character.getX() == portalRed.getPortal2X() && character.getY() == portalRed.getPortal2Y()) {
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
    public void setDelay(int newDelay) {delay = newDelay;}

    public int getScore(){return score;}
    
    public void setScore(int newScore){this.score = newScore;}
    
    public int getCoinCounter(){return coinCounter;}
    
    public void decreaseCoinCounter(){coinCounter--;}
    
    public void setGameOver(){gameOver = true;}

    public boolean getGameOver() {return gameOver;}
    
    public int getVoidX(){return voidX;}
    
    public int getVoidY(){return voidY;}

    public ArrayList<GameElement> getGameElementList() {return gameElementList;}

    //public Map<String, String> getPathToImageMap() {return pathToImageMap;}

    public Teleporter getPortalRed() {return portalRed;}

    public Teleporter getPortalBlue() {return portalBlue;}
    
}