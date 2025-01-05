package Controller;

import DAO.DatabaseConnection;
import DAO.PerusahaanDAO;
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
import model.Perusahaan;
import utils.SessionManager;

public class signInComController {
    public static Perusahaan perusahaan;
    @FXML
    private TextField username; // Field untuk email perusahaan
    @FXML
    private PasswordField password; // Field untuk password
    @FXML
    private Button signInUser; // Button untuk login
    @FXML
    private Button register; // Button untuk register
    @FXML
    private Button sign; // Button untuk berpindah halaman

    private PerusahaanDAO perusahaanDAO;

    @FXML
    private ImageView imageView;
    
    public void initialize() {
       Connection connection = DatabaseConnection.getCon();
       perusahaanDAO = new PerusahaanDAO(connection);
       Image img = new Image("file:C:\\Users\\User\\Documents\\NetBeansProjects\\Sabtinaman\\src\\main\\java\\Pictures\\Sabtinaman.png");
       imageView.setImage(img);
    }

    @FXML
    private void handleSignIn(ActionEvent event) {
        String emailInput = username.getText().trim();
        String passwordInput = password.getText().trim();

        if (emailInput.isEmpty() || passwordInput.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Input Error", "Email dan Password tidak boleh kosong.");
            return;
        }

        try {
            // Validasi login menggunakan DAO
            perusahaan = perusahaanDAO.login(emailInput, passwordInput);
            if (perusahaan != null) {
                SessionManager.setCurrentUserId(perusahaan.getIdPerusahaan());
                showAlert(Alert.AlertType.INFORMATION, "Login Berhasil", "Selamat datang, " + perusahaan.getNama());
                // Pindah ke halaman home perusahaan
                loadScene("/View/homePageCom.fxml");
            } else {
                showAlert(Alert.AlertType.ERROR, "Login Gagal", "Email atau Password salah.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Terjadi kesalahan pada koneksi database.");
        }
    }

    @FXML
    private void loadRegisterScene(ActionEvent event) {
        loadScene("/View/registerCom.fxml");
    }

    @FXML
    private void loadSignInUserScene(ActionEvent event) {
        loadScene("/View/signInUser.fxml");
    }

    private void loadScene(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            // Ambil stage dari tombol yang ditekan
            Stage stage = (Stage) sign.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.WARNING, "Load Error", "Gagal memuat halaman: " + fxmlPath);
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
