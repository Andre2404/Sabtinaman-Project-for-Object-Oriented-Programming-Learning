package dao;

import model.Alat;
import model.Perusahaan;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AlatDAO {
    private Connection connection;
    private static PreparedStatement st;
    private PerusahaanDAO perusahaanDAO;

    public AlatDAO(Connection connection, PerusahaanDAO perusahaanDAO) {
        this.connection = connection;
        this.perusahaanDAO = perusahaanDAO;
    }

    // Method untuk menambahkan alat baru
    public void addAlat(Alat alat) throws SQLException {
        String sql = "INSERT INTO alat (nama_alat, spesifikasi, harga_sewa, id_perusahaan) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, alat.getNamaAlat());
            stmt.setString(2, alat.getSpesifikasi());
            stmt.setDouble(3, alat.getHargaSewa());
            stmt.setInt(4, alat.getCompany().getIdPerusahaan());
            stmt.executeUpdate();
        }
    }
    // Method untuk mendapatkan daftar semua alat
    public List<Alat> getAllAlat() throws SQLException {
    List<Alat> alatList = new ArrayList<>();
    String sql = "SELECT a.id_alat, a.nama_alat, a.spesifikasi, a.harga_sewa, p.id_perusahaan, p.nama, p.alamat_perusahaan, p.email, p.nomor_kontak, p.saldo_perusahaan " +
                 "FROM alat a " +
                 "JOIN perusahaan p ON a.id_perusahaan = p.id_perusahaan ";
    try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
        while (rs.next()) {
            int idAlat = rs.getInt("id_alat");
            String spesifikasi = rs.getString("spesifikasi");
            String namaAlat = rs.getString("nama_alat");
            int hargaSewa = rs.getInt("harga_sewa");

            // Buat objek Perusahaan
            int idPerusahaan = rs.getInt("id_perusahaan");
            String nama = rs.getString("nama");
            String alamat = rs.getString("alamat_perusahaan");
            String email = rs.getString("email");
            int nomorKontak = rs.getInt("nomor_kontak");
            int saldoPerusahaan = rs.getInt("saldo_perusahaan");
            Perusahaan company = new Perusahaan(idPerusahaan, nama, alamat, email, nomorKontak,saldoPerusahaan);

            // Masukkan data ke objek Alat
            Alat alat = new Alat(idAlat, namaAlat, spesifikasi, company, hargaSewa);
            alatList.add(alat);
        }
    }
    return alatList;
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
                // Buat objek Alat menggunakan objek Perusahaan
                return new Alat(
                    rs.getInt("id_alat"),
                    rs.getString("nama_alat"),
                    rs.getString("spesifikasi"),
                    perusahaan,
                    rs.getInt("harga_sewa")
                );
            }
        }
    }
    return null; // Return null jika alat tidak ditemukan
}



    // Method untuk mendapatkan alat berdasarkan id
   
   public Alat getAlatById(int idAlat) throws SQLException {
    String sql = "SELECT * FROM Alat WHERE id_alat = ?";
    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
        stmt.setInt(1, idAlat); // Gunakan parameter untuk menghindari SQL Injection
        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                // Buat objek Perusahaan dari hasil query
                Perusahaan perusahaan = perusahaanDAO.getPerusahaanById(rs.getInt("id_perusahaan"));
                // Buat objek Alat menggunakan objek Perusahaan
                return new Alat(
                    rs.getInt("id_alat"),
                    rs.getString("nama_alat"),
                    rs.getString("spesifikasi"),
                    perusahaan,
                    rs.getInt("harga_sewa")
                );
            }
        }
    }
    return null; // Return null jika alat tidak ditemukan
}
}
