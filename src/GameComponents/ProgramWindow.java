package GameComponents;

import GameComponents.Board.Pieces.BoardLocation;
import GameComponents.Board.Pieces.GamePiece;
import GameComponents.Board.Turn.Action;
import GameComponents.Controllers.PlayerController;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
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


public class ProgramWindow extends Application {
    private FileLoader fileLoader;
    private GameState gameState;
    private GUIRenderer guiRenderer;
    private PlayerController playerController;

    // Window Customization Options
    private final int WINDOW_WIDTH = 500;
    private final int WINDOW_HEIGHT = 500;
    private String PROGRAM_TITLE = "Fuzzy-Logic Chess Program";


    private int debugValue = 0;
    private BoardLocation boardLocation;

    @Override
    public void start(Stage primaryStage) throws Exception{
        this.fileLoader = new FileLoader();
        this.playerController = new PlayerController();
        this.gameState = new GameState();


        // Create JAVAFX Visuals
        primaryStage.setTitle(PROGRAM_TITLE);

        Group root = new Group();
        Canvas canvas = createCanvas();
        GraphicsContext gc = canvas.getGraphicsContext2D();
        VBox vBox = new VBox(createMenu(),canvas);
        root.getChildren().add(vBox);
        //
        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        // Bind after Canvas creation to get proper sizings
        this.guiRenderer = new GUIRenderer(canvas.getWidth(),canvas.getHeight());

        primaryStage.show();


        System.out.println(gameState.getGameBoard());



        // This is the Window Rendering Cycle, will go to UI Manager class most likely
        // TODO move into a UI manager class that will handle rendering
        new AnimationTimer() {
            public void handle(long currentNanoTime) {
                guiRenderer.renderGUI(gc);
            }
        }.start();

    }

    public MenuBar createMenu() {
        Menu menu = new Menu("Menu 1");
        MenuItem menuItem1 = new MenuItem("Item 1");
        menuItem1.setGraphic(new ImageView("file:Files/TV_2.png"));
        MenuItem menuItem2 = new MenuItem("Item 2");
        menuItem2.setGraphic(new ImageView("file:Files/Wine_Bottle.png"));

        menu.getItems().add(menuItem1);
        menu.getItems().add(menuItem2);

        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().add(menu);

        // Add Events that reference back to the human controller

        return menuBar;
    }


    private final int selectedPieceX = 1;
    private final int selectedPieceY = 7;

    public Canvas createCanvas() {
        Canvas canvas = new Canvas(WINDOW_WIDTH+16,WINDOW_HEIGHT);
        canvas.setOnMouseMoved(event -> {

        });

        canvas.setOnMouseClicked(event -> {
            System.out.println("Bees");

            if (event.getButton() == MouseButton.PRIMARY) {
                GamePiece gamePiece = boardLocation == null ? gameState.getGameBoard().getPiece(selectedPieceX,selectedPieceY) : gameState.getGameBoard().getPiece(boardLocation);
                ArrayList<Action> actions = gameState.getValidActions(gamePiece);
                for(Action action: actions) {
                    System.out.println(action);
                }
            }

            if (event.getButton() == MouseButton.SECONDARY) {

                GamePiece gamePiece1 = boardLocation == null ? gameState.getGameBoard().getPiece(selectedPieceX, selectedPieceY) : gameState.getGameBoard().getPiece(boardLocation);
                ArrayList<Action> actions1 = gameState.getValidActions(gamePiece1);
                for(Action action: actions1) {
                    System.out.println(action);
                }
                gameState.preformAction(actions1.get(0));
                boardLocation = actions1.get(0).getGamePiece().getBoardLocation();

                System.out.println(gameState.getGameBoard());

                debugValue++;
            }
        });
        canvas.setOnKeyPressed(keyEvent -> {
            System.out.println("Bees");

            switch(debugValue) {
                case 0:
                    GamePiece gamePiece = gameState.getGameBoard().getPiece(0,6);
                    ArrayList<Action> actions = gameState.getValidActions(gamePiece);
                    for(Action action: actions) {
                        System.out.println(action);
                    }
                    break;
                case 1:


                    break;
                case 2:


                    break;
                case 3:


                    break;
            }
        });
        return canvas;
    }


    public static void main(String[] args) {
        launch(args);
    }
}
