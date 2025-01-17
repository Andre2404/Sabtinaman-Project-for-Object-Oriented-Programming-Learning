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
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.HistoryTransaksi;
import model.Keranjang;
import utils.SessionManager;

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
@FXML
private DatePicker startDatePicker;
@FXML
private DatePicker endDatePicker;

int currentUserId;
private HistoryTransaksiDAO historyTransaksiDAO;
private PenggunaDAO penggunaDAO;
private AlatDAO alatDAO;
private PerusahaanDAO perusahaanDAO;
private PupukDAO pupukDAO;
private Connection connection;

@FXML
public void initialize() {
    connection = DatabaseConnection.getCon();
    penggunaDAO = new PenggunaDAO(connection);
    perusahaanDAO = new PerusahaanDAO(connection);
    alatDAO = new AlatDAO(connection, perusahaanDAO);
    pupukDAO = new PupukDAO(connection, perusahaanDAO);
    historyTransaksiDAO = new HistoryTransaksiDAO(connection, penggunaDAO, alatDAO, perusahaanDAO, pupukDAO);
    currentUserId = SessionManager.getCurrentUserId();
    configureTable();
    loadAllData();
    
    // Tambahkan listener untuk checkbox
    checkBoxWithDraw.setOnAction(event -> filterData());
    checkBoxSewaAlat.setOnAction(event -> filterData());
    checkBoxBeliPupuk.setOnAction(event -> filterData());
    
    tableView.setOnMouseClicked(event -> {
        if (event.getClickCount() == 2) { // Deteksi klik ganda
            HistoryTransaksi selectedItem = tableView.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                try {
                    handleRowDoubleClick(selectedItem);
                } catch (SQLException ex) {
                    Logger.getLogger(HistoryPageUserController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    });
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
        List<HistoryTransaksi> userData = historyTransaksiDAO.getAllCompanyData(currentUserId);
        tableView.setItems(FXCollections.observableArrayList(userData));
    } catch (SQLException e) {
        showAlert(Alert.AlertType.ERROR, "Error", "Gagal memuat data transaksi: " + e.getMessage());
    }
}

private void filterData() {
    try {
        boolean withdrawChecked = checkBoxWithDraw.isSelected();
        boolean sewaChecked = checkBoxSewaAlat.isSelected();
        boolean pupukChecked = checkBoxBeliPupuk.isSelected();

        // Ambil data berdasarkan filter
        List<HistoryTransaksi> filteredData = historyTransaksiDAO.getFilteredDataCom(currentUserId,withdrawChecked, sewaChecked, pupukChecked);
        tableView.setItems(FXCollections.observableArrayList(filteredData));
    } catch (SQLException e) {
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
    
    public void showDetailTransaksiSewa(int transaksiId) throws SQLException {
    // Bikin Stage baru (popup)
    Stage detailStage = new Stage();
    detailStage.setTitle("Detail Transaksi - ID: " + transaksiId);

    // TableView buat nampilin detail transaksi
    TableView<Keranjang> detailTable = new TableView<>();
    TableColumn<Keranjang, String> colId = new TableColumn<>("ID");
    TableColumn<Keranjang, String> colAl = new TableColumn<>("Nama Alat");
    TableColumn<Keranjang, String> colPr = new TableColumn<>("Nama Perusahaan");
    TableColumn<Keranjang, String> colJm = new TableColumn<>("Jumlah");
    TableColumn<Keranjang, String> colDr = new TableColumn<>("Durasi");
    TableColumn<Keranjang, String> colTp = new TableColumn<>("Tanggal Pinjam");
    TableColumn<Keranjang, String> colTk = new TableColumn<>("Tanggal Kembali");
    TableColumn<Keranjang, String> colTh = new TableColumn<>("Harga");
    colId.setCellValueFactory(new PropertyValueFactory<>("idTransaksi"));
    colAl.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAlat().getNamaAlat()));
    colPr.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAlat().getCompany().getNama()));
    colJm.setCellValueFactory(new PropertyValueFactory<>("jumlah"));
    colDr.setCellValueFactory(new PropertyValueFactory<>("durasi"));
    colTp.setCellValueFactory(new PropertyValueFactory<>("tanggalPinjam"));
    colTk.setCellValueFactory(new PropertyValueFactory<>("tanggalKembali"));
    colTh.setCellValueFactory(new PropertyValueFactory<>("totalHarga"));
    
    detailTable.getColumns().addAll(colId, colAl, colPr, colJm, colDr, colTp, colTk,colTh);

    // Konversi List ke ObservableList
    List<Keranjang> keranjangList = historyTransaksiDAO.getDetailTransaksisewa(transaksiId);
    ObservableList<Keranjang> observableKeranjangList = FXCollections.observableArrayList(keranjangList);
    System.out.println("Keranjang list size: " + keranjangList.size()); // Cek apakah data ada
    // Set ObservableList ke TableView
    detailTable.setItems(observableKeranjangList);

    // Layout buat popup
    VBox layout = new VBox(10, detailTable);
    layout.setPadding(new javafx.geometry.Insets(10));

    // Set Scene ke Stage
    Scene scene = new Scene(layout, 500, 300);
    detailStage.setScene(scene);

    // Tampilkan Stage
    detailStage.show();
    
}
   public void showDetailTransaksiPupuk(int transaksiId) throws SQLException {
    // Bikin Stage baru (popup)
    Stage detailStage = new Stage();
    detailStage.setTitle("Detail Transaksi - ID: " + transaksiId);

    // TableView buat nampilin detail transaksi
    TableView<Keranjang> detailTable = new TableView<>();
    TableColumn<Keranjang, String> colId = new TableColumn<>("ID");
    TableColumn<Keranjang, String> colNp = new TableColumn<>("Nama Pupuk");
    TableColumn<Keranjang, String> colJp = new TableColumn<>("Jenis Pupuk");
    TableColumn<Keranjang, String> colPr = new TableColumn<>("Nama Perusahaan");
    TableColumn<Keranjang, String> colJm = new TableColumn<>("Jumlah");
    TableColumn<Keranjang, String> colTh = new TableColumn<>("Harga");
    colId.setCellValueFactory(new PropertyValueFactory<>("idTransaksi"));
    colNp.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPupuk().getNamaPupuk()));
    colJp.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPupuk().getJenisPupuk()));
    colPr.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPupuk().getCompany().getNama()));
    colJm.setCellValueFactory(new PropertyValueFactory<>("jumlah"));
    colTh.setCellValueFactory(new PropertyValueFactory<>("totalHarga"));
    
    detailTable.getColumns().addAll(colId, colNp, colJp, colPr, colJm, colTh);
    
    // Konversi List ke ObservableList
    List<Keranjang> keranjangList = historyTransaksiDAO.getDetailTransaksipupuk(transaksiId);
    ObservableList<Keranjang> observableKeranjangList = FXCollections.observableArrayList(keranjangList);
    System.out.println("Keranjang list size: " + keranjangList.size()); // Cek apakah data ada
    // Set ObservableList ke TableView
    detailTable.setItems(observableKeranjangList);

    // Layout buat popup
    VBox layout = new VBox(10, detailTable);
    layout.setPadding(new javafx.geometry.Insets(10));

    // Set Scene ke Stage
    Scene scene = new Scene(layout, 500, 300);
    detailStage.setScene(scene);

    // Tampilkan Stage
    detailStage.show();
}
   
   private void handleRowDoubleClick(HistoryTransaksi transaksi) throws SQLException {
    String jenisTransaksi = transaksi.getJenis(); // Ambil jenis transaksi
    int transaksiId = transaksi.getId(); // Ambil ID transaksi

    if (jenisTransaksi.equalsIgnoreCase("Sewa Alat")) {
        showDetailTransaksiSewa(transaksiId); // Tampilkan detail transaksi sewa
    } else if (jenisTransaksi.equalsIgnoreCase("Beli Pupuk")) {
        showDetailTransaksiPupuk(transaksiId); // Tampilkan detail transaksi pupuk
    } else {
        // Tidak melakukan apa-apa untuk jenis transaksi lainnya
        showAlert(Alert.AlertType.INFORMATION, "Info", "Tidak ada detail yang dapat ditampilkan untuk jenis transaksi ini.");
    }
}

@FXML
private void handleSearchByDate(ActionEvent event) {
    LocalDate startDate = startDatePicker.getValue();
    LocalDate endDate = endDatePicker.getValue();

    if (startDate != null && endDate != null) {
        try {
            // Ambil data transaksi yang sesuai dengan tanggal
            List<HistoryTransaksi> filteredData = historyTransaksiDAO.getFilteredDataByDate(currentUserId, startDate, endDate);
            tableView.setItems(FXCollections.observableArrayList(filteredData));
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Gagal memuat data transaksi berdasarkan tanggal: " + e.getMessage());
        }
    } else {
        showAlert(Alert.AlertType.WARNING, "Peringatan", "Silakan pilih tanggal mulai dan akhir.");
    }
  }
}

