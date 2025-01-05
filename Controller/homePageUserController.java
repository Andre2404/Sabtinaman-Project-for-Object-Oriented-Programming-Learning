package Controller;

import DAO.DatabaseConnection;
import DAO.PenggunaDAO;
import DAO.PerusahaanDAO;
import DAO.SaldoDAO;
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
import model.Pengguna;
import utils.SessionManager;
/**
 * FXML Controller class
 *
 * @author Andhi
 */
public class homePageUserController {

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
    
    private PenggunaDAO penggunaDAO;
    private Connection connection;
    private PerusahaanDAO perusahaanDAO;
    private SaldoDAO saldoDAO;

    public void initialize() {
        connection = DatabaseConnection.getCon();
        penggunaDAO = new PenggunaDAO(connection);
        saldoDAO = new SaldoDAO(connection, penggunaDAO, perusahaanDAO);
        int currentUserId = SessionManager.getCurrentUserId();
        tampilanPengguna(currentUserId);
    }

    @FXML
    private void handleTopup(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Top-Up Saldo");
        dialog.setHeaderText("Masukkan jumlah saldo yang ingin di-top up:");
        dialog.setContentText("Jumlah:");

        dialog.showAndWait().ifPresent(input -> {
            try {
                int amount = Integer.parseInt(input);
                int currentUserId = SessionManager.getCurrentUserId();
                saldoDAO.topUpSaldo(currentUserId, amount);
                showAlert(Alert.AlertType.INFORMATION, "Sukses", "Top-up berhasil! Saldo bertambah sebesar: " + amount);
                updateTampilanSaldo(currentUserId);
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Jumlah saldo harus berupa angka.");
            }
        });
    }
    
    private void tampilanPengguna(int idPengguna){
       
        try {
            Pengguna pengguna = penggunaDAO.getPenggunaById(idPengguna);
            if(pengguna != null){
                addressLabel.setText(pengguna.getAlamat());
                namaLabel.setText(pengguna.getNama());
                emailLabel.setText(pengguna.getEmail());
                kontakLabel.setText(String.valueOf(pengguna.getNomorKontak()));
                saldoLabel.setText(String.valueOf("Rp. " + pengguna.getSaldoPengguna()));
            }
            else{
                addressLabel.setText("Data Not Found");
            }
        } catch (SQLException ex) {
            Logger.getLogger(homePageUserController.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    private void updateTampilanSaldo (int idPengguna){
        try {
            int saldo = penggunaDAO.getSaldoPengguna(idPengguna);
            saldoLabel.setText(String.format("%s", "Rp. " + saldo));
        } catch (SQLException ex) {
            Logger.getLogger(homePageUserController.class.getName()).log(Level.SEVERE, null, ex);
        saldoLabel.setText("Saldo tidak tersedia");
        }
    }
            
            
    @FXML
    private void handleTransaction(ActionEvent event) throws IOException {
        navigateTo(event, "/View/HistoryPageUser.fxml");
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
        navigateTo(event, "/View/inventory.fxml");
    }
    
    @FXML
    public void handleBackButton(ActionEvent event) {
        try {
            navigateTo(event, "/View/signInUser.fxml");
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
