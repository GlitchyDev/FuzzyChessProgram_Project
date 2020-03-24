package GameComponents;

import GameComponents.Board.GameTeam;
import GameComponents.Board.Pieces.BoardLocation;
import GameComponents.Board.Pieces.GamePiece;
import GameComponents.Board.Pieces.GamePieceType;
import GameComponents.Board.Turn.Action;
import GameComponents.Controllers.AIController;
import GameComponents.Controllers.PlayerController;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;


/**
 * A Programing Window that will hold and split off all the functions of the applications into different objects
 */
public class ProgramWindow extends Application {
    private Stage primaryStage;
    private Canvas canvas;
    // The main Gamestate of the ongoing game
    private GameState gameState;
    // A renderer of all the elements in the window
    private GUIRenderer guiRenderer;
    // Takes note of player input and acts accordingly
    private PlayerController playerController;
    // Will take note of the board and let the AI controller take its action
    private AIController aiController;

    // Window Customizationhttps://i.imgur.com/JKRGsJI.png Options
    private boolean useAIDebugMode = false;

    private String PROGRAM_TITLE = "Fuzzy-Logic Chess Program";



    @Override
    public void start(Stage primaryStage) throws Exception{
        this.primaryStage = primaryStage;
        this.aiController = new AIController();
        this.gameState = new GameState(aiController);


        // Create JAVAFX Visuals
        primaryStage.setTitle(PROGRAM_TITLE);

        // Create the Canvas needed for rendering
        Group root = new Group();
        this.canvas = createCanvas();
        GraphicsContext gc = canvas.getGraphicsContext2D();
        VBox vBox = new VBox(createMenu(primaryStage),canvas);
        root.getChildren().add(vBox);
        //
        Scene scene = new Scene(root, GUIRenderer.WINDOW_WIDTH, GUIRenderer.WINDOW_HEIGHT);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        // Bind after Canvas creation to get proper sizings
        this.guiRenderer = new GUIRenderer(gameState, this, canvas.getWidth(),canvas.getHeight());
        this.playerController = new PlayerController(gameState, guiRenderer);
        aiController.setGuiRenderer(guiRenderer);


        primaryStage.show();

        root.requestFocus();
        // Add Keyboard commands here
        root.setOnKeyPressed(keyEvent -> {
            System.out.println(keyEvent.getCode().getName());


        });




        // This is the Window Rendering Cycle, will go to UI Manager class most likely
        new AnimationTimer() {
            public void handle(long currentNanoTime) {
                guiRenderer.renderGUI(gc);
            }
        }.start();

    }


    // Creates the menu Elements of the Canvas
    public MenuBar createMenu(Stage primaryStage) {
        Menu menu = new Menu("Game Options");

        MenuItem menuItem1 = new MenuItem("Restart Game");
        menuItem1.setOnAction(event -> {
            gameState = new GameState(aiController);
            playerController.setGameState(gameState);
            guiRenderer.setGameState(gameState);
        });
        MenuItem menuItem2 = new MenuItem("Undo");

        menuItem2.setOnAction(event -> {
            if(gameState.isUseAIMode() && gameState.getCurrentTeamTurn() == GameTeam.WHITE && gameState.getTurnActions(gameState.getCurrentTurnNumber()).size() % 2 == 0) {
                gameState.undoAction(gameState.getPastActions().get(gameState.getPastActions().size() - 1), false);
                gameState.undoAction(gameState.getPastActions().get(gameState.getPastActions().size() - 1), false);
                gameState.undoAction(gameState.getPastActions().get(gameState.getPastActions().size() - 1), false);

            } else {
                gameState.undoAction(gameState.getPastActions().get(gameState.getPastActions().size() - 1), false);
            }
        });
        //                 gameState.undoAction(gameState.getPastActions().get(gameState.getPastActions().size() - 1), false);
        MenuItem menuItem3 = new MenuItem("Toggle AI Mode");
        menuItem3.setOnAction(event -> {
            gameState.toggleAIMode(aiController);
        });
        MenuItem menuItem4 = new MenuItem("Toggle AI Debug Mode");
        menuItem4.setOnAction(event -> {
            //toggleAIDebugMode();
            GamePiece preservedPiece = gameState.getGameBoard().getPiece(GamePieceType.KING,GameTeam.WHITE).get(0);
            ArrayList<GamePiece> gamePieces = (ArrayList<GamePiece>) gameState.getGameBoard().getWhitePieces().clone();
            for(GamePiece gamePiece: gamePieces) {
                gameState.getGameBoard().deletePiece(gamePiece.getBoardLocation());
            }
            gameState.getGameBoard().addPiece(preservedPiece,preservedPiece.getBoardLocation().getX(),preservedPiece.getBoardLocation().getY());
        });
        //menuItem1.setGraphic(new ImageView("file:Files/TV_2.png"));
        MenuItem menuItem5 = new MenuItem("Quit");
        menuItem5.setOnAction(event -> {
            primaryStage.close();

        });
        //menuItem2.setGraphic(new ImageView("file:Files/Wine_Bottle.png"));

        menu.getItems().add(menuItem1);
        menu.getItems().add(menuItem2);
        menu.getItems().add(menuItem3);
        menu.getItems().add(menuItem4);
        menu.getItems().add(menuItem5);


        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().add(menu);

        // Add Events that reference back to the human controller

        return menuBar;
    }


    // Creates the canvas and related events ( passing to the playerController )
    public Canvas createCanvas() {
        Canvas canvas = new Canvas(GUIRenderer.WINDOW_WIDTH+16,GUIRenderer.WINDOW_HEIGHT);
        canvas.setOnMouseMoved(event -> {
            playerController.checkMouseMovement(event.getX(),event.getY());
        });

        canvas.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                playerController.checkMouseLeftClick((int)event.getX(),(int)event.getY());
            }

            if (event.getButton() == MouseButton.SECONDARY) {
                playerController.checkMouseRightClick((int)event.getX(),(int)event.getY());
            }
        });
        return canvas;
    }

    public void toggleAIDebugMode() {
        this.useAIDebugMode = !useAIDebugMode;
        if(useAIDebugMode) {
            primaryStage.setWidth(GUIRenderer.WINDOW_WIDTH * 2);
            canvas.setWidth(GUIRenderer.WINDOW_WIDTH * 2);

        } else {
            primaryStage.setWidth(GUIRenderer.WINDOW_WIDTH);
            canvas.setWidth(GUIRenderer.WINDOW_WIDTH);
        }
    }

    public boolean isUseAIDebugMode() {
        return useAIDebugMode;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
