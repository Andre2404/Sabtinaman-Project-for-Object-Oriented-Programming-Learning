package dao;

import model.Pengguna;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
            statement.setInt(5, pengguna.getSaldoPengguna());
            statement.setString(6, pengguna.getPassword());
            statement.executeUpdate();
        }
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

    // Mengambil data pengguna berdasarkan email
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

    // Mengambil semua data pengguna
    public List<Pengguna> getAllPengguna() throws SQLException {
        List<Pengguna> penggunaList = new ArrayList<>();
        String query = "SELECT * FROM pengguna";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                Pengguna pengguna = new Pengguna(
                    resultSet.getInt("id_pengguna"),
                    resultSet.getString("nama"),
                    resultSet.getString("alamat"),
                    resultSet.getString("email"),
                    resultSet.getInt("nomor_kontak"),
                    resultSet.getInt("saldo"),
                    resultSet.getString("password")
                );
                penggunaList.add(pengguna);
            }
        }
        return penggunaList;
    }

    // Update data pengguna
    public void updatePengguna(Pengguna pengguna) throws SQLException {
        String query = "UPDATE pengguna SET nama = ?, alamat = ?, email = ?, nomor_kontak = ?, saldo = ? WHERE id_pengguna = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, pengguna.getNama());
            statement.setString(2, pengguna.getAlamat());
            statement.setString(3, pengguna.getEmail());
            statement.setInt(4, pengguna.getNomorKontak());
            statement.setInt(5, pengguna.getSaldoPengguna());
            statement.setInt(6, pengguna.getIdPengguna());
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

    // Fungsi login menggunakan email dan password
    public Pengguna login(String email, String password) throws SQLException {
        Pengguna pengguna = getPenggunaByEmail(email);
        if (pengguna != null && pengguna.getPassword().equals(password)) {
            return pengguna;
        }
        return null;
    }

    // Method untuk mengambil saldo pengguna berdasarkan ID
    public int getSaldoPengguna(int userId) throws SQLException {
        String query = "SELECT saldo FROM pengguna WHERE id_pengguna = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("saldo");
                } else {
                    throw new SQLException("Pengguna dengan ID " + userId + " tidak ditemukan.");
                }
            }
        }
    }

    // Method untuk memperbarui saldo pengguna
    public void updateSaldoPengguna(int userId, int newSaldo) throws SQLException {
        String query = "UPDATE pengguna SET saldo = ? WHERE id_pengguna = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, newSaldo);
            stmt.setInt(2, userId);
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated == 0) {
                throw new SQLException("Gagal memperbarui saldo untuk pengguna dengan ID " + userId);
            }
        }
    }

    // Fungsi top-up saldo
    public void topUpSaldo(int currentUserId, int amount) {
        try {
            // Ambil saldo saat ini pengguna
            int existingSaldo = getSaldoPengguna(currentUserId);

            // Hitung saldo baru
            int newSaldo = existingSaldo + amount;

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
    
    // Mengurangi saldo pengguna
public void updateSaldoPengguna(int idPengguna, double newSaldo) throws SQLException {
    String query = "UPDATE pengguna SET saldo = ? WHERE id_pengguna = ?";
    try (PreparedStatement statement = connection.prepareStatement(query)) {
        statement.setDouble(1, newSaldo);
        statement.setInt(2, idPengguna);
        statement.executeUpdate();
    }
}

// Menambahkan saldo perusahaan
public void addSaldoPerusahaan(int idPerusahaan, double amount) throws SQLException {
    String query = "UPDATE perusahaan SET saldo = saldo + ? WHERE id_perusahaan = ?";
    try (PreparedStatement statement = connection.prepareStatement(query)) {
        statement.setDouble(1, amount);
        statement.setInt(2, idPerusahaan);
        statement.executeUpdate();
    }
}

}
