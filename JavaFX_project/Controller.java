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

public class Controller {
    
    // Constantes de configuration
    
    private static final int WIDTH = 1050;
    private static final int HEIGHT = 750;
    public static final int ROWS = 21;
    public static final int COLUMNS = 15;
    private static final int SQUARE_SIZE = WIDTH / ROWS;
    private static final int CROSS_OPENING = 5; // = La croix au centre du board
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
    private static final int NIGHT_DELAY = 250;
    
    // Variables de JavaFX
    
    private GraphicsContext gc;
    private Timeline timeline;

    // Déclaration des Models

    private GameBoard gameBoard;
    private View view;
    private MainCharacter mainChar;
    private OrangeGuard orangeGuard;
    private BlueGuard blueGuard;
    private PurpleGuard purpleGuard;
    private RedGuard redGuard;

    // Variables de sauvegarde

    private int coinNbr;

    private long lastTimeKeyPressed = System.currentTimeMillis();
    private long lastTimeGuardMove = System.currentTimeMillis();
    private long lastTimePurpleGuardMove = System.currentTimeMillis();

    // Variables boolean 

    private boolean isChronometerActivated = false;
    private boolean isReset = false;

    // Tableaux de characters

    private ArrayList<Character> charactersArray = new ArrayList<>();
    private ArrayList<Character> cheatedGuard = new ArrayList<>();

    // Map des paths vers les images

    private HashMap<String, String> charactersPathMap = new HashMap<>();


    public Controller(Stage primaryStage) {
        // Constructeur du Controller

        mainChar = new MainCharacter(ROWS / 2, COLUMNS / 4);
        orangeGuard = new OrangeGuard(TOWER_X, TOWER_Y);
        blueGuard = new BlueGuard(ROWS - TOWER_X, TOWER_Y, ROWS, COLUMNS);
        purpleGuard = new PurpleGuard(ROWS - TOWER_X, COLUMNS - TOWER_Y, TOWER_X, TOWER_Y, ROWS, COLUMNS);
        redGuard = new RedGuard(TOWER_X, COLUMNS - TOWER_Y, mainChar);
        gameBoard = new GameBoard(ROWS, COLUMNS, DAY, mainChar);
        view = new View(WIDTH, HEIGHT, ROWS, COLUMNS, SQUARE_SIZE);

        start(primaryStage);
    }

    public void start(Stage primaryStage) {
        // Démarrage de l'application

        // Configuration de l'application JavaFX
        primaryStage.setTitle("HELBTower");
        Group root = new Group();
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        root.getChildren().add(canvas);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
        gc = canvas.getGraphicsContext2D();

        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            // Interaction lors de la pression d'une touche

            @Override
            public void handle(KeyEvent event) {
                KeyCode code = event.getCode();

                // Déplacement du Héro
                if (gameBoard.isTimePassed(lastTimeKeyPressed, gameBoard.getMainCharDelay()) || gameBoard.isPotionEffect()) {
                    lastTimeKeyPressed = System.currentTimeMillis();

                    if (code == KeyCode.D || code == KeyCode.RIGHT) {
                        mainChar.tryMove(Character.RIGHT, gameBoard.getGameElementList(), gameBoard.isWearingCloak());
                    } else if (code == KeyCode.Q || code == KeyCode.LEFT) {
                        mainChar.tryMove(Character.LEFT, gameBoard.getGameElementList(), gameBoard.isWearingCloak());
                    } else if (code == KeyCode.Z || code == KeyCode.UP) {
                        mainChar.tryMove(Character.UP, gameBoard.getGameElementList(), gameBoard.isWearingCloak());
                    } else if (code == KeyCode.S || code == KeyCode.DOWN) {
                        mainChar.tryMove(Character.DOWN, gameBoard.getGameElementList(), gameBoard.isWearingCloak());
                    }

                    System.out.println("x:" + mainChar.getX() + " ; y:" + mainChar.getY()); // DEBUG POSITION
                }

                // Code de triches
                if (code == KeyCode.DIGIT0 || code == KeyCode.NUMPAD0) { // Reset de la game
                    isReset = true;
                    initGame();
                    gameBoard.setScore(0);
                    System.out.println("Game reset");
                } else if (code == KeyCode.DIGIT1 || code == KeyCode.NUMPAD1) { // Passage du jeu en mode Matin
                    gameBoard.setPeriod(MORNING);
                    gameBoard.setGuardDelay(MORNING_DELAY);
                    System.out.println("Game set on morning");
                } else if (code == KeyCode.DIGIT2 || code == KeyCode.NUMPAD2) { // Passage du jeu en mode Jour
                    gameBoard.setPeriod(DAY);
                    gameBoard.setGuardDelay(DAY_DELAY);
                    System.out.println("Game set on day");
                } else if (code == KeyCode.DIGIT3 || code == KeyCode.NUMPAD3) { // Passage du jeu en mode Nuit
                    gameBoard.setPeriod(NIGHT);
                    gameBoard.setGuardDelay(NIGHT_DELAY);
                    System.out.println("Game set on night");
                } else if (code == KeyCode.DIGIT4 || code == KeyCode.NUMPAD4) { // Génère une potion
                    gameBoard.generatePotion();
                } else if (code == KeyCode.DIGIT5 || code == KeyCode.NUMPAD5) { // Génère une cape
                    gameBoard.generateCloak();
                } else if (code == KeyCode.DIGIT6 || code == KeyCode.NUMPAD6) { // Génère un garde aléatoire
                    Guard newGuard = gameBoard.generateRandomGuard();
                    cheatedGuard.add(newGuard);
                    charactersArray.add(newGuard);
                    System.out.println("Random guard generate: " + newGuard);
                    
                    Timeline cheatedGuardTimeline = new Timeline(new KeyFrame(Duration.millis(gameBoard.getGuardDelay()), e -> newGuard.move(gameBoard.getGameElementList())));
                    cheatedGuardTimeline.setCycleCount(Animation.INDEFINITE);
                    cheatedGuardTimeline.play();
                } else if (code == KeyCode.R) { // Arrête le garde rouge original
                    redGuard.unsetAlive(); 
                } else if (code == KeyCode.B) { // Arrête le garde bleu original
                    blueGuard.unsetAlive();
                } else if (code == KeyCode.M) { // Arrête le garde mauve original
                    purpleGuard.unsetAlive();
                } else if (code == KeyCode.O) { // Arrête le garde orange original
                    orangeGuard.unsetAlive();
                } else if (code == KeyCode.S) { // Active/Désactive les chronomètre
                    isReset = true;
                    isChronometerActivated = !isChronometerActivated;
                    gameBoard.setScore(0);
                    initGame();
                    System.out.println("Additional " + (isChronometerActivated ? "activated" : "deactivated"));
                }
            }
        });
        
        // Initialise la partie
        initGame();

        coinNbr = gameBoard.getCoinCounter();
    }

    private void run(GraphicsContext gc) {
        // Méthode executé dans la Timeline du jeu
        
        // Verifie si la partie est finie
        if (gameBoard.getGameOver()) {
            if (gameBoard.getScore() > gameBoard.getBestScore()) {
                gameBoard.setBestScore(gameBoard.getScore());
                gameBoard.writeBestScore();
            }
            view.showGameOver(gc);
            timeline.stop();
            return;
        }

        // Views (Pour l'affichage)
        view.drawBackground(gameBoard.getPeriod(), gc);
        view.drawGameElements(gameBoard.getGameElementList(), gc);

        view.drawChar(charactersArray, gc);
        view.showScore(gameBoard.getScore(), gameBoard.getCoinCounter(), gameBoard.getBestScore(), gc);

        // GameBoard (Pour la logique)
        if (gameBoard.getLevelFinished()) { // Si toute les pièce sont récupérées
            timeline.stop();
            initGame();
            gameBoard.setGuardDelay(gameBoard.getGuardDelay()-10);
            gameBoard.unsetLevelFinished();
        }

        gameBoard.eatGameElement(purpleGuard.isAlive());

        // Donne vie aux garde si 25% des pièces sont ramassées
        if (is25percentCoinsTaked()) {
            orangeGuard.setAlive();
            blueGuard.setAlive();
            purpleGuard.setAlive();
            redGuard.setAlive();
        }

        // Les gardes bougent lorsque leur delay est dépassé
        if (gameBoard.isTimePassed(lastTimeGuardMove, gameBoard.getGuardDelay())) {
            lastTimeGuardMove = System.currentTimeMillis();
            orangeGuard.move(gameBoard.getGameElementList());
            blueGuard.move(gameBoard.getGameElementList());
            redGuard.move(gameBoard.getGameElementList());
        }

        // Même chose mais le garde mauve a un delay a part
        if (gameBoard.isTimePassed(lastTimePurpleGuardMove, gameBoard.getPurpleDelay())) {
            lastTimePurpleGuardMove = System.currentTimeMillis();
            purpleGuard.move(gameBoard.getGameElementList());
        }

        // Tue le héro s'il est rencontré par un garde
        mainChar.killByGuard(charactersArray);

        // Si le héro est mort la partie est terminée
        if (!(mainChar.isAlive())) {
            gameBoard.setGameOver();
        }
    }

    public void initGame() {
        if (isReset) {
            // On désactive le Game over
            gameBoard.unsetGameOver();

            // On vide l'array des characters
            charactersArray.clear();

            // Réinitialisation du Hero
            mainChar.setAlive();
            mainChar.setLocation(ROWS / 2, COLUMNS / 4);
            mainChar.setDown();

            // Réinitialisation des gardes
            orangeGuard = new OrangeGuard(TOWER_X, TOWER_Y);
            blueGuard = new BlueGuard(ROWS - TOWER_X, TOWER_Y, ROWS, COLUMNS);
            purpleGuard = new PurpleGuard(ROWS - TOWER_X, COLUMNS - TOWER_Y, TOWER_X, TOWER_Y, ROWS, COLUMNS);
            redGuard = new RedGuard(TOWER_X, COLUMNS - TOWER_Y, mainChar);

            // On vide la list des gameElements
            gameBoard.getGameElementList().clear();

            isReset = false;
        }

        // On rempli l'array des characters
        charactersArray.add(mainChar);
        charactersArray.add(orangeGuard);
        charactersArray.add(blueGuard);
        charactersArray.add(purpleGuard);
        charactersArray.add(redGuard);

        // Génération des structures
        gameBoard.generateTeleporter();
        gameBoard.generateBorder();
        gameBoard.generateWall(CROSS_OPENING);
        gameBoard.generateTower(TOWER_X, TOWER_Y);

        // Génération des potion
        for (int i = 0; i < NBR_OF_POTIONS; i++) {
            gameBoard.generatePotion();
        }
        
        // regéneration des capes
        for (int i = 0; i < NBR_OF_CLOAK; i++) {
            gameBoard.generateCloak();
        }

        // génération des chronometres si activé
        if (isChronometerActivated) {
            for (int i = 0; i < NBR_OF_CHRONOMETER; i++) {
                gameBoard.generateChronometer();
            }
        }

        // generation des coins
        gameBoard.generateCoin();

        // Lire le dernier best score enregistré
        gameBoard.readBestScore();

        if (!isReset) {
            // Ajout des paths des character pour la view
            charactersPathMap.putAll(mainChar.getCharSkinMap());
            charactersPathMap.putAll(orangeGuard.getCharSkinMap());
            charactersPathMap.putAll(blueGuard.getCharSkinMap());
            charactersPathMap.putAll(purpleGuard.getCharSkinMap());
            charactersPathMap.putAll(redGuard.getCharSkinMap());

            // Chargement des paths dans la view
            view.loadPaths(gameBoard.getGameElementList(),
                           charactersArray,
                           charactersPathMap);
        }

        // Mise en route de la timeline
        timeline = new Timeline(new KeyFrame(Duration.millis(20), e -> run(gc)));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }
    
    public boolean is25percentCoinsTaked() {
        // Renvoie true si 25% des pièces sont récupérées et tout les gardes ne sont pas encore en vie, false sinon
        return gameBoard.getCoinCounter() == (int) (coinNbr * 0.75) && !((orangeGuard.isAlive()) &&
                (blueGuard.isAlive()) &&
                (purpleGuard.isAlive()) &&
                (redGuard.isAlive()));
    }

}
