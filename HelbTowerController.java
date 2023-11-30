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
    
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final int ROWS = 20;
    private static final int COLUMNS = 15;
    private static final int SQUARE_SIZE = WIDTH / ROWS;
    private static final int RIGHT_BORDER = ROWS - 2;
    private static final int BOTTOM_BORDER = COLUMNS - 2;

    private GraphicsContext gc;

    private int currentDirection;

    private static final int RIGHT = 0;
    private static final int LEFT = 1;
    private static final int DOWN = 2;
    private static final int UP = 3;

    public HelbTowerController(Stage primaryStage) {
        model = new HelbTowerModel(ROWS, COLUMNS);
        view = new HelbTowerView(WIDTH, HEIGHT, ROWS, COLUMNS, SQUARE_SIZE, RIGHT, LEFT, DOWN, UP);
        teleporter = new Teleporter((int)ROWS/2, (int)COLUMNS/2, model.getCharHead());
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
                    if (model.getCharHead().getX() < RIGHT_BORDER || model.getCharHead().getY() == teleporter.getY()) {
                        model.moveRight();
                        currentDirection = RIGHT;
                    }
                } else if (code == KeyCode.LEFT || code == KeyCode.A) {
                    if (model.getCharHead().getX() > 1 || model.getCharHead().getY() == teleporter.getY()) {
                        model.moveLeft();
                        currentDirection = LEFT;
                    }
                } else if (code == KeyCode.UP || code == KeyCode.W) {
                    if (model.getCharHead().getY() > 1|| model.getCharHead().getX() == teleporter.getX() ) {
                        model.moveUp();
                        currentDirection = UP;
                    }
                } else if (code == KeyCode.DOWN || code == KeyCode.S) {
                    if (model.getCharHead().getY() < BOTTOM_BORDER || model.getCharHead().getX() == teleporter.getX()) {
                        model.moveDown();
                        currentDirection = DOWN;
                    }
                }
                System.out.println("x:" + model.getCharHead().getX() + " ; y:" + model.getCharHead().getY()); // DEBUG POSITION
            }
        });

        model.generateFood();
        
        for (int i = 0; i < model.getCoinCounter(); i++) {
            model.generateCoin();
        }

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(130), e -> run(gc)));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private void run(GraphicsContext gc) {
        if (model.getGameOver()) {
            view.gameOver(gc);
            return;
        }
        view.drawBackground(gc, teleporter.getX(), teleporter.getY());
        view.drawGameElements(model.getGameElementList(), model.getImageMap(), gc);
        
        view.drawChar(model.getCharHead(), currentDirection, gc);
        view.drawScore(model.getScore(), model.getCoinCounter(), gc);
        

        //gameOver();
        model.eatFood();
        model.eatCoin();
        teleporter.portal(RIGHT_BORDER, BOTTOM_BORDER);
    }
}
