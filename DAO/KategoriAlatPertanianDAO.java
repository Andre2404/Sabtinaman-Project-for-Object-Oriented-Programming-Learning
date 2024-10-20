package dao;

import model.KategoriAlatPertanian;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class KategoriAlatPertanianDAO {

    private Connection connection;

    public KategoriAlatPertanianDAO(Connection connection) {
        this.connection = connection;
    }

    // Menambahkan kategori alat pertanian baru ke database
    public void addKategoriAlatPertanian(KategoriAlatPertanian kategori) throws SQLException {
        String query = "INSERT INTO kategori_alat_pertanian (id_kategori_alat, id_alat_pertanian, nama_kategori, deskripsi) "
                + "VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, kategori.getIdKategoriAlat());
            statement.setInt(2, kategori.getIdAlatPertanian());
            statement.setString(3, kategori.getNamaKategori());
            statement.setString(4, kategori.getDeskripsi());
            statement.executeUpdate();
        }
    }

    // Mengambil data kategori alat pertanian berdasarkan ID
    public KategoriAlatPertanian getKategoriAlatPertanianById(int idKategoriAlat) throws SQLException {
        String query = "SELECT * FROM kategori_alat_pertanian WHERE id_kategori_alat = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, idKategoriAlat);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new KategoriAlatPertanian(
                    resultSet.getInt("id_kategori_alat"),
                    resultSet.getInt("id_alat_pertanian"),
                    resultSet.getString("nama_kategori"),
                    resultSet.getString("deskripsi")
                );
            }
        }
        return null;
    }

    // Mengambil semua data kategori alat pertanian
    public List<KategoriAlatPertanian> getAllKategoriAlatPertanian() throws SQLException {
        List<KategoriAlatPertanian> kategoriList = new ArrayList<>();
        String query = "SELECT * FROM kategori_alat_pertanian";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                KategoriAlatPertanian kategori = new KategoriAlatPertanian(
                    resultSet.getInt("id_kategori_alat"),
                    resultSet.getInt("id_alat_pertanian"),
                    resultSet.getString("nama_kategori"),
                    resultSet.getString("deskripsi")
                );
                kategoriList.add(kategori);
            }
        }
        return kategoriList;
    }

    // Update data kategori alat pertanian
    public void updateKategoriAlatPertanian(KategoriAlatPertanian kategori) throws SQLException {
        String query = "UPDATE kategori_alat_pertanian SET id_alat_pertanian = ?, nama_kategori = ?, deskripsi = ? "
                + "WHERE id_kategori_alat = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, kategori.getIdAlatPertanian());
            statement.setString(2, kategori.getNamaKategori());
            statement.setString(3, kategori.getDeskripsi());
            statement.setInt(4, kategori.getIdKategoriAlat());
            statement.executeUpdate();
        }
    }

    // Hapus kategori alat pertanian berdasarkan ID
    public void deleteKategoriAlatPertanian(int idKategoriAlat) throws SQLException {
        String query = "DELETE FROM kategori_alat_pertanian WHERE id_kategori_alat = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, idKategoriAlat);
            statement.executeUpdate();
        }
    }
}