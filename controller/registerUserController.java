package Controller;

import DAO.DatabaseConnection;
import DAO.PenggunaDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Pengguna;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import javafx.scene.control.Button;

public class registerUserController {

    @FXML
    private TextField username_register;

    @FXML
    private TextField address_register;

    @FXML
    private TextField email_register;

    @FXML
    private TextField nomorKontak_register;

    @FXML
    private TextField password_register;
    
    @FXML
    private Button back;

    
    private PenggunaDAO penggunaDAO;

    @FXML
public void initialize() {
    Connection connection = DatabaseConnection.getCon();
    penggunaDAO = new PenggunaDAO(connection); // Pastikan PenggunaDAO menerima Connection
}
    @FXML
    public void handleRegisterButton(ActionEvent event) {
        String nama = username_register.getText();
        String alamat = address_register.getText();
        String email = email_register.getText();
        String nomorKontakStr = nomorKontak_register.getText();
        String password = password_register.getText();

        // Validasi input
        if (nama.isEmpty() || alamat.isEmpty() || email.isEmpty() || nomorKontakStr.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Semua field harus diisi!");
            return;
        }

        try {
            int nomorKontak = Integer.parseInt(nomorKontakStr);

            // Membuat objek pengguna
            Pengguna pengguna = new Pengguna(0, nama, alamat, email, nomorKontak, 0, password);

            // Menambahkan pengguna ke database
            penggunaDAO.addPengguna(pengguna);

            // Menampilkan pesan sukses
            showAlert(Alert.AlertType.INFORMATION, "Sukses", "Registrasi berhasil!");

            // Beralih ke halaman login
            navigateTo(event, "/View/signInUser.fxml");

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Nomor kontak harus berupa angka!");
        } catch (SQLException | IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Gagal melakukan registrasi: " + e.getMessage());
        }
    }

    @FXML
    public void handleBackButton(ActionEvent event) {
        try {
            navigateTo(event, "/View/startPage.fxml");
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
