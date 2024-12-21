package Controller;

import DAO.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Alat;
import model.TransaksiSewa;
import model.Pengguna;
import model.Perusahaan;
import utils.SessionManager;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import model.Keluhan;
import model.TransaksiSewa;

public class InventoryController {
   @FXML
    private TableView<TransaksiSewa> tableInventory;
    @FXML
    private TableColumn<TransaksiSewa, String> colNamaAlat;
    @FXML
    private TableColumn<TransaksiSewa, Timestamp> colTanggalSewa;
    @FXML
    private TableColumn<TransaksiSewa, Timestamp> colTanggalKembali;
    @FXML
    private TableColumn<TransaksiSewa, String> colStatus;
    @FXML
    private Button btnKA;
    @FXML
    private Button btnLK;

    private Connection connection;
    private PerusahaanDAO perusahaanDAO;
    private PenggunaDAO penggunaDAO;
    private AlatDAO alatDAO;
    private TransaksiSewaDAO transaksiSewaDAO;
    private SaldoDAO saldoDAO;
    private KeluhanDAO keluhanDAO;

    private int currentUserId;

    public void initialize() {
        connection = DatabaseConnection.getCon();
        perusahaanDAO = new PerusahaanDAO(connection);
        penggunaDAO = new PenggunaDAO(connection);
        alatDAO = new AlatDAO(connection, perusahaanDAO);
        saldoDAO = new SaldoDAO(connection);
        keluhanDAO = new KeluhanDAO(connection, penggunaDAO, alatDAO, transaksiSewaDAO, perusahaanDAO);
        transaksiSewaDAO = new TransaksiSewaDAO(connection, saldoDAO, penggunaDAO, alatDAO, perusahaanDAO);
        currentUserId = SessionManager.getCurrentUserId(); // Menyimpan ID pengguna sekali
       try {
           loadInventory();
       } catch (SQLException ex) {
           Logger.getLogger(InventoryController.class.getName()).log(Level.SEVERE, null, ex);
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

    private void loadInventory() throws SQLException {
        List<TransaksiSewa> transaksiList = transaksiSewaDAO.getAlatDisewaByUser(currentUserId);
        ObservableList<TransaksiSewa> transaksiObservableList = FXCollections.observableArrayList(transaksiList);

        colNamaAlat.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAlat().getNamaAlat()));
        colTanggalSewa.setCellValueFactory(new PropertyValueFactory<>("tanggalSewa"));
        colTanggalKembali.setCellValueFactory(new PropertyValueFactory<>("tanggalKembali"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));


        tableInventory.setItems(transaksiObservableList);
    }

    @FXML
    private void handleTableClick(MouseEvent event) {
        if (event.getClickCount() == 1) {
            TransaksiSewa selectedItem = tableInventory.getSelectionModel().getSelectedItem();
        }
    }
    
    @FXML
    private void handlePengembalianAlat() {
        TransaksiSewa selectedItem = tableInventory.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            showAlert(Alert.AlertType.WARNING, "Warning", "Pilih alat yang ingin dikembalikan.");
            return;
        }

        try {
            transaksiSewaDAO.kembalikanAlat(selectedItem.getIdTransaksi(), selectedItem.getAlat().getIdAlat() );
            loadInventory();
            showAlert(Alert.AlertType.INFORMATION, "Success", "Alat berhasil dikembalikan.");
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Gagal mengembalikan alat: " + e.getMessage());
        }
    }

    @FXML
    private void handleLaporkanKeluhan() {
       try {
           TransaksiSewa selectedItem = tableInventory.getSelectionModel().getSelectedItem();
           if (selectedItem == null) {
               showAlert(Alert.AlertType.WARNING, "Warning", "Pilih alat yang ingin dilaporkan keluhannya.");
               return;
           }
           
           int company = selectedItem.getAlat().getCompany().getIdPerusahaan();
           Perusahaan perusahaan = perusahaanDAO.getPerusahaanById(company);
           int user = selectedItem.getUser().getIdPengguna();
           Pengguna pengguna = penggunaDAO.getPenggunaById(user);
           int idAlat = selectedItem.getAlat().getIdAlat();
           Alat alat = alatDAO.getAlatById(idAlat);
           int sewa = selectedItem.getIdTransaksi();
           TransaksiSewa transaksi = transaksiSewaDAO.getTransaksiById(sewa);
           
           
           TextInputDialog dialog = new TextInputDialog();
           dialog.setTitle("Laporkan Keluhan");
           dialog.setHeaderText("Masukkan keluhan terkait alat:");
           dialog.setContentText("Keluhan:");
           Optional<String> result = dialog.showAndWait();
           result.ifPresent(laporan -> {
               keluhanDAO.laporkanKeluhan(perusahaan, pengguna, alat, transaksi, laporan);
               showAlert(Alert.AlertType.INFORMATION, "Success", "Keluhan berhasil dikirim.");
           });
       } catch (SQLException ex) {
           Logger.getLogger(InventoryController.class.getName()).log(Level.SEVERE, null, ex);
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
