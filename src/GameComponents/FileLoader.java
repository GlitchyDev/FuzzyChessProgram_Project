package GameComponents;

import javafx.scene.image.Image;

import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

/**
 * Loads all the Images into the proper format
 */
public class FileLoader {
    private static HashMap<String,Image> loadedImages = new HashMap<>();
    private static boolean isJar;

    public FileLoader() {
        URL url = this.getClass().getClassLoader().getResource("50/BB.png");
        URLConnection urlConnection = null;
        try {
            urlConnection = url.openConnection();
            if (urlConnection instanceof JarURLConnection) {
                isJar = true;
            } else {
                isJar = false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("AM I a jar? " + isJar);
    }

    public static Image getImage(String fileLocation) {
        if(loadedImages.containsKey(fileLocation)) {
            return loadedImages.get(fileLocation);
        } else {
            if(isJar) {
                return loadImageJar(fileLocation);
            } else {
                return loadImage(fileLocation);
            }
        }
    }
    private static Image loadImage(String fileLocation) {
        Image image = new Image("file:Files" + fileLocation);
        loadedImages.put(fileLocation,image);
        return image;
    }

    public static Image loadImageJar(String fileLocation) {
        Image image = new Image(FileLoader.class.getResourceAsStream(fileLocation));
        loadedImages.put(fileLocation,image);
        return image;
    }
}
