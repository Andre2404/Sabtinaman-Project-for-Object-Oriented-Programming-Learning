package Controller;

import DAO.DatabaseConnection;
import DAO.PerusahaanDAO;
import DAO.PupukDAO;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import model.Perusahaan;
import model.Pupuk;
import utils.SessionManager;

public class GroceriesComController{

    @FXML
    private Button back;
    @FXML
    private Button perbarui;
    @FXML
    private Button hapus;
    @FXML
    private Button tambahkan;

    @FXML
    private TextField namaPupuk;
    @FXML
    private TextField hargaPupuk;
    @FXML
    private ChoiceBox<String> jenisPupuk;
    
    @FXML
    private TableView<Pupuk> tableView;
    @FXML
    private TableColumn<Pupuk, Integer> colStok;
    @FXML
    private TableColumn<Pupuk, String> colJenis;
    @FXML
    private TableColumn<Pupuk, String> colNamaPupuk;
    @FXML
    private TableColumn<Pupuk, String> colHargaPupuk;
    @FXML
    private TableColumn<Pupuk, String> colNamaPerusahaan;
    @FXML
    private ImageView preview;

    private Connection connection;
    private PerusahaanDAO perusahaanDAO;
    private PupukDAO pupukDAO;
    private String imageHash;
    private Pupuk selectedPupuk; // Menyimpan pupuk yang dipilih
    int currentUserId;

    public void initialize() {
        connection = DatabaseConnection.getCon();
        perusahaanDAO = new PerusahaanDAO(connection);
        pupukDAO = new PupukDAO(connection, perusahaanDAO);
        currentUserId = SessionManager.getCurrentUserId();
        
        jenisPupuk.getItems().addAll("Organik", "Anorganik");
    
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
        colStok.setCellValueFactory(new PropertyValueFactory<>("stok"));
        colJenis.setCellValueFactory(new PropertyValueFactory<>("jenisPupuk"));
        tableView.setItems(pupukObservableList);
    }
    
    private void handleTableClick(MouseEvent event) {
    Pupuk selectedPupuk = tableView.getSelectionModel().getSelectedItem();
    if (selectedPupuk != null) {
        // Mengisi form dengan data dari entri yang dipilih
        namaPupuk.setText(selectedPupuk.getNamaPupuk());
        hargaPupuk.setText(String.valueOf(selectedPupuk.getHargaPerKg()));
        jenisPupuk.setValue(selectedPupuk.getJenisPupuk());
        // Anda bisa menambahkan field lain jika diperlukan
    }
}
    
    @FXML
private void handleDeleteButton(ActionEvent event) {
    // Mendapatkan entri yang dipilih dari TableView
    Pupuk selectedPupuk = tableView.getSelectionModel().getSelectedItem();
    
    if (selectedPupuk != null) {
        // Konfirmasi penghapusan
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Konfirmasi Hapus");
        alert.setHeaderText("Anda yakin ingin menghapus pupuk ini?");
        alert.setContentText("Nama Pupuk: " + selectedPupuk.getNamaPupuk());

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                // Menghapus pupuk dari database
                pupukDAO.deletePupuk(selectedPupuk.getIdPupuk()); // Pastikan Anda memiliki metode deletePupuk di PupukDAO
                
                // Memuat ulang data dari database
                loadPupuk();
            } catch (SQLException ex) {
                Logger.getLogger(GroceriesComController.class.getName()).log(Level.SEVERE, null, ex);
                showAlert2(Alert.AlertType.ERROR, "Error", "Gagal menghapus pupuk.");
            }
        }
    } else {
        showAlert2(Alert.AlertType.WARNING, "Peringatan", "Silakan pilih pupuk yang ingin dihapus.");
    }
}

    @FXML
    private void handleChooseImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            try {
                Image image = new Image(new FileInputStream(selectedFile));
                preview.setImage(image);

                // Konversi file gambar ke Base64 (image hash)
                imageHash = encodeImageToBase64(selectedFile);
            } catch (IOException e) {
                showAlert2(Alert.AlertType.ERROR, "Error", "Failed to load image.");
            }
        } else {
            showAlert2(Alert.AlertType.INFORMATION, "No File Selected", "Please choose an image file.");
        }
    }
    
    private String encodeImageToBase64(File file) throws IOException {
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            byte[] bytes = fileInputStream.readAllBytes();
            return Base64.getEncoder().encodeToString(bytes);
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
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load the home page.", Alert.AlertType.ERROR);
        }
    }

   @FXML
    private void handleUpdateButton() {
    String nama = namaPupuk.getText();
    String harga = hargaPupuk.getText();
    String jenis = jenisPupuk.getValue();

    if (nama.isEmpty() || harga.isEmpty() || jenis == null) { 
        showAlert("Validation Error", "Please fill out all fields.", Alert.AlertType.WARNING);
        return;
    }

    try {
        // Validasi input harga agar berupa angka
        int hargaPerKg = Integer.parseInt(harga);
        // Asumsikan perusahaan dengan ID = 1 sebagai default
        Perusahaan perusahaan = perusahaanDAO.getPerusahaanById(1);
        if (selectedPupuk == null) {
            showAlert("Validation Error", "Please select a pupuk to update.", Alert.AlertType.WARNING);
            return;
        }
        
        // Buat objek Pupuk
        Pupuk pupuk = new Pupuk(selectedPupuk.getIdPupuk(), nama, hargaPerKg, selectedPupuk.getStok(), jenis, selectedPupuk.getSpesifikasi(), perusahaan, selectedPupuk.getImageHash());
        // Gunakan PupukDAO untuk memperbarui data di database
        PupukDAO pupukDAO = new PupukDAO(connection);
        pupukDAO.updatePupuk(pupuk); // Pastikan Anda memiliki metode updatePupuk di PupukDAO
        
        // Tampilkan pesan sukses
        showAlert("Success", "Pupuk updated successfully.", Alert.AlertType.INFORMATION);
        
         // Clear input fields
        namaPupuk.clear();
        hargaPupuk.clear();
        // Muat ulang data untuk memperbarui tampilan
        loadPupuk();
        
    } catch (NumberFormatException e) {
        showAlert("Validation Error", "Harga harus berupa angka.", Alert.AlertType.WARNING);
    } catch (SQLException e) {
        showAlert("Database Error", "Failed to update pupuk ke database.", Alert.AlertType.ERROR);
    }
}

// Gunakan PupukDAO untuk menambahkan data ke database
//        PupukDAO pupukDAO = new PupukDAO(connection);
//        pupukDAO.addPupuk(pupuk);
//
//        // Tampilkan pesan sukses
//        showAlert("Success", "Data successfully added.", Alert.AlertType.INFORMATION);
//
//        // Clear input fields
//        namaPupuk.clear();
//        hargaPupuk.clear();
//        loadPupuk();
//        
//    } catch (NumberFormatException e) {
//        showAlert("Validation Error", "Price must be a valid number.", Alert.AlertType.WARNING);
//    } catch (SQLException e) {
//        e.printStackTrace();
//        showAlert("Database Error", "Failed to insert data into the database.", Alert.AlertType.ERROR);
//    }
//}

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private void showAlert2(Alert.AlertType alertType, String title, String message) {
    Alert alert = new Alert(alertType);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
}

    @FXML
    private void handleAddButton(ActionEvent event) {
    }
}