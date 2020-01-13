package GameComponents;

import javafx.scene.image.Image;

public class FileLoader {


    public static Image loadImage(String fileLocation) {
        return new Image("file:Files/" + fileLocation);
    }
}
