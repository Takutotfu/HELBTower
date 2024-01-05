import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

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

    private int chronometerCooldown = 0;
    private long lastTimeChronometerTaked = 0;
    private boolean isChronometerEffect = false;
    private int guardDelaySave = guardDelay;

    private long lastTimePotionTaked;
    private boolean isPotionEffect = false;
    private int potionCooldown = 0;

    private int score;
    private int bestScore;
    private int coinCounter = 0;

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

    public void generateChronometer() {
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

            gameElementList.add(new Chronometer(randomXPos, randomYPos));
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

            gameElementList.add(new Potion(randomXPos, randomYPos, randomPotion));
            break;
        }
    }
    
    public void generateCoin() {
        coinCounter = 0;
        for (int i = 0; i < rows - 1; i++) {
            for (int j = 0; j < columns - 1; j++) {
                if (!(isGameElemInCase(i, j))) {
                    Coin newCoin = new Coin(i, j);
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

    public Guard generateRandomGuard() {
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
                    newGuard = new BlueGuard(randomPosX, randomPosY, rows, columns);
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

    public void eatGameElement(boolean isPurpleGuardAlive) {
        for (GameElement gameElem : gameElementList) {
            if (gameElem.getPosX() == mainChar.getX() && gameElem.getPosY() == mainChar.getY()) {
                gameElem.triggerAction(this);

                if (gameElem instanceof Coin && isPurpleGuardAlive) { // Eat Coin
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

                } else if (gameElem instanceof Chronometer) { // Take Chronometer
                    Chronometer chronometer = (Chronometer) gameElem;
                    Point postion = mainChar.getMemoryPostion().get(mainChar.getMemoryPostion().size()-6);
                    mainChar.setLocation(postion.getX(), postion.getY());

                    if (chronometer.isTaked() && !isChronometerEffect) {
                        chronometerCooldown = chronometer.getDuration();
                        lastTimeChronometerTaked = System.currentTimeMillis();
                        isChronometerEffect = true;
                        setGuardDelay(guardDelay+chronometer.getDuration());
                    }
                    chronometer.unsetTaked();

                } else if (gameElem instanceof Wall) { // Passed trough a Wall
                    isWearingCloak = false;
                }
            }
        }
        if (isEffectFinish(lastTimePotionTaked, potionCooldown)) { // verifPotionFinish
            isPotionEffect = false;
        }
        if (isEffectFinish(lastTimeChronometerTaked, chronometerCooldown)) {
            setGuardDelay(guardDelaySave);
            isChronometerEffect = false;
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

    public void writeBestScore() {
        try {
            String save = String.valueOf(bestScore);
            FileWriter myWriter = new FileWriter("best_score");
            myWriter.write(save);
            myWriter.close();
        } catch (IOException e) {
            System.out.println("Best score hasn't been write correctly");
            e.printStackTrace();
        }
    }

    public void readBestScore() {
        try {
            File myFile = new File("best_score");
            Scanner sc = new Scanner(myFile);
            while (sc.hasNextLine()) {
                String data = sc.nextLine();
                bestScore = Integer.parseInt(data);
            }
            sc.close();
        } catch (FileNotFoundException e) {
            System.out.println("Best score hasn't been read correctly");
            e.printStackTrace();
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
    public int getBestScore() {return bestScore;}

    public void setBestScore(int score) {bestScore = score;}

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

    public void unsetGameOver(){gameOver = false;}
    
    public void setGameOver(){gameOver = true;}

    public boolean getGameOver() {return gameOver;}
    
    public int getVoidX(){return voidX;}
    
    public int getVoidY(){return voidY;}

    public ArrayList<GameElement> getGameElementList() {return gameElementList;}

    public Teleporter getPortalRed() {return portalRed;}

    public Teleporter getPortalBlue() {return portalBlue;}
    
}