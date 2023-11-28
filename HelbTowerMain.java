import javafx.application.Application;
import javafx.stage.Stage;

public class HelbTowerMain extends Application {
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        HelbTowerController controller = new HelbTowerController(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }

}
