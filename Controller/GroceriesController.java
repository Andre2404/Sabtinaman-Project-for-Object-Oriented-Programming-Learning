package Controller;

import DAO.AlatDAO;
import DAO.DatabaseConnection;
import DAO.PenggunaDAO;
import DAO.PerusahaanDAO;
import DAO.PupukDAO;
import DAO.SaldoDAO;
import DAO.TransaksiPupukDAO;
import DAO.TransaksiSewaDAO;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.ModuleLayer.Controller;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Alat;
import model.Keranjang;
import model.Pengguna;
import model.Perusahaan;
import model.Pupuk;
import utils.SessionManager;

public class GroceriesController{

    @FXML
    private Label saldoLabel; // Pastikan ini terhubung dengan fx:id di FXML
    @FXML
    private Button back;
    @FXML
    private Button beli;
    @FXML
    private Button btnKeranjang;
    
    @FXML
    private TextField Qty;
    
    @FXML
    private TableView<Pupuk> tablePupuk;
    @FXML
    private TableColumn<Pupuk, String> colNamaPupuk;
    @FXML
    private TableColumn<Pupuk, Integer> colHargaperKg;
    @FXML
    private TableColumn<Pupuk, Integer> colStok;
    @FXML
    private TableColumn<Pupuk, String> colJenisPupuk;
    @FXML
    private TableColumn<Pupuk, Integer> colId;
    @FXML
    private TableView<Keranjang> tableKeranjang;
    @FXML
    private TableColumn<Keranjang, Integer> colKuantitas;
    @FXML
    private TableColumn<Keranjang, Integer> colTotalHarga;
    @FXML
    private TableColumn<Keranjang, Integer> colId2;
    @FXML
    private TableColumn<Keranjang, String> colNamaPupuk2;
    @FXML
    private TableColumn<Keranjang, String> colJenispupuk2;
    @FXML
    private ImageView pict;
    @FXML
    private TextField nama;
    @FXML
    private Label harga;
    @FXML
    private TextField total;
    
    private Connection connection;
    private PenggunaDAO penggunaDAO;
    private PerusahaanDAO perusahaanDAO;
    private PupukDAO pupukDAO;
    private TransaksiPupukDAO transaksiPupukDAO;
    private SaldoDAO saldoDAO;
    private int currentUserId;
    private ObservableList<Keranjang> keranjangObservableList = FXCollections.observableArrayList();

    public void initialize() throws SQLException {
    try {
        connection = DatabaseConnection.getCon();
        penggunaDAO = new PenggunaDAO(connection);
        perusahaanDAO = new PerusahaanDAO(connection);
        pupukDAO = new PupukDAO(connection, perusahaanDAO);
        saldoDAO = new SaldoDAO(connection, penggunaDAO, perusahaanDAO);
        transaksiPupukDAO = new TransaksiPupukDAO(connection, saldoDAO, penggunaDAO, pupukDAO, perusahaanDAO);
        currentUserId = SessionManager.getCurrentUserId(); // Menyimpan ID pengguna sekali
        keranjangObservableList.addListener((ListChangeListener.Change<? extends Keranjang> change) -> {
            updateTotalHarga();
        });
        
        // Inisialisasi total harga saat awal
        updateTotalHarga();
        tampilanPengguna(currentUserId);
        loadPupukTersedia();
        setupKeranjangTable();
    } catch (SQLException ex) {
        Logger.getLogger(GroceriesController.class.getName()).log(Level.SEVERE, null, ex);
        showAlert(Alert.AlertType.ERROR, "Error", "Gagal memuat data: " + ex.getMessage());
    }
}
    
    private void tampilanPengguna(int idPengguna) {
        try {
            Pengguna pengguna = penggunaDAO.getPenggunaById(idPengguna);
            if (pengguna != null) {
                saldoLabel.setText("Rp. " + pengguna.getSaldoPengguna());
            } else {
                saldoLabel.setText("Data pengguna tidak ditemukan");
            }
        } catch (SQLException ex) {
            Logger.getLogger(RentToolUserController.class.getName()).log(Level.SEVERE, null, ex);
            saldoLabel.setText("Error memuat saldo");
        }
    }
    private void loadPupukTersedia() throws SQLException {
        List<Pupuk> pupukList = pupukDAO.getAllPupuk();
        ObservableList<Pupuk> pupukObservableList = FXCollections.observableArrayList(pupukList);

        colId.setCellValueFactory(new PropertyValueFactory<>("idPupuk"));
        colNamaPupuk.setCellValueFactory(new PropertyValueFactory<>("namaPupuk"));
        colHargaperKg.setCellValueFactory(new PropertyValueFactory<>("hargaPerKg"));
        colStok.setCellValueFactory(new PropertyValueFactory<>("stok"));
        colJenisPupuk.setCellValueFactory(new PropertyValueFactory<>("jenisPupuk"));
        tablePupuk.setItems(pupukObservableList);
    }
    
    private void setupKeranjangTable() {
    colId2.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getPupuk().getIdPupuk()).asObject());
    colNamaPupuk2.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPupuk().getNamaPupuk()));
    colJenispupuk2.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPupuk().getJenisPupuk()));
    colKuantitas.setCellValueFactory(new PropertyValueFactory<>("jumlah"));
    colTotalHarga.setCellValueFactory(new PropertyValueFactory<>("totalHarga"));
    tableKeranjang.setItems(keranjangObservableList);
}
    
    private void updateTampilanSaldo (int idPengguna){
        try {
            int saldo = penggunaDAO.getSaldoPengguna(idPengguna);
            saldoLabel.setText(String.format("%s", saldo));
        } catch (SQLException ex) {
            Logger.getLogger(homePageUserController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML
    private void handleTableClick(MouseEvent event) {
    if (event.getClickCount() == 1) {
        Pupuk selectedPupuk = tablePupuk.getSelectionModel().getSelectedItem();
        if (selectedPupuk != null) {
            // Tampilkan informasi pupuk yang dipilih
            nama.setText(selectedPupuk.getNamaPupuk());
            harga.setText(String.valueOf(selectedPupuk.getHargaPerKg())); 
            
            // Tampilkan gambar pupuk
            String imageHash = selectedPupuk.getImageHash(); // Ambil imageHash
            if (imageHash != null && !imageHash.isEmpty()) {
                try {
                    Image image = decodeBase64ToImage(imageHash); // Dekode gambar dari Base64
                    pict.setImage(image); // Tampilkan gambar
                } catch (IOException e) {
                    e.printStackTrace(); // Tangani kesalahan saat mendekode gambar
                    pict.setImage(null); 
                }
            } else {
                pict.setImage(null); // Kosongkan gambar jika tidak ada
            }
        }
    }
} 
            
//    private void updateDetailView(Pupuk pupuk) {
//    nama.setText(pupuk.getNamaPupuk());
//    harga.setText("Rp. " + pupuk.getHargaPerKg());
//
//    if (pupuk.getImageHash() != null && !pupuk.getImageHash().isEmpty()) {
//        try {
//            Image image = decodeBase64ToImage(pupuk.getImageHash());
//            pict.setImage(image);
//        } catch (IOException e) {
//            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load the image.");
//            pict.setImage(null);
//        }
//    } else {
//        pict.setImage(null);
//    }
//}
     private Image decodeBase64ToImage(String base64String) throws IOException {
    byte[] imageBytes = Base64.getDecoder().decode(base64String);
    return new Image(new ByteArrayInputStream(imageBytes));
}
    @FXML
    private void handleTambahKeranjang() throws SQLException {
    Pupuk selectedPupuk = tablePupuk.getSelectionModel().getSelectedItem();
    if (selectedPupuk == null) {
        showAlert(Alert.AlertType.WARNING, "Warning", "Pilih pupuk terlebih dahulu.");
        return;
    }

    String kuantitasInput = Qty.getText();
    if (kuantitasInput.isEmpty() || !kuantitasInput.matches("\\d+")) {
        showAlert(Alert.AlertType.WARNING, "Warning", "Masukkan kuantitas yang valid.");
        return;
    }

    int kuantitas = Integer.parseInt(kuantitasInput);
    if (kuantitas > selectedPupuk.getStok()) {
        showAlert(Alert.AlertType.WARNING, "Warning", "Kuantitas melebihi stok tersedia.");
        return;
    }

    int totalHarga = (int) (selectedPupuk.getHargaPerKg() * kuantitas);
    Pengguna pengguna = penggunaDAO.getPenggunaById(currentUserId);
    Perusahaan perusahaan = perusahaanDAO.getPerusahaanById(selectedPupuk.getCompany().getIdPerusahaan());
    Pupuk pupuk = pupukDAO.getPupukById(selectedPupuk.getIdPupuk());
    String jenisPupuk = selectedPupuk.getJenisPupuk(); // Pastikan metode ini ada di kelas Pupuk
    Keranjang pupukKeranjang = new Keranjang(pengguna, perusahaan, pupuk, kuantitas, totalHarga);
    keranjangObservableList.add(pupukKeranjang);
    Qty.clear();
    loadPupukTersedia();
   }
    @FXML
    public void handleDelete() {
    Keranjang selectedKeranjang = tableKeranjang.getSelectionModel().getSelectedItem();
    if (selectedKeranjang == null) {
        showAlert(Alert.AlertType.WARNING, "Warning", "Pilih alat terlebih dahulu.");
        return;
    } 
   keranjangObservableList.remove(selectedKeranjang);
}
@FXML
private void handleCheckout() {
    if (keranjangObservableList.isEmpty()) {
        showAlert(Alert.AlertType.WARNING, "Warning", "Keranjang kosong, tambahkan alat terlebih dahulu.");
        return;
    }

    try {
        // Panggil DAO untuk proses checkout
        transaksiPupukDAO.checkout(currentUserId, keranjangObservableList);

        // Bersihkan keranjang dan perbarui tampilan
        for (Keranjang item : keranjangObservableList) {
            int updateStok = item.getPupuk().getStok()-item.getJumlah();
            pupukDAO.updateStokPupuk(item.getPupuk().getIdPupuk(),updateStok);
        }
        for (Keranjang item : keranjangObservableList) {
            int saldoUpdate = perusahaanDAO.getSaldoPerusahaan(item.getPupuk().getCompany().getIdPerusahaan()) + item.getTotalHarga();
            perusahaanDAO.updateSaldoPerusahaan(item.getPupuk().getCompany().getIdPerusahaan(), saldoUpdate);
        }
        keranjangObservableList.clear();
        loadPupukTersedia();
        updateTampilanSaldo(currentUserId);

        showAlert(Alert.AlertType.INFORMATION, "Success", "Checkout berhasil! Semua alat dalam keranjang berhasil disewa.");
    } catch (SQLException e) {
        Logger.getLogger(GroceriesController.class.getName()).log(Level.SEVERE, null, e);
        showAlert(Alert.AlertType.ERROR, "Error", "Terjadi kesalahan dalam proses checkout: " + e.getMessage());
    }
}

private void updateTotalHarga() {
    // Hitung total harga dari semua item di keranjang
    int totalHarga = keranjangObservableList.stream().mapToInt(Keranjang::getTotalHarga).sum();
    
    // Update TextField dengan total harga
    total.setText(String.valueOf(totalHarga));
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


//    private void handleBeliPupuk() {
//        try {
//            // Ambil alat yang dipilih dari TableView
//            Pupuk selectedPupuk = tablePupuk.getSelectionModel().getSelectedItem();
//            if (selectedPupuk == null) {
//                showAlert(Alert.AlertType.WARNING, "Warning", "Pilih alat terlebih dahulu.");
//                return;
//            }
//
//            // Dialog input jumlah hari
//            TextInputDialog dialog = new TextInputDialog();
//            dialog.setTitle("Input Berat Pupuk");
//            dialog.setHeaderText("Masukkan jumlah berat (satuan KG) untuk membeli pupuk:");
//            dialog.setContentText("Jumlah berat:");
//
//            dialog.showAndWait();
//
//            String input = dialog.getResult();
//            if (input == null || input.isEmpty()) {
//                showAlert(Alert.AlertType.WARNING, "Warning", "Jumlah berat tidak boleh kosong.");
//                return;
//            }
//
//            int jumlahKg = Integer.parseInt(input);
//            if (jumlahKg <= 0) {
//                showAlert(Alert.AlertType.WARNING, "Warning", "Jumlah berat harus lebih dari 0.");
//                return;
//            }
//
//            // Hitung total harga sewa
//            int totalHarga = selectedPupuk.getHargaPerKg() * jumlahKg;
//
//            // Ambil saldo pengguna
//            int saldoPengguna = penggunaDAO.getSaldoPengguna(currentUserId);
//            if (saldoPengguna < totalHarga) {
//                showAlert(Alert.AlertType.WARNING, "Warning", "Saldo Anda tidak mencukupi.");
//                return;
//            }
//
//            // Proses penyewaan alat
//            int idPerusahaan = selectedPupuk.getCompany().getIdPerusahaan();
//            Perusahaan perusahaan = perusahaanDAO.getPerusahaanById(idPerusahaan);
//            transaksiPupukDAO.buyPupuk(new Pengguna(currentUserId), selectedPupuk, jumlahKg, totalHarga, perusahaan);
//
//            // Perbarui tampilan alat yang tersedia
//            loadPupukTersedia();
//            updateTampilanSaldo(currentUserId);
//            // Tampilkan pesan sukses
//            showAlert(Alert.AlertType.INFORMATION, "Success", "Beli pupuk berhasil!");
//
//        } catch (NumberFormatException e) {
//            showAlert(Alert.AlertType.WARNING, "Warning", "Jumlah Berat harus berupa angka.");
//        } catch (SQLException e) {
//            Logger.getLogger(RentToolUserController.class.getName()).log(Level.SEVERE, null, e);
//            showAlert(Alert.AlertType.ERROR, "Error", "Terjadi kesalahan dalam proses sewa alat: " + e.getMessage());
//        }
//    }