package dao;

import model.Pupuk;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PupukDAO {

    private Connection connection;

    public PupukDAO(Connection connection) {
        this.connection = connection;
    }

    // Menambahkan pupuk baru ke database
    public void addPupuk(Pupuk pupuk) throws SQLException {
        String query = "INSERT INTO pupuk (id_pupuk, nama_pupuk, kategori, harga, stok) "
                + "VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, pupuk.getIdPupuk());
            statement.setString(2, pupuk.getNamaPupuk());
            statement.setString(3, pupuk.getKategori());
            statement.setInt(4, pupuk.getHarga());
            statement.setInt(5, pupuk.getStok());
            statement.executeUpdate();
        }
    }

    // Mengambil pupuk berdasarkan ID
    public Pupuk getPupukById(int idPupuk) throws SQLException {
        String query = "SELECT * FROM pupuk WHERE id_pupuk = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, idPupuk);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Pupuk(
                    resultSet.getInt("id_pupuk"),
                    resultSet.getString("nama_pupuk"),
                    resultSet.getString("kategori"),
                    resultSet.getInt("harga"),
                    resultSet.getInt("stok")
                );
            }
        }
        return null;
    }

    // Mengambil semua pupuk dari database
    public List<Pupuk> getAllPupuk() throws SQLException {
        List<Pupuk> pupukList = new ArrayList<>();
        String query = "SELECT * FROM pupuk";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                Pupuk pupuk = new Pupuk(
                    resultSet.getInt("id_pupuk"),
                    resultSet.getString("nama_pupuk"),
                    resultSet.getString("kategori"),
                    resultSet.getInt("harga"),
                    resultSet.getInt("stok")
                );
                pupukList.add(pupuk);
            }
        }
        return pupukList;
    }

    // Mengupdate data pupuk
    public void updatePupuk(Pupuk pupuk) throws SQLException {
        String query = "UPDATE pupuk SET nama_pupuk = ?, kategori = ?, harga = ?, stok = ? "
                + "WHERE id_pupuk = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, pupuk.getNamaPupuk());
            statement.setString(2, pupuk.getKategori());
            statement.setInt(3, pupuk.getHarga());
            statement.setInt(4, pupuk.getStok());
            statement.setInt(5, pupuk.getIdPupuk());
            statement.executeUpdate();
        }
    }

    // Menghapus pupuk berdasarkan ID
    public void deletePupuk(int idPupuk) throws SQLException {
        String query = "DELETE FROM pupuk WHERE id_pupuk = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, idPupuk);
            statement.executeUpdate();
        }
    }
}
