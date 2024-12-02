package DAO;

import model.Pengguna;
import java.sql.*;

public class PenggunaDAO {

    private Connection connection;

    public PenggunaDAO(Connection connection) {
    this.connection = connection;
}


    // Menambahkan pengguna baru ke database
    public void addPengguna(Pengguna pengguna) throws SQLException {
        String query = "INSERT INTO pengguna (nama, alamat, email, nomor_kontak, saldo, password) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, pengguna.getNama());
            statement.setString(2, pengguna.getAlamat());
            statement.setString(3, pengguna.getEmail());
            statement.setInt(4, pengguna.getNomorKontak());
            statement.setDouble(5, pengguna.getSaldoPengguna());
            statement.setString(6, pengguna.getPassword());
            statement.executeUpdate();
        }
    }
    
    public Pengguna getPenggunaByEmail(String email) throws SQLException {
        String query = "SELECT * FROM pengguna WHERE email = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Pengguna(
                    resultSet.getInt("id_pengguna"),
                    resultSet.getString("nama"),
                    resultSet.getString("alamat"),
                    resultSet.getString("email"),
                    resultSet.getInt("nomor_kontak"),
                    resultSet.getInt("saldo"),
                    resultSet.getString("password")
                );
            }
        }
        return null;
    }
    
    public Pengguna login(String email, String password) throws SQLException {
    String query = "SELECT * FROM pengguna WHERE email = ? AND password = ?";
    try (PreparedStatement statement = connection.prepareStatement(query)) {
        statement.setString(1, email);
        statement.setString(2, password);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            return new Pengguna(
                resultSet.getInt("id_pengguna"),
                resultSet.getString("nama"),
                resultSet.getString("alamat"),
                resultSet.getString("email"),
                resultSet.getInt("nomor_kontak"),
                resultSet.getInt("saldo"),
                resultSet.getString("password")
            );
        }
    }
    return null; // Jika email atau password tidak cocok
}

    
    // Mengambil data pengguna berdasarkan ID
    public Pengguna getPenggunaById(int idPengguna) throws SQLException {
        String query = "SELECT * FROM pengguna WHERE id_pengguna = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, idPengguna);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Pengguna(
                    resultSet.getInt("id_pengguna"),
                    resultSet.getString("nama"),
                    resultSet.getString("alamat"),
                    resultSet.getString("email"),
                    resultSet.getInt("nomor_kontak"),
                    resultSet.getInt("saldo"),
                    resultSet.getString("password")
                );
            }
        }
        return null;
    }
public void topUpSaldo(int currentUserId, int amount) {
        try {
            // Ambil saldo saat ini pengguna
            double existingSaldo = getSaldoPengguna(currentUserId);

            // Hitung saldo baru
            double newSaldo = existingSaldo + amount;

            // Perbarui saldo pengguna di database
            updateSaldoPengguna(currentUserId, newSaldo);

            // Tampilkan pesan sukses
            System.out.println("Top-up saldo berhasil untuk ID pengguna " + currentUserId + ". Saldo baru: " + newSaldo);
        } catch (SQLException e) {
            // Menangani error dan mencatat pesan error
            System.err.println("Error saat top-up saldo untuk ID pengguna " + currentUserId + ": " + e.getMessage());
            throw new RuntimeException("Error topping up saldo.", e);
        }
    }
    // Mengambil saldo pengguna berdasarkan ID
    public double getSaldoPengguna(int userId) throws SQLException {
        String query = "SELECT saldo FROM pengguna WHERE id_pengguna = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("saldo");
                } else {
                    throw new SQLException("Pengguna dengan ID " + userId + " tidak ditemukan.");
                }
            }
        }
    }

    // Memperbarui saldo pengguna
    public void updateSaldoPengguna(int idPengguna, double newSaldo) throws SQLException {
        String query = "UPDATE pengguna SET saldo = ? WHERE id_pengguna = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setDouble(1, newSaldo);
            statement.setInt(2, idPengguna);
            statement.executeUpdate();
        }
    }

    // Menghapus pengguna berdasarkan ID
    public void deletePengguna(int idPengguna) throws SQLException {
        String query = "DELETE FROM pengguna WHERE id_pengguna = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, idPengguna);
            statement.executeUpdate();
        }
    }
    
    public void addSaldoPerusahaan(int idPerusahaan, double amount) throws SQLException {
    String query = "UPDATE perusahaan SET saldo_perusahaan = saldo_perusahaan + ? WHERE id_perusahaan = ?";
    try (PreparedStatement statement = connection.prepareStatement(query)) {
        statement.setDouble(1, amount);
        statement.setInt(2, idPerusahaan);
        statement.executeUpdate();
    }
}
    
}
