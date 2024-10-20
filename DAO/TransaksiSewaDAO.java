package dao;

import model.TransaksiSewa;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransaksiSewaDAO {

    private Connection connection;

    public TransaksiSewaDAO(Connection connection) {
        this.connection = connection;
    }

    // Menambahkan transaksi sewa baru ke database
    public void addTransaksiSewa(TransaksiSewa transaksi) throws SQLException {
        String query = "INSERT INTO transaksi_sewa (id_transaksi, id_pengguna, id_alat, tanggal_mulai_sewa, tanggal_akhir_sewa, status_pembayaran) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, transaksi.getIdTransaksi());
            statement.setInt(2, transaksi.getIdPengguna());
            statement.setInt(3, transaksi.getIdAlat());
            statement.setInt(4, transaksi.getTanggalMulaiSewa());
            statement.setInt(5, transaksi.getTanggalAkhirSewa());
            statement.setString(6, transaksi.getStatusPembayaran());
            statement.executeUpdate();
        }
    }

    // Mengambil transaksi sewa berdasarkan ID transaksi
    public TransaksiSewa getTransaksiSewaById(int idTransaksi) throws SQLException {
        String query = "SELECT * FROM transaksi_sewa WHERE id_transaksi = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, idTransaksi);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new TransaksiSewa(
                    resultSet.getInt("id_transaksi"),
                    resultSet.getInt("id_pengguna"),
                    resultSet.getInt("id_alat"),
                    resultSet.getInt("tanggal_mulai_sewa"),
                    resultSet.getInt("tanggal_akhir_sewa"),
                    resultSet.getString("status_pembayaran")
                );
            }
        }
        return null;
    }

    // Mengambil semua transaksi sewa dari database
    public List<TransaksiSewa> getAllTransaksiSewa() throws SQLException {
        List<TransaksiSewa> transaksiList = new ArrayList<>();
        String query = "SELECT * FROM transaksi_sewa";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                TransaksiSewa transaksi = new TransaksiSewa(
                    resultSet.getInt("id_transaksi"),
                    resultSet.getInt("id_pengguna"),
                    resultSet.getInt("id_alat"),
                    resultSet.getInt("tanggal_mulai_sewa"),
                    resultSet.getInt("tanggal_akhir_sewa"),
                    resultSet.getString("status_pembayaran")
                );
                transaksiList.add(transaksi);
            }
        }
        return transaksiList;
    }

    // Mengupdate transaksi sewa
    public void updateTransaksiSewa(TransaksiSewa transaksi) throws SQLException {
        String query = "UPDATE transaksi_sewa SET id_pengguna = ?, id_alat = ?, tanggal_mulai_sewa = ?, tanggal_akhir_sewa = ?, status_pembayaran = ? "
                + "WHERE id_transaksi = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, transaksi.getIdPengguna());
            statement.setInt(2, transaksi.getIdAlat());
            statement.setInt(3, transaksi.getTanggalMulaiSewa());
            statement.setInt(4, transaksi.getTanggalAkhirSewa());
            statement.setString(5, transaksi.getStatusPembayaran());
            statement.setInt(6, transaksi.getIdTransaksi());
            statement.executeUpdate();
        }
    }

    // Menghapus transaksi sewa berdasarkan ID transaksi
    public void deleteTransaksiSewa(int idTransaksi) throws SQLException {
        String query = "DELETE FROM transaksi_sewa WHERE id_transaksi = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, idTransaksi);
            statement.executeUpdate();
        }
    }
}
