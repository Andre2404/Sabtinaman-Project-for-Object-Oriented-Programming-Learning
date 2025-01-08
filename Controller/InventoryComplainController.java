package Controller;

import DAO.*;
import java.io.ByteArrayInputStream;
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
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Base64;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.Alat;

public class InventoryComplainController {

    @FXML
    private TableView<Keluhan> tableInventory;
    @FXML
    private TableColumn<Keluhan, String> colNamaAlat;
    @FXML
    private TableColumn<Keluhan, String> colNamaPengguna;
    @FXML
    private TableColumn<Keluhan, Timestamp> colTanggal;
    @FXML
    private TableColumn<Keluhan, String> colStatus;
    
    @FXML
    private ImageView gambar;

    private Connection connection;
    private KeluhanDAO keluhanDAO;
    private PerusahaanDAO perusahaanDAO;
    private PenggunaDAO penggunaDAO;
    private AlatDAO alatDAO;
    private SaldoDAO saldoDAO;
    private TransaksiSewaDAO transaksiSewaDAO;
    private Keluhan keluhanTerpilih;
    private Keluhan selectedKeluhan; 
    private int currentUserId;
    @FXML
    private Button back;
    @FXML
    private Button btnLK;
    @FXML
    private TextArea detail;
    @FXML
    private TextArea tanggapan;
    

    public void initialize() {
        try {
        connection = DatabaseConnection.getCon();
        perusahaanDAO = new PerusahaanDAO(connection);
        penggunaDAO = new PenggunaDAO(connection);
        alatDAO = new AlatDAO(connection, perusahaanDAO);
        saldoDAO = new SaldoDAO(connection, penggunaDAO, perusahaanDAO);
        transaksiSewaDAO = new TransaksiSewaDAO(connection, saldoDAO, penggunaDAO, alatDAO, perusahaanDAO);
        keluhanDAO = new KeluhanDAO(connection, penggunaDAO, alatDAO, transaksiSewaDAO, perusahaanDAO);
        currentUserId = SessionManager.getCurrentUserId();
            loadKeluhan();
        } catch (SQLException ex) {
            Logger.getLogger(InventoryComplainController.class.getName()).log(Level.SEVERE, "Gagal inisialisasi controller", ex);
            showAlert(Alert.AlertType.ERROR, "Gagal memuat data: " + ex.getMessage());
        }
    }

    private void loadKeluhan() throws SQLException {
    // Ambil daftar keluhan dari keluhanDAO berdasarkan ID perusahaan
    List<Keluhan> keluhanList = keluhanDAO.getKeluhanByCompanyId(1); // Ganti 1 dengan ID perusahaan yang sesuai
    ObservableList<Keluhan> keluhanObservableList = FXCollections.observableArrayList(keluhanList);

    // Set kolom tabel
    colStatus.setCellValueFactory(new PropertyValueFactory<>("status")); // Mengambil status keluhan
    colNamaAlat.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAlat().getNamaAlat())); // Mengambil nama alat
    colNamaPengguna.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUser ().getNama())); // Mengambil nama pengguna
    colTanggal.setCellValueFactory(cellData -> {
    Timestamp timestamp = cellData.getValue().getTanggal(); // Mengambil Timestamp
    return new SimpleObjectProperty<>(timestamp);
});
    // Set data ke TableView
    tableInventory.setItems(keluhanObservableList);
}

    @FXML
    private void handleTableClick(MouseEvent event) throws SQLException, IOException {
    if (event.getClickCount() == 1) {
        Keluhan selectedItem = tableInventory.getSelectionModel().getSelectedItem();
        
        if (selectedItem != null) {
            keluhanTerpilih = selectedItem; // Simpan keluhan yang dipilih
            detail.setText(keluhanTerpilih.getDeskripsi());
            tanggapan.setText(keluhanTerpilih.getTanggapan());

            // Ambil ID alat dari keluhan yang dipilih
            int idAlat = keluhanTerpilih.getAlat().getIdAlat(); // Pastikan getAlat() mengembalikan objek Alat
            Alat alatDetail = alatDAO.getAlatById(idAlat);
           
                if (alatDetail != null) {
                    // Ambil imageHash dan tampilkan gambar
                    String imageHash = alatDetail.getImageHash();
                    if (imageHash != null && !imageHash.isEmpty()) {
                    Image image = decodeBase64ToImage(imageHash);    
                    
                        // Tampilkan gambar di ImageView
                        gambar.setImage(image);
                    } else {
                    // Jika alat tidak ditemukan, kosongkan gambar
                    gambar.setImage(null);
                }
                }

                // Tampilkan deskripsi masalah di TextArea
                detail.setText(keluhanTerpilih.getDeskripsi());
             // Kosongkan gambar jika terjadi kesalahan
            }
        } else {
            // Jika tidak ada keluhan yang dipilih, kosongkan semua fiel
            tanggapan.setText("");
            detail.setText("");
            gambar.setImage(null); // Kosongkan gambar
        }
    }

    private Image decodeBase64ToImage(String base64String) throws IOException {
    byte[] imageBytes = Base64.getDecoder().decode(base64String);
    InputStream inputStream = new ByteArrayInputStream(imageBytes);
    return new Image(inputStream);
}   
    
    @FXML
    public void simpanTanggapan() {
        if (keluhanTerpilih != null && !tanggapan.getText().isEmpty()) {
            boolean isUpdated = keluhanDAO.updateTanggapan(keluhanTerpilih.getIdKeluhan(), tanggapan.getText());
            if (isUpdated) {
                keluhanTerpilih.setTanggapan(tanggapan.getText());
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
