package sample;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;


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
        StackPane stackPane = new StackPane();

        Canvas canvas = new Canvas(WINDOW_WIDTH,WINDOW_HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        stackPane.getChildren().add(canvas);

        gc.setFill(Color.AQUA);
        gc.fillRect(0,0,WINDOW_WIDTH, WINDOW_HEIGHT);

        Scene scene = new Scene(stackPane, WINDOW_WIDTH, WINDOW_HEIGHT);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();



        scene.setOnMouseMoved(event -> {
            x = (int) event.getSceneX();
            y = (int) event.getSceneY();
        });

        new AnimationTimer() {
            // This happens 60fps, but still check time with StateStartTime
            public void handle(long currentNanoTime) {
                // Clear Field
                gc.setFill(Color.WHITE);
                gc.setGlobalAlpha(1.0);
                gc.fillRect(0,0,WINDOW_WIDTH,WINDOW_HEIGHT);

                gc.setFill(Color.BLUE);
                gc.fillRect(x,y,10,10);


            }
        }.start();

    }


    public static void main(String[] args) {
        launch(args);
    }
}
