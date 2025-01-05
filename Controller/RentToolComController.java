package Controller;

import DAO.AlatDAO;
import DAO.DatabaseConnection;
import DAO.PerusahaanDAO;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Base64;
import model.Alat;
import utils.SessionManager;
import java.net.URL;
import java.nio.file.Files;
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
    private TableColumn<Alat, Integer> colStokAlat;
    @FXML
    private Button back;
    @FXML
    private Button tambah;
    @FXML
    private Button hapus;
    @FXML
    private Button addPict;
    @FXML
    private TextField namaAlat;
    @FXML
    private TextArea spesifikasi;
    @FXML
    private TextField stokAlat;
    @FXML
    private TextField hargaAlat;
    @FXML
    private ImageView preview; // Preview gambar

    private String imageHash; // Untuk menyimpan hash gambar

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

//    private void loadAlat() throws SQLException {
//        List<Alat> alatlist = alatDAO.getAllAlat();
//        if (alatlist == null || alatlist.isEmpty()) {
//            tableAlatTersedia.setItems(FXCollections.observableArrayList()); // Bersihkan tabel
//            return;
//        }
//        ObservableList<Alat> alatObservableList = FXCollections.observableArrayList(alatlist);
//        colIdAlat.setCellValueFactory(new PropertyValueFactory<>("idAlat"));
//        colNamaAlat.setCellValueFactory(new PropertyValueFactory<>("namaAlat"));
//        colSpekAlat.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSpesifikasi()));
//        colHargaSewa.setCellValueFactory(new PropertyValueFactory<>("hargaSewa"));
//        colStokAlat.setCellValueFactory(new PropertyValueFactory<>("stok"));
//
//        tableAlatTersedia.setItems(alatObservableList);
//    }
    
    private void loadAlat() throws SQLException {
        List<Alat> alatList = alatDAO.getAvailableAlat();
        ObservableList<Alat> alatObservableList = FXCollections.observableArrayList(alatList);

        colIdAlat.setCellValueFactory(new PropertyValueFactory<>("idAlat"));
        colNamaAlat.setCellValueFactory(new PropertyValueFactory<>("namaAlat"));
        colSpekAlat.setCellValueFactory(new PropertyValueFactory<>("spesifikasi"));
        colHargaSewa.setCellValueFactory(new PropertyValueFactory<>("hargaSewa"));
        colStokAlat.setCellValueFactory(new PropertyValueFactory<>("stok"));

        tableAlatTersedia.setItems(alatObservableList);
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
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to load image.");
            }
        } else {
            showAlert(Alert.AlertType.INFORMATION, "No File Selected", "Please choose an image file.");
        }
    }
    
    private String encodeImageToBase64(File file) throws IOException {
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            byte[] bytes = fileInputStream.readAllBytes();
            return Base64.getEncoder().encodeToString(bytes);
        }
    }
    
//    @FXML
//    private void handleChooseImage(ActionEvent event) {
//    FileChooser fileChooser = new FileChooser();
//    fileChooser.getExtensionFilters().addAll(
//            new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
//    );
//    File selectedFile = fileChooser.showOpenDialog(null);
//
//    if (selectedFile != null) {
//        try {
//            // Load the image and display it
//            Image image = new Image(new FileInputStream(selectedFile));
//            preview.setImage(image);
//
//            // Convert the image file to a byte array
//            byte[] imageBytes = Files.readAllBytes(selectedFile.toPath());
//
//            // Store the image bytes in the Alat object
//            // Assuming you have a method to get the current Alat object
//            currentAlat.setImage(imageBytes); // Set the image bytes to the current Alat object
//
//        } catch (IOException e) {
//            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load image.");
//        }
//    } else {
//        showAlert(Alert.AlertType.INFORMATION, "No File Selected", "Please choose an image file.");
//    }
//}

    @FXML
    private void handleTableClick(MouseEvent event) {
        Alat selectedAlat = tableAlatTersedia.getSelectionModel().getSelectedItem();
        if (selectedAlat != null) {
            namaAlat.setText(selectedAlat.getNamaAlat());
            spesifikasi.setText(selectedAlat.getSpesifikasi());
            hargaAlat.setText(String.valueOf(selectedAlat.getHargaSewa()));
            stokAlat.setText(String.valueOf(selectedAlat.getStok()));

            // Load and display the image if available
            if (selectedAlat.getImageHash() != null && !selectedAlat.getImageHash().isEmpty()) {
                try {
                    // Decode Base64 to an image and show it in the ImageView
                    Image image = decodeBase64ToImage(selectedAlat.getImageHash());
                    preview.setImage(image);
                } catch (IOException e) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to load the image.");
                }
            } else {
                preview.setImage(null); // Clear the preview if no image is available
            }
        }
    }
    
    private Image decodeBase64ToImage(String base64String) throws IOException {
    byte[] imageBytes = Base64.getDecoder().decode(base64String);
    return new Image(new ByteArrayInputStream(imageBytes));
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
    private void handleAddButton(ActionEvent event) throws SQLException, IOException {
        String nama = namaAlat.getText().trim();
        String spek = spesifikasi.getText().trim();
        String stok = stokAlat.getText().trim();
        String harga = hargaAlat.getText().trim();

        if (nama.isEmpty() || spek.isEmpty() || stok.isEmpty() || harga.isEmpty() || imageHash == null) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Please fill out all fields and choose an image.");
            return;
        }
            int stokValue = Integer.parseInt(stok);
            int hargaValue = Integer.parseInt(harga);

            if (stokValue < 0 || hargaValue < 0) {
                showAlert(Alert.AlertType.WARNING, "Validation Error", "Stock and Price must be positive numbers.");
                return;
            }

            Perusahaan perusahaan = perusahaanDAO.getPerusahaanById(1);
            Alat alat = new Alat(0, nama, spek, perusahaan, hargaValue, "available", stokValue, imageHash);
            alatDAO.addAlat(alat);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Data successfully added.");
            resetForm();
            loadAlat();
    }

    @FXML
    private void handleDeleteButton(ActionEvent event) {
        Alat selectedAlat = tableAlatTersedia.getSelectionModel().getSelectedItem();
        if (selectedAlat == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a tool to delete.");
            return;
        }

        try {
            boolean isDeleted = alatDAO.deleteAlat(selectedAlat.getIdAlat());
            if (isDeleted) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Tool successfully deleted.");
                loadAlat(); // Refresh tabel
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete the tool.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "An error occurred while trying to delete the tool.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void resetForm() {
    namaAlat.clear();
    spesifikasi.clear();
    stokAlat.clear();
    hargaAlat.clear();
    preview.setImage(null);
    imageHash = null;
    }   
    private static final Logger logger = Logger.getLogger(RentToolComController.class.getName());

private void logError(String message, Exception e) {
    logger.log(Level.SEVERE, message, e);
}

}
