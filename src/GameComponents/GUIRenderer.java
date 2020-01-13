package GameComponents;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class GUIRenderer {
    private final double canvasWidth;
    private final double canvasHeight;

    public GUIRenderer(double canvasWidth, double canvasHeight) {
        this.canvasWidth = canvasWidth;
        this.canvasHeight = canvasHeight;
    }


    public void renderGUI(GraphicsContext gc) {
        // Clear Field
        gc.setFill(Color.WHITE);
        gc.setGlobalAlpha(1.0);
        gc.fillRect(0,0,canvasWidth,canvasHeight);

        gc.setFill(Color.BLUE);
        gc.fillRect(Math.random()*canvasWidth,Math.random()*canvasHeight,10,10);
    }
}
