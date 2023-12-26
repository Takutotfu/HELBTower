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

    private GraphicsContext gc;

    private int numberOfGameElements;
    
    private ArrayList<Character> charactersArray = new ArrayList<>();
    private HashMap<String, String> charactersPathMap = new HashMap<>();

    public HelbTowerController(Stage primaryStage) {
        mainChar = new MainCharacter(6, 2);
        orangeGuard = new OrangeGuard();
        blueGuard = new BlueGuard();
        purpleGuard = new PurpleGuard();
        redGuard = new RedGuard();
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
                if (code == KeyCode.RIGHT || code == KeyCode.D) {
                    mainChar.setRight();
                    if (mainChar.isNextCaseIsAvaible(model.getGameElementList())) {
                        mainChar.moveRight();
                    }
                } else if (code == KeyCode.LEFT || code == KeyCode.Q) {
                    mainChar.setLeft();
                    if (mainChar.isNextCaseIsAvaible(model.getGameElementList())) {
                        mainChar.moveLeft();
                    }
                } else if (code == KeyCode.UP || code == KeyCode.Z) {
                    mainChar.setUp();
                    if (mainChar.isNextCaseIsAvaible(model.getGameElementList())) {
                        mainChar.moveUp();
                    }
                } else if (code == KeyCode.DOWN || code == KeyCode.S) {
                    mainChar.setDown();
                    if (mainChar.isNextCaseIsAvaible(model.getGameElementList())) {
                        mainChar.moveDown();
                    }
                }
                System.out.println("x:" + mainChar.getX() + " ; y:" + mainChar.getY()); // DEBUG POSITION
            }
        });

        model.generateBorder();
        model.generateWall(CROSS_OPENING);
        model.generateTower();
        
        model.generateFood();

        numberOfGameElements = (ROWS*COLUMNS)-model.getGameElementList().size();

        for (int i = 0; i < numberOfGameElements; i++) {
            model.generateCoin();
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
        model.eatFood();
        model.eatCoin();
        
        if (is25percentCoinsTaked()) {
            orangeGuard.setAlive();
            blueGuard.setAlive();
            purpleGuard.setAlive();
            redGuard.setAlive();
        }
        
        if (model.isANewCycle()) {
            orangeGuard.spawnGuard(model.getGameElementList());
            blueGuard.spawnGuard(model.getGameElementList());
            //purpleGuard.spawnGuard(wall.getWallArrayList());
            //redGuard.spawnGuard(wall.getWallArrayList());
        }

        model.triggerPortal(mainChar);
        model.triggerPortal(orangeGuard);
        model.triggerPortal(blueGuard);

        mainChar.isKillByGuards(charactersArray);

        if (!(mainChar.isAlive())) {
            model.setGameOver();
        }
    }
    
    public boolean is25percentCoinsTaked() {
        return model.getCoinCounter() == (int) (numberOfGameElements - (numberOfGameElements * 0.25)) && !(orangeGuard.isAlive());
    }

}
