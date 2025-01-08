package Controller;

import DAO.*;
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
import model.Pengguna;
import model.Perusahaan;
import utils.SessionManager;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ListChangeListener;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.Keranjang;

public class RentToolUserController {
    @FXML
    private TableView<Alat> tableAlatTersedia;
    @FXML
    private Label saldoLabel;
    @FXML
    private TableColumn<Alat, Integer> colIdAlat;
    @FXML
    private TableColumn<Alat, String> colNamaAlat;
    @FXML
    private TableColumn<Alat, String> colSpekAlat;
    @FXML
    private TableColumn<Alat, Integer> colHargaSewa;
    @FXML
    private TableColumn<Alat, Integer> colStok;
    @FXML
    private Button back;
    @FXML
    private TextField qty;
    @FXML
    private TextField total;
    @FXML
    private Button btnKeranjang;
    @FXML
    private Button btnSewa;
    @FXML
    private ImageView pict;
    @FXML
    private TextField nama;
    @FXML
    private Label harga;
    @FXML
    private TextArea spesifikasi;
    @FXML
    private TableView<Keranjang> tableKeranjang;
    @FXML
    private TableColumn<Keranjang, Integer> colIdAlatSewa;
    @FXML
    private TableColumn<Keranjang, String> colKeranjangNamaAlat;
    @FXML
    private TableColumn<Keranjang, Integer> colKeranjangKuantitas;
    @FXML
    private TableColumn<Keranjang, Integer> colKeranjangDurasi;
    @FXML
    private TableColumn<Keranjang, Integer> colKeranjangTotalHarga;
    @FXML
    private DatePicker tanggalPinjam, tanggalKembali;
    private int currentUserId;
    
    private Connection connection;
    private PerusahaanDAO perusahaanDAO;
    private PenggunaDAO penggunaDAO;
    private AlatDAO alatDAO;
    private TransaksiSewaDAO transaksiSewaDAO;
    private SaldoDAO saldoDAO;
    private ObservableList<Keranjang> keranjangObservableList = FXCollections.observableArrayList();
    
    public void initialize() {
        try {
            connection = DatabaseConnection.getCon();
            perusahaanDAO = new PerusahaanDAO(connection);
            penggunaDAO = new PenggunaDAO(connection);
            alatDAO = new AlatDAO(connection, perusahaanDAO);
            saldoDAO = new SaldoDAO(connection);
            transaksiSewaDAO = new TransaksiSewaDAO(connection, saldoDAO, penggunaDAO, alatDAO, perusahaanDAO);
            currentUserId = SessionManager.getCurrentUserId(); // Menyimpan ID pengguna sekali
            keranjangObservableList.addListener((ListChangeListener.Change<? extends Keranjang> change) -> {
        updateTotalHarga();
    });
    
    // Inisialisasi total harga saat awal
    updateTotalHarga();
            tampilanPengguna(currentUserId);
            loadAlatTersedia();
            setupKeranjangTable();

        } catch (SQLException ex) {
            Logger.getLogger(RentToolUserController.class.getName()).log(Level.SEVERE, null, ex);
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

    @FXML  
    public void handleBackButton(ActionEvent event) {
        try {
            navigateTo(event, "/View/homePageUser.fxml");
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Gagal kembali ke halaman awal: " + e.getMessage());
        }
    }

    private void loadAlatTersedia() throws SQLException {
        List<Alat> alatList = alatDAO.getAvailableAlat();
        ObservableList<Alat> alatObservableList = FXCollections.observableArrayList(alatList);

        colIdAlat.setCellValueFactory(new PropertyValueFactory<>("idAlat"));
        colNamaAlat.setCellValueFactory(new PropertyValueFactory<>("namaAlat"));
        colSpekAlat.setCellValueFactory(new PropertyValueFactory<>("spesifikasi"));
        colHargaSewa.setCellValueFactory(new PropertyValueFactory<>("hargaSewa"));
        colStok.setCellValueFactory(new PropertyValueFactory<>("stok"));

        tableAlatTersedia.setItems(alatObservableList);
    }

    
    private void setupKeranjangTable() {
    colIdAlatSewa.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getAlat().getIdAlat()).asObject());
    colKeranjangNamaAlat.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAlat().getNamaAlat()));
    colKeranjangKuantitas.setCellValueFactory(new PropertyValueFactory<>("jumlah"));
    colKeranjangDurasi.setCellValueFactory(new PropertyValueFactory<>("durasi"));
    colKeranjangTotalHarga.setCellValueFactory(new PropertyValueFactory<>("totalHarga"));
    tableKeranjang.setItems(keranjangObservableList);
}

    @FXML
private void handleTableClick(MouseEvent event) throws IOException {
    if (event.getClickCount() == 1) {
        Alat selectedAlat = tableAlatTersedia.getSelectionModel().getSelectedItem();
        Keranjang selectedKeranjang = tableKeranjang.getSelectionModel().getSelectedItem();

        if (selectedAlat == null && selectedKeranjang == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select an item in one of the tables.");
            return;
        }

        if (selectedAlat != null) {
            updateDetailView(selectedAlat);
        } else if (selectedKeranjang != null && selectedKeranjang.getAlat() != null) {
            updateDetailView(selectedKeranjang.getAlat());
        }
    }
}

private void updateDetailView(Alat alat) {
    nama.setText(alat.getNamaAlat());
    harga.setText("Rp. " + alat.getHargaSewa());
    spesifikasi.setText(alat.getSpesifikasi());

    if (alat.getImageHash() != null && !alat.getImageHash().isEmpty()) {
        try {
            Image image = decodeBase64ToImage(alat.getImageHash());
            pict.setImage(image);
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load the image.");
            pict.setImage(null);
        }
    } else {
        pict.setImage(null);
    }
}
     private Image decodeBase64ToImage(String base64String) throws IOException {
    byte[] imageBytes = Base64.getDecoder().decode(base64String);
    return new Image(new ByteArrayInputStream(imageBytes));
}
    
    @FXML
    private void handleTambahKeranjang() throws SQLException {
    LocalDate tanggalMulai = tanggalPinjam.getValue();
    LocalDate tanggalSelesai = tanggalKembali.getValue();
    Alat selectedAlat = tableAlatTersedia.getSelectionModel().getSelectedItem();
    if (selectedAlat == null) {
        showAlert(Alert.AlertType.WARNING, "Warning", "Pilih alat terlebih dahulu.");
        return;
    }

    String kuantitasInput = qty.getText();
    if (kuantitasInput.isEmpty() || !kuantitasInput.matches("\\d+")) {
        showAlert(Alert.AlertType.WARNING, "Warning", "Masukkan kuantitas yang valid.");
        return;
    }

    int kuantitas = Integer.parseInt(kuantitasInput);
    if (kuantitas > selectedAlat.getStok()) {
        showAlert(Alert.AlertType.WARNING, "Warning", "Kuantitas melebihi stok tersedia.");
        return;
    }

    long durasi = ChronoUnit.DAYS.between(tanggalMulai, tanggalSelesai);
    if (durasi <= 0) {
        System.out.println("Tanggal selesai harus setelah tanggal mulai.");
        return;
    }

    int totalHarga = (int) (selectedAlat.getHargaSewa() * kuantitas * durasi);
    Pengguna pengguna = penggunaDAO.getPenggunaById(currentUserId);
    Perusahaan perusahaan = perusahaanDAO.getPerusahaanById(selectedAlat.getCompany().getIdPerusahaan());
    Alat alat = alatDAO.getAlatById(selectedAlat.getIdAlat());
    Keranjang alatKeranjang = new Keranjang(pengguna, perusahaan, alat, kuantitas, durasi, totalHarga, tanggalMulai, tanggalSelesai);
    keranjangObservableList.add(alatKeranjang);
    qty.clear();
    tanggalPinjam.setValue(null);
    tanggalKembali.setValue(null);
    loadAlatTersedia();
}
    
    public void handleHapus() {
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
        // Ambil saldo pengguna dari DAO
        double saldoPengguna = penggunaDAO.getSaldoPengguna(currentUserId); // Pastikan ada metode ini di penggunaDAO

        // Hitung total biaya dari semua item dalam keranjang
        double totalBiaya = 0;
        for (Keranjang item : keranjangObservableList) {
            totalBiaya += item.getAlat().getHargaSewa() * item.getJumlah(); // Asumsikan ada metode getHargaSewa()
        }

        // Periksa apakah saldo mencukupi
        if (saldoPengguna < totalBiaya) {
            showAlert(Alert.AlertType.WARNING, "Warning", "Saldo pengguna tidak mencukupi untuk melakukan pembelian.");
            return;
        }

        // Panggil DAO untuk proses checkout
        transaksiSewaDAO.checkout(currentUserId, "debit", keranjangObservableList);

        // Bersihkan keranjang dan perbarui tampilan
        for (Keranjang item : keranjangObservableList) {
            int updateStok = item.getAlat().getStok() - item.getJumlah();
            alatDAO.updateStokAlat(item.getAlat().getIdAlat(), updateStok);
        }
        for (Keranjang item : keranjangObservableList) {
            int saldoUpdate = perusahaanDAO.getSaldoPerusahaan(item.getAlat().getCompany().getIdPerusahaan()) + item.getTotalHarga();
            perusahaanDAO.updateSaldoPerusahaan(item.getAlat().getCompany().getIdPerusahaan(), saldoUpdate);
        }
        keranjangObservableList.clear();
        loadAlatTersedia();
        updateTampilanSaldo(currentUserId);

        showAlert(Alert.AlertType.INFORMATION, "Success", "Checkout berhasil! Semua alat dalam keranjang berhasil disewa.");
    } catch (SQLException e) {
        Logger.getLogger(RentToolUserController.class.getName()).log(Level.SEVERE, null, e);
        showAlert(Alert.AlertType.ERROR, "Error", "Terjadi kesalahan dalam proses checkout: " + e.getMessage());
    }
}

private void updateTotalHarga() {
    // Hitung total harga dari semua item di keranjang
    int totalHarga = keranjangObservableList.stream().mapToInt(Keranjang::getTotalHarga).sum();
    
    // Update TextField dengan total harga
    total.setText(String.valueOf(totalHarga));
}
    
    private void updateTampilanSaldo (int idPengguna){
        try {
            int saldo = penggunaDAO.getSaldoPengguna(idPengguna);
            saldoLabel.setText(String.format("%s", saldo));
        } catch (SQLException ex) {
            Logger.getLogger(homePageUserController.class.getName()).log(Level.SEVERE, null, ex);
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

//   @FXML
//private void handleCheckout() {
//    if (keranjangObservableList.isEmpty()) {
//        showAlert(Alert.AlertType.WARNING, "Warning", "Keranjang kosong, tambahkan alat terlebih dahulu.");
//        return;
//    }
//
//    LocalDate tanggalMulai = tanggalPinjam.getValue();
//    LocalDate tanggalSelesai = tanggalKembali.getValue();
//    if (tanggalPinjam == null || tanggalKembali == null) {
//        showAlert(Alert.AlertType.WARNING, "Warning", "Pilih tanggal pinjam dan tanggal kembali.");
//        return;
//    }
//
//    long durasi = ChronoUnit.DAYS.between(tanggalMulai, tanggalSelesai);
//    if (durasi <= 0) {
//        showAlert(Alert.AlertType.WARNING, "Warning", "Tanggal kembali harus lebih besar dari tanggal pinjam.");
//        return;
//    }
//
//    try {
//        int totalHarga = keranjangObservableList.stream().mapToInt(Keranjang::getTotalHarga).sum();
//        int saldoPengguna = penggunaDAO.getSaldoPengguna(currentUserId);
//        if (saldoPengguna < totalHarga) {
//            showAlert(Alert.AlertType.WARNING, "Warning", "Saldo Anda tidak mencukupi.");
//            return;
//        }
//
//        // Simpan transaksi utama
//        int idTransaksi = transaksiSewaDAO.addTransaksi(currentUserId, tanggalMulai, tanggalSelesai, totalHarga);
//
//        // Simpan detail transaksi
//        for (Keranjang item : keranjangObservableList) {
//            transaksiDAO.simpanDetailTransaksi(idTransaksi, item.getIdAlat(), item.getJumlah(), item.getTotalHarga());
//        }
//
//        // Bersihkan keranjang dan perbarui tampilan
//        keranjangObservableList.clear();
//        loadAlatTersedia();
//        updateTampilanSaldo(currentUserId);
//
//        showAlert(Alert.AlertType.INFORMATION, "Success", "Checkout berhasil! Semua alat dalam keranjang berhasil disewa.");
//    } catch (SQLException e) {
//        Logger.getLogger(RentToolUserController.class.getName()).log(Level.SEVERE, null, e);
//        showAlert(Alert.AlertType.ERROR, "Error", "Terjadi kesalahan dalam proses checkout: " + e.getMessage());
//    }
//}

//    @FXML
//    private void handleSewaAlat() {
//        try {
//            // Ambil alat yang dipilih dari TableView
//            Alat selectedAlat = tableAlatTersedia.getSelectionModel().getSelectedItem();
//            if (selectedAlat == null) {
//                showAlert(Alert.AlertType.WARNING, "Warning", "Pilih alat terlebih dahulu.");
//                return;
//            }
//
//            // Dialog input jumlah hari
//            TextInputDialog dialog = new TextInputDialog();
//            dialog.setTitle("Input Jumlah Hari");
//            dialog.setHeaderText("Masukkan jumlah hari untuk menyewa alat:");
//            dialog.setContentText("Jumlah hari:");
//
//            dialog.showAndWait();
//
//            String input = dialog.getResult();
//            if (input == null || input.isEmpty()) {
//                showAlert(Alert.AlertType.WARNING, "Warning", "Jumlah hari tidak boleh kosong.");
//                return;
//            }
//
//            int jumlahHari = Integer.parseInt(input);
//            if (jumlahHari <= 0) {
//                showAlert(Alert.AlertType.WARNING, "Warning", "Jumlah hari harus lebih dari 0.");
//                return;
//            }
//
//            // Hitung total harga sewa
//            int totalHarga = selectedAlat.getHargaSewa() * jumlahHari;
//
//            // Ambil saldo pengguna
//            int saldoPengguna = penggunaDAO.getSaldoPengguna(currentUserId);
//            if (saldoPengguna < totalHarga) {
//                showAlert(Alert.AlertType.WARNING, "Warning", "Saldo Anda tidak mencukupi.");
//                return;
//            }
//
//            // Proses penyewaan alat
//            int idPerusahaan = selectedAlat.getCompany().getIdPerusahaan();
//            Perusahaan perusahaan = perusahaanDAO.getPerusahaanById(idPerusahaan);
//            transaksiSewaDAO.sewaAlat(new Pengguna(currentUserId), selectedAlat, perusahaan, jumlahHari, totalHarga);
//            
//            // Perbarui tampilan alat yang tersedia
//            loadAlatTersedia();
//            updateTampilanSaldo(currentUserId);
//            // Tampilkan pesan sukses
//            showAlert(Alert.AlertType.INFORMATION, "Success", "Sewa alat berhasil!");
//
//        } catch (NumberFormatException e) {
//            showAlert(Alert.AlertType.WARNING, "Warning", "Jumlah hari harus berupa angka.");
//        } catch (SQLException e) {
//            Logger.getLogger(RentToolUserController.class.getName()).log(Level.SEVERE, null, e);
//            showAlert(Alert.AlertType.ERROR, "Error", "Terjadi kesalahan dalam proses sewa alat: " + e.getMessage());
//        }
//    }