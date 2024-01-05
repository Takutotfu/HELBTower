import java.util.ArrayList;
import java.util.HashMap;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.util.Duration;

public class HelbTowerController {
    private Timeline timeline;

    private HelbTowerModel model;
    private HelbTowerView view;
    private MainCharacter mainChar;
    private OrangeGuard orangeGuard;
    private BlueGuard blueGuard;
    private PurpleGuard purpleGuard;
    private RedGuard redGuard;

    private static final int WIDTH = 1050;
    private static final int HEIGHT = 750;
    private static final int ROWS = 21;
    private static final int COLUMNS = 15;
    private static final int SQUARE_SIZE = WIDTH / ROWS;
    private static final int CROSS_OPENING = 5;
    private static final int TOWER_X = ROWS / 4;
    private static final int TOWER_Y = COLUMNS / 4;

    private static final int NBR_OF_POTIONS = 6;
    private static final int NBR_OF_CLOAK = 5;
    private static final int NBR_OF_CHRONOMETER = 3;

    private static final int MORNING = 0;
    private static final int DAY = 1;
    private static final int NIGHT = 2;

    private static final int MORNING_DELAY = 1000;
    private static final int DAY_DELAY = 300;
    private static final int NIGHT_DELAY = 200;

    private int coinNbr;

    private long lastTimeKeyPressed = System.currentTimeMillis();
    private long lastTimeGuardMove = System.currentTimeMillis();
    private long lastTimePurpleGuardMove = System.currentTimeMillis();

    private GraphicsContext gc;

    private boolean isAdditionalActivated = false;

    private ArrayList<Character> charactersArray = new ArrayList<>();
    private ArrayList<Character> cheatedGuard = new ArrayList<>();

    private HashMap<String, String> charactersPathMap = new HashMap<>();

    public HelbTowerController(Stage primaryStage) {
        mainChar = new MainCharacter(ROWS / 2, COLUMNS / 4);
        orangeGuard = new OrangeGuard(TOWER_X, TOWER_Y);
        blueGuard = new BlueGuard(ROWS - TOWER_X, TOWER_Y, ROWS, COLUMNS);
        purpleGuard = new PurpleGuard(ROWS - TOWER_X, COLUMNS - TOWER_Y, TOWER_X, TOWER_Y, ROWS, COLUMNS);
        redGuard = new RedGuard(TOWER_X, COLUMNS - TOWER_Y, mainChar);
        model = new HelbTowerModel(ROWS, COLUMNS, DAY, mainChar);
        view = new HelbTowerView(WIDTH, HEIGHT, ROWS, COLUMNS, SQUARE_SIZE);

        start(primaryStage);
    }

    public void start(Stage primaryStage) {
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

                if (model.isANewCycle(lastTimeKeyPressed, model.getMainCharDelay()) || model.isPotionEffect()) {
                    lastTimeKeyPressed = System.currentTimeMillis();

                    if (code == KeyCode.RIGHT || code == KeyCode.D) {
                        mainChar.setRight();
                        if (mainChar.isNextCaseIsAvaible(model.getGameElementList())
                                || model.isWearingCloak() && mainChar.getX() + 1 < ROWS - 1) {
                            mainChar.moveRight();
                            // add in memory for additional
                            mainChar.addThisPositionInMemory();
                        }
                    } else if (code == KeyCode.LEFT || code == KeyCode.Q) {
                        mainChar.setLeft();
                        if (mainChar.isNextCaseIsAvaible(model.getGameElementList())
                                || model.isWearingCloak() && mainChar.getX() - 1 > 0) {
                            mainChar.moveLeft();
                            // add in memory for additional
                            mainChar.addThisPositionInMemory();
                        }
                    } else if (code == KeyCode.UP || code == KeyCode.Z) {
                        mainChar.setUp();
                        if (mainChar.isNextCaseIsAvaible(model.getGameElementList())
                                || model.isWearingCloak() && mainChar.getY() - 1 > 0) {
                            mainChar.moveUp();
                            // add in memory for additional
                            mainChar.addThisPositionInMemory();
                        }
                    } else if (code == KeyCode.DOWN || code == KeyCode.S) {
                        mainChar.setDown();
                        if (mainChar.isNextCaseIsAvaible(model.getGameElementList())
                                || model.isWearingCloak() && mainChar.getY() + 1 < COLUMNS - 1) {
                            mainChar.moveDown();
                            // add in memory for additional
                            mainChar.addThisPositionInMemory();
                        }
                    }
                    System.out.println("x:" + mainChar.getX() + " ; y:" + mainChar.getY()); // DEBUG POSITION
                }

                // CHEAT CODES
                if (code == KeyCode.DIGIT0 || code == KeyCode.NUMPAD0) {
                    initGame();
                    model.setScore(0);
                    System.out.println("Game reset");
                } else if (code == KeyCode.DIGIT1 || code == KeyCode.NUMPAD1) {
                    model.setPeriod(MORNING);
                    model.setGuardDelay(MORNING_DELAY);
                    System.out.println("Game set on morning");
                } else if (code == KeyCode.DIGIT2 || code == KeyCode.NUMPAD2) {
                    model.setPeriod(DAY);
                    model.setGuardDelay(DAY_DELAY);
                    System.out.println("Game set on day");
                } else if (code == KeyCode.DIGIT3 || code == KeyCode.NUMPAD3) {
                    model.setPeriod(NIGHT);
                    model.setGuardDelay(NIGHT_DELAY);
                    System.out.println("Game set on night");
                } else if (code == KeyCode.DIGIT4 || code == KeyCode.NUMPAD4) {
                    model.generatePotion();
                } else if (code == KeyCode.DIGIT5 || code == KeyCode.NUMPAD5) {
                    model.generateCloak();
                } else if (code == KeyCode.DIGIT6 || code == KeyCode.NUMPAD6) {
                    Guard newGuard = model.generateRandomGuard();
                    cheatedGuard.add(newGuard);
                    charactersArray.add(newGuard);
                    System.out.println("Random guard generate: " + newGuard);
                    
                    Timeline cheatedGuardTimeline = new Timeline(new KeyFrame(Duration.millis(model.getGuardDelay()), e -> newGuard.move(model.getGameElementList())));
                    cheatedGuardTimeline.setCycleCount(Animation.INDEFINITE);
                    cheatedGuardTimeline.play();
                } else if (code == KeyCode.R) {
                    redGuard.unsetAlive();
                } else if (code == KeyCode.B) {
                    blueGuard.unsetAlive();
                } else if (code == KeyCode.M) {
                    purpleGuard.unsetAlive();
                } else if (code == KeyCode.O) {
                    orangeGuard.unsetAlive();
                } else if (code == KeyCode.S) {
                    if (isAdditionalActivated) {
                        isAdditionalActivated = false;
                        model.setScore(0);
                        initGame();
                        System.out.println("Additional desactivated");
                    } else {
                        isAdditionalActivated = true;
                        model.setScore(0);
                        initGame();
                        System.out.println("Additional activated");
                    }
                }
            }
        });

        charactersPathMap.putAll(mainChar.getCharSkinMap());
        charactersPathMap.putAll(orangeGuard.getCharSkinMap());
        charactersPathMap.putAll(blueGuard.getCharSkinMap());
        charactersPathMap.putAll(purpleGuard.getCharSkinMap());
        charactersPathMap.putAll(redGuard.getCharSkinMap());
        
        initGame();

        coinNbr = model.getCoinCounter();
    }

    private void run(GraphicsContext gc) {
        
        if (model.getGameOver()) {
            if (model.getScore() > model.getBestScore()) {
                model.setBestScore(model.getScore());
                model.writeBestScore();
            }
            view.drawGameOver(gc);
            timeline.stop();
            return;
        }

        // Views
        view.drawBackground(model.getPeriod(), gc);
        view.drawGameElements(model.getGameElementList(), gc);

        view.drawChar(charactersArray, gc);
        view.drawScore(model.getScore(), model.getCoinCounter(), model.getBestScore(), gc);

        // Models
        if (model.getLevelFinished()) {
            timeline.stop();
            initGame();
            model.setGuardDelay(model.getGuardDelay()-10);
            model.unsetLevelFinished();
        }

        model.eatGameElement(purpleGuard.isAlive());

        if (is25percentCoinsTaked()) {
            orangeGuard.setAlive();
            blueGuard.setAlive();
            purpleGuard.setAlive();
            redGuard.setAlive();
        }

        if (model.isANewCycle(lastTimeGuardMove, model.getGuardDelay())) {
            lastTimeGuardMove = System.currentTimeMillis();
            orangeGuard.move(model.getGameElementList());
            blueGuard.move(model.getGameElementList());
            redGuard.move(model.getGameElementList());
        }

        if (model.isANewCycle(lastTimePurpleGuardMove, model.getPurpleDelay())) {
            lastTimePurpleGuardMove = System.currentTimeMillis();
            purpleGuard.move(model.getGameElementList());
        }

        model.triggerPortal(mainChar);

        //mainChar.isKillByGuards(charactersArray);

        if (!(mainChar.isAlive())) {
            model.setGameOver();
        }
    }

    public void initGame() {

        // On désactive le  Game over
        model.unsetGameOver();

        // On vide l'array des characters
        charactersArray.clear();

        // On rempli l'array des characters
        charactersArray.add(mainChar);
        charactersArray.add(orangeGuard);
        charactersArray.add(blueGuard);
        charactersArray.add(purpleGuard);
        charactersArray.add(redGuard);

        // On vide la list des games elements
        model.getGameElementList().clear();

        // Reset du Hero
        mainChar.getMemoryPostion().clear();
        mainChar.setAlive();
        mainChar.setLocation(ROWS / 2, COLUMNS / 4);
        
        // reset des structures
        model.generateTeleporter();
        model.generateBorder();
        model.generateWall(CROSS_OPENING);
        model.generateTower(TOWER_X, TOWER_Y);

        // Reset du garde orange
        orangeGuard.unsetAlive();
        orangeGuard.setLocation(TOWER_X, TOWER_Y);
        orangeGuard.setDown();

        // Reset du garde bleue
        blueGuard.unsetAlive();
        blueGuard.setLocation(ROWS - TOWER_X, TOWER_Y);
        blueGuard.setDown();

        // Reset du garde rouge
        redGuard.unsetAlive();
        redGuard.setLocation(TOWER_X, COLUMNS - TOWER_Y);
        redGuard.setDown();

        // Reset du garde mauve
        purpleGuard.unsetAlive();
        purpleGuard.setLocation(ROWS - TOWER_X, COLUMNS - TOWER_Y);
        purpleGuard.setDown();
        
        // regeneration des potion
        for (int i = 0; i < NBR_OF_POTIONS; i++) {
            model.generatePotion();
        }
        
        // regeneration des capes
        for (int i = 0; i < NBR_OF_CLOAK; i++) {
            model.generateCloak();
        }

        // generation des chronometre si activé
        if (isAdditionalActivated) {
            for (int i = 0; i < NBR_OF_CHRONOMETER; i++) {
                model.generateChronometer();
            }
        }

        // Lire le dernier best score enregistré
        model.readBestScore();

        // generation des coins
        model.generateCoin();

        // conversion des path en image dans la view
        view.convertPathToImage(model.getGameElementList(),
                                charactersArray,
                                charactersPathMap);

        // Remise en place de la timeline
        timeline = new Timeline(new KeyFrame(Duration.millis(20), e -> run(gc)));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    public boolean is25percentCoinsTaked() {
        return model.getCoinCounter() == (int) (coinNbr * 0.75) && !((orangeGuard.isAlive()) &&
                (blueGuard.isAlive()) &&
                (purpleGuard.isAlive()) &&
                (redGuard.isAlive()));
    }

}
