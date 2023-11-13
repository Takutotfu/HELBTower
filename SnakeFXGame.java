import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.awt.Point;

public class SnakeFXGame extends Application {

    public static final int WIDTH = 800;
    public static final int HEIGHT = WIDTH;
    public static final int ROWS = 20;
    public static final int COLUMNS = ROWS;
    public static final int SQUARE_SIZE = WIDTH / ROWS;
    public static final int RIGHT_BORDER = ROWS - 1;
    public static final int BOTTOM_BORDER = COLUMNS - 1;

    private GraphicsContext gc;
    public static Point snakeHead;

    public Model model = new Model();

    @Override
    public void start(Stage primaryStage) throws Exception {
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
                    if (snakeHead.getX() < RIGHT_BORDER) {
                        moveRight();
                    }
                } else if (code == KeyCode.LEFT || code == KeyCode.A) {
                    if (snakeHead.getX() > 0) {
                        moveLeft();
                    }
                } else if (code == KeyCode.UP || code == KeyCode.W) {
                    if (snakeHead.getY() > 0) {
                        moveUp();
                    }
                } else if (code == KeyCode.DOWN || code == KeyCode.S) {
                    if (snakeHead.getY() < BOTTOM_BORDER) {
                        moveDown();
                    }
                }
                // DEBUG POSITION System.out.println(snakeHead.getX() + " || " + snakeHead.getY());
            }
        });
        
        snakeHead = new Point(5, ROWS/2);

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(130), e -> model.start(gc)));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private void moveRight() {
        snakeHead.x++;
    }

    private void moveLeft() {
        snakeHead.x--;
    }

    private void moveUp() {
        snakeHead.y--;
    }

    private void moveDown() {
        snakeHead.y++;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
