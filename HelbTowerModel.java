import java.util.ArrayList;

public class HelbTowerModel {
    private int period;

    private int rows;
    private int columns;
    private int voidX = -2;
    private int voidY = -2;

    private boolean gameOver;
    private boolean levelFinished;

    private MainCharacter mainChar;
    private Teleporter portalBlue, portalRed;

    private int mainChardelay = 300; // 1000 = 1sec
    private int guardDelay = 300;
    private int purpleDelay = 300;

    private boolean isWearingCloak = false;

    private long lastTimePotionTaked;
    private boolean isPotionEffect = false;
    private int potionCooldown = 0;

    private int score;
    private int coinCounter = 0;

    private ArrayList<Coin> coinList = new ArrayList<Coin>();
    private ArrayList<GameElement> gameElementList = new ArrayList<GameElement>();

    public HelbTowerModel(int rows, int columns, int period, MainCharacter mainChar) {
        this.rows = rows;
        this.columns = columns;
        this.period = period;
        this.mainChar = mainChar;
    }


    public void generateCloak() {
        start:
        while (true) {
            int randomXPos = (int) (Math.random() * rows-1);
            int randomYPos = (int) (Math.random() * columns-1);

            for (GameElement gameElement : gameElementList) {
                if (gameElement.getPosX() == randomXPos 
                        && gameElement.getPosY() == randomYPos) {
                    continue start;
                }
            }

            Cloak newCloak = new Cloak(randomXPos, randomYPos);

            gameElementList.add(newCloak);
            break;
        }
    }

    public void generatePotion() {
        start:
        while (true) {
            int randomPotion = (int) (Math.random() * 3);
            int randomXPos = (int) (Math.random() * rows-1);
            int randomYPos = (int) (Math.random() * columns-1);

            for (GameElement gameElement : gameElementList) {
                if (gameElement.getPosX() == randomXPos 
                        && gameElement.getPosY() == randomYPos) {
                    continue start;
                }
            }

            Potion newPotion = new Potion(randomXPos, randomYPos, randomPotion);
            gameElementList.add(newPotion);
            break;
        }
    }
    
    public void generateCoin() {
        coinCounter = 0;
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

    public void generateTeleporter() {
        portalBlue = new Teleporter((rows-1)/2, 0, (rows-1)/2, columns-1, 1);
        portalRed = new Teleporter(0, (columns-1)/2, rows-1, (columns-1)/2, 0);
        gameElementList.add(portalBlue);
        gameElementList.add(portalRed);
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

    public Guard generateRandomGuard(int maxCaseForBlueGuard) {
        Guard newGuard;
        int randomGuard = (int) (Math.random() * 4);

        start: while (true) {
            int randomPosX = (int) (Math.random() * rows);
            int randomPosY = (int) (Math.random() * columns);

            for (GameElement gameElement : gameElementList) {
                if (gameElement instanceof Wall && gameElement.getPosX() == randomPosX
                        && gameElement.getPosY() == randomPosY) {
                    continue start;
                }
            }

            switch (randomGuard) {
                case 0:
                    newGuard = new RedGuard(voidX, voidY, mainChar);
                    break;
                case 1:
                    newGuard = new OrangeGuard(randomPosX, randomPosY);
                    break;
                case 2:
                    newGuard = new BlueGuard(randomPosX, randomPosY, maxCaseForBlueGuard, rows, columns);
                    break;
                default:
                    newGuard = new PurpleGuard(randomPosX, randomPosY, randomPosY, randomPosY, randomPosY, randomPosY);
                    break;
            }
            newGuard.setAlive();
            newGuard.setLocation(randomPosX, randomPosY);

            return newGuard;
        }

    }

    public void eatGameElement() {
        for (GameElement gameElem : gameElementList) {
            if (gameElem.getPosX() == mainChar.getX() && gameElem.getPosY() == mainChar.getY()) {
                gameElem.triggerAction(this);

                if (gameElem instanceof Coin) { // Eat Coin
                    purpleDelay--;

                } else if (gameElem instanceof Potion) { // Drink Potion
                    Potion potion = (Potion) gameElem;
                    lastTimePotionTaked = System.currentTimeMillis();

                    if (potion.isTaked()) {
                        isPotionEffect = true;
                        potionCooldown = potion.getDuration();
                        potion.unsetTaked();
                    }

                } else if (gameElem instanceof Cloak) { // Take Cloak
                    Cloak cloak = (Cloak) gameElem;
                
                    if (cloak.isTaked()) {
                        isWearingCloak = true;
                    }
                    cloak.unsetTaked();

                } else if (gameElem instanceof Wall) { // Passed trough a Wall
                    isWearingCloak = false;
                }
            }
        }
        if (isEffectFinish(lastTimePotionTaked, potionCooldown)) { // verifPotionFinish
            isPotionEffect = false;
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

    public boolean isEffectFinish(long lastTimeTaked, int cooldown) {
        return System.currentTimeMillis() >= (lastTimeTaked + cooldown);
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
    public int getPeriod() {return period;}

    public void setPeriod(int period) {this.period = period;}

    public void setGuardDelay(int newDelay) {guardDelay = newDelay;}

    public int getGuardDelay() {return guardDelay;}

    public int getMainCharDelay() {return mainChardelay;}

    public int getPurpleDelay() {return purpleDelay;}

    public boolean isPotionEffect() {return isPotionEffect;}

    public boolean isWearingCloak() {return isWearingCloak;}

    public int getScore(){return score;}
    
    public void setScore(int newScore){this.score = newScore;}
    
    public int getCoinCounter(){return coinCounter;}
    
    public void decreaseCoinCounter(){coinCounter--;}

    public void unsetLevelFinished(){levelFinished = false;}

    public void setLevelFinished(){levelFinished = true;}

    public boolean getLevelFinished() {return levelFinished;}
    
    public void setGameOver(){gameOver = true;}

    public boolean getGameOver() {return gameOver;}
    
    public int getVoidX(){return voidX;}
    
    public int getVoidY(){return voidY;}

    public ArrayList<GameElement> getGameElementList() {return gameElementList;}

    public Teleporter getPortalRed() {return portalRed;}

    public Teleporter getPortalBlue() {return portalBlue;}
    
}