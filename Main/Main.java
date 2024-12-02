package Main;

import java.io.File;
import java.net.URL;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class Main extends Application {

    private static Scene scene;

    @Override
   public void start(Stage stage) throws Exception {
        // Memuat file FXML
        Parent root = FXMLLoader.load(getClass().getResource("/View/startPage.fxml"));
        
        // Mengatur scene dan menampilkan aplikasi
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("SABTINAMAN");
        stage.show();
    }
   public static void main(String[] args) {
        launch(args);
    }
}