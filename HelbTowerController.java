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

    private int coinNbr;
    private int maxCaseForBlueGuard = 0;

    private long lastTimeKeyPressed = System.currentTimeMillis();
    private long lastTimeGuardMove = System.currentTimeMillis();
    private long lastTimePurpleGuardMove = System.currentTimeMillis();

    private GraphicsContext gc;

    private ArrayList<Character> charactersArray = new ArrayList<>();
    private HashMap<String, String> charactersPathMap = new HashMap<>();

    public HelbTowerController(Stage primaryStage) {
        mainChar = new MainCharacter(ROWS / 2, COLUMNS / 4);
        orangeGuard = new OrangeGuard(TOWER_X, TOWER_Y);
        blueGuard = new BlueGuard(ROWS - TOWER_X, TOWER_Y);
        purpleGuard = new PurpleGuard(ROWS - TOWER_X, COLUMNS - TOWER_Y, TOWER_X, TOWER_Y, ROWS, COLUMNS);
        redGuard = new RedGuard(TOWER_X, COLUMNS - TOWER_Y);
        model = new HelbTowerModel(ROWS, COLUMNS, mainChar);
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
                if (model.isANewCycle(lastTimeKeyPressed, model.getDelay()) || model.isPotionEffect()) {
                    lastTimeKeyPressed = System.currentTimeMillis();

                    if (code == KeyCode.RIGHT || code == KeyCode.D) {
                        mainChar.setRight();
                        if (mainChar.isNextCaseIsAvaible(model.getGameElementList())
                                || model.isWearingCloak() && mainChar.getX() + 1 < ROWS - 1) {
                            mainChar.moveRight();
                        }
                    } else if (code == KeyCode.LEFT || code == KeyCode.Q) {
                        mainChar.setLeft();
                        if (mainChar.isNextCaseIsAvaible(model.getGameElementList())
                                || model.isWearingCloak() && mainChar.getX() - 1 >= 0) {
                            mainChar.moveLeft();
                        }
                    } else if (code == KeyCode.UP || code == KeyCode.Z) {
                        mainChar.setUp();
                        if (mainChar.isNextCaseIsAvaible(model.getGameElementList())
                                || model.isWearingCloak() && mainChar.getY() - 1 > 0) {
                            mainChar.moveUp();
                        }
                    } else if (code == KeyCode.DOWN || code == KeyCode.S) {
                        mainChar.setDown();
                        if (mainChar.isNextCaseIsAvaible(model.getGameElementList())
                                || model.isWearingCloak() && mainChar.getY() + 1 < COLUMNS - 1) {
                            mainChar.moveDown();
                        }
                    }
                    System.out.println("x:" + mainChar.getX() + " ; y:" + mainChar.getY()); // DEBUG POSITION
                }

            }
        });

        model.generateBorder();
        model.generateWall(CROSS_OPENING);
        model.generateTower(TOWER_X, TOWER_Y);
        for (int i = 0; i < 5; i++) {
            model.generateCloak();
        }
        for (int i = 0; i < 6; i++) {
            model.generatePotion();
        }
        model.generateCoin();

        coinNbr = model.getCoinCounter();

        for (GameElement gameElem : model.getGameElementList()) {
            if (!(gameElem instanceof Wall)) {
                maxCaseForBlueGuard++;
            }
        }

        charactersArray.add(mainChar);
        charactersArray.add(orangeGuard);
        charactersArray.add(blueGuard);
        charactersArray.add(purpleGuard);
        charactersArray.add(redGuard);

        charactersPathMap.putAll(mainChar.getCharSkinMap());
        charactersPathMap.putAll(orangeGuard.getCharSkinMap());
        charactersPathMap.putAll(blueGuard.getCharSkinMap());
        charactersPathMap.putAll(purpleGuard.getCharSkinMap());
        charactersPathMap.putAll(redGuard.getCharSkinMap());

        view.convertPathToImage(model.getGameElementList(),
                charactersArray,
                charactersPathMap);

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(20), e -> run(gc)));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private void run(GraphicsContext gc) {
        // Views
        if (model.getGameOver()) {
            view.drawGameOver(gc);
            return;
        }

        view.drawBackground(gc);
        view.drawGameElements(model.getGameElementList(), gc);

        view.drawChar(charactersArray, gc);
        view.drawScore(model.getScore(), model.getCoinCounter(), gc);

        // Models
        model.eatGameElement();

        if (is25percentCoinsTaked()) {
            orangeGuard.setAlive();
            blueGuard.setAlive();
            purpleGuard.setAlive();
            redGuard.setAlive();
        }

        if (model.isANewCycle(lastTimeGuardMove, model.getDelay())) {
            lastTimeGuardMove = System.currentTimeMillis();
            orangeGuard.move(model.getGameElementList());
            blueGuard.move(model.getGameElementList(), ROWS, COLUMNS, maxCaseForBlueGuard);
            redGuard.move(model.getGameElementList(), mainChar.getX(), mainChar.getY());
        }

        if (model.isANewCycle(lastTimePurpleGuardMove, model.getPurpleDelay())) {
            lastTimePurpleGuardMove = System.currentTimeMillis();
            purpleGuard.move(model.getGameElementList());
        }

        model.triggerPortal(mainChar);

        // mainChar.isKillByGuards(charactersArray);

        if (!(mainChar.isAlive())) {
            model.setGameOver();
        }
    }

    public boolean is25percentCoinsTaked() {
        return model.getCoinCounter() == (int) (coinNbr * 0.75) && !((orangeGuard.isAlive()) &&
                (blueGuard.isAlive()) &&
                (purpleGuard.isAlive()) &&
                (redGuard.isAlive()));
    }

}
