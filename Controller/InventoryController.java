package Controller;

import DAO.TransaksiSewaDAO;
import DAO.AlatDAO;
import DAO.KeluhanDAO;
import DAO.PenggunaDAO;
import DAO.PerusahaanDAO;
import DAO.SaldoDAO;
import DAO.DatabaseConnection;
import java.awt.Insets;
import java.io.ByteArrayInputStream;
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
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import model.Keluhan;
import model.Keranjang;

public class InventoryController {
   @FXML
    private TableView<Keranjang> tableInventory;
    @FXML
    private TableColumn<Keranjang, String> colNamaAlat;
    @FXML
    private TableColumn<Keranjang, Timestamp> colTanggalSewa;
    @FXML
    private TableColumn<Keranjang, Timestamp> colTanggalKembali;
    @FXML
    private TableColumn<Keranjang, String> colPerusahaan;
    @FXML
    private TableColumn<Keranjang, Integer> colStokAlat;
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
    
    @FXML
    private Button back;
    @FXML
    private ImageView preview;
    @FXML
    private TextArea spek;

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
    
    private void loadInventory() throws SQLException {
        List<Keranjang> inventoryList = transaksiSewaDAO.getAlatDisewaByUser (currentUserId);
        ObservableList<Keranjang> transaksiObservableList = FXCollections.observableArrayList(inventoryList);

        colNamaAlat.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAlat().getNamaAlat()));
        colStokAlat.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getJumlah()).asObject());  colTanggalSewa.setCellValueFactory(new PropertyValueFactory<>("tanggalPinjam"));
        colTanggalKembali.setCellValueFactory(new PropertyValueFactory<>("tanggalKembali"));
        colPerusahaan.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAlat().getCompany().getNama()));

        tableInventory.setItems(transaksiObservableList);
    }
    
    @FXML
    public void handleBackButton(ActionEvent event) {
        try {
            navigateTo(event, "/View/homePageUser.fxml");
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Gagal kembali ke halaman awal: " + e.getMessage());
        }
    }

   @FXML
    private void handleTableClick(MouseEvent event) throws SQLException, IOException {
    if (event.getClickCount() == 1) {
        Keranjang selectedItem = tableInventory.getSelectionModel().getSelectedItem();
        
        if (selectedItem != null) {
            // Ambil ID alat dari item yang dipilih
            int alatId = selectedItem.getAlat().getIdAlat(); 
            
            // Ambil spesifikasi dari database menggunakan DAO
            Alat alatDetail = alatDAO.getAlatById(alatId); 
            
            // Tampilkan spesifikasi di UI
            if (alatDetail != null) {
                spek.setText(alatDetail.getSpesifikasi()); 
                String imageHash = alatDetail.getImageHash(); // Mengambil imageHash
                if (imageHash != null && !imageHash.isEmpty()) {
                    Image image = decodeBase64ToImage(imageHash); // Dekode dari Base64
                    preview.setImage(image); // Tampilkan gambar
                } else {
                    preview.setImage(null); // Set gambar menjadi null jika imageHash kosong
                }
            }
        }
    }
}
    
    // Metode untuk mendekode gambar dari Base64
    private Image decodeBase64ToImage(String base64String) throws IOException {
    byte[] imageBytes = Base64.getDecoder().decode(base64String);
    InputStream inputStream = new ByteArrayInputStream(imageBytes);
    return new Image(inputStream);
}

    @FXML                                                                   
private void handleLaporkanKeluhan() {
    try {
        Keranjang selectedItem = tableInventory.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            showAlert(Alert.AlertType.WARNING, "Warning", "Pilih alat yang ingin dilaporkan keluhannya.");
            return;
        }
        
        int company = selectedItem.getAlat().getCompany().getIdPerusahaan();
        Perusahaan perusahaan = perusahaanDAO.getPerusahaanById(company);
        Keranjang user = selectedItem;
        Pengguna pengguna = penggunaDAO.getPenggunaById(currentUserId);
        int idAlat = selectedItem.getAlat().getIdAlat();
        Alat alat = alatDAO.getAlatById(idAlat);
        int sewa = selectedItem.getIdTransaksi();
        TransaksiSewa transaksi = transaksiSewaDAO.getTransaksiById(sewa);
        
        // Membuat dialog kustom
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Laporkan Keluhan");
        
        // Membuat TextArea untuk input keluhan
        TextArea textArea = new TextArea();
        textArea.setPromptText("Masukkan keluhan terkait alat...");
        textArea.setWrapText(true);
        
        // Membuat tombol untuk mengirim keluhan
        Button submitButton = new Button("Kirim");
        submitButton.setOnAction(e -> {
            String laporan = textArea.getText();
            if (laporan.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Warning", "Keluhan tidak boleh kosong.");
            } else {
                keluhanDAO.laporkanKeluhan(perusahaan, pengguna, alat, transaksi, laporan);
                showAlert(Alert.AlertType.INFORMATION, "Success", "Keluhan berhasil dikirim.");
                dialogStage.close(); // Menutup dialog setelah mengirim keluhan
            }
        });
        
        // Mengatur layout dialog
        VBox dialogPane = new VBox(10);
        dialogPane.getChildren().addAll(textArea, submitButton);
        Insets dialogPadding = new Insets(10, 15, 20, 25); // top, right, bottom, left
        
        Scene dialogScene = new Scene(dialogPane, 300, 300); // Ukuran dialog
        dialogStage.setScene(dialogScene);
        dialogStage.showAndWait(); // Menampilkan dialog dan menunggu hingga ditutup
        
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
    
        @FXML
    private void handlePengembalianAlat() {
        Keranjang selectedItem = tableInventory.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            showAlert(Alert.AlertType.WARNING, "Warning", "Pilih alat yang ingin dikembalikan.");
            return;
        }

        try {
            int stokUpdate = selectedItem.getAlat().getStok()-selectedItem.getJumlah();
            transaksiSewaDAO.kembalikanAlat(selectedItem.getIdTransaksi() ,selectedItem.getAlat().getIdAlat(), stokUpdate );
            loadInventory();
            showAlert(Alert.AlertType.INFORMATION, "Success", "Alat berhasil dikembalikan.");
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Gagal mengembalikan alat: " + e.getMessage());
        }
    }
}
