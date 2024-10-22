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

    // Menambah transaksi sewa alat
    public void sewaAlat(int userId, int alatId) throws SQLException {
        String query = "INSERT INTO transaksisewa (id_pengguna, id_alat, tanggal_sewa) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, alatId);
            stmt.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            stmt.executeUpdate();
        }
    }

    // Mengambil transaksi sewa alat berdasarkan id pengguna
    public List<TransaksiSewa> getTransaksiByUserId(int userId) throws SQLException {
        String query = "SELECT * FROM transaksi_sewa WHERE id_pengguna = ?";
        List<TransaksiSewa> transaksiList = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                transaksiList.add(new TransaksiSewa(
                    rs.getInt("id_transaksi"),
                    rs.getInt("id_pengguna"),
                    rs.getInt("id_alat"),
                    rs.getTimestamp("tanggal_sewa")
                ));
            }
        }
        return transaksiList;
    }

    // Mengambil transaksi sewa alat berdasarkan id perusahaan
    public List<TransaksiSewa> getTransaksiByCompanyId(int companyId) throws SQLException {
        String query = "SELECT ts.* FROM transaksi_sewa ts JOIN alat a ON ts.id_alat = a.id_alat WHERE a.id_perusahaan = ?";
        List<TransaksiSewa> transaksiList = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, companyId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                transaksiList.add(new TransaksiSewa(
                    rs.getInt("id_transaksi"),
                    rs.getInt("id_pengguna"),
                    rs.getInt("id_alat"),
                    rs.getTimestamp("tanggal_sewa")
                ));
            }
        }
        return transaksiList;
    }
}
