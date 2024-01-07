import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    
    // Point d'entrer du programme
    @Override
    public void start(Stage primaryStage) throws Exception {
        Controller controller = new Controller(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }

}
