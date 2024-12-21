package Controller;

import DAO.*;
import model.Keluhan;
import utils.SessionManager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class InventoryComplainController {

    @FXML
    private TableView<Keluhan> tableInventory;

    @FXML
    private TableColumn<Keluhan, Integer> colIDKeluhan;

    @FXML
    private TableColumn<Keluhan, String> colNamaAlat;

    @FXML
    private TableColumn<Keluhan, Integer> colIdPengguna;

    @FXML
    private TextArea deskripsiField;

    @FXML
    private TextField tanggapanField;

    private Connection connection;
    private KeluhanDAO keluhanDAO;
    private PerusahaanDAO perusahaanDAO;
    private PenggunaDAO penggunaDAO;
    private AlatDAO alatDAO;
    private SaldoDAO saldoDAO;
    private TransaksiSewaDAO transaksiSewaDAO;
    private Keluhan keluhanTerpilih;
    private int currentUserId;

    public void initialize() {
        try {
        connection = DatabaseConnection.getCon();
        perusahaanDAO = new PerusahaanDAO(connection);
        penggunaDAO = new PenggunaDAO(connection);
        alatDAO = new AlatDAO(connection, perusahaanDAO);
        saldoDAO = new SaldoDAO(connection, penggunaDAO, perusahaanDAO);
        keluhanDAO = new KeluhanDAO(connection, penggunaDAO, alatDAO, transaksiSewaDAO, perusahaanDAO);
        transaksiSewaDAO = new TransaksiSewaDAO(connection, saldoDAO, penggunaDAO, alatDAO, perusahaanDAO);
        currentUserId = SessionManager.getCurrentUserId();
            loadKeluhan();
        } catch (SQLException ex) {
            Logger.getLogger(InventoryComplainController.class.getName()).log(Level.SEVERE, "Gagal inisialisasi controller", ex);
            showAlert(Alert.AlertType.ERROR, "Gagal memuat data: " + ex.getMessage());
        }
    }

    private void loadKeluhan() throws SQLException {
        List<Keluhan> keluhanList = keluhanDAO.getKeluhanByCompanyId(currentUserId);
        ObservableList<Keluhan> keluhanObservableList = FXCollections.observableArrayList(keluhanList);

        // Set kolom tabel
        colIDKeluhan.setCellValueFactory(new PropertyValueFactory<>("idKeluhan"));
        colNamaAlat.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAlat().getNamaAlat()));
        colIdPengguna.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getUser().getIdPengguna()).asObject());

        tableInventory.setItems(keluhanObservableList);
    }

    @FXML
    private void handleTableClick(MouseEvent event) {
        if (event.getClickCount() == 1) {
            Keluhan selectedItem = tableInventory.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                keluhanTerpilih = selectedItem;
                deskripsiField.setText(keluhanTerpilih.getDeskripsi());
                tanggapanField.setText(keluhanTerpilih.getTanggapan());
            }
        }
    }

    @FXML
    public void simpanTanggapan() {
        if (keluhanTerpilih != null && !tanggapanField.getText().isEmpty()) {
            boolean isUpdated = keluhanDAO.updateTanggapan(keluhanTerpilih.getIdKeluhan(), tanggapanField.getText());
            if (isUpdated) {
                keluhanTerpilih.setTanggapan(tanggapanField.getText());
                tableInventory.refresh();
                showAlert(Alert.AlertType.INFORMATION, "Tanggapan berhasil disimpan!");
            } else {
                showAlert(Alert.AlertType.ERROR, "Gagal menyimpan tanggapan!");
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Pilih keluhan dari tabel dan masukkan tanggapan terlebih dahulu!");
        }
    }

    @FXML
    public void handleBackButton(ActionEvent event) {
        try {
            navigateTo(event, "/View/homePageCom.fxml");
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Gagal kembali ke halaman awal: " + e.getMessage());
        }
    }

    private void navigateTo(ActionEvent event, String fxmlPath) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    private void showAlert(Alert.AlertType alertType, String message) {
        Alert alert = new Alert(alertType);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
