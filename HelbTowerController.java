import java.awt.Point;
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
    private Teleporter teleporter;
    private Wall wall;
    private MainCharacter mainChar;
    private OrangeGuard orangeGuard;
    
    private static final int WIDTH = 1050;
    private static final int HEIGHT = 750;
    private static final int ROWS = 21;
    private static final int COLUMNS = 15;
    private static final int SQUARE_SIZE = WIDTH / ROWS;

    private GraphicsContext gc;

    private int numberOfGameElements;
    private long tmpTime = System.currentTimeMillis() / 1000;

    private ArrayList<Point> gameElementsPoints = new ArrayList<>();
    private ArrayList<Character> charactersArray = new ArrayList<>();
    private HashMap<String, String> charactersPathMap = new HashMap<>();

    public HelbTowerController(Stage primaryStage) {
        mainChar = new MainCharacter(6, 2);
        orangeGuard = new OrangeGuard();
        model = new HelbTowerModel(ROWS, COLUMNS, mainChar.getCharPoint(), gameElementsPoints);
        view = new HelbTowerView(WIDTH, HEIGHT, ROWS, COLUMNS, SQUARE_SIZE);
        teleporter = new Teleporter(ROWS, COLUMNS);
        wall = new Wall(ROWS, COLUMNS, teleporter.getPortalHashMap());

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
                    if (mainChar.isNextCaseIsAvaible(wall.getWallArrayList())) {
                        mainChar.moveRight();
                    }
                } else if (code == KeyCode.LEFT || code == KeyCode.Q) {
                    mainChar.setLeft();
                    if (mainChar.isNextCaseIsAvaible(wall.getWallArrayList())) {
                        mainChar.moveLeft();
                    }
                } else if (code == KeyCode.UP || code == KeyCode.Z) {
                    mainChar.setUp();
                    if (mainChar.isNextCaseIsAvaible(wall.getWallArrayList())) {
                        mainChar.moveUp();
                    }
                } else if (code == KeyCode.DOWN || code == KeyCode.S) {
                    mainChar.setDown();
                    if (mainChar.isNextCaseIsAvaible(wall.getWallArrayList())) {
                        mainChar.moveDown();
                    }
                }
                System.out.println("x:" + mainChar.getCharPoint().getX() + " ; y:" + mainChar.getCharPoint().getY()); // DEBUG POSITION
            }
        });

        wall.generateWall();
        wall.generateTower();
        
        gameElementsPoints.add(mainChar.getCharPoint());
        gameElementsPoints.addAll(wall.getWallArrayList());

        model.generateFood();

        numberOfGameElements = (ROWS*COLUMNS)-gameElementsPoints.size()-teleporter.getPortalHashMap().size()+1;

        for (int i = 0; i < numberOfGameElements; i++) {
            model.generateCoin();
        }
        
        charactersArray.add(mainChar);
        charactersArray.add(orangeGuard);
        charactersPathMap.putAll(mainChar.getCharSkinMap());
        charactersPathMap.putAll(orangeGuard.getCharSkinMap());

        view.convertPathToImage(model.getGameElementList(), 
                                charactersArray,
                                model.getPathToImageMap(),
                                charactersPathMap, 
                                wall.getPathToImg(),
                                teleporter.getPathToImages());
        

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(20), e -> run(gc)));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }
    
    private void run(GraphicsContext gc) {
        if (model.getGameOver()) {
            view.drawGameOver(gc);
            return;
        }

        view.drawBackground(gc);
        view.drawWall(wall.getWallArrayList(), gc);
        view.drawTelporter(teleporter.getPortalHashMap(), gc);
        view.drawGameElements(model.getGameElementList(), gc);

        view.drawChar(charactersArray, gc);
        view.drawScore(model.getScore(), model.getCoinCounter(), gc);

        model.eatFood();
        model.eatCoin();
        teleporter.triggerPortal(mainChar.getCharPoint());

        if (model.getCoinCounter() == (int) (numberOfGameElements - (numberOfGameElements * 0.25))) {
            orangeGuard.setAlive();
        }
        
        if ((System.currentTimeMillis() / 1000) >= (tmpTime + 1)) {
            orangeGuard.spawnGuard(wall.getWallArrayList());
            tmpTime = System.currentTimeMillis() / 1000;
        }

        teleporter.triggerPortal(orangeGuard.getCharPoint());

    }

}
