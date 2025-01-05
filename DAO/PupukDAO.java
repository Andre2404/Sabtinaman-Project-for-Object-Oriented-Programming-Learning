package DAO;

import model.Pupuk;
import model.Perusahaan;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PupukDAO {
    private Connection connection;
    private PerusahaanDAO perusahaanDAO;

    // Constructor pertama (default)
    public PupukDAO(Connection connection) {
        this(connection, new PerusahaanDAO(connection)); // Memanggil konstruktor kedua
    }

    // Constructor kedua (dengan parameter perusahaanDAO)
    public PupukDAO(Connection connection, PerusahaanDAO perusahaanDAO) {
        this.connection = connection;
        this.perusahaanDAO = perusahaanDAO;
    }

    // Menambahkan pupuk baru ke database
    public void addPupuk(Pupuk pupuk) throws SQLException {
        String query = "INSERT INTO pupuk (nama_pupuk, harga_per_kg, id_perusahaan) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, pupuk.getNamaPupuk());
            statement.setInt(2, pupuk.getHargaPerKg());
            statement.setObject(3, pupuk.getCompany().getIdPerusahaan());
            statement.executeUpdate();
        }
    }

    // Mengambil semua pupuk dari database
    
    public List<Pupuk> getAllPupuk() throws SQLException {
    List<Pupuk> pupuklist = new ArrayList<>();
    String query = "SELECT * FROM pupuk";
    try (Statement statement = connection.createStatement();
         ResultSet resultSet = statement.executeQuery(query)) {
        while (resultSet.next()) {
            if (perusahaanDAO == null) {
                throw new IllegalStateException("PerusahaanDAO belum diinisialisasi.");
            }
            Perusahaan perusahaan = perusahaanDAO.getPerusahaanById(resultSet.getInt("id_perusahaan"));
            Pupuk pupuk = new Pupuk(
                resultSet.getInt("id_pupuk"),
                resultSet.getString("nama_pupuk"),
                resultSet.getInt("harga_per_kg"), 
                resultSet.getInt("stok"),
                resultSet.getString("jenis_pupuk"),
                resultSet.getString("spesifikasi"),
                perusahaan,
                resultSet.getString("imagehash") // Ambil imageHash dari ResultSet
            );
            pupuklist.add(pupuk);
        }
    }
    return pupuklist;
}
    
    public void deletePupuk(int idPupuk) throws SQLException {
    String query = "DELETE FROM pupuk WHERE id_pupuk = ?";
    try (PreparedStatement statement = connection.prepareStatement(query)) {
        statement.setInt(1, idPupuk);
        statement.executeUpdate();
    }
}

    public List<Pupuk> getAvailablePupuk() throws SQLException {
    List<Pupuk> pupukList = new ArrayList<>();
    String sql = "SELECT * FROM pupuk WHERE status = 'available'";
    
    if (perusahaanDAO == null) {
        throw new IllegalStateException("PerusahaanDAO tidak diinisialisasi!");
    }
    
    try (Statement statement = connection.createStatement();
         ResultSet resultSet = statement.executeQuery(sql)) {
        while (resultSet.next()) {
            Perusahaan perusahaan = perusahaanDAO.getPerusahaanById(resultSet.getInt("id_perusahaan"));
            pupukList.add(new Pupuk(
                resultSet.getInt("id_pupuk"),
                resultSet.getString("nama_pupuk"),
                resultSet.getInt("harga_per_kg"), 
                resultSet.getInt("stok"),
                resultSet.getString("jenis_pupuk"),
                resultSet.getString("spesifikasi"),
                perusahaan,
                resultSet.getString("imagehash") // Ambil imageHash dari ResultSet
            ));
        }
    }
    return pupukList;
}
    
    public void updatePupuk(Pupuk pupuk) throws SQLException {
    String query = "UPDATE pupuk SET nama_pupuk = ?, harga_per_kg = ?, stok = ?, jenis_pupuk = ?, spesifikasi = ?, id_perusahaan = ?, imagehash = ? WHERE id_pupuk = ?";
    try (PreparedStatement statement = connection.prepareStatement(query)) {
        statement.setString(1, pupuk.getNamaPupuk());
        statement.setInt(2, pupuk.getHargaPerKg());
        statement.setInt(3, pupuk.getStok());
        statement.setString(4, pupuk.getJenisPupuk());
        statement.setString(5, pupuk.getSpesifikasi());
        statement.setInt(6, pupuk.getCompany().getIdPerusahaan());
        statement.setString(7, pupuk.getImageHash());
        statement.setInt(8, pupuk.getIdPupuk()); // ID yang akan diperbarui

        statement.executeUpdate();
    }
}
    
    // Method untuk menambah stok pupuk
    public void updateStokPupuk(int idPupuk, int tambahanStok) throws SQLException {
    String query = "UPDATE pupuk SET stok = stok + ? WHERE id_pupuk = ?";
    try (PreparedStatement statement = connection.prepareStatement(query)) {
        statement.setInt(1, tambahanStok); // Menambahkan stok
        statement.setInt(2, idPupuk); // ID pupuk yang akan diperbarui
        statement.executeUpdate(); // Menjalankan pernyataan update
    }
}
    
    public void updateStatusPupuk(int idPupuk, String status){
       String sql = "UPDATE pupuk SET status = ? WHERE id_pupuk = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
        stmt.setString(1, status);
        stmt.setInt(2, idPupuk);
        stmt.executeUpdate();
    } catch (SQLException ex) {
        Logger.getLogger(AlatDAO.class.getName()).log(Level.SEVERE, null, ex);
    }
   }
    
    // Mengambil pupuk berdasarkan ID
    public Pupuk getPupukById(int idPupuk) throws SQLException {
    String query = "SELECT * FROM pupuk WHERE id_pupuk = ?";
    try (PreparedStatement statement = connection.prepareStatement(query)) {
        statement.setInt(1, idPupuk);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            if (perusahaanDAO == null) {
                throw new IllegalStateException("PerusahaanDAO belum diinisialisasi.");
            }
            Perusahaan perusahaan = perusahaanDAO.getPerusahaanById(resultSet.getInt("id_perusahaan"));
            return new Pupuk(
                resultSet.getInt("id_pupuk"),
                resultSet.getString("nama_pupuk"),
                resultSet.getInt("harga_per_kg"), 
                resultSet.getInt("stok"),
                resultSet.getString("jenis_pupuk"),
                resultSet.getString("spesifikasi"),
                perusahaan,
                resultSet.getString("imagehash") // Ambil imageHash dari ResultSet
            );
        }
    }
    return null; // Jika tidak ditemukan, kembalikan null
}
}
