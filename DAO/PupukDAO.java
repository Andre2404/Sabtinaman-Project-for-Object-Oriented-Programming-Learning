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
        String query = "INSERT INTO pupuk (nama_pupuk, harga_per_kg, id_perusahaan) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, pupuk.getNamaPupuk());
            statement.setDouble(2, pupuk.getHargaPerKg());
            statement.setInt(3, pupuk.getIdPerusahaan());
            statement.executeUpdate();
        }
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
                    resultSet.getDouble("harga_per_kg"),
                    resultSet.getInt("id_perusahaan")
                );
                pupukList.add(pupuk);
            }
        }
        return pupukList;
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
                    resultSet.getDouble("harga_per_kg"),
                    resultSet.getInt("id_perusahaan")
                );
            }
        }
        return null;
    }
}
