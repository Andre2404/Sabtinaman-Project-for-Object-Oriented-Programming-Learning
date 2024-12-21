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
import model.Pengguna;
import model.Perusahaan;
import utils.SessionManager;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    private TableColumn<Alat, String> colStatus;

    private Connection connection;
    private PerusahaanDAO perusahaanDAO;
    private PenggunaDAO penggunaDAO;
    private AlatDAO alatDAO;
    private TransaksiSewaDAO transaksiSewaDAO;
    private SaldoDAO saldoDAO;

    private int currentUserId;
    @FXML
    private Button back;
    @FXML
    private TableView<?> tableAlatTersedia1;
    @FXML
    private Button back1;

    public void initialize() {
        try {
            connection = DatabaseConnection.getCon();
            perusahaanDAO = new PerusahaanDAO(connection);
            penggunaDAO = new PenggunaDAO(connection);
            alatDAO = new AlatDAO(connection, perusahaanDAO);
            saldoDAO = new SaldoDAO(connection);
            transaksiSewaDAO = new TransaksiSewaDAO(connection, saldoDAO, penggunaDAO, alatDAO, perusahaanDAO);
            currentUserId = SessionManager.getCurrentUserId(); // Menyimpan ID pengguna sekali

            tampilanPengguna(currentUserId);
            loadAlatTersedia();

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
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        tableAlatTersedia.setItems(alatObservableList);
    }

    @FXML
    private void handleTableClick(MouseEvent event) {
        if (event.getClickCount() == 1) {
            Alat selectedAlat = tableAlatTersedia.getSelectionModel().getSelectedItem();
            if (selectedAlat != null) {
                System.out.println("Selected Alat: " + selectedAlat.getNamaAlat());
            }
        }
    }

    @FXML
    private void handleSewaAlat() {
        try {
            // Ambil alat yang dipilih dari TableView
            Alat selectedAlat = tableAlatTersedia.getSelectionModel().getSelectedItem();
            if (selectedAlat == null) {
                showAlert(Alert.AlertType.WARNING, "Warning", "Pilih alat terlebih dahulu.");
                return;
            }

            // Dialog input jumlah hari
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Input Jumlah Hari");
            dialog.setHeaderText("Masukkan jumlah hari untuk menyewa alat:");
            dialog.setContentText("Jumlah hari:");

            dialog.showAndWait();

            String input = dialog.getResult();
            if (input == null || input.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Warning", "Jumlah hari tidak boleh kosong.");
                return;
            }

            int jumlahHari = Integer.parseInt(input);
            if (jumlahHari <= 0) {
                showAlert(Alert.AlertType.WARNING, "Warning", "Jumlah hari harus lebih dari 0.");
                return;
            }

            // Hitung total harga sewa
            int totalHarga = selectedAlat.getHargaSewa() * jumlahHari;

            // Ambil saldo pengguna
            int saldoPengguna = penggunaDAO.getSaldoPengguna(currentUserId);
            if (saldoPengguna < totalHarga) {
                showAlert(Alert.AlertType.WARNING, "Warning", "Saldo Anda tidak mencukupi.");
                return;
            }

            // Proses penyewaan alat
            int idPerusahaan = selectedAlat.getCompany().getIdPerusahaan();
            Perusahaan perusahaan = perusahaanDAO.getPerusahaanById(idPerusahaan);
            transaksiSewaDAO.sewaAlat(new Pengguna(currentUserId), selectedAlat, perusahaan, jumlahHari);

            // Perbarui tampilan alat yang tersedia
            loadAlatTersedia();
            updateTampilanSaldo(currentUserId);
            // Tampilkan pesan sukses
            showAlert(Alert.AlertType.INFORMATION, "Success", "Sewa alat berhasil!");

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.WARNING, "Warning", "Jumlah hari harus berupa angka.");
        } catch (SQLException e) {
            Logger.getLogger(RentToolUserController.class.getName()).log(Level.SEVERE, null, e);
            showAlert(Alert.AlertType.ERROR, "Error", "Terjadi kesalahan dalam proses sewa alat: " + e.getMessage());
        }
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
