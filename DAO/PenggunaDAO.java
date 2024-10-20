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
        String query = "INSERT INTO pengguna (nama, alamat, email, nomor_kontak, saldo_pengguna, password) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, pengguna.getNama());
            statement.setString(2, pengguna.getAlamat());
            statement.setString(3, pengguna.getEmail());
            statement.setInt(4, pengguna.getNomorKontak());
            statement.setInt(5, pengguna.getSaldoPengguna());
            statement.setString(6, pengguna.getPassword()); // Simpan password langsung
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
                    resultSet.getInt("saldo_pengguna"),
                    resultSet.getString("password") // Ambil password langsung dari database
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
                    resultSet.getInt("saldo_pengguna"),
                    resultSet.getString("password") // Ambil password langsung dari database
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
                    resultSet.getInt("saldo_pengguna"),
                    resultSet.getString("password") // Ambil password langsung dari database
                );
                penggunaList.add(pengguna);
            }
        }
        return penggunaList;
    }

    // Update data pengguna
    public void updatePengguna(Pengguna pengguna) throws SQLException {
        String query = "UPDATE pengguna SET nama = ?, alamat = ?, email = ?, nomor_kontak = ?, saldo_pengguna = ? WHERE id_pengguna = ?";
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
            return pengguna; // Login berhasil
        }
        return null; // Login gagal
    }
}