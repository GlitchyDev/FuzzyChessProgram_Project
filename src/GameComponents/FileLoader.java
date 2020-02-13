package GameComponents;

import javafx.scene.image.Image;

import java.util.HashMap;

public class FileLoader {
    private static HashMap<String,Image> loadedImages = new HashMap<>();

    public static Image getImage(String fileLocation) {
        if(loadedImages.containsKey(fileLocation)) {
            return loadedImages.get(fileLocation);
        } else {
            return loadImage(fileLocation);
        }
    }
    private static Image loadImage(String fileLocation) {
        Image image = new Image("file:Files/" + fileLocation);
        loadedImages.put(fileLocation,image);
        return image;
    }
}
