package Controller;

import DAO.DatabaseConnection;
import DAO.PerusahaanDAO;
import DAO.PupukDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Perusahaan;
import model.Pupuk;
import utils.SessionManager;

public class GroceriesComController{

    @FXML
    private Button back;
    
    @FXML
    private Button post;

    @FXML
    private TextField namaPupuk;

    @FXML
    private TextField hargaPupuk;

    @FXML
    private TableView<Pupuk> tableView;
    
    @FXML
    private TableColumn<Pupuk, Integer> colIdPupuk;

    @FXML
    private TableColumn<Pupuk, String> colNamaPupuk;

    @FXML
    private TableColumn<Pupuk, String> colHargaPupuk;

    @FXML
    private TableColumn<Pupuk, String> colNamaPerusahaan;

    private Connection connection;
    private PerusahaanDAO perusahaanDAO;
    private PupukDAO pupukDAO;
    int currentUserId;
    public void initialize() {
        connection = DatabaseConnection.getCon();
        perusahaanDAO = new PerusahaanDAO(connection);
        pupukDAO = new PupukDAO(connection, perusahaanDAO);
        currentUserId = SessionManager.getCurrentUserId();
    
        try {
            // Load data from database into TableView
            loadPupuk();
        } catch (SQLException ex) {
            Logger.getLogger(GroceriesComController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadPupuk() throws SQLException {
        List<Pupuk> pupuklist = pupukDAO.getAllPupuk();
        ObservableList<Pupuk> pupukObservableList = FXCollections.observableArrayList(pupuklist);
        colNamaPupuk.setCellValueFactory(new PropertyValueFactory<>("namaPupuk"));
        colHargaPupuk.setCellValueFactory(new PropertyValueFactory<>("hargaPerKg"));
        colNamaPerusahaan.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCompany().getNama()));

        tableView.setItems(pupukObservableList);
    }
    
    @FXML
    private void handleBackButton(ActionEvent event) {
        try {
            // Load the home page
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/homePageCom.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) back.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load the home page.", Alert.AlertType.ERROR);
        }
    }

   @FXML
private void handlePostButton() {
    String nama = namaPupuk.getText();
    String harga = hargaPupuk.getText();

    if (nama.isEmpty() || harga.isEmpty()) {
        showAlert("Validation Error", "Please fill out all fields.", Alert.AlertType.WARNING);
        return;
    }

    try {
        // Validasi input harga agar berupa angka
        int hargaPerKg = Integer.parseInt(harga);

        // Asumsikan perusahaan dengan ID = 1 sebagai default
        Perusahaan perusahaan = perusahaanDAO.getPerusahaanById(currentUserId);

        // Buat objek Pupuk
        Pupuk pupuk = new Pupuk(0, nama, hargaPerKg, perusahaan);

        // Gunakan PupukDAO untuk menambahkan data ke database
        PupukDAO pupukDAO = new PupukDAO(connection);
        pupukDAO.addPupuk(pupuk);

        // Tampilkan pesan sukses
        showAlert("Success", "Data successfully added.", Alert.AlertType.INFORMATION);

        // Clear input fields
        namaPupuk.clear();
        hargaPupuk.clear();

        loadPupuk();
        
    } catch (NumberFormatException e) {
        showAlert("Validation Error", "Price must be a valid number.", Alert.AlertType.WARNING);
    } catch (SQLException e) {
        e.printStackTrace();
        showAlert("Database Error", "Failed to insert data into the database.", Alert.AlertType.ERROR);
    }
}



    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}