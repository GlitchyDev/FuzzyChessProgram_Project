package GameComponents;

import GameComponents.Board.Pieces.BoardLocation;
import GameComponents.Board.Pieces.GamePiece;
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


public class ProgramWindow extends Application {
    private FileLoader fileLoader;
    private GameState gameState;
    private GUIRenderer guiRenderer;
    private PlayerController playerController;
    private AIController aiController;

    // Window Customization Options
    private final int WINDOW_WIDTH = 420;
    private final int WINDOW_HEIGHT = 445;
    private String PROGRAM_TITLE = "Fuzzy-Logic Chess Program";


    private BoardLocation boardLocation;

    @Override
    public void start(Stage primaryStage) throws Exception{
        this.fileLoader = new FileLoader();
        this.aiController = new AIController();
        this.gameState = new GameState(aiController);


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
        this.guiRenderer = new GUIRenderer(gameState, canvas.getWidth(),canvas.getHeight());
        this.playerController = new PlayerController(gameState, guiRenderer);
        aiController.setGuiRenderer(guiRenderer);


        primaryStage.show();

        root.requestFocus();
        // Add Debug Commands
        root.setOnKeyPressed(keyEvent -> {
            System.out.println(keyEvent.getCode().getName());


        });




        // This is the Window Rendering Cycle, will go to UI Manager class most likely
        // TODO move into a UI manager class that will handle rendering
        new AnimationTimer() {
            public void handle(long currentNanoTime) {
                guiRenderer.renderGUI(gc);
            }
        }.start();

    }

    public MenuBar createMenu() {
        Menu menu = new Menu("Game Options");

        MenuItem menuItem1 = new MenuItem("Restart Game");
        MenuItem menuItem2 = new MenuItem("Undo");
        MenuItem menuItem3 = new MenuItem("Redo");
        MenuItem menuItem4 = new MenuItem("Toggle AI Mode");

        menuItem4.setOnAction(event -> {
            gameState.toggleAIMode(aiController);
        });

        //menuItem1.setGraphic(new ImageView("file:Files/TV_2.png"));
        MenuItem menuItem5 = new MenuItem("Quit");
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



    public Canvas createCanvas() {
        Canvas canvas = new Canvas(WINDOW_WIDTH+16,WINDOW_HEIGHT);
        canvas.setOnMouseMoved(event -> {

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


    public static void main(String[] args) {
        launch(args);
    }
}
