package Controller;

import DAO.DatabaseConnection;
import DAO.PenggunaDAO;
import DAO.PerusahaanDAO;
import DAO.SaldoDAO;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
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
    private PenggunaDAO penggunaDAO;
    private Connection connection;
    private PerusahaanDAO perusahaanDAO;
    private SaldoDAO saldoDAO;
    int currentUserId ;

    public void initialize() {
        connection = DatabaseConnection.getCon();
        penggunaDAO = new PenggunaDAO(connection);
        perusahaanDAO = new PerusahaanDAO (connection);
        saldoDAO = new SaldoDAO(connection, penggunaDAO, perusahaanDAO);
        currentUserId = SessionManager.getCurrentUserId();
        tampilanPerusahaan(currentUserId);
    }

    @FXML
    private void handleWithDraw(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Tarik Saldo");
        dialog.setHeaderText("Masukkan jumlah saldo yang ingin ditarik:");
        dialog.setContentText("Jumlah:");

        dialog.showAndWait().ifPresent(input -> {
            try {
                int amount = Integer.parseInt(input);
                int currentUserId = SessionManager.getCurrentUserId();
                saldoDAO.withDraw(currentUserId, amount);
                showAlert(Alert.AlertType.INFORMATION, "Sukses", "Saldo berhasil diambil! With draw sebesar: " + amount);
                updateTampilanSaldo(currentUserId);
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Jumlah saldo harus berupa angka.");
            }
        });
    }
    private void tampilanPerusahaan(int idPerusahaan){
       
        try {
            Perusahaan perusahaan = perusahaanDAO.getPerusahaanById(currentUserId);
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
    
    private void updateTampilanSaldo (int idPengguna){
        try {
            int saldo = perusahaanDAO.getSaldoPerusahaan(idPengguna);
            saldoLabel.setText(String.format("%s", "Rp. " + saldo));
        } catch (SQLException ex) {
            Logger.getLogger(homePageUserController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML
    private void handleTransaction(ActionEvent event) throws IOException {
        navigateTo(event, "/View/HistoryPageCompany.fxml");
    }

    @FXML
    private void handleRent(ActionEvent event) throws IOException {
        navigateTo(event, "/View/rentToolCom.fxml");
    }
    
    @FXML
    private void handleGroceries(ActionEvent event) throws IOException {
        navigateTo(event, "/View/GroceriesCom.fxml");
    }

    @FXML
    private void handleComplaint(ActionEvent event) throws IOException {
        
        navigateTo(event, "/View/inventoryComplaint.fxml");
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
