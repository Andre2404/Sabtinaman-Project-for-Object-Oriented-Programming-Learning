package dao;

import model.AlatPertanian;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AlatPertanianDAO {

    private Connection connection;

    public AlatPertanianDAO(Connection connection) {
        this.connection = connection;
    }

    // Insert data alat pertanian ke database
    public void addAlatPertanian(AlatPertanian alat) throws SQLException {
        String query = "INSERT INTO alat_pertanian (id_alat, nama_alat, kategori, spesifikasi, harga_sewa, status_ketersediaan, gambar) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, alat.getIdAlat());
            statement.setString(2, alat.getNamaAlat());
            statement.setString(3, alat.getKategori());
            statement.setString(4, alat.getSpesifikasi());
            statement.setInt(5, alat.getHargaSewa());
            statement.setString(6, alat.getStatusKetersediaan());
            statement.setString(7, alat.getGambar());
            statement.executeUpdate();
        }
    }

    // Mendapatkan data alat pertanian berdasarkan ID
    public AlatPertanian getAlatPertanianById(int idAlat) throws SQLException {
        String query = "SELECT * FROM alat_pertanian WHERE id_alat = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, idAlat);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new AlatPertanian(
                    resultSet.getInt("id_alat"),
                    resultSet.getString("nama_alat"),
                    resultSet.getString("kategori"),
                    resultSet.getString("spesifikasi"),
                    resultSet.getInt("harga_sewa"),
                    resultSet.getString("status_ketersediaan"),
                    resultSet.getString("gambar")
                );
            }
        }
        return null;
    }

    // Mendapatkan semua data alat pertanian
    public List<AlatPertanian> getAllAlatPertanian() throws SQLException {
        List<AlatPertanian> alatList = new ArrayList<>();
        String query = "SELECT * FROM alat_pertanian";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                AlatPertanian alat = new AlatPertanian(
                    resultSet.getInt("id_alat"),
                    resultSet.getString("nama_alat"),
                    resultSet.getString("kategori"),
                    resultSet.getString("spesifikasi"),
                    resultSet.getInt("harga_sewa"),
                    resultSet.getString("status_ketersediaan"),
                    resultSet.getString("gambar")
                );
                alatList.add(alat);
            }
        }
        return alatList;
    }

    // Update data alat pertanian
    public void updateAlatPertanian(AlatPertanian alat) throws SQLException {
        String query = "UPDATE alat_pertanian SET nama_alat = ?, kategori = ?, spesifikasi = ?, harga_sewa = ?, "
                + "status_ketersediaan = ?, gambar = ? WHERE id_alat = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, alat.getNamaAlat());
            statement.setString(2, alat.getKategori());
            statement.setString(3, alat.getSpesifikasi());
            statement.setInt(4, alat.getHargaSewa());
            statement.setString(5, alat.getStatusKetersediaan());
            statement.setString(6, alat.getGambar());
            statement.setInt(7, alat.getIdAlat());
            statement.executeUpdate();
        }
    }

    // Hapus data alat pertanian berdasarkan ID
    public void deleteAlatPertanian(int idAlat) throws SQLException {
        String query = "DELETE FROM alat_pertanian WHERE id_alat = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, idAlat);
            statement.executeUpdate();
        }
    }
}