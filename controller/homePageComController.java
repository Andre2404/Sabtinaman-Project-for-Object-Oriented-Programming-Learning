package Controller;

import DAO.DatabaseConnection;
import DAO.PerusahaanDAO;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;
import model.Perusahaan;
import utils.SessionManager;
/**
 * FXML Controller class
 *
 * @author Andhi
 */
public class homePageComController {

    @FXML
    private Button logout;
    @FXML
    private Button transaction;
    @FXML
    private Label addressLabel;
    @FXML
    private Label kontakLabel;
    @FXML
    private Label emailLabel;
    @FXML
    private Label saldoLabel;
    @FXML
    private Label namaLabel;
    @FXML
    private Button topup;
    @FXML
    private Button inventory;
    @FXML
    private Button rent;
    @FXML
    private Button groceries;
    
    private PerusahaanDAO perusahaanDAO;
    private Connection connection;

    public void initialize() {
        connection = DatabaseConnection.getCon();
        perusahaanDAO = new PerusahaanDAO(connection);
        int currentUserId = SessionManager.getCurrentUserId();
        tampilanPerusahaan(currentUserId);
    }

    @FXML
    private void handleWithdraw(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Top-Up Saldo");
        dialog.setHeaderText("Masukkan jumlah saldo yang ingin di-top up:");
        dialog.setContentText("Jumlah:");

        dialog.showAndWait().ifPresent(input -> {
            
        });
    }

    private double getSaldoPengguna(int userId) throws SQLException {
        String query = "SELECT saldo FROM perusahaan WHERE id_pengguna = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("saldo");
                } else {
                    throw new SQLException("Pengguna dengan ID " + userId + " tidak ditemukan.");
                }
            }
        }
    }

    private void tampilanPerusahaan(int idPerusahaan){
       
        try {
            Perusahaan perusahaan = perusahaanDAO.getPerusahaanById(idPerusahaan);
            if(perusahaan != null){
                addressLabel.setText(perusahaan.getAlamat());
                namaLabel.setText(perusahaan.getNama());
                emailLabel.setText(perusahaan.getEmail());
                kontakLabel.setText(String.valueOf(perusahaan.getNomorKontak()));
                saldoLabel.setText(String.valueOf("Rp. " + perusahaan.getSaldoPerusahaan()));
            }
            else{
                addressLabel.setText("Data not found");
            }
        } catch (SQLException ex) {
            Logger.getLogger(homePageUserController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    
    @FXML
    private void handleTransaction(ActionEvent event) throws IOException {
        navigateTo(event, "/View/historyPageUser.fxml");
    }

    @FXML
    private void handleRent(ActionEvent event) throws IOException {
        navigateTo(event, "/View/rentToolUser.fxml");
    }
    
    @FXML
    private void handleGroceries(ActionEvent event) throws IOException {
        navigateTo(event, "/View/Groceries.fxml");
    }

    @FXML
    private void handleInventory(ActionEvent event) throws IOException {
        navigateTo(event, "/View/Inventory.fxml");
    }
    
    @FXML
    public void handleBackButton(ActionEvent event) {
        try {
            navigateTo(event, "/View/signInCom.fxml");
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
