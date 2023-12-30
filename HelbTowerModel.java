import java.util.ArrayList;

public class HelbTowerModel {
    private int rows;
    private int columns;
    private int voidX = -2;
    private int voidY = -2;

    private boolean gameOver;

    private Character mainChar;
    private Teleporter portalBlue, portalRed;

    private int delay = 300; // 1000 = 1sec
    private int purpleDelay = 300;

    private int score;
    private int coinCounter = 0;

    //private String foodClassDescriptionString = food.getClass().getName();
    //private String pathToFoodImage = food.getPathToImage();

    private ArrayList<Coin> coinList = new ArrayList<Coin>();
    private ArrayList<Potion> potionList = new ArrayList<Potion>();
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


        //pathToImageMap.put(foodClassDescriptionString, pathToFoodImage);
        //System.out.println(portalBlue.getClass().getName() + " " + portalBlue.getPathToImage());
        //pathToImageMap.put(portalBlue.getClass().getName()+"Blue", portalBlue.getPathToImage());
        //System.out.println(portalRed.getClass().getName() + " " + portalRed.getPathToImage());
        //pathToImageMap.put(portalRed.getClass().getName()+"Red", portalRed.getPathToImage());
    }

    public void generatePotion() {
        start:
        while (true) {
            int randomPotion = (int) (Math.random() * 3);
            int randomXPos = (int) (Math.random() * rows);
            int randomYPos = (int) (Math.random() * columns);

            for (GameElement gameElement : gameElementList) {
                if (gameElement.getPosX() == randomXPos 
                        && gameElement.getPosY() == randomYPos) {
                    continue start;
                }
            }

            Potion newPotion = new Potion(randomXPos, randomYPos, randomPotion);
            potionList.add(newPotion);
            gameElementList.add(newPotion);

            //pathToFoodImage = food.getPathToImage();
            //pathToImageMap.put(foodClassDescriptionString, pathToFoodImage);
            break;
        }
    }
    
    public void generateCoin() {
        for (int i = 0; i < rows - 1; i++) {
            for (int j = 0; j < columns - 1; j++) {
                if (!(isGameElemInCase(i, j))) {
                    Coin newCoin = new Coin(i, j);
                    coinList.add(newCoin);
                    gameElementList.add(newCoin);
                    coinCounter++;
                }
            }
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

    public void generateTower(int towerX, int towerY) {
        // top left tower
        for (int i = towerX-1; i <= towerX; i++) {
            for (int j = towerY-1; j <= towerY; j++) {
                gameElementList.add(new Wall(i,j));
            }
        }

        // top right tower
        for (int i = rows-towerX-1; i <= rows-towerX; i++) {
            for (int j = towerY-1; j <= towerY; j++) {
                gameElementList.add(new Wall(i,j));
            }
        }

        // Bottom left tower
        for (int i = towerX-1; i <= towerX; i++) {
            for (int j = columns-towerY-1; j <= columns-towerY; j++) {
                gameElementList.add(new Wall(i,j));
            }
        }

        // Bottom right tower
        for (int i = rows-towerX-1; i <= rows-towerX; i++) {
            for (int j = columns-towerY-1; j <= columns-towerY; j++) {
                gameElementList.add(new Wall(i,j));
            }
        }
    }

    
    public void eatCoin() {
        for (Coin coin : coinList) {
            if (mainChar.getX() == coin.getPosX() && mainChar.getY() == coin.getPosY()) {
                coin.triggerAction(this);
                purpleDelay -= 3;
            }
        }
    }

    public void drinkPotion() {
        for (Potion potion : potionList) {
            if (mainChar.getX() == potion.getPosX() && mainChar.getY() == potion.getPosY()) {
                potion.triggerAction(this);
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

    public boolean isANewCycle(long time, int delay) {
        return System.currentTimeMillis() >= (time + delay);
    }

    public boolean isGameElemInCase(int x, int y) {
        for (GameElement gameElement : gameElementList) {
            if (gameElement.getPosX() == x
                    && gameElement.getPosY() == y) {
                        return true;
            }
        }
        return false;
    }
    

    // GET & SET
    public void setDelay(int newDelay) {delay = newDelay;}

    public int getDelay() {return delay;}

    public int getPurpleDelay() {return purpleDelay;}

    public int getScore(){return score;}
    
    public void setScore(int newScore){this.score = newScore;}
    
    public int getCoinCounter(){return coinCounter;}
    
    public void decreaseCoinCounter(){coinCounter--;}
    
    public void setGameOver(){gameOver = true;}

    public boolean getGameOver() {return gameOver;}
    
    public int getVoidX(){return voidX;}
    
    public int getVoidY(){return voidY;}

    public ArrayList<GameElement> getGameElementList() {return gameElementList;}

    public ArrayList<Potion> getPotionList() {return potionList;}

    //public Map<String, String> getPathToImageMap() {return pathToImageMap;}

    public Teleporter getPortalRed() {return portalRed;}

    public Teleporter getPortalBlue() {return portalBlue;}
    
}