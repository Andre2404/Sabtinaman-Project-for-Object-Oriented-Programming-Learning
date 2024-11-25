package dao;

import model.Pupuk;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import model.Perusahaan;

public class PupukDAO {
    private Connection connection;
    private PerusahaanDAO perusahaanDAO;
    
    public PupukDAO(Connection connection){
        this.connection = connection;
    }
            
    public PupukDAO(Connection connection, PerusahaanDAO perusahaanDAO) {
        this.connection = connection;
        this.perusahaanDAO = perusahaanDAO;
    }

    // Menambahkan pupuk baru ke database
    public void addPupuk(Pupuk pupuk) throws SQLException {
        String query = "INSERT INTO pupuk (nama_pupuk, harga_per_kg, id_perusahaan) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, pupuk.getNamaPupuk());
            statement.setDouble(2, pupuk.getHargaPerKg());
            statement.setObject(3, pupuk.getCompany().getIdPerusahaan());
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
                Perusahaan perusahaan = perusahaanDAO.getPerusahaanById(resultSet.getInt("id_perusahaan"));
                Pupuk pupuk = new Pupuk(
                    resultSet.getInt("id_pupuk"),
                    resultSet.getString("nama_pupuk"),
                    resultSet.getDouble("harga_per_kg"),
                    perusahaan
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
                Perusahaan perusahaan = perusahaanDAO.getPerusahaanById(resultSet.getInt("id_perusahaan"));
                return new Pupuk(
                    resultSet.getInt("id_pupuk"),
                    resultSet.getString("nama_pupuk"),
                    resultSet.getDouble("harga_per_kg"),
                    perusahaan
                );
            }
        }
        return null;
    }
}
