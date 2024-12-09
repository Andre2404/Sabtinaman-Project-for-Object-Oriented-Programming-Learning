package Controller;

import DAO.AlatDAO;
import DAO.DatabaseConnection;
import DAO.PenggunaDAO;
import DAO.PerusahaanDAO;
import DAO.TransaksiSewaDAO;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Alat;
import model.Pengguna;
import utils.SessionManager;
    
/**
 * FXML Controller class
 *
 * @author User
 */
public class RentToolUserController{

    @FXML
    private TableView<Alat> tableAlatTersedia;
    @FXML
    private Label saldoLabel; // Pastikan ini terhubung dengan fx:id di FXML
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
    /**
     * Initializes the controller class.
     */
    public void initialize() {
    try {
        connection = DatabaseConnection.getCon();
        perusahaanDAO = new PerusahaanDAO(connection);
        penggunaDAO = new PenggunaDAO(connection);
        alatDAO = new AlatDAO(connection, perusahaanDAO);

        int currentUserId = SessionManager.getCurrentUserId();
        tampilanPengguna(currentUserId);
        loadAlatTersedia();
    } catch (SQLException ex) {
        Logger.getLogger(RentToolUserController.class.getName()).log(Level.SEVERE, null, ex);
    }
}

    
    private void tampilanPengguna(int idPengguna) {
        try {
            Pengguna pengguna = penggunaDAO.getPenggunaById(idPengguna);
            if (pengguna != null) {
                // Format saldo dengan "Rp." dan tampilkan di saldoLabel
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
    List<Alat> alatList = alatDAO.getAvailableAlat(); // Ambil data dari database

    // Ubah List menjadi ObservableList untuk digunakan di TableView
    ObservableList<Alat> alatObservableList = FXCollections.observableArrayList(alatList);

    // Pasangkan kolom dengan atribut di objek Alat
        colIdAlat.setCellValueFactory(new PropertyValueFactory<>("idAlat"));
        colNamaAlat.setCellValueFactory(new PropertyValueFactory<>("namaAlat"));
        colSpekAlat.setCellValueFactory(new PropertyValueFactory<>("spesifikasi"));
        colHargaSewa.setCellValueFactory(new PropertyValueFactory<>("hargaSewa"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

    // Set data ke TableView
    tableAlatTersedia.setItems(alatObservableList);
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
