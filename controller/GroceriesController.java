package Controller;

import DAO.DatabaseConnection;
import DAO.PenggunaDAO;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import model.Pengguna;
import utils.SessionManager;

/**
 * FXML Controller class
 *
 * @author User
 */
public class GroceriesController implements Initializable {

    @FXML
    private Label saldoLabel; // Pastikan ini terhubung dengan fx:id di FXML
    
    private Connection connection;
    private PenggunaDAO penggunaDAO;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        connection = DatabaseConnection.getCon();
        penggunaDAO = new PenggunaDAO(connection);
        int currentUserId = SessionManager.getCurrentUserId();
        tampilanPengguna(currentUserId);
    }
    
    private void tampilanPengguna(int idPengguna) {
        try {
            Pengguna pengguna = penggunaDAO.getPenggunaById(idPengguna);
            if (pengguna != null) {
                // Format saldo dengan "Rp." dan tampilkan di saldoLabel
                saldoLabel.setText("Rp. " + pengguna.getSaldoPengguna());
            } else {
                saldoLabel.setText("Data pengguna tidak ditemukan");
            }
        } catch (SQLException ex) {
            Logger.getLogger(RentToolUserController.class.getName()).log(Level.SEVERE, null, ex);
            saldoLabel.setText("Error memuat saldo");
        }
    }
    
    @FXML
    public void handleBackButton(ActionEvent event) {
        try {
            navigateTo(event, "/View/homePageUser.fxml");
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Gagal kembali ke halaman awal: " + e.getMessage());
        }
    }

    private void navigateTo(ActionEvent event, String fxmlPath) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
