package dao;

import model.TransaksiPupuk;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransaksiPupukDAO {

    private Connection connection;

    public TransaksiPupukDAO(Connection connection) {
        this.connection = connection;
    }

    // Menambahkan transaksi pupuk baru ke database
    public void addTransaksiPupuk(TransaksiPupuk transaksi) throws SQLException {
        String query = "INSERT INTO transaksi_pupuk (id_transaksi, id_pengguna, id_pupuk, tanggal_mulai_sewa, tanggal_akhir_sewa, status_pembayaran) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, transaksi.getIdTransaksi());
            statement.setInt(2, transaksi.getIdPengguna());
            statement.setInt(3, transaksi.getIdPupuk());
            statement.setInt(4, transaksi.getTanggalMulaiSewa());
            statement.setInt(5, transaksi.getTanggalAkhirSewa());
            statement.setString(6, transaksi.getStatusPembayaran());
            statement.executeUpdate();
        }
    }

    // Mengambil transaksi pupuk berdasarkan ID transaksi
    public TransaksiPupuk getTransaksiPupukById(int idTransaksi) throws SQLException {
        String query = "SELECT * FROM transaksi_pupuk WHERE id_transaksi = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, idTransaksi);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new TransaksiPupuk(
                    resultSet.getInt("id_transaksi"),
                    resultSet.getInt("id_pengguna"),
                    resultSet.getInt("id_pupuk"),
                    resultSet.getInt("tanggal_mulai_sewa"),
                    resultSet.getInt("tanggal_akhir_sewa"),
                    resultSet.getString("status_pembayaran")
                );
            }
        }
        return null;
    }

    // Mengambil semua transaksi pupuk dari database
    public List<TransaksiPupuk> getAllTransaksiPupuk() throws SQLException {
        List<TransaksiPupuk> transaksiList = new ArrayList<>();
        String query = "SELECT * FROM transaksi_pupuk";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                TransaksiPupuk transaksi = new TransaksiPupuk(
                    resultSet.getInt("id_transaksi"),
                    resultSet.getInt("id_pengguna"),
                    resultSet.getInt("id_pupuk"),
                    resultSet.getInt("tanggal_mulai_sewa"),
                    resultSet.getInt("tanggal_akhir_sewa"),
                    resultSet.getString("status_pembayaran")
                );
                transaksiList.add(transaksi);
            }
        }
        return transaksiList;
    }

    // Mengupdate transaksi pupuk
    public void updateTransaksiPupuk(TransaksiPupuk transaksi) throws SQLException {
        String query = "UPDATE transaksi_pupuk SET id_pengguna = ?, id_pupuk = ?, tanggal_mulai_sewa = ?, tanggal_akhir_sewa = ?, status_pembayaran = ? "
                + "WHERE id_transaksi = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, transaksi.getIdPengguna());
            statement.setInt(2, transaksi.getIdPupuk());
            statement.setInt(3, transaksi.getTanggalMulaiSewa());
            statement.setInt(4, transaksi.getTanggalAkhirSewa());
            statement.setString(5, transaksi.getStatusPembayaran());
            statement.setInt(6, transaksi.getIdTransaksi());
            statement.executeUpdate();
        }
    }

    // Menghapus transaksi pupuk berdasarkan ID transaksi
    public void deleteTransaksiPupuk(int idTransaksi) throws SQLException {
        String query = "DELETE FROM transaksi_pupuk WHERE id_transaksi = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, idTransaksi);
            statement.executeUpdate();
        }
    }
}