package Controller;

import DAO.AlatDAO;
import DAO.DatabaseConnection;
import DAO.PenggunaDAO;
import DAO.PerusahaanDAO;
import DAO.PupukDAO;
import DAO.SaldoDAO;
import DAO.TransaksiPupukDAO;
import DAO.TransaksiSewaDAO;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
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
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Alat;
import model.Pengguna;
import model.Perusahaan;
import model.Pupuk;
import utils.SessionManager;

/**
 * FXML Controller class
 *
 * @author User
 */
public class GroceriesController{

    @FXML
    private Label saldoLabel; // Pastikan ini terhubung dengan fx:id di FXML
    
    private Connection connection;
    private PenggunaDAO penggunaDAO;
    @FXML
    private Button back;
    @FXML
    private TableColumn<Pupuk, String> colNamaPupuk;
    @FXML
    private TableColumn<Pupuk, Integer> colHargaperKg;
    @FXML
    private TableColumn<Pupuk, String> ColDistributor;
    @FXML
    private Button beli;

    private PerusahaanDAO perusahaanDAO;
    private PupukDAO pupukDAO;
    private TransaksiPupukDAO transaksiPupukDAO;
    private SaldoDAO saldoDAO;

    private int currentUserId;
    @FXML
    private TableView<Pupuk> tableView;
    @FXML
    private Button back1;

    public void initialize() {
        try {
            connection = DatabaseConnection.getCon();
            penggunaDAO = new PenggunaDAO(connection);
            perusahaanDAO = new PerusahaanDAO(connection);
            pupukDAO = new PupukDAO(connection, perusahaanDAO);
            saldoDAO = new SaldoDAO(connection, penggunaDAO, perusahaanDAO);
            transaksiPupukDAO = new TransaksiPupukDAO(connection, saldoDAO, penggunaDAO, pupukDAO, perusahaanDAO);

            //transaksiSewaDAO = new TransaksiSewaDAO(connection, saldoDAO, penggunaDAO, alatDAO, perusahaanDAO);
            currentUserId = SessionManager.getCurrentUserId(); // Menyimpan ID pengguna sekali

            tampilanPengguna(currentUserId);
            loadPupukTersedia();

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
    private void loadPupukTersedia() throws SQLException {
        List<Pupuk> pupukList = pupukDAO.getAvailablePupuk();
        ObservableList<Pupuk> pupukObservableList = FXCollections.observableArrayList(pupukList);

        colNamaPupuk.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNamaPupuk()));
        colHargaperKg.setCellValueFactory(new PropertyValueFactory<>("hargaPerKg"));
        ColDistributor.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCompany().getNama()));
        //colHargaSewa.setCellValueFactory(new PropertyValueFactory<>("hargaSewa"));
        //colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        tableView.setItems(pupukObservableList);
    }

    @FXML
    private void handleBeliPupuk() {
        try {
            // Ambil alat yang dipilih dari TableView
            Pupuk selectedPupuk = tableView.getSelectionModel().getSelectedItem();
            if (selectedPupuk == null) {
                showAlert(Alert.AlertType.WARNING, "Warning", "Pilih alat terlebih dahulu.");
                return;
            }

            // Dialog input jumlah hari
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Input Berat Pupuk");
            dialog.setHeaderText("Masukkan jumlah berat (satuan KG) untuk membeli pupuk:");
            dialog.setContentText("Jumlah berat:");

            dialog.showAndWait();

            String input = dialog.getResult();
            if (input == null || input.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Warning", "Jumlah berat tidak boleh kosong.");
                return;
            }

            int jumlahKg = Integer.parseInt(input);
            if (jumlahKg <= 0) {
                showAlert(Alert.AlertType.WARNING, "Warning", "Jumlah berat harus lebih dari 0.");
                return;
            }

            // Hitung total harga sewa
            int totalHarga = selectedPupuk.getHargaPerKg() * jumlahKg;

            // Ambil saldo pengguna
            int saldoPengguna = penggunaDAO.getSaldoPengguna(currentUserId);
            if (saldoPengguna < totalHarga) {
                showAlert(Alert.AlertType.WARNING, "Warning", "Saldo Anda tidak mencukupi.");
                return;
            }

            // Proses penyewaan alat
            int idPerusahaan = selectedPupuk.getCompany().getIdPerusahaan();
            Perusahaan perusahaan = perusahaanDAO.getPerusahaanById(idPerusahaan);
            transaksiPupukDAO.buyPupuk(new Pengguna(currentUserId), selectedPupuk, jumlahKg, totalHarga, perusahaan);

            // Perbarui tampilan alat yang tersedia
            loadPupukTersedia();
            updateTampilanSaldo(currentUserId);
            // Tampilkan pesan sukses
            showAlert(Alert.AlertType.INFORMATION, "Success", "Beli pupuk berhasil!");

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.WARNING, "Warning", "Jumlah Berat harus berupa angka.");
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
    
    @FXML
    private void handleTableClick(MouseEvent event) {
        if (event.getClickCount() == 1) {
            Pupuk selectedPupuk = tableView.getSelectionModel().getSelectedItem();
            if (selectedPupuk != null) {
                System.out.println("Selected Alat: " + selectedPupuk.getNamaPupuk());
            }
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
