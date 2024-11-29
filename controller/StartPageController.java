package Controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;

public class StartPageController {

    @FXML
    private ChoiceBox<String> choose;

    @FXML
    private Button userButton;

    @FXML
    private Button companyButton;

    public void initialize() {
        choose.getItems().addAll("User", "Company");
        choose.getSelectionModel().selectFirst(); // Select first item by default
    }

    public void handleUserButtonClicked() {
        // Load signInUser.fxml
        loadScene("view/signInUser.fxml");
    }

    public void handleCompanyButtonClicked() {
        // Load signInCom.fxml
        loadScene("view/signInCom.fxml");
    }

    public void handleChoiceBoxChanged() {
        String selectedItem = choose.getSelectionModel().getSelectedItem();
        if ("User".equals(selectedItem)) {
            userButton.setDisable(true); // Disable user button to prevent double clicking
            companyButton.setDisable(false); // Enable company button
        } else if ("Company".equals(selectedItem)) {
            companyButton.setDisable(true); // Disable company button to prevent double clicking
            userButton.setDisable(false); // Enable user button
        }
    }

    private void loadScene(String fxmlPath) {
        Stage stage = (Stage) choose.getScene().getWindow();
        try {
            stage.setScene(new javafx.scene.Scene(FXMLLoader.load(getClass().getResource(fxmlPath))));
        } catch (Exception e) {
            e.printStackTrace();
            Platform.exit();
        }
    }
}