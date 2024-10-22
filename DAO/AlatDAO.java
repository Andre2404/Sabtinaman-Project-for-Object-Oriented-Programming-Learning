package dao;

import model.Alat;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AlatDAO {
    private Connection connection;

    public AlatDAO(Connection connection) {
        this.connection = connection;
    }

    // Method untuk menambahkan alat baru
    public void addAlat(Alat alat) throws SQLException {
        String sql = "INSERT INTO Alat (nama_alat, spesifikasi, harga_sewa, id_perusahaan) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, alat.getNamaAlat());
            stmt.setString(2, alat.getSpesifikasi());
            stmt.setDouble(3, alat.getHargaSewa());
            stmt.setInt(4, alat.getIdPerusahaan());
            stmt.executeUpdate();
        }
    }

    // Method untuk mendapatkan daftar semua alat
    public List<Alat> getAllAlat() throws SQLException {
        List<Alat> alatList = new ArrayList<>();
        String sql = "SELECT * FROM Alat";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Alat alat = new Alat(
                    rs.getInt("id_alat"),
                    rs.getString("nama_alat"),
                        rs.getInt("id_perusahaan"),
                    rs.getDouble("harga_sewa"));
                alatList.add(alat);
            }
        }
        return alatList;
    }

    // Method untuk mendapatkan alat berdasarkan nama
    public Alat getAlatByName(String namaAlat) throws SQLException {
        String sql = "SELECT * FROM Alat WHERE nama_alat = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, namaAlat);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Alat(
                        rs.getInt("id_alat"),
                        rs.getString("nama_alat"),
                            rs.getInt("id_perusahaan"),
                        rs.getDouble("harga_sewa"));
                }
            }
        }
        return null;  // Return null jika alat tidak ditemukan
    }

    // Method untuk mendapatkan alat berdasarkan id
    public Alat getAlatById(int idAlat) throws SQLException {
        String sql = "SELECT * FROM Alat WHERE id_alat = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idAlat);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Alat(
                        rs.getInt("id_alat"),
                        rs.getString("nama_alat"),
                            rs.getInt("id_perusahaan"),
                        rs.getDouble("harga_sewa"));
                }
            }
        }
        return null;
    }
}
