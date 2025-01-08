package DAO;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import model.Alat;
import model.Perusahaan;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AlatDAO {
    private Connection connection;
    private static PreparedStatement st;
    private PerusahaanDAO perusahaanDAO;

    public AlatDAO(Connection connection, PerusahaanDAO perusahaanDAO) {
    this.connection = connection;
    this.perusahaanDAO = perusahaanDAO; // Gunakan objek yang sudah diberikan
}
    public AlatDAO(Connection connection) {
        this.connection = connection;
    }
   
        // Method untuk menambahkan alat baru
       public void addAlat(Alat alat) throws SQLException {
        String query = "INSERT INTO alat (nama_alat, spesifikasi, id_perusahaan, harga_sewa, status, stok, imagehash) " +
                       "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, alat.getNamaAlat());
            statement.setString(2, alat.getSpesifikasi());
            statement.setInt(3, alat.getCompany().getIdPerusahaan());
            statement.setDouble(4, alat.getHargaSewa());
            statement.setString(5, alat.getStatus());
            statement.setInt(6, alat.getStok());
            statement.setString(7, alat.getImageHash()); // Upload gambar
            statement.executeUpdate();
        }
    }
       

//       public void addAlat(Alat alat) throws SQLException {
//    String query = "INSERT INTO alat (nama_alat, spesifikasi, id_perusahaan, harga_sewa, status, stok, imagehash) " +
//                   "VALUES (?, ?, ?, ?, ?, ?, ?)";
//    try (PreparedStatement statement = connection.prepareStatement(query)) {
//        statement.setString(1, alat.getNamaAlat());
//        statement.setString(2, alat.getSpesifikasi());
//        statement.setInt(3, alat.getCompany().getIdPerusahaan());
//        statement.setDouble(4, alat.getHargaSewa());
//        statement.setString(5, alat.getStatus());
//        statement.setInt(6, alat.getStok());
//
//        // Store the image as BLOB
//        statement.setBytes(7, alat.getImage()); // Store the image bytes as BLOB
//
//        statement.executeUpdate();
//    }
//}
       
       // Method untuk menghapus alat berdasarkan ID
    public boolean deleteAlat(int idAlat) throws SQLException {
        String query = "DELETE FROM alat WHERE id_alat = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, idAlat);
            int affectedRows = preparedStatement.executeUpdate();
            return affectedRows > 0; // Mengembalikan true jika ada baris yang terhapus
        }
    }
    
    // Method untuk menambah stok alat
    
    public void updateStokAlat(int idAlat, int tambahanStok) throws SQLException {
        String query = "UPDATE alat SET stok = ? WHERE id_alat = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, tambahanStok);
            statement.setInt(2, idAlat);
            statement.executeUpdate();
        }
    }
    
    // Method untuk mendapatkan daftar semua alat
    public List<Alat> getAllAlat() throws SQLException {
        List<Alat> alatList = new ArrayList<>();
        String sql = "SELECT a.id_alat, a.nama_alat, a.spesifikasi, a.harga_sewa, a.status, a.stok, a.imagehash, " +
                     "p.id_perusahaan, p.nama, p.alamat_perusahaan, p.email, p.nomor_kontak, p.saldo_perusahaan " +
                     "FROM alat a " +
                     "JOIN perusahaan p ON a.id_perusahaan = p.id_perusahaan";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int idAlat = rs.getInt("id_alat");
                String spesifikasi = rs.getString("spesifikasi");
                String namaAlat = rs.getString("nama_alat");
                int hargaSewa = rs.getInt("harga_sewa");
                String status = rs.getString("status");
                int stok = rs.getInt("stok");
                String imageHash = rs.getString("imageHash");

                int idPerusahaan = rs.getInt("id_perusahaan");
                String nama = rs.getString("nama");
                String alamat = rs.getString("alamat_perusahaan");
                String email = rs.getString("email");
                int nomorKontak = rs.getInt("nomor_kontak");
                int saldoPerusahaan = rs.getInt("saldo_perusahaan");

                Perusahaan company = new Perusahaan(idPerusahaan, nama, alamat, email, nomorKontak, saldoPerusahaan);
                Alat alat = new Alat(idAlat, namaAlat, spesifikasi, company, hargaSewa, status, stok, imageHash);
                alatList.add(alat);
            }
        }
        return alatList;
    }
    
    public String getStatusAlat(int idAlat) throws SQLException {
    String query = "SELECT status FROM alat WHERE id_alat = ?";
    try (PreparedStatement stmt = connection.prepareStatement(query)) {
        stmt.setInt(1, idAlat);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return rs.getString("status");
        }
    }
    return null; // Jika alat tidak ditemukan
}

   // Method untuk mendapatkan alat berdasarkan nama
public Alat getAlatByName(String namaAlat) throws SQLException {
    String sql = "SELECT * FROM alat WHERE nama_alat = ?";

    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
        stmt.setString(1, namaAlat); // Hindari SQL Injection
        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                // Buat objek Perusahaan dari hasil query
                Perusahaan perusahaan = perusahaanDAO.getPerusahaanById(rs.getInt("id_perusahaan"));
                String imageHash = rs.getString("imageHash"); // Initialize gambar from ResultSet
                // Buat objek Alat menggunakan objek Perusahaan
                return new Alat(
                    rs.getInt("id_alat"),
                    rs.getString("nama_alat"),
                    rs.getString("spesifikasi"),
                    perusahaan,
                    rs.getInt("harga_sewa"),
                    rs.getString("status"),
                    rs.getInt("stok"), // Assuming you have a stok field
                    rs.getString("imageHash") // Pass gambar to the Alat constructor
                );
            }
        }
    }
    return null; 
}

    public List<Alat> getAvailableAlat() throws SQLException {
    List<Alat> alatList = new ArrayList<>();
    String sql = "SELECT * FROM alat WHERE stok > 0 ";
    if (perusahaanDAO == null) {
        throw new IllegalStateException("PerusahaanDAO tidak diinisialisasi!");
    }
    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
        try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                // Buat objek Perusahaan
                Perusahaan perusahaan = perusahaanDAO.getPerusahaanById(rs.getInt("id_perusahaan"));
                String imageHash = rs.getString("imageHash"); // Initialize gambar from ResultSet
                // Buat objek Alat dan tambahkan ke list
                alatList.add(new Alat(
                    rs.getInt("id_alat"),
                    rs.getString("nama_alat"),
                    rs.getString("spesifikasi"),
                    perusahaan,
                    rs.getInt("harga_sewa"),
                    rs.getString("status"),
                    rs.getInt("stok"), // Assuming you have a stok field
                    rs.getString("imageHash") // Pass gambar to the Alat constructor
                ));
            }
        }
    }
    return alatList;
}

   public void updateStatusAlat(int idAlat, String status){
       String sql = "UPDATE alat SET status = ? WHERE id_alat = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
        stmt.setString(1, status);
        stmt.setInt(2, idAlat);
        stmt.executeUpdate();
    } catch (SQLException ex) {
        Logger.getLogger(AlatDAO.class.getName()).log(Level.SEVERE, null, ex);
    }
   }
   public Alat getAlatById(int idAlat) throws SQLException {
    String sql = "SELECT * FROM alat WHERE id_alat = ?";
    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
        stmt.setInt(1, idAlat); // Gunakan parameter untuk menghindari SQL Injection
        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                if (perusahaanDAO == null) {
                    throw new IllegalStateException("PerusahaanDAO belum diinisialisasi.");
                }
                Perusahaan perusahaan = perusahaanDAO.getPerusahaanById(rs.getInt("id_perusahaan"));
                String imageHash = rs.getString("imageHash"); // Initialize gambar from ResultSet
                return new Alat(
                    rs.getInt("id_alat"),
                    rs.getString("nama_alat"),
                    rs.getString("spesifikasi"),
                    perusahaan,
                    rs.getInt("harga_sewa"),
                    rs.getString("status"),
                    rs.getInt("stok"), // Assuming you have a stok field
                    rs.getString ("imageHash") // Pass gambar to the Alat constructor
                );
            }
        }
    }
    return null; // Return null jika alat tidak ditemukan
    }
}