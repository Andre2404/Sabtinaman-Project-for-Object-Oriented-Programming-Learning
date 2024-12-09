package Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class StartPageController {

    @FXML
    private Button com;

    @FXML
    private Button user;

    @FXML
    private ImageView imageView;

    @FXML
    public void initialize() {
        // Attach event handlers
        com.setOnAction(event -> loadScene("/View/signInCom.fxml"));
        user.setOnAction(event -> loadScene("/View/signInUser.fxml"));

        // Load image from resources folder
        try {
            Image img = new Image(getClass().getResource("file:C:\\Users\\User\\Documents\\NetBeansProjects\\Sabtinaman\\src\\main\\java\\Pictures\\Header.png").toExternalForm());
            imageView.setImage(img);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to load image.");
        }
    }

    private void loadScene(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            Stage stage = (Stage) user.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to load FXML: " + fxmlPath);
        }
    }
}
