package sample;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;

import static com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type.Root;


public class FuzzyChessAIProgram extends Application {
    public static final int WINDOW_WIDTH = 500;
    public static int WINDOW_HEIGHT = 500;
    public static int WINDOW_SCALING = 5;

    private ArrayList<String> currentKeyPresses = new ArrayList<String>();

    private int x = 0;
    private int y = 0;

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("FuzzyAI Program");


        Group root = new Group();
        Canvas canvas = new Canvas(WINDOW_WIDTH+16,WINDOW_HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        Menu menu = new Menu("Menu 1");
        MenuItem menuItem1 = new MenuItem("Item 1");
        menuItem1.setGraphic(new ImageView("file:Files/TV_2.png"));
        MenuItem menuItem2 = new MenuItem("Item 2");
        menuItem2.setGraphic(new ImageView("file:Files/Wine_Bottle.png"));

        menu.getItems().add(menuItem1);
        menu.getItems().add(menuItem2);

        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().add(menu);


        VBox vBox = new VBox(menuBar,canvas);

        root.getChildren().add(vBox);


        gc.setFill(Color.AQUA);
        gc.fillRect(0,0,WINDOW_WIDTH, WINDOW_HEIGHT);

        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();



        canvas.setOnMouseMoved(event -> {
            x = (int) event.getX();
            y = (int) event.getY();
        });

        canvas.setOnMouseClicked(event -> {
            System.out.println("Primary Stage" + primaryStage.getWidth() + " " + primaryStage.getHeight());
            System.out.println("MenuBar" + menuBar.getWidth() + " " + menuBar.getHeight());
            System.out.println("Canvas" + canvas.getWidth() + " " + canvas.getHeight());
        });
        new AnimationTimer() {
            // This happens 60fps, but still check time with StateStartTime
            public void handle(long currentNanoTime) {
                // Clear Field
                gc.setFill(Color.WHITE);
                gc.setGlobalAlpha(1.0);
                gc.fillRect(0,0,canvas.getWidth(),canvas.getHeight());

                gc.setFill(Color.BLUE);
                gc.fillRect(x,y,10,10);


            }
        }.start();

    }


    public static void main(String[] args) {
        launch(args);
    }
}
