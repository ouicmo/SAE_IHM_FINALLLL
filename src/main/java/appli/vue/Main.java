package appli.vue;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    public void start(Stage stage) {
        VBoxRoot root = new VBoxRoot();
        Scene scene = new Scene(root, 800, 300);
        stage.setScene(scene);
        stage.setTitle("Table de scenario");
        stage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
