/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Controller;
import DAO.*;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.HistoryTransaksi;
import utils.SessionManager;
/**
 * FXML Controller class
 *
 * @author User
 */
public class HistoryPageCompanyController{


   @FXML
private CheckBox checkBoxWithDraw, checkBoxSewaAlat, checkBoxBeliPupuk;
@FXML
private TableView<HistoryTransaksi> tableView;
@FXML
private TableColumn<HistoryTransaksi, Integer> colIdTransaksi;
@FXML
private TableColumn<HistoryTransaksi, String> colNama;
@FXML
private TableColumn<HistoryTransaksi, String> colJenisTransaksi;
@FXML
private TableColumn<HistoryTransaksi, Integer> colJumlahTransaksi;
@FXML
private TableColumn<HistoryTransaksi, String> colTipeSaldo;
@FXML
private TableColumn<HistoryTransaksi, LocalDate> colTanggalTransaksi;
int currentUserId;
private HistoryTransaksiDAO historyTransaksiDAO;
private Connection connection;
@FXML
public void initialize() {
    connection = DatabaseConnection.getCon();
    historyTransaksiDAO = new HistoryTransaksiDAO(connection);
    currentUserId = SessionManager.getCurrentUserId();
    configureTable();
    loadAllData();

    // Tambahkan listener untuk checkbox
    checkBoxWithDraw.setOnAction(event -> filterData());
    checkBoxSewaAlat.setOnAction(event -> filterData());
    checkBoxBeliPupuk.setOnAction(event -> filterData());
}

private void configureTable() {
    // Konfigurasi TableView
    colIdTransaksi.setCellValueFactory(new PropertyValueFactory<>("id"));
    colNama.setCellValueFactory(new PropertyValueFactory<>("keterangan"));
    colJenisTransaksi.setCellValueFactory(new PropertyValueFactory<>("jenis"));
    colJumlahTransaksi.setCellValueFactory(new PropertyValueFactory<>("jumlah"));
    colTipeSaldo.setCellValueFactory(new PropertyValueFactory<>("tipeSaldo"));
    colTanggalTransaksi.setCellValueFactory(new PropertyValueFactory<>("tanggal"));
}

private void loadAllData() {
    try {
        // Load data untuk ID pengguna yang sedang login saja
        List<HistoryTransaksi> companyData = historyTransaksiDAO.getAllCompanyData(currentUserId);
        tableView.setItems(FXCollections.observableArrayList(companyData));
    } catch (SQLException e) {
        showAlert(Alert.AlertType.ERROR, "Error", "Gagal memuat data transaksi: " + e.getMessage());
    }
}

@FXML
    public void handleBackButton(ActionEvent event) {
        try {
            navigateTo(event, "/View/homePageCom.fxml");
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Gagal kembali ke halaman awal: " + e.getMessage());
        }
    }
private void filterData() {
    try {
        boolean withDrawChecked = checkBoxWithDraw.isSelected();
        boolean sewaChecked = checkBoxSewaAlat.isSelected();
        boolean pupukChecked = checkBoxBeliPupuk.isSelected();

        // Ambil data berdasarkan filter
        List<HistoryTransaksi> filteredData = historyTransaksiDAO.getFilteredDataCom(currentUserId,withDrawChecked, sewaChecked, pupukChecked);
        tableView.setItems(FXCollections.observableArrayList(filteredData));
    } catch (SQLException e) {
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

