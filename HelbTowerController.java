import java.awt.Point;
import java.util.ArrayList;

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
    private Character mainChar;
    
    private static final int WIDTH = 1050;
    private static final int HEIGHT = 750;
    private static final int ROWS = 21;
    private static final int COLUMNS = 15;
    private static final int SQUARE_SIZE = WIDTH / ROWS;

    private GraphicsContext gc;

    private int currentDirection;
    private int numberOfGameElements;

    private ArrayList<Point> gameElementsPoints = new ArrayList<>();

    private static final int RIGHT = 0;
    private static final int LEFT = 1;
    private static final int DOWN = 2;
    private static final int UP = 3;

    public HelbTowerController(Stage primaryStage) {
        mainChar = new Character(6, 2);
        model = new HelbTowerModel(ROWS, COLUMNS, mainChar.getCharPoint(), gameElementsPoints);
        view = new HelbTowerView(WIDTH, HEIGHT, ROWS, COLUMNS, SQUARE_SIZE, RIGHT, LEFT, DOWN, UP);
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
                    if (wall.isNextCaseIsAWall(mainChar.getCharPoint(), RIGHT)) {
                        mainChar.moveRight();
                        currentDirection = RIGHT;
                    }
                } else if (code == KeyCode.LEFT || code == KeyCode.Q) {
                    if (wall.isNextCaseIsAWall(mainChar.getCharPoint(), LEFT)) {
                        mainChar.moveLeft();
                        currentDirection = LEFT;
                    }
                } else if (code == KeyCode.UP || code == KeyCode.Z) {
                    if (wall.isNextCaseIsAWall(mainChar.getCharPoint(), UP)) {
                        mainChar.moveUp();
                        currentDirection = UP;
                    }
                } else if (code == KeyCode.DOWN || code == KeyCode.S) {
                    if (wall.isNextCaseIsAWall(mainChar.getCharPoint(), DOWN)) {
                        mainChar.moveDown();
                        currentDirection = DOWN;
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
        
        view.convertPathToImage(model.getGameElementList(), 
                                model.getPathToImageMap(),
                                mainChar.getCharSkinMap(), 
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
        view.drawTelporter(teleporter.getPortalHashMap(),
                           gc);
        view.drawGameElements(model.getGameElementList(), gc);

        view.drawChar(mainChar.getCharPoint(), currentDirection, gc);
        view.drawScore(model.getScore(), model.getCoinCounter(), gc);

        model.eatFood();
        model.eatCoin();
        teleporter.triggerPortal(mainChar.getCharPoint());
    }

}
