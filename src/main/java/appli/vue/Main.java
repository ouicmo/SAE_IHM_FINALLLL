package appli.vue;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;

public class Main extends Application {
    public void start(Stage stage) throws Exception {
        VBoxRoot root = new VBoxRoot();
        Scene scene = new Scene(root, 1500, 1000);
        stage.setScene(scene);
        stage.setTitle("Table de scenario");
        stage.show();
        File css = new File("CSS/stylesheet.css");
        scene.getStylesheets().add(css.toURI().toString());
    }


    public static void main(String[] args) {
        launch(args);
    }
}
