package DAO;

import model.Perusahaan;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PerusahaanDAO {

    private Connection connection;

    public PerusahaanDAO(Connection connection) {
        this.connection = connection;
    }

    // Menambahkan perusahaan baru ke database
    public void addPerusahaan(Perusahaan perusahaan) throws SQLException {
        String query = "INSERT INTO perusahaan (nama, alamat_perusahaan, email, nomor_kontak, saldo_perusahaan, password) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, perusahaan.getNama());
            statement.setString(2, perusahaan.getAlamat());
            statement.setString(3, perusahaan.getEmail());
            statement.setInt(4, perusahaan.getNomorKontak());
            statement.setInt(5, perusahaan.getSaldoPerusahaan());
            statement.setString(6, perusahaan.getPassword()); // Simpan password langsung
            statement.executeUpdate();
        }
    }

    // Mengambil perusahaan berdasarkan ID
    public Perusahaan getPerusahaanById(int idPerusahaan) throws SQLException {
        String query = "SELECT * FROM perusahaan WHERE id_perusahaan = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, idPerusahaan);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Perusahaan(
                    resultSet.getInt("id_perusahaan"),
                    resultSet.getString("nama"),
                    resultSet.getString("alamat_perusahaan"),
                    resultSet.getString("email"),
                    resultSet.getInt("nomor_kontak"),
                    resultSet.getInt("saldo_perusahaan"),
                    resultSet.getString("password") // Ambil password langsung dari database
                );
            }
        }
        return null;
    }

    // Mengambil perusahaan berdasarkan email
    public Perusahaan getPerusahaanByEmail(String email) throws SQLException {
        String query = "SELECT * FROM perusahaan WHERE email = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Perusahaan(
                    resultSet.getInt("id_perusahaan"),
                    resultSet.getString("nama"),
                    resultSet.getString("alamat_perusahaan"),
                    resultSet.getString("email"),
                    resultSet.getInt("nomor_kontak"),
                    resultSet.getInt("saldo_perusahaan"),
                    resultSet.getString("password") // Ambil password langsung dari database
                );
            }
        }
        return null;
    }

    // Mengambil semua perusahaan dari database
    public List<Perusahaan> getAllPerusahaan() throws SQLException {
        List<Perusahaan> perusahaanList = new ArrayList<>();
        String query = "SELECT * FROM perusahaan";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                Perusahaan perusahaan = new Perusahaan(
                    resultSet.getInt("id_perusahaan"),
                    resultSet.getString("nama"),
                    resultSet.getString("alamat_perusahaan"),
                    resultSet.getString("email"),
                    resultSet.getInt("nomor_kontak"),
                    resultSet.getInt("saldo_perusahaan"),
                    resultSet.getString("password") // Ambil password langsung dari database
                );
                perusahaanList.add(perusahaan);
            }
        }
        return perusahaanList;
    }

    // Mengupdate data perusahaan
    public void updatePerusahaan(Perusahaan perusahaan) throws SQLException {
        String query = "UPDATE perusahaan SET nama = ?, alamat_perusahaan = ?, email = ?, nomor_kontak = ?, saldo_perusahaan = ?, password = ? WHERE id_perusahaan = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, perusahaan.getNama());
            statement.setString(2, perusahaan.getAlamat());
            statement.setString(3, perusahaan.getEmail());
            statement.setInt(4, perusahaan.getNomorKontak());
            statement.setInt(5, perusahaan.getSaldoPerusahaan());
            statement.setString(6, perusahaan.getPassword());
            statement.setInt(7, perusahaan.getIdPerusahaan());
            statement.executeUpdate();
        }
    }

    // Menghapus perusahaan berdasarkan ID
    public void deletePerusahaan(int idPerusahaan) throws SQLException {
        String query = "DELETE FROM perusahaan WHERE id_perusahaan = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setObject(1, idPerusahaan);
            statement.executeUpdate();
        }
    }

    public void updateSaldoPerusahaan(int idPerusahaan, int newSaldo) throws SQLException {
        String query = "UPDATE perusahaan SET saldo_perusahaan = ? WHERE id_perusahaan = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, newSaldo);
            statement.setInt(2, idPerusahaan);
            statement.executeUpdate();
        }
    }
    
    public int getSaldoPerusahaan(int idPerusahaan) throws SQLException {
        String query = "SELECT saldo_perusahaan FROM perusahaan WHERE id_perusahaan = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, idPerusahaan);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("saldo_perusahaan");
                } else {
                    throw new SQLException("Perusahaan dengan ID " + idPerusahaan + " tidak ditemukan.");
                }
            }
        }
    }
    
    // Metode untuk login
    public Perusahaan login(String email, String password) throws SQLException {
        Perusahaan perusahaan = getPerusahaanByEmail(email);
        if (perusahaan != null && perusahaan.getPassword().equals(password)) {
            return perusahaan; // Login berhasil
        }
        return null; // Login gagal
    }
}