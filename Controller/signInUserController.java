package Controller;

import DAO.DatabaseConnection;
import DAO.PenggunaDAO;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import utils.SessionManager;
import model.Pengguna;

public class signInUserController {
    public static Pengguna pengguna;
    @FXML
    private TextField username;
    @FXML
    private TextField password;
    @FXML
    private javafx.scene.control.Button signInCom;
    @FXML
    private javafx.scene.control.Button register;
    @FXML
    private javafx.scene.control.Button sign;
    private PenggunaDAO penggunaDAO;
    @FXML
    private Button close_btn;
    @FXML
    private ImageView imageView;

public void initialize() {
    Connection connection = DatabaseConnection.getCon();
    penggunaDAO = new PenggunaDAO(connection); // Pastikan PenggunaDAO menerima Connection
}

    @FXML
    private void handleSignIn(ActionEvent event) {
        if (penggunaDAO == null) {
    showAlert(Alert.AlertType.ERROR, "Error", "PenggunaDAO belum diinisialisasi.");
    return;
}

        String emailInput = username.getText();
        String passwordInput = password.getText();

        if (emailInput.isEmpty() || passwordInput.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Input Error", "Email dan Password tidak boleh kosong.");
            return;
        }

        try {
            pengguna = penggunaDAO.login(emailInput, passwordInput);
            if (pengguna != null) {
                SessionManager.setCurrentUserId(pengguna.getIdPengguna());
                showAlert(Alert.AlertType.INFORMATION, "Login Berhasil", "Selamat datang, " + pengguna.getNama());
                // Navigasi ke halaman berikutnya
                loadScene("/View/homePageUser.fxml");
            } else {
                showAlert(Alert.AlertType.ERROR, "Login Gagal", "Email atau Password salah.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Terjadi kesalahan pada database.");
        }
    }

    @FXML
    private void loadRegisterScene(ActionEvent event) {
        loadScene("/View/registerUser.fxml");
    }

    @FXML
    private void loadSignInComScene(ActionEvent event) {
        loadScene("/View/signInCom.fxml");
    }

    private void loadScene(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            loader.setController(this); 
            Stage stage = (Stage) sign.getScene().getWindow(); // Ambil stage dari salah satu tombol
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.WARNING, "Error", "Failed to load the scene: " + fxmlPath);
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
