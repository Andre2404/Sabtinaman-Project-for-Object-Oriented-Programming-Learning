package Controller;

import DAO.AlatDAO;
import DAO.DatabaseConnection;
import DAO.PerusahaanDAO;
import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Alert;
import model.Alat;
import utils.SessionManager;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Perusahaan;

public class RentToolComController{

    @FXML
    private TableView<Alat> tableAlatTersedia;
    @FXML
    private TableColumn<Alat, Integer> colIdAlat;
    @FXML
    private TableColumn<Alat, String> colNamaAlat;
    @FXML
    private TableColumn<Alat, String> colSpekAlat;
    @FXML
    private TableColumn<Alat, Double> colHargaSewa;
    @FXML
    private TableColumn<Alat, String> colStatus;
    @FXML
    private Button back;
    @FXML
    private Button post;
    @FXML
    private TextField namaAlat;
    @FXML
    private TextArea spesifikasi;
    @FXML
    private TextField hargaAlat;

    private Connection connection;
    private AlatDAO alatDAO;
    private PerusahaanDAO perusahaanDAO;
    int currentUserId;
    public void initialize() {
        connection = DatabaseConnection.getCon();
        perusahaanDAO = new PerusahaanDAO(connection);
        alatDAO = new AlatDAO(connection, perusahaanDAO);
        currentUserId = SessionManager.getCurrentUserId();
        try {
            // setupTable();
            loadAlat();
        } catch (SQLException ex) {
            Logger.getLogger(RentToolComController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

private void loadAlat() throws SQLException {
        List<Alat> alatlist = alatDAO.getAllAlat();
        ObservableList<Alat> alatObservableList = FXCollections.observableArrayList(alatlist);
        colIdAlat.setCellValueFactory(new PropertyValueFactory<>("idAlat"));
        colNamaAlat.setCellValueFactory(new PropertyValueFactory<>("namaAlat"));
        colSpekAlat.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSpesifikasi()));
        colHargaSewa.setCellValueFactory(new PropertyValueFactory<>("hargaSewa"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        
        tableAlatTersedia.setItems(alatObservableList);
    }
    

    @FXML
    private void handleTableClick(MouseEvent event) {
        Alat selectedAlat = tableAlatTersedia.getSelectionModel().getSelectedItem();
        if (selectedAlat != null) {
            namaAlat.setText(selectedAlat.getNamaAlat());
            spesifikasi.setText(selectedAlat.getSpesifikasi());
            hargaAlat.setText(String.valueOf(selectedAlat.getHargaSewa()));
        }
    }

    @FXML
    private void handleBackButton(ActionEvent event) {
        try {
            // Load the home page
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/homePageCom.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) back.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR,"Error", "Failed to load the home page.");
        }
    }
    @FXML
    private void handlePostButton(ActionEvent event) {
    String nama = namaAlat.getText();
    String harga = hargaAlat.getText();
    String spek = spesifikasi.getText();

    if (nama.isEmpty() || harga.isEmpty() || spek.isEmpty()) {
        showAlert(Alert.AlertType.WARNING, "Validation Error", "Please fill out all fields." );
        return;
    }

    try {
        // Validasi input harga agar berupa angka
        int hargaPerHari = Integer.parseInt(harga);

        // Asumsikan perusahaan dengan ID = 1 sebagai default
        Perusahaan perusahaan = perusahaanDAO.getPerusahaanById(currentUserId);

        // Buat objek Alat
        Alat alat = new Alat(0, nama, spek, perusahaan, hargaPerHari, "available");

        // Gunakan PupukDAO untuk menambahkan data ke database
        AlatDAO alatDAO = new AlatDAO(connection);
        alatDAO.addAlat(alat);

        // Tampilkan pesan sukses
        showAlert(Alert.AlertType.INFORMATION, "Success", "Data successfully added.");

        // Clear input fields
        namaAlat.clear();
        hargaAlat.clear();
        spesifikasi.clear();

        loadAlat();
        
    } catch (NumberFormatException e) {
        showAlert(Alert.AlertType.WARNING, "Validation Error", "Price must be a valid number.");
    } catch (SQLException e) {
        e.printStackTrace();
        showAlert(Alert.AlertType.WARNING, "Database Error", "Failed to insert data into the database.");
    }
}
    
    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
